package mma.admin.infrastructures.dao;

import mma.admin.domains.Reservation;
import mma.admin.domains.TimeSlot;
import mma.admin.domains.interfaces.dao.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements Dao<Reservation> {

    private final Connection connection;
    public ReservationDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Reservation> findAll() {
        String query = "SELECT * FROM reservation";
        List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) { // Inclure ResultSet dans le try-with-resources

            while (resultSet.next()) {
                reservations.add(new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getString("room_id"),
                        resultSet.getString("person_id"),
                        new TimeSlot(
                                resultSet.getTimestamp("start_time").toLocalDateTime(),
                                resultSet.getTimestamp("end_time").toLocalDateTime()
                        ),
                        resultSet.getString("description"),
                        resultSet.getInt("expected_person")
                ));
            }
            return reservations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void insert(Reservation object) {
        String query = "INSERT INTO reservation (id,room_id, person_id, start_time, end_time, description,expected_person) " +
                "VALUES (?,?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Remplir les paramètres de la requête préparée
            statement.setInt(1, object.getId());
            statement.setString(2, object.getRoomId().id());           // ID de la salle
            statement.setString(3, object.getPersonId());      // ID de la personne
            statement.setTimestamp(4, Timestamp.valueOf(object.getTimeSlot().getStartTime())); // Début (LocalDateTime -> Timestamp)
            statement.setTimestamp(5, Timestamp.valueOf(object.getTimeSlot().getEndTime()));   // Fin (LocalDateTime -> Timestamp)
            statement.setString(6, object.getDescription());   // Description
            statement.setInt(7, object.getExpectedPerson());
            // Exécuter la requête d'insertion
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting reservation", e);
        }
    }
}
