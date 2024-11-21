package mma.admin.infrastructures.repository;

import mma.admin.domains.Agenda;
import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.RoomId;
import mma.admin.domains.interfaces.CalendarsReader;
import mma.admin.domains.interfaces.CalendarsWriter;
import mma.admin.domains.interfaces.repository.CalendarsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DBCalendarsRepository implements CalendarsRepository {
    private static final Logger LOGGER = LogManager.getLogger();
    Map<RoomId, Agenda> calendars;

    public DBCalendarsRepository(CalendarsReader calendarsReader, CalendarsWriter calendarsWriter) {
        calendarsReader.readAllReservations();
        calendars=calendarsReader.getCalendars();

    }

    public Map<RoomId, Agenda> getCalendars() {
        return calendars;
    }

    @Override
    public List<Reservation> getReservations() {
        return List.of();
    }

    @Override
    public boolean exists(Reservation reservation) {
        for(Agenda agenda : calendars.values()) {
            if(agenda.getReservations().contains(reservation)) return true;
        }
        return false;
    }

    @Override
    public Collection<Agenda> getAgendas(){
        return calendars.values();
    }

    public Reservation getReservation(Reservation reservation) {
        Reservation result = null;
        for(Agenda agenda : calendars.values()) {

            result=agenda.getReservation(reservation);
            System.out.println(result);
            if(result!=null) return result;
        }
        return null;
    }

    @Override
    public void addReservation(Reservation reservation) {

    }


}
