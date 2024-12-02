import mma.admin.infrastructures.sql.SQLDataStorage;
import mma.admin.infrastructures.sql.SQLDataStorageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SQLDataStorageFactoryTest {

    private SQLDataStorageFactory sqlDataStorageFactory;

    @BeforeEach
    void setUp() {

    }

//    @Test
//    void testSuccessfulConnectionLogs() throws SQLException {
//        // Arrange
//        Logger mockLogger = mock(Logger.class);
//        String connectionString = "jdbc:h2:mem:test";
//        SQLDataStorageFactory factory = new SQLDataStorageFactory(connectionString);
//        factory.setLOGGER(mockLogger);
//
//        // Act
//        SQLDataStorage dataStorage = factory.getSQLDataStorage();
//
//        // Assert
//        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
//        verify(mockLogger, atLeastOnce()).info(logCaptor.capture());
//
//        // Check captured logs
//        List<String> capturedLogs = logCaptor.getAllValues();
//        System.out.println(capturedLogs);
//
//        // Verify the expected log message
//        assertTrue(capturedLogs.contains("La connexion est réussie !"));
//    }
//
//    @Test
//    void testFailedConnectionLogs() {
//        Logger mockLogger = mock(Logger.class);
//        // Arrange
//        String invalidConnectionString = "jdbc:invalid://invalid";
//        SQLDataStorageFactory factory = new SQLDataStorageFactory(invalidConnectionString);
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, factory::getSQLDataStorage);
//        assertEquals("Failed to create SQLDataStorage", exception.getMessage());
//
//        // Assert logs
//        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
//        verify(mockLogger, atLeastOnce()).error(logCaptor.capture());
//        assertTrue(logCaptor.getAllValues().contains("Échec de la connexion."));
//    }

    @Test
    void testGetSQLDataStorage_SuccessfulConnection() throws SQLException {
        // Mock a successful connection
        Connection mockConnection = mock(Connection.class);
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection("jdbc:test"))
                    .thenReturn(mockConnection);

            SQLDataStorageFactory factory = new SQLDataStorageFactory("jdbc:test");
            SQLDataStorage storage = factory.getSQLDataStorage();

            assertNotNull(storage);
            mockedDriverManager.verify(() -> DriverManager.getConnection("jdbc:test"), times(1));
        }
    }

//    @Test
//    void testGetSQLDataStorage_NullConnection() throws SQLException {
//        // Mock a null connection
//        SQLDataStorageFactory factory = new SQLDataStorageFactory("jdbc:mysql://192.0.2.1:3306/test_db");
//
//        Exception exception = assertThrows(RuntimeException.class, factory::getSQLDataStorage);
//        System.out.println(exception.getMessage());
//        assertEquals("Failed to create SQLDataStorage", exception.getMessage());
//    }
}
