package mma.admin.infrastructures.dao;

import mma.admin.domains.Reservation;
import mma.admin.domains.TimeSlot;
import mma.admin.domains.User;
import mma.admin.infrastructures.dao.interfaces.Dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements Dao<Reservation> {

    private Connection connection;
    public ReservationDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Reservation findById(String id) {
        String query = "SELECT * FROM user WHERE badge = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,id);
            ResultSet resultSet = statement.executeQuery();

            return new Reservation(
                    resultSet.getString("room_id"),
                    resultSet.getString("person_id"),
                    resultSet.getTimestamp("created_date").toLocalDateTime(),
                    new TimeSlot(resultSet.getTimestamp("start_time").toLocalDateTime(),
                            resultSet.getTimestamp("end_time").toLocalDateTime()),
                    resultSet.getString("description")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reservation> findAll() {
        String query = "SELECT * FROM reservation";
        List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                reservations.add(new Reservation(
                        resultSet.getString("room_id"),
                        resultSet.getString("person_id"),
                        resultSet.getTimestamp("created_date").toLocalDateTime(),
                        new TimeSlot(resultSet.getTimestamp("start_time").toLocalDateTime(),
                                resultSet.getTimestamp("end_time").toLocalDateTime()),
                        resultSet.getString("description"))
                );
            }
        return reservations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Reservation object) {
        String query = "INSERT INTO reservation (room_id, person_id, created_date, start_time, end_time, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Remplir les paramètres de la requête préparée
            statement.setString(1, object.getRoomId().getId());           // ID de la salle
            statement.setString(2, object.getPersonId());      // ID de la personne
            statement.setDate(3, Date.valueOf(object.getDate().toLocalDate())); // Date de réservation (LocalDate -> java.sql.Date)
            statement.setTimestamp(4, Timestamp.valueOf(object.getTimeSlot().getStartTime())); // Début (LocalDateTime -> Timestamp)
            statement.setTimestamp(5, Timestamp.valueOf(object.getTimeSlot().getEndTime()));   // Fin (LocalDateTime -> Timestamp)
            statement.setString(6, object.getDescription());   // Description

            // Exécuter la requête d'insertion
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Reservation successfully inserted!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting reservation", e);
        }
    }

    @Override
    public void update(Reservation object) {

    }
}
