/*
 * This source file is an example
 */
package mma.admin.presentations;

import mma.admin.domains.*;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import mma.admin.domains.interfaces.repository.RoomsRepository;
import mma.admin.domains.interfaces.repository.UsersRepository;

import java.io.IOException;
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

    public Presenter(IView view, CalendarsRepository calendarsRep, UsersRepository usersRep, RoomsRepository roomsRep) {
        view.setPresenter(this);
        this.view = view;
        this.calendarsRep = calendarsRep;
        this.usersRep = usersRep;
        this.roomsRep = roomsRep;
        this.workingHours=new TimeSlot(LocalTime.of(8, 0), LocalTime.of(17, 0));
        this.workingTimes = createHeader();
    }

    public void addReservation(String roomId, String matricule,LocalDateTime start,LocalDateTime end,String description,int expected,String services) throws IOException, ParseException, SQLException {

        TimeSlot timeSlot=new TimeSlot(start,end);
        Reservation reservation=new Reservation(calendarsRep.getNbrReservations()+1,roomId,matricule,timeSlot,description,expected);

        if (isReservationValid(roomId, matricule, expected, timeSlot, reservation)) {
            //Permet d'ajouter les services à une réservation
            addPrestation(reservation,services);
            calendarsRep.addReservation(reservation);
        }
    }

    private void addPrestation(Reservation reservation,String services){
        if(!services.isEmpty()){
            return;
        }
        for(Prestation p :calendarsRep.getPrestations(services)){
            reservation.addPrestation(p);
        }
    }

    private boolean isReservationValid(String roomId, String matricule, int expected, TimeSlot timeSlot, Reservation reservation) throws SQLException {
        return usersRep.exists(matricule) &&
                roomsRep.exists(roomId) &&
                roomsRep.capacityRoomIsNotExceeded(roomId, expected) &&
                !calendarsRep.exists(reservation) &&
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
        view.displayMessage("Disponibilités du "+date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

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

    public void displayReservationIfExist(String roomId,LocalDateTime s,LocalDateTime e) {
        TimeSlot timeSlot=new TimeSlot(s,e);

        if (!validateRoomAndTimeSlot(roomId, timeSlot)) {
            return;
        }

        Reservation result= calendarsRep.getReservation(roomId,timeSlot);
        Room room=roomsRep.getRoom(new RoomId(roomId));
        User user=usersRep.getUser(result.getPersonId());

        if(result==null) {
            view.displayMessage("Aucune correspondance trouvé");
        }else{
            view.displayMessage(room+""+result+user+result.prestationsToString());
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

        List<Availability> availabilities =calendarsRep.getAvailabilities(workingHours,date,dur,person);
        Collections.sort(availabilities);
        Collections.reverse(availabilities);
        String header=String.format("%-10s%-15s%-20s%-18s", "Salle","Date","Heure de debut","Heure de fin");
        view.displayMessage(header);

        availabilities.stream()
                .limit(limit)
                .map(a-> String.format("%-10s%-15s%-20s%-18s", a.getRoom().id(),a.getTimeSlot().getDay()
                        ,a.getTimeSlot().getStart(),a.getTimeSlot().getEnd()))
                .forEach(view::displayMessage);
    }


	
}
