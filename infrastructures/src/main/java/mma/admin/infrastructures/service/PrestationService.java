package mma.admin.infrastructures.service;

import mma.admin.domains.Prestation;
import mma.admin.domains.Reservation;
import mma.admin.domains.ReservationPrestation;
import mma.admin.domains.interfaces.DataStorage;
import mma.admin.infrastructures.sql.SQLDataStorage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrestationService {

    private final DataStorage dataStorage;
    public PrestationService(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
    }

    public void addReservationAndService(Reservation reservation) throws SQLException {
        try{
            dataStorage.beginTransaction();
            for(Prestation p: reservation.getPrestations()) {
                dataStorage.getPrestationDao().insert(reservation.getId(), p.getId());
            }
            dataStorage.commitTransaction();
        }catch (Exception e) {
            dataStorage.rollbackTransaction();
            e.printStackTrace();
        }
    }

    public List<ReservationPrestation> getAllReservationService() throws SQLException {
        return dataStorage.getPrestationDao().findAllReservationService();}

    public List<Prestation> getAllPrestations() {
        return dataStorage.getPrestationDao().findAll();
    }
}
