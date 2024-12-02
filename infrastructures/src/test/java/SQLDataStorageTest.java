import mma.admin.infrastructures.sql.SQLDataStorage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLDataStorageTest {

    @Test
    public void testBeginTransaction() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        SQLDataStorage sqlDataStorage = new SQLDataStorage(connection);

        sqlDataStorage.beginTransaction();

        Mockito.verify(connection).setAutoCommit(false);
    }

    @Test
    public void testCommitTransaction() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        SQLDataStorage sqlDataStorage = new SQLDataStorage(connection);

        sqlDataStorage.commitTransaction();

        Mockito.verify(connection).commit();
        Mockito.verify(connection).setAutoCommit(true);
    }

    @Test
    public void testRollbackTransaction() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        SQLDataStorage sqlDataStorage = new SQLDataStorage(connection);

        sqlDataStorage.rollbackTransaction();

        Mockito.verify(connection).rollback();
        Mockito.verify(connection).setAutoCommit(true);
    }

    @Test
    public void testCloseConnection_whenConnectionIsOpen() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.isClosed()).thenReturn(false);
        SQLDataStorage sqlDataStorage = new SQLDataStorage(connection);

        sqlDataStorage.closeConnection();

        Mockito.verify(connection).isClosed();
        Mockito.verify(connection).close();
    }

    @Test
    public void testCloseConnection_whenConnectionIsClosed() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.isClosed()).thenReturn(true);
        SQLDataStorage sqlDataStorage = new SQLDataStorage(connection);

        sqlDataStorage.closeConnection();

        Mockito.verify(connection).isClosed();
        // No need to call close on a closed connection
    }
}
