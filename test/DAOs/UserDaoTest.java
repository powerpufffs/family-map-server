package DAOs;

import DAOs.PersonDao;
import DAOs.UserDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Models.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {
    private Database db;
    private User user;

//    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        user = new User(
                "user123",
                "hellomydear",
                "jorg@gmail.com",
                "john",
                "crusade",
                "m",
                "231415"
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
        User compareUser = null;

        try {
            Connection connection = db.openConnection();
            UserDao userDao = new UserDao(connection);
            userDao.insert(user);
            compareUser = userDao.readOneUser(user.getUserName());

            assertNotNull(compareUser);
            assertTrue(user.equals(compareUser));

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
        User compareUser = null;
        Connection connection = null;
        UserDao userDao = null;

        try {
            connection = db.openConnection();
            userDao = new UserDao(connection);
            userDao.insert(user);

            // Test that assertion is thrown in duplicate insert
            UserDao finalUserDao = userDao;
            assertThrows(DataAccessException.class, () -> {
                finalUserDao.insert(user);
            });

            // Test that an non-existing user doesn't exist
            assertNull(userDao.readOneUser("thisShouldn't Work"));

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
        User compareUser = null;

        try {
            Connection connection = db.openConnection();
            UserDao userDao = new UserDao(connection);
            userDao.insert(user);

            compareUser = userDao.readOneUser(user.getUserName());
            assertNotNull(compareUser);

            compareUser.setUserName("somethingElse");
            userDao.insert(compareUser);
            compareUser = userDao.readOneUser(compareUser.getUserName());
            assertNotNull(compareUser);

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
        User comparedUser = null;

        try {
            Connection connection = db.openConnection();
            UserDao userDao = new UserDao(connection);
            comparedUser = userDao.readOneUser("any");
            assertNull(comparedUser);

            userDao.insert(user);
            userDao.deleteAllUsers();

            comparedUser = userDao.readOneUser(user.getUserName());
            assertNull(comparedUser);

            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) {

        }
    }

    @Test
    public void deletePositive() throws DataAccessException {
        setup();
        User comparedUser = null;

        try {
            Connection connection = db.openConnection();
            UserDao userDao = new UserDao(connection);

            //Insert user then delete table
            userDao.insert(user);
            assertNotNull(userDao.readOneUser(user.getUserName()));
            userDao.deleteAllUsers();

            //Check that user no longer exists
            assertNull(userDao.readOneUser(user.getUserName()));
            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) {

        }
    }

}