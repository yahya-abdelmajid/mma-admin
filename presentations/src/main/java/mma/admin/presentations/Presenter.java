/*
 * This source file is an example
 */
package mma.admin.presentations;

import mma.admin.domains.*;
import mma.admin.domains.exceptions.NoPositiveCapacityException;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import mma.admin.domains.interfaces.repository.RoomsRepository;
import mma.admin.domains.interfaces.repository.UsersRepository;
import mma.admin.domains.interfaces.service.CalendarsExporter;
import mma.admin.domains.interfaces.service.CalendarsImporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Presenter {
    private final CalendarsRepository calendarsRep;
    private final UsersRepository usersRep;
    private final RoomsRepository roomsRep;
    private final TimeSlot workingHours;
    private final LocalTime[] workingTimes;
    private final IView view;
    private final CalendarsExporter calendarsExporter;
    private final CalendarsImporter calImporter;

    public Presenter(IView view, CalendarsRepository calendarsRep, UsersRepository usersRep, RoomsRepository roomsRep, CalendarsExporter calendarsExp, CalendarsImporter calImp) {
        view.setPresenter(this);
        this.view = view;
        this.calendarsRep = calendarsRep;
        this.usersRep = usersRep;
        this.roomsRep = roomsRep;
        this.calendarsExporter=calendarsExp;
        this.calImporter=calImp;
        this.workingHours=new TimeSlot(LocalTime.of(8, 0), LocalTime.of(17, 0));
        this.workingTimes = createHeader();
    }

    public void addReservation(ReservationT r) throws IOException, ParseException, SQLException {

        TimeSlot timeSlot=new TimeSlot(r.start(),r.end());
        try {
            Reservation reservation=new Reservation(calendarsRep.getNbrReservations()+1,r.roomId(),r.matricule(),timeSlot,r.description(),r.expected());
            if (isReservationValid(r.roomId(), r.matricule(), r.expected(), timeSlot, reservation)) {
                //Permet d'ajouter les services à une réservation
                addPrestation(reservation,r.services());
                calendarsRep.addReservation(reservation);
            }
        } catch (NoPositiveCapacityException e) {
            view.displayMessage(e.getMessage());
        }
    }

    private void addPrestation(Reservation reservation,String services){
        if(services.isEmpty()){
            return;
        }

        for(Prestation p :calendarsRep.getPrestations(services)){
            reservation.addPrestation(p);
        }
    }

    private boolean isReservationValid(String roomId, String matricule, int expected, TimeSlot timeSlot, Reservation reservation) throws SQLException {

        if(!usersRep.exists(matricule)){
            view.displayMessage("Matricule inconnu");
            return false;
        }

        if(!roomsRep.capacityRoomIsNotExceeded(roomId, expected)){
            view.displayMessage("Capacité insuffisante");
            return false;
        }
        if(calendarsRep.exists(reservation)){
            view.displayMessage(String.format("Créneau de %s déjà occupé",timeSlot.getStart()));
            return false;
        }
        return
                roomsRep.exists(roomId) &&
                timeSlot.isWithinWorkingHours(workingHours) &&
                timeSlot.isNotInPast();
    }

    public void displayHeader(DateTimeFormatter formatter) {
        StringBuilder header=new StringBuilder();
        header.append(String.format("%-10s", "Salle"));
        for(LocalTime time:workingTimes){
            header.append(String.format("%-10s", time.format(formatter)));
            time = time.plusMinutes(30);
        }
        view.displayMessage(header.toString());
    }

    private LocalTime[] createHeader() {

        LocalTime time = workingHours.getStart();
        List<LocalTime> header = new ArrayList<>();

        while (time.isBefore(workingHours.getEnd())) {
            header.add(time);
            time = time.plusMinutes(30);
        }

        return header.toArray(new LocalTime[0]);
    }

    public void displayAvailability(LocalDate date) {
        view.displayMessage("Disponibilites du "+date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // Formatteur pour afficher les heures au format HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Création des en-têtes et affichage
        displayHeader(formatter);

        // Génération des créneaux horaires (08h00 - 16h30) généré par une IA
        LocalTime[] timeTab=createHeader();
        char signe = '❌';
        Map<RoomId,Agenda> agendas= calendarsRep.getCalendars();

        for(Agenda agenda: agendas.values()){
            view.displayMessage(agenda.getLigneMenu(timeTab,date,signe+""));
        }
    }

    public void displayReservationIfExist(String roomId,LocalDateTime s) {
        TimeSlot timeSlot=new TimeSlot(s,s.plusMinutes(30));

        if (!validateRoomAndTimeSlot(roomId, timeSlot)) {
            return;
        }

        Reservation result= calendarsRep.getReservation(roomId,s);

        if(result==null) {
            view.displayMessage("Aucune correspondance trouve");
        }else{
            Room room=roomsRep.getRoom(new RoomId(roomId));
            User user=usersRep.getUser(result.getPersonId());
            view.displayMessage(room+result.toString()+user+result.prestationsToString());
        }
    }

    private boolean validateRoomAndTimeSlot(String roomId, TimeSlot timeSlot) {
        if (!roomsRep.exists(roomId)) {
            view.displayMessage("La salle n'existe pas");
            return false;
        }
        if (!timeSlot.isWithinWorkingHours(workingHours)) {
            view.displayMessage("Le créneau horaire est en dehors des heures d'ouverture!");
            return false;
        }
        return true;
    }


    public void displayRoomRequest(LocalDate date,int person,Duration dur, int limit) {

        List<Availability> availabilities = new ArrayList<>(calendarsRep.getAvailabilities(workingHours, date, dur, person));
        availabilities.sort(Comparator.reverseOrder());

        String header = String.format("%-10s%-15s%-20s%-18s", "Salle","Date","Heure de debut","Heure de fin");
        view.displayMessage(header);

        availabilities.stream()
                .limit(limit)
                .distinct()
                .map(a -> String.format("%-10s%-15s%-20s%-18s",
                        a.getRoom().id(), a.getTimeSlot().getDay(),
                        a.getTimeSlot().getStart(), a.getTimeSlot().getEnd()))
                .forEach(view::displayMessage);

    }

    public void exportReservationRoom(String roomId, LocalDate dateStart, LocalDate dateEnd, String path) throws IOException, ParseException {
        boolean result = true;

        result &= check(roomsRep.exists(roomId), "Salle non trouvée");
        result &= check(!dateEnd.isBefore(dateStart), "Période invalide");
        result &= check(Files.exists(Paths.get(path)), "Chemin vers le fichier incorrect");

        if (result) {
            Agenda agenda = calendarsRep.getAgenda(new RoomId(roomId));
            List<Reservation> reservations = agenda.getReservations(dateStart, dateEnd);
            calendarsExporter.registerInCalendar(path, reservations);
        }
    }

    private boolean check(boolean condition, String message) {
        if (!condition) {
            view.displayMessage(message);
            return false;
        }
        return true;
    }

    public void importReservations(String absolutePath) throws SQLException {
        List<Reservation> reservations=calImporter.readReservations(absolutePath);

        if (!roomsRep.allRoomsExist(reservations)) {
            view.displayMessage("Une ou plusieurs occupations font reference a une salle qui n’existe pas");
            return;
        }
        if (calendarsRep.allExists(reservations)) {
            view.displayMessage("Une ou plusieurs occupations entrent en conflit avec des occupations déjà enregistrées");
            return;
        }

        calendarsRep.addAllReservation(reservations);
    }
}
