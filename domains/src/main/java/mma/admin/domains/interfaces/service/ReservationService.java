package mma.admin.domains.interfaces.service;

import mma.admin.domains.Prestation;
import mma.admin.domains.Reservation;
import mma.admin.domains.ReservationPrestation;

import java.sql.SQLException;
import java.util.List;

public interface ReservationService {

    void addReservationWithServices(Reservation reservation) throws SQLException;

    List<Reservation> getAllReservations();

    List<ReservationPrestation> getAllReservationService();

    List<Prestation> getAllPrestations();
}
