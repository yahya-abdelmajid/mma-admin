import mma.admin.domains.Reservation;
import mma.admin.domains.Room;
import mma.admin.domains.RoomId;
import mma.admin.domains.TimeSlot;
import mma.admin.domains.interfaces.repository.RoomsRepository;
import mma.admin.domains.interfaces.service.RoomService;
import mma.admin.infrastructures.dao.ReservationDAOImpl;
import mma.admin.infrastructures.dao.RoomDAOImpl;
import mma.admin.infrastructures.repository.DBRoomsRepository;
import mma.admin.infrastructures.service.DBRoomService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ReservationDAOTest {

    DataBaseInitializer db;
    RoomsRepository roomsRepository;


    @BeforeEach
    void setUp() throws SQLException {
       db = new DataBaseInitializer();
        db.clearTable();
       db.createTable();

        List<Room> rooms = List.of(
                new Room(new RoomId("LC1"), "Learning Center 1", 20),
                new Room(new RoomId("LC2"), "Learning Center 2", 20),
                new Room(new RoomId("LB1"), "Learning Box 1", 5),
                new Room(new RoomId("LB2"), "Learning Box 2", 5)
        );

        // Ajout des utilisateurs

        db.addRoom(rooms);

        RoomService roomService= new DBRoomService(db.getSQLDataStorage());
        roomsRepository = new DBRoomsRepository(roomService);
    }

    @Test
    void findAll_ShouldCoverAllBranches() throws SQLException {
        // Mock des objets JDBC
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Configuration des retours simulés pour le cas nominal
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        // Cas 1 : Plusieurs réservations dans le résultat
        when(mockResultSet.next()).thenReturn(true, true, false); // Deux lignes
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("room_id")).thenReturn("LC1", "LB1");
        when(mockResultSet.getString("person_id")).thenReturn("A123", "B234");
        when(mockResultSet.getTimestamp("start_time")).thenReturn(
                Timestamp.valueOf(LocalDateTime.of(2024, 12, 24, 9, 0)),
                Timestamp.valueOf(LocalDateTime.of(2024, 12, 25, 10, 0))
        );
        when(mockResultSet.getTimestamp("end_time")).thenReturn(
                Timestamp.valueOf(LocalDateTime.of(2024, 12, 24, 11, 0)),
                Timestamp.valueOf(LocalDateTime.of(2024, 12, 25, 12, 0))
        );
        when(mockResultSet.getString("description")).thenReturn("Réunion", "Atelier");
        when(mockResultSet.getInt("expected_person")).thenReturn(10, 5);

        ReservationDAOImpl reservationDAO = new ReservationDAOImpl(mockConnection);
        List<Reservation> reservations = reservationDAO.findAll();

        // Vérifications pour le cas nominal
        assertNotNull(reservations);
        assertEquals(2, reservations.size());

        Reservation first = reservations.get(0);
        assertEquals(1, first.getId());
        assertEquals("LC1", first.getRoomId().id());
        assertEquals("A123", first.getPersonId());
        assertEquals(LocalDateTime.of(2024, 12, 24, 9, 0), first.getTimeSlot().getStartTime());
        assertEquals(LocalDateTime.of(2024, 12, 24, 11, 0), first.getTimeSlot().getEndTime());
        assertEquals("Réunion", first.getDescription());
        assertEquals(10, first.getExpectedPerson());

        // Cas 2 : Aucun résultat
        when(mockResultSet.next()).thenReturn(false); // Pas de lignes
        reservations = reservationDAO.findAll();

        assertNotNull(reservations);
        assertTrue(reservations.isEmpty());

        // Cas 3 : Exception SQL
        when(mockStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, reservationDAO::findAll);
        assertEquals("java.sql.SQLException: Database error", exception.getMessage());

        // Vérification des interactions
        verify(mockConnection, atLeastOnce()).prepareStatement(anyString());
        verify(mockStatement, atLeastOnce()).executeQuery();
        verify(mockResultSet, atLeastOnce()).next();
    }

    @Test
    void insert_ShouldCoverAllBranches() throws SQLException {
        // Mock des objets JDBC
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        // Configuration du mock pour le cas nominal
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        // Création d'une réservation à insérer
        Reservation reservation = new Reservation(
                1,
                "LC1",
                "A123",
                new TimeSlot(
                        LocalDateTime.of(2024, 12, 24, 9, 0),
                        LocalDateTime.of(2024, 12, 24, 11, 0)
                ),
                "Réunion",
                10
        );

        ReservationDAOImpl reservationDAO = new ReservationDAOImpl(mockConnection);

        // Cas nominal : insertion réussie
        assertDoesNotThrow(() -> reservationDAO.insert(reservation));

        // Vérification des interactions avec le mock
        verify(mockStatement).setInt(1, reservation.getId());
        verify(mockStatement).setString(2, reservation.getRoomId().id());
        verify(mockStatement).setString(3, reservation.getPersonId());
        verify(mockStatement).setTimestamp(4, Timestamp.valueOf(reservation.getTimeSlot().getStartTime()));
        verify(mockStatement).setTimestamp(5, Timestamp.valueOf(reservation.getTimeSlot().getEndTime()));
        verify(mockStatement).setString(6, reservation.getDescription());
        verify(mockStatement).setInt(7, reservation.getExpectedPerson());
        verify(mockStatement).executeUpdate();

        // Cas d'exception SQL
        when(mockStatement.executeUpdate()).thenThrow(new SQLException("Insert failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reservationDAO.insert(reservation));
        assertEquals("Error inserting reservation", exception.getMessage());

        // Vérification des interactions
        verify(mockConnection, atLeastOnce()).prepareStatement(anyString());
    }

    @AfterEach
    void cleanup() throws SQLException {
        db.clearTable();
        db.connection.close();
    }
}
