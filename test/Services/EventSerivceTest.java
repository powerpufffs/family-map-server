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
import Responses.MultipleEventsResponse;
import Responses.SingleEventResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
            event.getEventID(),
            authToken.getTokenId()
        ));

        assertEquals(response.getEventID(), event.getEventID());
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
        assertEquals(SingleEventResponse.INVALID_EVENT_ID, response.getError().getMessage());
    }

    @Test
    public void getSingleEventUnauthenticated() throws DataAccessException {
        typicalSetup();
        db.openConnection();
        AuthToken anotherAuth = new AuthToken(
            "secondToken",
            "randomId",
            "randomUserName"
        );
        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
        authTokenDao.insertAuthToken(anotherAuth);
        db.closeConnection(true);

        SingleEventResponse response = EventService.getSingleEvent(new EventRequest(
                event.getEventID(),
                anotherAuth.getTokenId()
        ));
        assertEquals(SingleEventResponse.REQUESTED_EVENT_DOESNT_BELONG_TO_USER, response.getError().getMessage());
    }

    // Multiple Events
    @Test
    public void getMultipleEventsPositive() throws DataAccessException {
        typicalSetup();
        Event baptism = new Event(
                "baptismId",
                user.getUserName(),
                user.getPersonId(),
                1.04f,
                1.05f,
                "Hong Kong",
                "North Point",
                "marriage",
                2000
        );
        Event death = new Event(
                "deathId",
                user.getUserName(),
                user.getPersonId(),
                1.10f,
                1.25f,
                "Hong Kong",
                "North Point",
                "marriage",
                2010
        );
        //This event does not belong to user.
        Event death2 = new Event(
                "deathId2",
                "someoneElse",
                "someoneElse",
                1.10f,
                1.25f,
                "Hong Kong",
                "North Point",
                "marriage",
                2010
        );
        db.openConnection();
        EventDao eventDao = new EventDao(db.getConnection());
        eventDao.insert(baptism);
        eventDao.insert(death);
        eventDao.insert(death2);
        db.closeConnection(true);

        MultipleEventsResponse response = EventService.getAllEvents(new EventRequest(
            "",
            authToken.getTokenId()
        ));
        List<SingleEventResponse> responses = response.getData();

        assertTrue(responses.size() == 3);
    }

    @Test
    public void getMultipleEventsInvalidAuth() throws DataAccessException {
        typicalSetup();
        Event baptism = new Event(
                "baptismId",
                user.getUserName(),
                user.getPersonId(),
                1.04f,
                1.05f,
                "Hong Kong",
                "North Point",
                "marriage",
                2000
        );
        Event death = new Event(
                "deathId",
                user.getUserName(),
                user.getPersonId(),
                1.10f,
                1.25f,
                "Hong Kong",
                "North Point",
                "marriage",
                2010
        );
        db.openConnection();
        EventDao eventDao = new EventDao(db.getConnection());
        eventDao.insert(baptism);
        eventDao.insert(death);
        db.closeConnection(true);

        MultipleEventsResponse response = EventService.getAllEvents(new EventRequest(
                "",
                "wrongToken"
        ));

        assertEquals(MultipleEventsResponse.INVALID_AUTH_TOKEN_ERROR, response.getError().getMessage());
    }
}
