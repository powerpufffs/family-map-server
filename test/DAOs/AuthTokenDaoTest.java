package DAOs;

import Helpers.DataAccessException;
import Helpers.Database;
import Models.AuthToken;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDaoTest {
    private Database db;
    private AuthToken authToken;

    //    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        authToken = new AuthToken(
            "randomTokenId",
            "randomPersonId",
            "randomUserName"
        );
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    //    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void insertPositive() throws DataAccessException {
        setup();
        AuthToken compareToken = null;

        try {
            Connection connection = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(connection);
            authTokenDao.insertAuthToken(authToken);
            compareToken = authTokenDao.readAuthToken(authToken.getTokenId());

            assertNotNull(compareToken);
            assertEquals(authToken.getTokenId(), compareToken.getTokenId());

            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) {

        }
    }

    @Test
    public void insertNegative() throws DataAccessException {
        setup();
        Connection connection = null;
        AuthTokenDao authTokenDao = null;

        try {
            connection = db.openConnection();
            authTokenDao = new AuthTokenDao(connection);
            authTokenDao.insertAuthToken(authToken);

            // Test that assertion is thrown in duplicate insert
            AuthTokenDao finalAuthTokenDao = authTokenDao;
            assertThrows(DataAccessException.class, () -> {
                finalAuthTokenDao.insertAuthToken(authToken);
            });
            // Test that an non-existing user doesn't exist
            assertNull(authTokenDao.readAuthToken("thisShouldn'tWork"));
            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) {

        }
    }

    @Test
    public void findPositive() throws DataAccessException {
        setup();
        AuthToken compareAuthToken = null;

        try {
            Connection connection = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(connection);

            authTokenDao.insertAuthToken(authToken);
            compareAuthToken = authTokenDao.readAuthToken(authToken.getTokenId());
            assertNotNull(compareAuthToken);

            compareAuthToken.setTokenId("somethingElse");
            authTokenDao.insertAuthToken(compareAuthToken);
            assertNotNull(authTokenDao.readAuthToken(compareAuthToken.getTokenId()));

            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) {
        }
    }

    @Test
    public void findNegative() throws DataAccessException {
        setup();
        AuthToken compareAuthToken = null;

        try {
            Connection connection = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(connection);
            compareAuthToken = authTokenDao.readAuthToken("any");

            authTokenDao.insertAuthToken(authToken);
            authTokenDao.deleteAllAuthTokens();

            compareAuthToken = authTokenDao.readAuthToken(authToken.getTokenId());
            assertNull(compareAuthToken);
            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) { }
    }

    @Test
    public void deletePositive() throws DataAccessException {
        setup();
        AuthToken compareAuthToken = null;

        try {
            Connection connection = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(connection);

            //Insert user then delete table
            authTokenDao.insertAuthToken(authToken);
            assertNotNull(authTokenDao.readAuthToken(authToken.getTokenId()));
            authTokenDao.deleteAllAuthTokens();

            //Check that person no longer exists
            assertNull(authTokenDao.readAuthToken(authToken.getTokenId()));
            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) {

        }
    }
}
