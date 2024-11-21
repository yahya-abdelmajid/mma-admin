package mma.admin.infrastructures.csv;

import mma.admin.domains.*;
import mma.admin.domains.interfaces.RoomsReader;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static net.fortuna.ical4j.model.property.immutable.ImmutableCalScale.GREGORIAN;
import static net.fortuna.ical4j.model.property.immutable.ImmutableVersion.VERSION_2_0;

/**
 * Gestionnaire de fichiers pour la création et l'écriture dans les fichiers .ics.
 */
public class ICSReservationsImpl{
    private final String icsFilePath;
    Calendar calendar;
    List<Reservation> reservations;
    RoomsReader roomsReader;
    VEvent event;

    public ICSReservationsImpl(String icsFilePath, RoomsReader roomsReader) {
        this.icsFilePath=icsFilePath;
        this.calendar = new Calendar();
        calendar.add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.add(VERSION_2_0);
        calendar.add(GREGORIAN);
        reservations = new ArrayList<>();
        //readAllReservations();
        this.roomsReader = roomsReader;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }



    public void readAllReservations() {

        try {
            // Lecture du fichier ICS
            FileInputStream fin = new FileInputStream(icsFilePath);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(fin);

            // Parcours des composants (événements)
            for (Object o : calendar.getComponents(Component.VEVENT)) {
                VEvent event = (VEvent) o;
                this.calendar.add(event);

                // Extraire les informations de l'événement
                Uid uid=event.getUid().get();
                String uidString=uid.getValue();
                String personId = uid.getValue().substring(uidString.length()-7);

                //Description
                Description description = event.getDescription().get();

                //Format de la date à enregistrer
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

                //Date du jour de réservation
                Optional<Property> date = event.getProperty(Property.DTSTAMP);
                DtStamp dtStamp=(DtStamp)date.get();
                LocalDateTime reservationDate = LocalDateTime.parse(dtStamp.getDate().toString(), formatter);

                //Date de départ
                Optional<Property> dtStart = event.getProperty(Property.DTSTART);
                DtStart dt=(DtStart)dtStart.get();
                LocalDateTime startDate = LocalDateTime.from(dt.getDate());

                //Date de fin
                Optional<Property> dtEndProperty = event.getProperty(Property.DTEND);
                DtEnd dtEnd = (DtEnd) dtEndProperty.get();
                LocalDateTime endDate = LocalDateTime.from(dtEnd.getDate());

                //La Salle avec l'id
                Location location = event.getLocation().get();
                String roomId = location.getValue();

                // Créer le TimeSlot
                TimeSlot timeSlot = new TimeSlot(startDate, endDate);

                // Créer la réservation et l'ajouter à la liste
                Reservation reservation = new Reservation(roomId, personId, reservationDate, timeSlot, description.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean exists(Reservation reservation) {
        if (reservations.contains(reservation)) {
            System.out.println("La réservation existe déjà.");
            return true;  // La réservation existe déjà
        } else {
            reservations.add(reservation);
            System.out.println("Réservation ajoutée avec succès.");
            return false;  // Nouvelle réservation ajoutée
        }
    }


    /**
     * Crée fichier ics si il n'existe pas
     */
    public void createIcsFileIfNotExist() throws IOException {
        Path icsPath= Paths.get(icsFilePath);
        if (!Files.exists(icsPath)) {
            // Créer le fichier s'il n'existe pas
            Files.createFile(icsPath);
        }
    }

    public void registerInCalendar(User user, Reservation reservation) throws IOException, ParseException {
        createIcsFileIfNotExist();
        addReservationToCalendar(user,reservation);

        // Écrire le fichier .ics sur le disque
        try (FileOutputStream fout = new FileOutputStream(icsFilePath)) {
            calendar.validate();
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
            fout.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode ajoute les réservations d'une salle dans le calendrier .ics.
     *
     * @param reservation La réservation à ajouter
     */
    public void addReservationToCalendar(User user,Reservation reservation) throws ParseException {
        // Parcourir les créneaux de disponibilité et les ajouter en tant qu'événements .ics
            createEventForReservation(user,reservation);
            calendar.add(event);
    }

    /**
     * Crée un événement .ics (VEvent) pour une réservation donnée.
     * @param reservation La réservation contenant les informations de l'événement
     * @return Un objet VEvent représentant la réservation
     */
    public void createEventForReservation(User user, Reservation reservation) {
        String eventName = reservation.getDescription();
        TimeSlot ts=reservation.getTimeSlot();
        Uid uid = new Uid(UUID.randomUUID().toString()+"-"+user.getBadge());
        Description description=new Description(reservation.getDescription());
        Location loc=new Location(reservation.getRoomId().toString());
        this.event = new VEvent(ts.getStartTime(), ts.getEndTime(), eventName);
        addOrganizer(user);
        event.add(description);
        event.add(uid);
        event.add(loc);
    }

    public void addOrganizer(User user) {
        // Ajouter un Organizer (Organisateur)
        String organizerEmail = "MAILTO:"+user.getEmail();
        Organizer organizer = new Organizer(URI.create(organizerEmail));
        organizer.add(new Cn(user.getFullName())); // Ajouter le nom de l'organisateur (facultatif)
        event.add(organizer);
    }

}



