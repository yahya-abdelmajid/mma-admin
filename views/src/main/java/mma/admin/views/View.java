/*
 * This source file is an example
 */
package mma.admin.views;

import mma.admin.presentations.IView;
import mma.admin.presentations.Presenter;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class View implements IView {
    Presenter presenter;

    public View() {}

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setDate(Scanner scanner) {
        displayMessage("Entrez une date au format yyyy-mm-dd : ");
        String inputDate = scanner.nextLine();

        // Validation et formatage de la date
        try {
            LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ISO_LOCAL_DATE);
            displayMessage("Date valide : " + date);
            presenter.displayAvailability(date);
        } catch (DateTimeParseException e) {
            displayMessage("Erreur : format de date invalide. Le format attendu est yyyy-mm-dd.");
        }
    }

    public void displayMessage(String message){
        System.out.println(message);
    }

    public void addReservation(Scanner scanner) throws IOException, ParseException, SQLException {
        displayMessage("Entrez un numéro de salle existant: ");
        String roomId = scanner.nextLine();
        displayMessage("Entrez un matricule: ");
        String badge = scanner.nextLine();
        displayMessage("Entrez une date au format yyyy-mm-dd (ex:2024-10-24): ");
        String dateReservation = scanner.nextLine();
        displayMessage("Entrez l'heure de début au format hh:mm (ex: 09:00): ");
        String start = scanner.nextLine();
        displayMessage("Entrez l'heure de fin au format hh:mm (ex: 11:00): ");
        String end = scanner.nextLine();
        displayMessage("Entrez une description: ");
        String description = scanner.nextLine();
        displayMessage("Entrez le nombre de personne attendu");
        int excepted=scanner.nextInt();
        displayMessage("Voulez-vous ajouter des services (oui/non):");
        String boolService = scanner.nextLine();

        String resultatService="";

        if(boolService.equals("oui")) {
            displayMessage("----Services Disponible----");
            displayMessage("1.Café et boissons:");
            displayMessage("2.Nourriture:");
            displayMessage("3.Projecteur:");
            displayMessage("4.Matériel Sonore:");
            displayMessage("Choisissez un/des service(s) " +
                    "en les séparent par une virgule (ex: 1,2");
            resultatService = scanner.nextLine();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //LocalDateTime s = LocalDateTime.parse("2024-11-29"+" "+"09:00", formatter);
        //LocalDateTime e = LocalDateTime.parse("2024-11-28"+" "+"10:30", formatter);
        LocalDateTime dateStart = LocalDateTime.parse(dateReservation+" "+start, formatter);
        LocalDateTime dateEnd = LocalDateTime.parse(dateReservation+" "+end, formatter);
        presenter.addReservation(roomId,badge,dateStart,dateEnd,description,excepted,resultatService);

        //presenter.addReservation("LC1","C345678",s,e,"Kickoff trapèze",3,"1,2");
    }

    public void displayReservation(Scanner scanner) throws IOException, ParseException {
        displayMessage("Entrez un numéro de salle existant: ");
        String roomId = scanner.nextLine();
        displayMessage("Entrez une date au format yyyy-mm-dd (ex:2024-10-24): ");
        String dateReservation = scanner.nextLine();
        displayMessage("Entrez l'heure de début au format hh:mm (ex: 09:00): ");
        String start = scanner.nextLine();
        displayMessage("Entrez l'heure de fin au format hh:mm (ex: 11:00): ");
        String end = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateStart = LocalDateTime.parse(dateReservation+" "+start, formatter);
        LocalDateTime dateEnd = LocalDateTime.parse(dateReservation+" "+end, formatter);
//        LocalDateTime dateStart = LocalDateTime.parse("2024-12-02"+" "+"09:00", formatter);
//        LocalDateTime dateEnd = LocalDateTime.parse("2024-12-02"+" "+"10:00", formatter);

        presenter.displayReservationIfExist(roomId,dateStart,dateEnd);
    }

    public void displayGrid(LocalDate date) {
        presenter.displayAvailability(date);
    }

    public void searchAvailabilityRoom(Scanner scanner) {

        displayMessage("Entrez le jour de la réservation (format AAAA-MM-JJ) : ");
        String day = scanner.nextLine();

        displayMessage("Quelle est la durée de réservation (ex: 01:30 pour 1h30): ");
        String duration = scanner.nextLine();

        displayMessage("Entrez le nombre de personne attendu");
        int excepted=scanner.nextInt();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalTime durat=LocalTime.parse(duration, DateTimeFormatter.ofPattern("HH:mm"));
        Duration dur=Duration.between(LocalTime.MIN, durat);
        LocalDate date = LocalDate.parse(day, formatter);

        presenter.displayRoomRequest(date,excepted,dur,5);
    }



    /*
    private static final Logger LOGGER = LogManager.getLogger();
    public static void greets() {
        LOGGER.info("Bonjour de la vue");
    }*/
}
