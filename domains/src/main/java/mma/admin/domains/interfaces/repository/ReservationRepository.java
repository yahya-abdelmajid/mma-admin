package mma.admin.domains.interfaces.repository;

import mma.admin.domains.Reservation;
import mma.admin.domains.User;
import java.io.IOException;
import java.text.ParseException;

public interface ReservationRepository {
    void createEventForReservation(User user,Reservation reservation);
    void addReservationToCalendar(User user, Reservation reservation) throws ParseException;
    void registerInCalendar(User user,Reservation reservation) throws IOException, ParseException;
    void readAllReservations();
}
