import mma.admin.domains.Room;
import mma.admin.domains.RoomId;
import mma.admin.domains.User;
import mma.admin.domains.interfaces.repository.RoomsRepository;
import mma.admin.domains.interfaces.service.RoomService;
import mma.admin.domains.interfaces.service.UserService;
import mma.admin.infrastructures.dao.RoomDAOImpl;
import mma.admin.infrastructures.repository.DBRoomsRepository;
import mma.admin.infrastructures.service.DBRoomService;
import mma.admin.infrastructures.service.DBUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RoomDAOTest {

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
    void test_select_room() {
        //SQLiteOperations.insertData("Charlie", 28);
        // Ajouter des assertions pour vérifier les données.

        List<Room> rooms=roomsRepository.getRooms();

        assertEquals(rooms.get(0),  new Room(new RoomId("LC1"), "Learning Center 1", 20));
    }

    @Test
    void findAll_ShouldCoverAllBranches() throws SQLException {
        // Mock de la connexion et des objets JDBC
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Configuration des retours simulés pour le cas nominal
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        // Cas 1 : Une ligne dans le résultat
        when(mockResultSet.next()).thenReturn(true, false); // Une seule ligne
        when(mockResultSet.getString("roomId")).thenReturn("LC1");
        when(mockResultSet.getString("name")).thenReturn("Learning Center 1");
        when(mockResultSet.getInt("capacity")).thenReturn(20);

        // Appel de la méthode testée
        RoomDAOImpl roomDAO = new RoomDAOImpl(mockConnection);
        List<Room> rooms = roomDAO.findAll();

        // Vérifications pour le cas nominal
        assertNotNull(rooms);
        assertEquals(1, rooms.size());
        assertEquals("LC1", rooms.get(0).roomId().id());
        assertEquals("Learning Center 1", rooms.get(0).name());
        assertEquals(20, rooms.get(0).capacity());

        // Cas 2 : Pas de résultat
        when(mockResultSet.next()).thenReturn(false); // Aucun résultat
        rooms = roomDAO.findAll();

        // Vérifications pour une table vide
        assertNotNull(rooms);
        assertTrue(rooms.isEmpty());

        // Cas 3 : Exception SQL
        when(mockStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, roomDAO::findAll);
        assertEquals("java.sql.SQLException: Database error", exception.getMessage());

        // Vérification des interactions avec les mocks
        verify(mockConnection, atLeastOnce()).prepareStatement(anyString());
        verify(mockStatement, atLeastOnce()).executeQuery();
        verify(mockResultSet, atLeastOnce()).next();
    }

    @AfterEach
    void cleanup() throws SQLException {
        db.clearTable();
        db.connection.close();
    }
}
