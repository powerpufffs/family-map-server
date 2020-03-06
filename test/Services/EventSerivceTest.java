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
import Requests.EventRequest;
import Responses.SingleEventResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

public class EventSerivceTest {
    private Database db;
    private User user = new User(
            "Hello",
            "IncrediblePassword",
            "johnny@gmail.com",
            "John",
            "Gailey",
            "m",
            "idid"
    );
    private Event event = new Event(
            "EventId",
            user.getUserName(),
            user.getPersonId(),
            1.04f,
            1.05f,
            "Hong Kong",
            "North Point",
            "marriage",
            1995
    );
    private AuthToken authToken = new AuthToken(
            "authTokenId1",
            user.getPersonId(),
            user.getUserName()
    );

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    private void typicalSetup() throws DataAccessException {
        db.openConnection();
        // Create User, Person, Event
        UserDao userdao = new UserDao(db.getConnection());
        userdao.insert(user);

        PersonDao personDao = new PersonDao(db.getConnection());
        personDao.insert(new Person(user));

        EventDao eventDao = new EventDao(db.getConnection());
        eventDao.insert(event);

        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
        authTokenDao.insertAuthToken(authToken);
        db.closeConnection(true);
    }

    @Test
    public void getSingleEventPositive() throws DataAccessException {
        typicalSetup();

        SingleEventResponse response = EventService.getSingleEvent(new EventRequest(
            event.getEventId(),
            authToken.getTokenId()
        ));

        assertEquals(response.getEventId(), event.getEventId());
        assertEquals(response.getAssociatedUsername(), event.getAssociatedUsername());
        assertEquals(response.getPersonId(), event.getPersonId());
        assertEquals(response.getLatitude(), event.getLatitude());
        assertEquals(response.getLongitude(), event.getLongitude());
        assertEquals(response.getCountry(), event.getCountry());
        assertEquals(response.getCity(), event.getCity());
        assertEquals(response.getEventType(), event.getEventType());
        assertEquals(response.getYear(), event.getYear());
    }

    @Test
    public void getSingleEventNegative() throws DataAccessException {
        typicalSetup();
        SingleEventResponse response = EventService.getSingleEvent(new EventRequest(
                "wrongId",
                authToken.getTokenId()
        ));
        assertEquals(response.getError().getMessage(), SingleEventResponse.INVALID_EVENT_ID);
    }

    @Test
    public void getSingleEventUnauthenticated() throws DataAccessException {
        typicalSetup();
        System.out.println("nihao");
        db.openConnection();
        AuthToken anotherAuth = new AuthToken(
            "secondToken",
            "randomId",
            "randomUserName"
        );
        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
        authTokenDao.insertAuthToken(authToken);
        db.closeConnection(true);

//        SingleEventResponse response = EventService.getSingleEvent(new EventRequest(
//                event.getEventId(),
//                anotherAuth.getTokenId()
//        ));
//        assertEquals(response.getError().getMessage(), SingleEventResponse.REQUESTED_EVENT_DOESNT_BELONG_TO_USER);
    }

}
