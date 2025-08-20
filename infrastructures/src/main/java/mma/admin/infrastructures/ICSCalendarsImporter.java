package mma.admin.infrastructures;

import mma.admin.domains.Reservation;
import mma.admin.domains.TimeSlot;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import mma.admin.domains.interfaces.service.CalendarsImporter;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static net.fortuna.ical4j.model.property.immutable.ImmutableCalScale.GREGORIAN;
import static net.fortuna.ical4j.model.property.immutable.ImmutableVersion.VERSION_2_0;

public class ICSCalendarsImporter implements CalendarsImporter {
    private final Calendar calendar;
    private final CalendarsRepository calendarsRepo;

    public ICSCalendarsImporter(CalendarsRepository calRep) {
        this.calendar = new Calendar();
        calendar.add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.add(VERSION_2_0);
        calendar.add(GREGORIAN);
        this.calendarsRepo = calRep;
    }

    @Override
    public List<Reservation> readReservations(String path) {
        List<Reservation> reservations = new ArrayList<>();
        try {
            Calendar calendar = loadCalendarFromFile(Paths.get(path));
            int i = 1;

            for (Object o : calendar.getComponents(Component.VEVENT)) {
                VEvent event = (VEvent) o;
                this.calendar.add(event); // Garde une copie de l'événement

                Reservation reservation = createReservationFromEvent(event, i);
                reservations.add(reservation);
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    /**
     * Charge un fichier ICS et retourne un objet Calendar.
     */
    private Calendar loadCalendarFromFile(Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            return new CalendarBuilder().build(in);
        } catch (IOException | ParserException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convertit un événement ICS en objet Reservation.
     */
    private Reservation createReservationFromEvent(VEvent event, int index) {
        // UID et ID personne
        Uid uid = event.getUid().get();
        String uidString = uid.getValue();
        String personId = uidString.substring(uidString.length() - 7);

        // Description
        String description = event.getDescription().map(Description::getValue).orElse("");

        // Dates
        LocalDateTime startDate = getLocalDateTime(event, Property.DTSTART);
        LocalDateTime endDate = getLocalDateTime(event, Property.DTEND);

        // Salle
        String roomId = event.getLocation().map(Location::getValue).orElse("");

        // Créneau
        TimeSlot timeSlot = new TimeSlot(startDate, endDate);

        // Création réservation
        return new Reservation(
                calendarsRepo.getNbrReservations() + index,
                roomId,
                personId,
                timeSlot,
                description,
                3
        );
    }


    private LocalDateTime getLocalDateTime(VEvent event, String propertyName) {
        return LocalDateTime.from(
                ((DateProperty) event.getProperty(propertyName).get()).getDate()
        );
    }

}
