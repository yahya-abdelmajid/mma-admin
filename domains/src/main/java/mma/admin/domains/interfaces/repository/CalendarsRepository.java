package mma.admin.domains.interfaces.repository;

import mma.admin.domains.*;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CalendarsRepository {
    boolean exists(Reservation reservation);
    Reservation getReservation(String roomId,TimeSlot timeSlot);
    void addReservation(Reservation reservation) throws SQLException;
    List<Prestation> getPrestations(String services);
    int getNbrReservations();
    List<Availability> getAvailabilities(TimeSlot workingHours,LocalDate date, Duration duration, int roomCapacity);
    Map<RoomId, Agenda> getCalendars();
    void readAllReservations();
}
