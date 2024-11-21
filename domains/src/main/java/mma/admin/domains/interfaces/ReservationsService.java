package mma.admin.domains.interfaces;

import mma.admin.domains.Reservation;
import mma.admin.domains.interfaces.repository.CalendarsRepository;


import java.io.IOException;
import java.util.List;

public interface ReservationsService extends CalendarsRepository {
    void createIcsFileIfNotExist() throws IOException;
    List<Reservation> getReservations();
    boolean exists(Reservation reservation);
}
