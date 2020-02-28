package DAOs;

import Helpers.DataAccessException;
import Models.AuthToken;
import Models.User;

import java.sql.*;
import java.util.UUID;

/**
 * A Class that defines the properties and methods of an AuthTokenDao.
 */
public class AuthTokenDao {

    private final Connection connection;

    /**
     * Constructs an AuthTokenDao
     * @param connection a connection to the database.
     */
    public AuthTokenDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Saves an AuthToken into the database.
     * @param authToken the AuthToken to be saved.
     * @throws DataAccessException
     */
    public void insertAuthToken(AuthToken authToken) throws DataAccessException {
        String sql = "INSERT INTO authToken (token_id, person_id, userName) VALUES(?, ?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authToken.getTokenId());
            stmt.setString(2, authToken.getPersonId());
            stmt.setString(3, authToken.getUserName());
            stmt.execute();
        } catch(SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database.");
        }
    }

    /**
     * Reads an AuthToken from the database.
     * @param tokenId a unique id that will be used to retrieve the AuthToken.
     * @throws DataAccessException
     * @return the AuthToken that matches the id.
     */
    public AuthToken readAuthToken(String tokenId) throws DataAccessException {
        if(tokenId == null) {
            throw new DataAccessException("Error. tokenId was null when attempting to read one authToken");
        }

        AuthToken authToken = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM authToken WHERE token_id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, tokenId);
            rs = stmt.executeQuery();

            if(rs.next()) {
                return new AuthToken(
                    rs.getString("token_id"),
                    rs.getString("person_id"),
                    rs.getString("userName")
                );
            }
        } catch(SQLException e) {
            throw new DataAccessException("Error encountered while finding an auth token from the database.");
        }
        return null;
    }

    /**
     * Deletes an AuthToken currently in the Database.
     * @throws DataAccessException
     */
    public void deleteAllAuthTokens() throws DataAccessException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM authToken");
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while attempting to delete all persons.");
        }
    }

}

