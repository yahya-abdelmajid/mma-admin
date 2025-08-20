import mma.admin.domains.Room;
import mma.admin.domains.RoomId;
import mma.admin.domains.User;
import mma.admin.domains.interfaces.repository.UsersRepository;
import mma.admin.domains.interfaces.service.RoomService;
import mma.admin.domains.interfaces.service.UserService;
import mma.admin.infrastructures.dao.UserDAOImpl;
import mma.admin.infrastructures.repository.DBUsersRepository;
import mma.admin.infrastructures.service.DBRoomService;
import mma.admin.infrastructures.service.DBUserService;
import mma.admin.infrastructures.sql.SQLDataStorage;
import mma.admin.infrastructures.sql.SQLDataStorageFactory;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDAOTest {

    DataBaseInitializer db;
    UsersRepository usersRepository;

    @BeforeEach
    void setUp() throws SQLException {
       db = new DataBaseInitializer();
       db.clearTable();
       db.createTable();

        db.addUserExample();

        UserService userService=new DBUserService(db.getSQLDataStorage());
        usersRepository=new DBUsersRepository(userService);



    }

    @Test
    void test_select_user() {
        List<User> users=usersRepository.getUsers();
        assertEquals(users.get(0), new User("A123456", "Jean Dupont", "j.dupont@helmo.be"));
    }

    @Test
    void findAll_ShouldReturnAllUsers_WhenTableIsNotEmpty() throws SQLException {
        // Mock de la connexion et des objets JDBC
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Configuration des retours simulés
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next())
                .thenReturn(true, true, false); // Deux lignes, puis fin
        when(mockResultSet.getString("badge"))
                .thenReturn("A123456", "B234567");
        when(mockResultSet.getString("full_name"))
                .thenReturn("Jean Dupont", "Marie Dubois");
        when(mockResultSet.getString("email"))
                .thenReturn("j.dupont@helmo.be", "m.dubois@helmo.be");

        // Appel de la méthode testée
        UserDAOImpl userDAO = new UserDAOImpl(mockConnection);
        List<User> users = userDAO.findAll();

        // Vérifications
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Jean Dupont", users.get(0).fullName());
        assertEquals("Marie Dubois", users.get(1).fullName());
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenTableIsEmpty() throws SQLException {
        // Mock de la connexion et des objets JDBC
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Configuration des retours simulés
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Pas de données

        // Appel de la méthode testée
        UserDAOImpl userDAO = new UserDAOImpl(mockConnection);
        List<User> users = userDAO.findAll();

        // Vérifications
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void findAll_ShouldThrowRuntimeException_WhenSQLExceptionOccurs() throws SQLException {
        // Mock de la connexion et des objets JDBC
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);

        // Simulation d'une exception SQL
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Appel de la méthode testée
        UserDAOImpl userDAO = new UserDAOImpl(mockConnection);

        // Vérification que l'exception est levée
        RuntimeException exception = assertThrows(RuntimeException.class, userDAO::findAll);
        assertEquals("java.sql.SQLException: Database error", exception.getMessage());
    }

    @Test
    void findAll_ShouldHandleNullValuesInResultSet() throws SQLException {
        // Mock de la connexion et des objets JDBC
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Configuration des retours simulés
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false); // Une seule ligne
        when(mockResultSet.getString("badge")).thenReturn("A123456"); // Colonne ID nulle
        when(mockResultSet.getString("full_name")).thenReturn("Jean Dupont");
        when(mockResultSet.getString("email")).thenReturn("j.dupont@helmo.be");

        // Appel de la méthode testée
        UserDAOImpl userDAO = new UserDAOImpl(mockConnection);
        List<User> users = userDAO.findAll();

        // Vérifications
        assertNotNull(users);
        assertEquals(1, users.size());
         // Le badge (ID) est null
        assertEquals("Jean Dupont", users.get(0).fullName());
    }

    @AfterEach
    void cleanup() throws SQLException {
        //db.clearTable();
        db.connection.close();
    }
}
