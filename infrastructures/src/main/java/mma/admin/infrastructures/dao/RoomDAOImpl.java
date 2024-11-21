package mma.admin.infrastructures.dao;

import mma.admin.domains.Room;
import mma.admin.domains.User;
import mma.admin.infrastructures.dao.interfaces.ReaderDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements ReaderDao<Room> {

    private Connection connection;
    public RoomDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Room findById(String id) {
        String query = "SELECT * FROM room WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,id);
            ResultSet resultSet = statement.executeQuery();

            return new Room(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("capacity"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Room> findAll() {
        String query = "SELECT * FROM room";
        List<Room> rooms = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                rooms.add(new Room(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("capacity"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rooms;
    }
}
