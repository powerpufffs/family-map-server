package Services;

import DAOs.AuthTokenDao;
import DAOs.EventDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Responses.ClearResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private Database db;
    private User user;
    private Person person;
    private Event event;
    private AuthToken authToken;

    @BeforeEach
    void setup() throws DataAccessException {
        db = new Database();
        this.user = new User(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "m",
                UUID.randomUUID().toString()
        );
        this.person = new Person(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "m",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        );
        this.event = new Event(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                12.123f,
                13f,
                "Korean",
                "Seoul",
                "Baptism",
                2010
        );
        this.authToken = new AuthToken(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        );
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @AfterEach
    void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void clearPositive() throws Exception {
        UserDao userDao = new UserDao(db.getConnection());
        PersonDao personDao = new PersonDao(db.getConnection());
        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
        EventDao eventDao = new EventDao(db.getConnection());

        userDao.insert(user);
        personDao.insert(person);
        authTokenDao.insertAuthToken(authToken);
        eventDao.insert(event);
        // Check that values are actually in the db
        assertNotNull(userDao.readOneUser(user.getUserName()));
        assertNotNull(personDao.readOnePersons(person.getPersonId()));
        assertNotNull(authTokenDao.readAuthToken(authToken.getTokenId()));
        assertNotNull(eventDao.readOneEvent(event.getEventID()));

        db.closeConnection(true);

        ClearResponse response = ClearService.clear();

        db.openConnection();

        assertThrows(DataAccessException.class, () -> {userDao.readOneUser(user.getUserName());});
        assertThrows(DataAccessException.class, () -> {personDao.readOnePersons(person.getPersonId());});
        assertThrows(DataAccessException.class, () -> {authTokenDao.readAuthToken(authToken.getTokenId());});
        assertThrows(DataAccessException.class, () -> {eventDao.readOneEvent(event.getEventID());});

        db.closeConnection(true);
    }

    @Test
    public void clearNegative() throws Exception {
        db.openConnection();

        UserDao userDao = new UserDao(db.getConnection());
        PersonDao personDao = new PersonDao(db.getConnection());
        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
        EventDao eventDao = new EventDao(db.getConnection());

        userDao.insert(user);
        personDao.insert(person);
        authTokenDao.insertAuthToken(authToken);
        eventDao.insert(event);

        // Check that values are actually in the db
        assertNotNull(userDao.readOneUser(user.getUserName()));
        assertNotNull(personDao.readOnePersons(person.getPersonId()));
        assertNotNull(authTokenDao.readAuthToken(authToken.getTokenId()));
        assertNotNull(eventDao.readOneEvent(event.getEventID()));

        db.closeConnection(true);
        ClearResponse response = ClearService.clear();
    }
}