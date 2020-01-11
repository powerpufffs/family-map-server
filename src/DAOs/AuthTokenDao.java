package DAOs;

import Helpers.DataAccessException;
import Models.AuthToken;
import Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthTokenDao {

    private final Connection connection;

    public AuthTokenDao(Connection connection) {
        this.connection = connection;
    }

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

    public void deleteAllAuthTokens() throws DataAccessException {
        String sqlToDrop = "DROP TABLE IF EXISTS ?";
        String createSql = "CREATE TABLE IF NOT EXISTS ?" +
            "(" +
            "token_id TEXT PRIMARY KEY, " +
            "person_id TEXT NOT NULL, " +
            "userName TEXT NOT NULL " +
            ");";

        try(
            PreparedStatement stmt = connection.prepareStatement(sqlToDrop);
            PreparedStatement createStmt = connection.prepareStatement(createSql)
        ) {
            stmt.setString(1, "person");
            createStmt.setString(1, "person");
            stmt.executeUpdate();
            createStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while attempting to delete all persons.");
        }
    }

}

