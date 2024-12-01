package mma.admin.domains.interfaces.dao;

import mma.admin.domains.Prestation;
import mma.admin.domains.ReservationPrestation;

import java.util.List;

public interface PrestationDao extends ReaderDao<Prestation> {
    void insert(int reservationId, int serviceId);

    List<ReservationPrestation> findAllReservationService();
}