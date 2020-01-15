package DAOs;

import Helpers.DataAccessException;
import Models.User;

import java.sql.*;

public class UserDao {

    private final Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO user (userName, password, email, first_name, "
            + "last_name, gender, person_id) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonId());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public User readOneUser(String userName) throws DataAccessException {
        if(userName == null) {
            throw new DataAccessException("Error. userName was null when attempting to read one user");
        }

        User user = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE userName = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            if(userName == null)
                throw new SQLException();

            stmt.setString(1, userName);
            rs = stmt.executeQuery();

            if(rs.next()) {
                user = new User(
                    rs.getString("userName"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("gender"),
                    rs.getString("person_id")
                );
                return user;
            }
        } catch(SQLException e) {
            throw new DataAccessException("Error encountered while finding a user from the database.");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DataAccessException("Error encountered while closing result set while reading user");
                }
            }
        }
        return null;
    }

    public void updatePersonIdForUser(User user) throws DataAccessException {
        String sql = "UPDATE user " +
                     "SET person_id = ? " +
                     "WHERE userName = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getPersonId());
            stmt.setString(2, user.getUserName());
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new DataAccessException("Error encountered while updating personId for given user.");
        }
    }

    public static User[] readAllUsers(String userId) { return null; }

    public void deleteAllUsers() throws DataAccessException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM user;");
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting all users from the database.");
        }
    }
}
