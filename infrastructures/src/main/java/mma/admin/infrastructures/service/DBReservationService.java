package mma.admin.infrastructures.service;

import mma.admin.domains.Prestation;
import mma.admin.domains.Reservation;
import mma.admin.domains.ReservationPrestation;
import mma.admin.domains.interfaces.DataStorage;
import mma.admin.domains.interfaces.service.ReservationService;

import java.sql.SQLException;
import java.util.List;

public class DBReservationService implements ReservationService {

    private final DataStorage dataStorage;
    public DBReservationService(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
    }

    @Override
    public void addReservationWithServices(Reservation reservation) throws SQLException {
        try {
            dataStorage.beginTransaction();

            dataStorage.getReservationDao().insert(reservation);
            for (Prestation p : reservation.getPrestations()) {
                dataStorage.getPrestationDao().insert(reservation.getId(), p.id());
            }

            dataStorage.commitTransaction();
        } catch (Exception e) {
            dataStorage.rollbackTransaction();
            throw e;
        }
    }
    @Override
    public List<Reservation> getAllReservations(){
        return dataStorage.getReservationDao().findAll();
    }

    @Override
    public List<ReservationPrestation> getAllReservationService(){
        return dataStorage.getPrestationDao().findAllReservationService();}

    @Override
    public List<Prestation> getAllPrestations() {
        return dataStorage.getPrestationDao().findAll();
    }
}
