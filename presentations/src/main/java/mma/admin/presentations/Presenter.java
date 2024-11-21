/*
 * This source file is an example
 */
package mma.admin.presentations;

import mma.admin.domains.*;
import mma.admin.domains.interfaces.*;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import mma.admin.domains.interfaces.repository.RoomsRepository;
import mma.admin.domains.interfaces.repository.UsersRepository;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Presenter {
    CalendarsRepository calendarsRep;
    UsersRepository usersRep;
    RoomsRepository roomsRep;
    private final LocalTime startTime = LocalTime.of(8, 0);   // Heure de début (08:00)
    private final LocalTime endTime = LocalTime.of(17, 0);
    private final LocalTime[] workingTimes;


    public Presenter(CalendarsRepository calendarsRep, UsersRepository usersRep, RoomsRepository roomsRep) {
        this.calendarsRep = calendarsRep;
        this.usersRep = usersRep;
        this.roomsRep = roomsRep;
        this.workingTimes = createHeader();
    }

    public LocalTime getStartTime() {
        return startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }

    public void addReservations(String roomId, String matricule,String day,String start,
                                String end,String description,int expected) throws IOException, ParseException {
        System.out.println("Adding reservations");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime s = LocalDateTime.parse(day+" "+start, formatter);
        LocalDateTime e = LocalDateTime.parse(day+" "+end, formatter);
        LocalDateTime d = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        TimeSlot timeSlot=new TimeSlot(s,e);
        Reservation reservation=new Reservation(roomId,matricule,LocalDateTime.now(),timeSlot,description);

        User user= usersRep.getUser(matricule);
        //Vérifie que le matricule et que la room existe
        if(usersRep.exists(matricule)&& roomsRep.exists(roomId)
                //Vérifie que la capacité de la room est suffisante
                && roomsRep.capacityRoomIsNotExceeded(roomId,expected)){
            //Vérifie que la réservation n'existe pas et le créneau horaire se trouve dans les heures d'ouverture
            if(!calendarsRep.exists(reservation)&&timeSlot.isWithinWorkingHours(startTime,endTime)&&
                    //Vérifie que le créneau horaire ne commence pas dans le passé
                timeSlot.isNotInPast()){
                System.out.println("la reservation n'existe pas donc c'est bon pour ajouter");
                //reservationsService.registerInCalendar(user,reservation);
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
        System.out.println(header);
    }

    public LocalTime[] createHeader() {

        LocalTime time = startTime;
        List<LocalTime> header = new ArrayList<>();

        while (time.isBefore(endTime)) {
            header.add(time);
            time = time.plusMinutes(30);
        }

        return header.toArray(new LocalTime[0]);
    }

    public void displayAvailability(LocalDate date) {
        System.out.println("Disponibilités du "+date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // Formatteur pour afficher les heures au format HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Création des en-têtes et affichage
        displayHeader(formatter);

        // Génération des créneaux horaires (08h00 - 16h30) généré par une IA
        LocalTime[] timeTab=createHeader();
        char signe = '❌';
        Collection<Agenda> agendas= calendarsRep.getAgendas();
        for(Agenda agenda: agendas){
            System.out.println(agenda.getLigneMenu(timeTab,date,signe+""));
        }
    }

    public void displayMenu() throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Changer la date à afficher (format yyyy-mm-dd)");
            System.out.println("2. Ajouter une réservation");
            System.out.println("3.Consulter une réservation");
            System.out.println("4. Quitter");
            System.out.print("Veuillez choisir une option : ");

            String choix = scanner.nextLine();

            switch (choix) {
                case "1":
                    System.out.println("Utilisez le format yyyy-mm-dd");
                    setDate(scanner);
                    break;
                case "2":
                    //les valeurs sont par défaut pour l'instant
                    addReservations("LB1","E567890","2024-10-28","09:30","12:30","Kickoff trapèze",3);
                    break;
                case "3":
                    displayReservationIfExist("2024-10-28","LB1","09:30","12:30");
                    break;
                case "4":
                    System.out.println("Fermeture du programme...");
                    quit = true;
                    break;
                default:
                    System.out.println("Choix non valide. Veuillez réessayer.");
                    break;
            }
        }
        scanner.close();
    }

    private void setDate(Scanner scanner) {
        System.out.print("Entrez une date au format yyyy-mm-dd : ");
        String inputDate = scanner.nextLine();

        // Validation et formatage de la date
        try {
            LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ISO_LOCAL_DATE);
            System.out.println("Date valide : " + date);
            displayAvailability(date);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur : format de date invalide. Le format attendu est yyyy-mm-dd.");
        }
    }

    public void displayReservationIfExist(String day,String roomId,String start,String end) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime s = LocalDateTime.parse(day+" "+start, formatter);
        LocalDateTime e = LocalDateTime.parse(day+" "+end, formatter);
        TimeSlot timeSlot=new TimeSlot(s,e);
        Reservation reservation=new Reservation(roomId,"",null,timeSlot,"");
        Reservation result;
        if(!roomsRep.exists(roomId)) System.out.println("La salle n'existe pas");

        if(!timeSlot.isWithinWorkingHours(startTime,endTime))
            System.out.println("Le créneau horaire est en dehors des heures d'ouverture!");

        result= calendarsRep.getReservation(reservation);

        if(result==null) {
            System.out.println("Aucune correspondance trouvé");
        }else{
            System.out.println(result);
        }
    }


	
}
