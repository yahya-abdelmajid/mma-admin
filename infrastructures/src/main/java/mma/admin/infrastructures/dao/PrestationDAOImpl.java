package mma.admin.infrastructures.dao;

import mma.admin.domains.Prestation;
import mma.admin.domains.ReservationPrestation;
import mma.admin.domains.interfaces.dao.PrestationDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestationDAOImpl implements PrestationDao {
    private Connection connection;
    public PrestationDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Prestation findById(String id) {
        String query = "SELECT * FROM service WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,id);
            ResultSet resultSet = statement.executeQuery();

            return new Prestation(
                    resultSet.getInt("id"),
                    resultSet.getString("description"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Prestation> findAll() {
        String query = "SELECT * FROM service";
        List<Prestation> prestations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                prestations.add(new Prestation(
                        resultSet.getInt("id"),
                        resultSet.getString("description"))
                );
            }
            return prestations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReservationPrestation> findAllReservationService() {
        String query = "SELECT * FROM reservation_service";
        List<ReservationPrestation> reservationPrestation = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                reservationPrestation.add(new ReservationPrestation(
                        resultSet.getInt("reservation_id"),
                        resultSet.getInt("service_id"))
                );
            }
            return reservationPrestation;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void insert(int reservationId, int serviceId) {
        String query = "INSERT INTO reservation_service (reservation_id, service_id) " +
                "VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Remplir les paramètres de la requête préparée
            System.out.println("bonjour");
            statement.setInt(1, reservationId);
            statement.setInt(2, serviceId);
            System.out.println("bonjour");
            // Exécuter la requête d'insertion
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Reservation service successfully inserted!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting reservation", e);
        }
    }
}
