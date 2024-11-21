package mma.admin.infrastructures.service;

import mma.admin.domains.Reservation;
import mma.admin.domains.User;
import mma.admin.infrastructures.sql.SQLDataStorage;

import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private final SQLDataStorage dataStorage;
    public ReservationService(SQLDataStorage dataStorage) {
        this.dataStorage=dataStorage;
    }

    public void addReservationService(Reservation reservation) {
        try{
            dataStorage.beginTransaction();
            dataStorage.getReservationDao().insert(reservation);
            dataStorage.commitTransaction();
        }catch (Exception e) {
            dataStorage.rollbackTransaction();
            e.printStackTrace();
        }
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations=new ArrayList<Reservation>();
        try{
            dataStorage.beginTransaction();
            reservations=dataStorage.getReservationDao().findAll();
            dataStorage.commitTransaction();
            return reservations;
        }catch (Exception e) {
            dataStorage.rollbackTransaction();
            e.printStackTrace();
        }
        return null;
    }
}
