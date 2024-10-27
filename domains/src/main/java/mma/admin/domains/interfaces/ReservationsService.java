package mma.admin.domains.interfaces;

import mma.admin.domains.Reservation;
import mma.admin.domains.interfaces.repository.ReservationRepository;


import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

public interface ReservationsService extends ReservationRepository {
    void createIcsFileIfNotExist() throws IOException;
    List<Reservation> getReservations();
    boolean exists(Reservation reservation);
}
