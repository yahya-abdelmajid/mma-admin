/*
 * This source file is an example
 */
package mma.admin.presentations;

import IMainView.IView;
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
    CalendarsRepository calendarsRep;
    UsersRepository usersRep;
    RoomsRepository roomsRep;
    private final TimeSlot workingHours;
    private final LocalTime[] workingTimes;
    private IView view;

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime d = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        TimeSlot timeSlot=new TimeSlot(start,end);
        Reservation reservation=new Reservation(calendarsRep.getNbrReservations()+1,roomId,matricule,LocalDateTime.now(),timeSlot,description);

        User user= usersRep.getUser(matricule);
        //Vérifie que le matricule et que la room existe
        if(usersRep.exists(matricule)&& roomsRep.exists(roomId)
                //Vérifie que la capacité de la room est suffisante
                && roomsRep.capacityRoomIsNotExceeded(roomId,expected)){
            view.displayMessage("La réservation existe déjà");

            //Vérifie que la réservation n'existe pas et le créneau horaire se trouve dans les heures d'ouverture
            if(!calendarsRep.exists(reservation)&&timeSlot.isWithinWorkingHours(workingHours)&&
                    //Vérifie que le créneau horaire ne commence pas dans le passé
                timeSlot.isNotInPast()){
                if(!services.isEmpty()) reservation.setPrestations(calendarsRep.getPrestations(services));
                calendarsRep.addReservation(reservation);
            }
        }
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

    public LocalTime[] createHeader() {

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
        Collection<Agenda> agendas= calendarsRep.getAgendas();
        for(Agenda agenda: agendas){
            view.displayMessage(agenda.getLigneMenu(timeTab,date,signe+""));
        }
    }

    public void displayReservationIfExist(String roomId,LocalDateTime s,LocalDateTime e) {
        TimeSlot timeSlot=new TimeSlot(s,e);
        Reservation result;

        if(!roomsRep.exists(roomId)){
            view.displayMessage("La salle n'existe pas");
        }else if(!timeSlot.isWithinWorkingHours(workingHours)){
            view.displayMessage("Le créneau horaire est en dehors des heures d'ouverture!");
        }

        result= calendarsRep.getReservation(roomId,timeSlot);
        Room room=roomsRep.getRoom(new RoomId(roomId));
        User user=usersRep.getUser(result.getPersonId());

        if(result==null) {
            view.displayMessage("Aucune correspondance trouvé");
        }else{
            view.displayMessage(room+""+result+user+result.prestationsToString());
        }
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
