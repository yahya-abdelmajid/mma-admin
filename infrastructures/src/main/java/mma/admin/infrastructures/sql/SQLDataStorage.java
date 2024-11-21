package mma.admin.infrastructures.sql;

import mma.admin.domains.Prestation;
import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.User;
import mma.admin.infrastructures.dao.PrestationDAOImpl;
import mma.admin.infrastructures.dao.ReservationDAOImpl;
import mma.admin.infrastructures.dao.RoomDAOImpl;
import mma.admin.infrastructures.dao.UserDAOImpl;
import mma.admin.infrastructures.dao.interfaces.Dao;
import mma.admin.infrastructures.dao.interfaces.ReaderDao;
import mma.admin.infrastructures.dao.interfaces.WriterDao;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLDataStorage {
    private final Connection connection;
    private final ReaderDao<User> userDao;
    private final ReaderDao<Room> roomDao;
    private final ReaderDao<Prestation> prestationDao;
    private final Dao<Reservation> reservationDao;

    public SQLDataStorage(Connection connection) {
        this.connection = connection;
        this.userDao = new UserDAOImpl(connection);
        this.roomDao = new RoomDAOImpl(connection);
        this.prestationDao=new PrestationDAOImpl(connection);
        this.reservationDao=new ReservationDAOImpl(connection);
    }

    public ReaderDao<User> getUserDao() {
        return userDao;
    }
    public ReaderDao<Room> getRoomDao() {
        return roomDao;
    }
    public ReaderDao<Prestation> getPrestationDao() {
        return prestationDao;
    }
    public Dao<Reservation> getReservationDao() {
        return reservationDao;
    }

    public void beginTransaction() {
        try{
            connection.setAutoCommit(false);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void commitTransaction() {
        try{
            connection.commit();
            connection.setAutoCommit(true);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void rollbackTransaction() {
        try{
            connection.rollback();
            connection.setAutoCommit(true);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
