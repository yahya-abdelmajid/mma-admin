package mma.admin.infrastructures.dao;

import mma.admin.domains.Prestation;
import mma.admin.domains.ReservationPrestation;
import mma.admin.domains.interfaces.dao.PrestationDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestationDAOImpl implements PrestationDao {
    private final Connection connection;
    public PrestationDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Prestation> findAll() {
        String query = "SELECT * FROM service";
        List<Prestation> prestations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) { // ResultSet géré dans try-with-resources
            while (resultSet.next()) {
                prestations.add(new Prestation(
                        resultSet.getInt("id"),
                        resultSet.getString("description"))
                );
            }
            return prestations;
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching all Prestations", e);
        }
    }

    @Override
    public List<ReservationPrestation> findAllReservationService() {
        String query = "SELECT * FROM reservation_service";
        List<ReservationPrestation> reservationPrestations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) { // ResultSet géré dans try-with-resources
            while (resultSet.next()) {
                reservationPrestations.add(new ReservationPrestation(
                        resultSet.getInt("reservation_id"),
                        resultSet.getInt("service_id"))
                );
            }
            return reservationPrestations;
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching reservations", e);
        }
    }


    @Override
    public void insert(int reservationId, int serviceId) {
        String query = "INSERT INTO reservation_service (reservation_id, service_id) " +
                "VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Remplir les paramètres de la requête préparée
            statement.setInt(1, reservationId);
            statement.setInt(2, serviceId);
            // Exécuter la requête d'insertion
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting reservation", e);
        }
    }
}
