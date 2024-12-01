package mma.admin.domains.interfaces.repository;

import mma.admin.domains.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public interface CalendarsRepository {
    boolean exists(Reservation reservation);
    Collection<Agenda> getAgendas();
    Reservation getReservation(String roomId,TimeSlot timeSlot);
    void addReservation(Reservation reservation) throws SQLException;
    List<Prestation> getPrestations(String services);
    int getNbrReservations();
    List<Availability> getAvailabilities(TimeSlot workingHours,LocalDate date, Duration duration, int roomCapacity);

}
