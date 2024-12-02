package mma.admin.infrastructures.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDataStorageFactory {
    private Logger logger = LogManager.getLogger();
    private final String connectionString;

    public SQLDataStorageFactory(String connectionString) {
        this.connectionString=connectionString;
    }

    public void setLOGGER(Logger logger) {
        this.logger=logger;
    }

    public SQLDataStorage getSQLDataStorage() {
        try{
            Connection connection= DriverManager.getConnection(connectionString);
            if (connection != null) {
                logger.info("La connexion est réussie !");
            } else {
                logger.error("Échec de la connexion.");
            }
            return new SQLDataStorage(connection);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to create SQLDataStorage");
        }
    }
}

