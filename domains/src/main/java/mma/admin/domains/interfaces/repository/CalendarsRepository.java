package mma.admin.domains.interfaces.repository;

import mma.admin.domains.*;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CalendarsRepository {
    boolean exists(Reservation reservation);
    Reservation getReservation(String roomId, LocalDateTime start);
    void addReservation(Reservation reservation) throws SQLException;
    List<Prestation> getPrestations(String services);
    int getNbrReservations();
    List<Availability> getAvailabilities(TimeSlot workingHours, LocalDate date, TemporalAmount duration, int roomCapacity);
    Map<RoomId, Agenda> getCalendars();
    void readAllReservations();
    Agenda getAgenda(RoomId roomId);
    boolean allExists(List<Reservation> reservations);
    void addAllReservation(List<Reservation> reservations)throws SQLException;
}
