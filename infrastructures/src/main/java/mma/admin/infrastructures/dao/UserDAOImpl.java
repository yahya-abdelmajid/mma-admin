package mma.admin.infrastructures.dao;

import mma.admin.domains.User;
import mma.admin.domains.interfaces.dao.ReaderDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements ReaderDao<User> {

    private Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User findById(String id) {
        String query = "SELECT * FROM user WHERE badge = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,id);
            ResultSet resultSet = statement.executeQuery();
            return new User(
                    resultSet.getString("badge"),
                    resultSet.getString("full_name"),
                    resultSet.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
    public List<User> findAll() {
            String query = "SELECT * FROM user";
            List<User> users = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) {
                    users.add(new User(
                            resultSet.getString("badge"),
                            resultSet.getString("full_name"),
                            resultSet.getString("email"))
                    );
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return users;
    }

}
