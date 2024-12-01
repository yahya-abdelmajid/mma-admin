package mma.admin.domains.interfaces;

import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.User;
import mma.admin.domains.interfaces.dao.Dao;
import mma.admin.domains.interfaces.dao.PrestationDao;
import mma.admin.domains.interfaces.dao.ReaderDao;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataStorage {
    Connection getConnection() throws SQLException;
    void beginTransaction() throws SQLException;
    void commitTransaction() throws SQLException;
    void rollbackTransaction() throws SQLException;
    ReaderDao<User> getUserDao();
    ReaderDao<Room> getRoomDao();
    PrestationDao getPrestationDao();
    Dao<Reservation> getReservationDao();
    void closeConnection() throws SQLException;
}
