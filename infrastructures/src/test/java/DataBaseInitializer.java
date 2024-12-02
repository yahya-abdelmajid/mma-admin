import mma.admin.domains.Prestation;
import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.User;
import mma.admin.domains.interfaces.service.UserService;
import mma.admin.infrastructures.service.DBUserService;
import mma.admin.infrastructures.sql.SQLDataStorage;
import mma.admin.infrastructures.sql.SQLDataStorageFactory;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseInitializer {
    SQLDataStorageFactory sqlDataStorageFactory;
    SQLDataStorage sqlDataStorage;
    Connection connection;

    public DataBaseInitializer() throws SQLException {
        sqlDataStorageFactory=new SQLDataStorageFactory("jdbc:sqlite:src/test/resources/DBSQlite.sqlite");
        sqlDataStorage=sqlDataStorageFactory.getSQLDataStorage();
        connection=sqlDataStorage.getConnection();

    }

    public void createTable() throws SQLException {
        sqlDataStorage=sqlDataStorageFactory.getSQLDataStorage();
        String user = "CREATE TABLE IF NOT EXISTS user (" +
                "    id VARCHAR(50) PRIMARY KEY," +
                "    full_name VARCHAR(50) NOT NULL," +
                "    email VARCHAR(100) NOT NULL UNIQUE" +
                ");";

        String room = "CREATE TABLE IF NOT EXISTS room ("
                + "    id Integer NOT NULL,"
                + "    roomId VARCHAR(3) PRIMARY KEY,"
                + "    name VARCHAR(25) NOT NULL,"
                + "    capacity Integer NOT NULL"
                + ");";
        String service = "CREATE TABLE IF NOT EXISTS service ("
                + "    id Int PRIMARY KEY,"
                + "    description VARCHAR(25)"
                + ");";
        String reservation = "CREATE TABLE IF NOT EXISTS reservation ("
                + "    id INTEGER PRIMARY KEY,"
                + "    room_id VARCHAR(3),"
                + "    person_id VARCHAR(8),"
                + "    start_time Datetime,"
                + "    end_time Datetime,"
                + "    description VARCHAR(50),"
                + "    expected_person INTEGER"
                + ");";

        String reservationService = "CREATE TABLE IF NOT EXISTS reservation_service ("
                + "  reservation_id INTEGER NOT NULL,"
                + "  service_id INTEGER NOT NULL," +
                "    PRIMARY KEY (reservation_id, service_id)," +
                "    FOREIGN KEY (reservation_id) REFERENCES reservation (id) ON DELETE CASCADE," +
                "    FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE CASCADE"
                + ");";




        String[] queries = { user, room, service, reservation, reservationService };

        try(Connection connection=sqlDataStorage.getConnection()) {
            sqlDataStorage.beginTransaction();
            try{
                for (String query : queries) {
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.execute();
                        System.out.println("Table créée : " + query.split(" ")[2]); // Affiche le nom de la table
                    }
                }
                sqlDataStorage.commitTransaction();
            }catch (SQLException e){
                sqlDataStorage.rollbackTransaction();
            }
        }catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    public SQLDataStorage getSQLDataStorage() {
        return sqlDataStorage;
    }

    public void addUser(List<User> users) throws SQLException {
        sqlDataStorage=sqlDataStorageFactory.getSQLDataStorage();
        connection=sqlDataStorage.getConnection();
        String query = "INSERT INTO user (id, full_name, email) VALUES (?, ?, ?)";
        try{
            sqlDataStorage.beginTransaction();


            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (User user : users) {
                    // Remplir les paramètres pour chaque utilisateur
                    statement.setString(1, user.badge());          // ID de l'utilisateur
                    statement.setString(2, user.fullName());    // Nom complet de l'utilisateur
                    statement.setString(3, user.email());       // Email de l'utilisateur

                    // Ajouter cette requête à un batch
                    statement.execute();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de l'insertion des utilisateurs", e);
            }
            sqlDataStorage.commitTransaction();
        }catch (Exception e) {
            sqlDataStorage.rollbackTransaction();
            e.printStackTrace();
        }
    }

    public void addRoom(List<Room> rooms) throws SQLException {
        sqlDataStorage=sqlDataStorageFactory.getSQLDataStorage();
        String query = "INSERT INTO room (id, roomId, name,capacity) VALUES (?, ?, ?,?)";
        connection=sqlDataStorage.getConnection();
        try{
            sqlDataStorage.beginTransaction();

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (Room room : rooms) {
                    // Remplir les paramètres pour chaque utilisateur
                    statement.setString(1, room.getId());          // ID de l'utilisateur
                    statement.setString(2, room.roomId().id());    // Nom complet de l'utilisateur
                    statement.setString(3, room.name());       // Email de l'utilisateur
                    statement.setInt(4, room.capacity());

                    statement.execute();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de l'insertion des rooms", e);
            }
            sqlDataStorage.commitTransaction();
        }catch (Exception e) {
            sqlDataStorage.rollbackTransaction();
            e.printStackTrace();
        }
    }

    public void addPrestation(List<Prestation> prestations) throws SQLException {
        String query = "INSERT INTO service (id, description) VALUES (?, ?)";

        sqlDataStorage=sqlDataStorageFactory.getSQLDataStorage();
        connection=sqlDataStorage.getConnection();
        try{
            sqlDataStorage.beginTransaction();
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (Prestation prestation : prestations) {
                    // Remplir les paramètres pour chaque utilisateur
                    statement.setInt(1, prestation.id());          // ID de l'utilisateur
                    statement.setString(2, prestation.description());    // Nom complet de l'utilisateur

                    statement.execute();
                }

            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de l'insertion des salles", e);
            }

            sqlDataStorage.commitTransaction();
        }catch (Exception e) {
            sqlDataStorage.rollbackTransaction();
            e.printStackTrace();
        }
    }

    public void clearTable() throws SQLException {
        String user = "DELETE FROM user;";

        String room = "DELETE FROM room;";
        String reservationService = "DELETE FROM reservation_service;";
        String service = "DELETE FROM service;";
        String reservation = "DELETE FROM reservation;";



        String[] queries = { user, room,reservationService, service, reservation };


            for (String query : queries) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.execute();
                    System.out.println("Table créée : " + query.split(" ")[2]); // Affiche le nom de la table
                }
            }
    }

    public void deleteTable() {
        File dbFile = new File("../resources/DBSQlite.db");
        if (dbFile.delete()) {
            System.out.println("Base de données supprimée.");
        } else {
            System.out.println("Échec de la suppression.");
        }
    }
}
