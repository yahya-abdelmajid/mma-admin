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

        db.addRoomExample();

        RoomService roomService = new DBRoomService(db.getSQLDataStorage());
        roomsRepository = new DBRoomsRepository(roomService);
    }


    @Test
    void findAll_ShouldCoverAllBranches_MultipleAndEmptyResults() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        // 2 lignes
        when(mockResultSet.next()).thenReturn(true, true, false);
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
        assertEquals(2, reservations.size());

        // Aucun résultat
        when(mockResultSet.next()).thenReturn(false);
        assertTrue(reservationDAO.findAll().isEmpty());

        // Exception SQL sur executeQuery
        when(mockStatement.executeQuery()).thenThrow(new SQLException("Database error"));
        RuntimeException ex = assertThrows(RuntimeException.class, reservationDAO::findAll);
        assertEquals("java.sql.SQLException: Database error", ex.getMessage());
    }

    @Test
    void findAll_ShouldCloseResources_OnSuccess() throws SQLException {
        Connection c = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(c.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(42);
        when(rs.getString("room_id")).thenReturn("LC1");
        when(rs.getString("person_id")).thenReturn("A123");
        when(rs.getTimestamp("start_time")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2024, 12, 24, 9, 0)));
        when(rs.getTimestamp("end_time")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2024, 12, 24, 11, 0)));
        when(rs.getString("description")).thenReturn("Réunion");
        when(rs.getInt("expected_person")).thenReturn(10);

        ReservationDAOImpl dao = new ReservationDAOImpl(c);
        assertDoesNotThrow(dao::findAll);
        verify(rs).close();
        verify(ps).close();
    }

    @Test
    void findAll_ShouldThrowRuntimeException_OnRowMappingStartTime() throws SQLException {
        Connection c = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(c.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("room_id")).thenReturn("LC1");
        when(rs.getString("person_id")).thenReturn("A123");
        when(rs.getTimestamp("start_time")).thenThrow(new SQLException("bad timestamp"));

        ReservationDAOImpl dao = new ReservationDAOImpl(c);
        RuntimeException ex = assertThrows(RuntimeException.class, dao::findAll);
        assertEquals("bad timestamp", ex.getCause().getMessage());
    }

    @Test
    void findAll_ShouldThrowRuntimeException_OnDescription() throws Exception {
        Connection c = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(c.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("room_id")).thenReturn("LC1");
        when(rs.getString("person_id")).thenReturn("A123");
        when(rs.getTimestamp("start_time")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2024, 12, 24, 9, 0)));
        when(rs.getTimestamp("end_time")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2024, 12, 24, 11, 0)));
        when(rs.getString("description")).thenThrow(new SQLException("bad description"));

        ReservationDAOImpl dao = new ReservationDAOImpl(c);
        RuntimeException ex = assertThrows(RuntimeException.class, dao::findAll);
        assertEquals("bad description", ex.getCause().getMessage());
    }

    @Test
    void findAll_ShouldThrowRuntimeException_OnExpectedPerson() throws Exception {
        Connection c = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(c.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("room_id")).thenReturn("LC1");
        when(rs.getString("person_id")).thenReturn("A123");
        when(rs.getTimestamp("start_time")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2024, 12, 24, 9, 0)));
        when(rs.getTimestamp("end_time")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2024, 12, 24, 11, 0)));
        when(rs.getString("description")).thenReturn("Réunion");
        when(rs.getInt("expected_person")).thenThrow(new SQLException("bad expected"));

        ReservationDAOImpl dao = new ReservationDAOImpl(c);
        RuntimeException ex = assertThrows(RuntimeException.class, dao::findAll);
        assertEquals("bad expected", ex.getCause().getMessage());
    }

    @Test
    void findAll_ShouldHandleExceptionAndCloseAlsoFails() throws Exception {
        Connection c = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(c.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("room_id")).thenReturn("LC1");
        when(rs.getString("person_id")).thenReturn("A123");
        when(rs.getTimestamp("start_time")).thenThrow(new SQLException("main error"));
        doThrow(new SQLException("close failed")).when(rs).close();

        ReservationDAOImpl dao = new ReservationDAOImpl(c);
        RuntimeException ex = assertThrows(RuntimeException.class, dao::findAll);
        assertEquals("main error", ex.getCause().getMessage());
        assertTrue(ex.getCause().getSuppressed().length > 0);
    }

    // ---- INSERT ----

    @Test
    void insert_ShouldWorkAndFailOnExecuteUpdate() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        Reservation reservation = new Reservation(
                1, "LC1", "A123",
                new TimeSlot(LocalDateTime.of(2024, 12, 24, 9, 0),
                        LocalDateTime.of(2024, 12, 24, 11, 0)),
                "Réunion", 10
        );

        ReservationDAOImpl dao = new ReservationDAOImpl(mockConnection);
        assertDoesNotThrow(() -> dao.insert(reservation));

        when(mockStatement.executeUpdate()).thenThrow(new SQLException("Insert failed"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> dao.insert(reservation));
        assertEquals("Error inserting reservation", ex.getMessage());
    }

    @Test
    void insert_ShouldThrowRuntimeException_OnPrepareStatement() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("prep failed"));

        Reservation reservation = new Reservation(
                1, "LC1", "A123",
                new TimeSlot(LocalDateTime.of(2024, 12, 24, 9, 0),
                        LocalDateTime.of(2024, 12, 24, 11, 0)),
                "Réunion", 10
        );

        ReservationDAOImpl dao = new ReservationDAOImpl(mockConnection);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> dao.insert(reservation));
        assertEquals("prep failed", ex.getCause().getMessage());
    }

    @Test
    void insert_ShouldThrowRuntimeException_OnSetTimestamp() throws Exception {
        Connection c = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(c.prepareStatement(anyString())).thenReturn(ps);
        doThrow(new SQLException("set ts failed")).when(ps).setTimestamp(eq(4), any());

        Reservation r = new Reservation(
                1, "LC1", "A123",
                new TimeSlot(LocalDateTime.of(2024, 12, 24, 9, 0),
                        LocalDateTime.of(2024, 12, 24, 11, 0)),
                "Réunion", 10
        );

        ReservationDAOImpl dao = new ReservationDAOImpl(c);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> dao.insert(r));
        assertEquals("set ts failed", ex.getCause().getMessage());
    }

    @Test
    void insert_ShouldThrowRuntimeException_OnSetStringRoomId() throws Exception {
        Connection c = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(c.prepareStatement(anyString())).thenReturn(ps);
        doThrow(new SQLException("set string failed")).when(ps).setString(eq(2), anyString());

        Reservation r = new Reservation(
                1, "LC1", "A123",
                new TimeSlot(LocalDateTime.of(2024, 12, 24, 9, 0),
                        LocalDateTime.of(2024, 12, 24, 11, 0)),
                "Réunion", 10
        );

        ReservationDAOImpl dao = new ReservationDAOImpl(c);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> dao.insert(r));
        assertEquals("set string failed", ex.getCause().getMessage());
    }

    @Test
    void insert_ShouldThrowRuntimeException_OnStatementClose() throws Exception {
        Connection c = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(c.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);
        doThrow(new SQLException("ps close failed")).when(ps).close();

        Reservation r = new Reservation(
                1, "LC1", "A123",
                new TimeSlot(LocalDateTime.of(2024, 12, 24, 9, 0),
                        LocalDateTime.of(2024, 12, 24, 11, 0)),
                "Réunion", 10
        );

        ReservationDAOImpl dao = new ReservationDAOImpl(c);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> dao.insert(r));
        assertEquals("ps close failed", ex.getCause().getMessage());
    }

    @AfterEach
    void cleanup() throws SQLException {
        db.clearTable();
        db.connection.close();
    }
}
