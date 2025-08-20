package mma.admin.infrastructures;

import mma.admin.domains.Reservation;
import mma.admin.domains.RoomId;
import mma.admin.domains.TimeSlot;
import mma.admin.domains.User;
import mma.admin.domains.interfaces.repository.UsersRepository;
import mma.admin.domains.interfaces.service.CalendarsExporter;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.property.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.fortuna.ical4j.model.property.immutable.ImmutableCalScale.GREGORIAN;
import static net.fortuna.ical4j.model.property.immutable.ImmutableVersion.VERSION_2_0;

public class ICSCalendarsExporter implements CalendarsExporter {
    private String path;
    private final Calendar calendar;
    private final UsersRepository usersRep;
    private VEvent event;

    public ICSCalendarsExporter(UsersRepository usersRepository) {
        this.calendar = new Calendar();
        calendar.add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.add(VERSION_2_0);
        calendar.add(GREGORIAN);
        this.usersRep = usersRepository;
    }

    /**
     * Crée fichier ics si il n'existe pas
     */
    @Override
    public void createIcsFileIfNotExist() throws IOException {
        Path icsPath= Paths.get(path);
        if (!Files.exists(icsPath)) {
            // Créer le fichier s'il n'existe pas
            Files.createFile(icsPath);
        }
    }

    @Override
    public void registerInCalendar(String path, List<Reservation> reservation) throws IOException, ParseException {
        this.path = path + "\\reservation.ics";
        createIcsFileIfNotExist();
        addReservationToCalendar(reservation);

        // Écrire le fichier .ics sur le disque
        try (OutputStream fout = Files.newOutputStream(Path.of(this.path))) {
            calendar.validate();
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode ajoute les réservations d'une salle dans le calendrier .ics.
     * @param reservations La réservation à ajouter
     */
    private void addReservationToCalendar(List<Reservation> reservations) throws ParseException {
        // Parcourir les créneaux de disponibilité et les ajouter en tant qu'événements .ics
        for (Reservation reservation : reservations) {
            createEventForReservation(usersRep.getUser(reservation.getPersonId()),reservation);
            calendar.add(event);
        }

    }

    /**
     * Crée un événement .ics (VEvent) pour une réservation donnée.
     * @param reservation La réservation contenant les informations de l'événement
     * @return Un objet VEvent représentant la réservation
     */
    private void createEventForReservation(User user, Reservation reservation) {
        String eventName = reservation.getDescription();
        TimeSlot ts=reservation.getTimeSlot();
        Uid uid = new Uid(UUID.randomUUID().toString()+"-"+user.badge());
        Description description=new Description(reservation.getDescription());
        Location loc=new Location(reservation.getRoomId().toString());
        this.event = new VEvent(ts.getStartTime(), ts.getEndTime(), eventName);
        addOrganizer(user);
        event.add(description);
        event.add(uid);
        event.add(loc);
    }

    private void addOrganizer(User user) {
        // Ajouter un Organizer (Organisateur)
        String organizerEmail = "MAILTO:"+user.email();
        Organizer organizer = new Organizer(URI.create(organizerEmail));
        organizer.add(new Cn(user.fullName())); // Ajouter le nom de l'organisateur (facultatif)
        event.add(organizer);
    }
}
