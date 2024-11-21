package mma.admin.infrastructures.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDataStorageFactory {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public SQLDataStorageFactory(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public SQLDataStorage getSQLDataStorage() {
        try{
            Connection connection= DriverManager.getConnection(dbUrl,dbUser,dbPassword);
            if (connection != null) {
                System.out.println("La connexion est réussie !");
            } else {
                System.out.println("Échec de la connexion.");
            }
            return new SQLDataStorage(connection);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to create SQLDataStorage");
        }
    }
}

