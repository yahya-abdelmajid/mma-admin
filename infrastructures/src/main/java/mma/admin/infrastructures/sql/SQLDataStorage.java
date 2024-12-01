package mma.admin.infrastructures.sql;

import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.User;
import mma.admin.domains.interfaces.DataStorage;
import mma.admin.infrastructures.dao.PrestationDAOImpl;
import mma.admin.infrastructures.dao.ReservationDAOImpl;
import mma.admin.infrastructures.dao.RoomDAOImpl;
import mma.admin.infrastructures.dao.UserDAOImpl;
import mma.admin.domains.interfaces.dao.Dao;
import mma.admin.domains.interfaces.dao.PrestationDao;
import mma.admin.domains.interfaces.dao.ReaderDao;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLDataStorage implements DataStorage {
    private final Connection connection;
    private final ReaderDao<User> userDao;
    private final ReaderDao<Room> roomDao;
    private final PrestationDao prestationDao;
    private final Dao<Reservation> reservationDao;

    public SQLDataStorage(Connection connection) {
        this.connection = connection;
        this.userDao = new UserDAOImpl(connection);
        this.roomDao = new RoomDAOImpl(connection);
        this.prestationDao=new PrestationDAOImpl(connection);
        this.reservationDao=new ReservationDAOImpl(connection);
    }

    @Override
    public ReaderDao<User> getUserDao() {
        return userDao;
    }

    @Override
    public ReaderDao<Room> getRoomDao() {
        return roomDao;
    }
    @Override
    public PrestationDao getPrestationDao() {
        return prestationDao;
    }
    @Override
    public Dao<Reservation> getReservationDao() {
        return reservationDao;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public void beginTransaction() {
        try{
            connection.setAutoCommit(false);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public void commitTransaction() {
        try{
            connection.commit();
            connection.setAutoCommit(true);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public void rollbackTransaction() {
        try{
            connection.rollback();
            connection.setAutoCommit(true);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
