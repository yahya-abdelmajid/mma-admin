package mma.admin.infrastructures.service;

import mma.admin.domains.Prestation;
import mma.admin.domains.Reservation;
import mma.admin.domains.interfaces.DataStorage;
import mma.admin.infrastructures.sql.SQLDataStorage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private final DataStorage dataStorage;
    public ReservationService(DataStorage dataStorage) {
        this.dataStorage=dataStorage;
    }

    public void addReservation(Reservation reservation) throws SQLException {
        try{
            dataStorage.beginTransaction();
            dataStorage.getReservationDao().insert(reservation);
            dataStorage.commitTransaction();
        }catch (Exception e) {
            dataStorage.rollbackTransaction();
            e.printStackTrace();
        }
    }

    public List<Reservation> getAllReservations() throws SQLException {
        return dataStorage.getReservationDao().findAll();
    }
}
