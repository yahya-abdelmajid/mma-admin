package mma.admin.domains.interfaces.repository;

import mma.admin.domains.Agenda;
import mma.admin.domains.Reservation;
import mma.admin.domains.User;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

public interface CalendarsRepository {
    List<Reservation> getReservations();
    boolean exists(Reservation reservation);
    Collection<Agenda> getAgendas();
    Reservation getReservation(Reservation reservation);
    void addReservation(Reservation reservation);
}
