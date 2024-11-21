package mma.admin.infrastructures;

import mma.admin.domains.*;
import mma.admin.domains.interfaces.CalendarsReader;
import mma.admin.domains.interfaces.ReservationsService;
import mma.admin.domains.interfaces.RoomsReader;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DBCalendarsReader implements CalendarsReader {
    private Map<RoomId, Agenda> calendars;
    private ReservationsService reservationsService;

    public DBCalendarsReader(ReservationsService reservationsService,RoomsReader roomsReader) {
        this.calendars = new HashMap<>();
        this.reservationsService = reservationsService;
        initializeDictionary(roomsReader.getRooms());
    }

    // Getter pour calendars
    public Map<RoomId, Agenda> getCalendars() {
        return calendars;
    }

    // Méthode pour initialiser le dictionnaire
    private void initializeDictionary(Collection<Room> room) {
        for (Room r : room) {
            calendars.put(r.getRoomId(), new Agenda(r.getRoomId()));
        }
    }

    public void readAllReservations() {
        List<Reservation> reservations = reservationsService.getReservations();
        for (Reservation r : reservations) calendars.get(r.getRoomId()).add(r);
    }
}
