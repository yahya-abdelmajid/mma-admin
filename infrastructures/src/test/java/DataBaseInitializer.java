import mma.admin.domains.*;
import mma.admin.domains.interfaces.service.UserService;
import mma.admin.infrastructures.service.DBUserService;
import mma.admin.infrastructures.sql.SQLDataStorage;
import mma.admin.infrastructures.sql.SQLDataStorageFactory;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
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
                "    badge VARCHAR(50) PRIMARY KEY," +
                "    full_name VARCHAR(50) NOT NULL," +
                "    email VARCHAR(100) NOT NULL UNIQUE" +
                ");";

        String room = "CREATE TABLE IF NOT EXISTS room (" +
                "    roomId VARCHAR(3) PRIMARY KEY," +
                "    name VARCHAR(25) NOT NULL," +
                "    capacity INTEGER NOT NULL" +
                ");";
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
        String query = "INSERT INTO user (badge, full_name, email) VALUES (?, ?, ?)";
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

    public void addUserExample() throws SQLException {
        List<User> users = new ArrayList<>();

        // Ajout des utilisateurs
        users.add(new User("A123456", "Jean Dupont", "j.dupont@helmo.be"));
        users.add(new User("B234567", "Marie Dubois", "m.dubois@helmo.be"));
        users.add(new User("C345678", "Pierre Martin", "p.martin@helmo.be"));
        users.add(new User("D456789", "Sophie Lambert", "s.lambert@helmo.be"));
        users.add(new User("E567890", "Luc Lefevre", "l.lefevre@helmo.be"));
        users.add(new User("F678901", "Claire Simon", "c.simon@helmo.be"));
        users.add(new User("G789012", "Marc Durand", "m.durand@helmo.be"));
        users.add(new User("H890123", "Julie Bernard", "j.bernard@helmo.be"));
        users.add(new User("I901234", "Paul Richard", "p.richard@helmo.be"));
        users.add(new User("J012345", "Emma Petit", "e.petit@helmo.be"));

        addUser(users);
    }

    public void addRoomExample() throws SQLException {
        List<Room> rooms = List.of(
                new Room(new RoomId("LC1"), "Learning Center 1", 20),
                new Room(new RoomId("LC2"), "Learning Center 2", 20),
                new Room(new RoomId("LB1"), "Learning Box 1", 5),
                new Room(new RoomId("LB2"), "Learning Box 2", 5)
        );

        // Ajout des utilisateurs

        addRoom(rooms);
    }

    public void addServiceExample() throws SQLException {
        List<Prestation> prestations = List.of(
                new Prestation(1, "Café et boissons"),
                new Prestation(2, "Nourriture"),
                new Prestation(3, "Projecteur"),
                new Prestation(4, "Matériel sonore")
        );
        addPrestation(prestations);
    }

    public void addRoom(List<Room> rooms) throws SQLException {
        sqlDataStorage=sqlDataStorageFactory.getSQLDataStorage();
        String query = "INSERT INTO room (roomId, name,capacity) VALUES (?, ?,?)";
        connection=sqlDataStorage.getConnection();
        try{
            sqlDataStorage.beginTransaction();

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (Room room : rooms) {
                    statement.setString(1, room.roomId().id());
                    statement.setString(2, room.name());
                    statement.setInt(3, room.capacity());

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

    public List<Reservation> getReservationsExample() {
        List<Reservation> reservations = new ArrayList<>();

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // 1ère réservation
        Reservation r1 = new Reservation(
                1,
                "LC1",
                "C345678",
                new TimeSlot(tomorrow.atTime(LocalTime.of(9, 0)), tomorrow.atTime(LocalTime.of(10, 0))),
                "Kickoff trapèze",
                15
        );
        r1.addPrestation(new Prestation(1,"Café et boissons"));
        r1.addPrestation(new Prestation(4,"Matériel sonore"));

        // 2ème réservation
        Reservation r2 = new Reservation(
                2,
                "LC1",
                "A123456",
                new TimeSlot(tomorrow.atTime(LocalTime.of(10, 0)), tomorrow.atTime(LocalTime.of(11, 0))),
                "Suivi qualité",
                13
        );
        r2.addPrestation(new Prestation(2,"Nourriture"));
        r2.addPrestation(new Prestation(3,"Projecteur"));

        // 3ème réservation
        Reservation r3 = new Reservation(
                3,
                "LB2",
                "C345678",
                new TimeSlot(tomorrow.atTime(LocalTime.of(10, 0)), tomorrow.atTime(LocalTime.of(12, 0))),
                "Suivi qualité",
                4
        );
        r3.addPrestation(new Prestation(3,"Projecteur"));
        r3.addPrestation(new Prestation(4,"Matériel sonore"));

        reservations.add(r1);
        reservations.add(r2);
        reservations.add(r3);

        return reservations;
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

    public void clearTableReservation() throws SQLException {
        String reservationService = "DELETE FROM reservation_service;";
        String reservation = "DELETE FROM reservation;";

        String[] queries = { reservationService, reservation };


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
