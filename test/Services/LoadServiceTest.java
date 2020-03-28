package Services;

import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoadRequest;
import Responses.LoadResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {

    private Database db;
    private LoadRequest request;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);

        List<User> users = new ArrayList<User>();
        List<Person> persons = new ArrayList<Person>();
        List<Event> events = new ArrayList<Event>();

        User user1 = new User(
                "User1",
                "IncrediblePassword",
                "johnny@gmail.com",
                "John",
                "Gailey",
                "m",
                "user1"
        );
        User user2 = new User(
                "User2",
                "IncrediblePassword",
                "user2@gmail.com",
                "user2",
                "Gailey",
                "m",
                "user2"
        );
        users.add(user1);
        users.add(user2);
        persons.add(new Person(user1));
        persons.add(new Person(user2));

        events.add(new Event(
                "EventId",
                user1.getUserName(),
                user1.getPersonId(),
                1.04f,
                1.05f,
                "Hong Kong",
                "North Point",
                "marriage",
                1995
        ));
        events.add(new Event(
                "eventId2",
                user2.getUserName(),
                user2.getPersonId(),
                1.04f,
                1.05f,
                "Japan",
                "North Point",
                "birth",
                1995
        ));
        request = new LoadRequest(users, persons, events);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void loadPositive() {
        LoadResponse response = LoadService.load(request);

        assertEquals(
            String.format(
                "Successfully added %d users, " +
                "%d persons, " +
                "and %d events to the database.",
                2, 2, 2
            ),
            response.getMessage()
        );
    }

    @Test
    public void loadFalseUser() {
        List<User> users = request.getUsers();
        users.add( new User(
            null,
            "IncrediblePassword",
            "johnny@gmail.com",
            "John",
            "Gailey",
            "m",
            "user1"
            )
        );
        request.setUsers(users);

        LoadResponse response = LoadService.load(request);
        assertEquals(LoadResponse.INVALID_REQUEST_DATA, response.getError().getMessage());
    }

    @Test
    public void loadFalsePerson() {
        List<Person> persons = request.getPersons();
        persons.add( new Person(
        null,
        "bubbles",
        "buttercup",
        "blossom",
        "f",
        null,
        null,
        null
            )
        );
        request.setPersons(persons);

        LoadResponse response = LoadService.load(request);
        assertEquals(LoadResponse.INVALID_REQUEST_DATA, response.getError().getMessage());
    }

    @Test
    public void loadFalseEvent() {
        List<Event> events = request.getEvents();
        events.add( new Event(
                null,
                "random",
                "random2",
                1.04f,
                1.05f,
                "Hong Kong",
                "North Point",
                "marriage",
                1995
            )
        );
        request.setEvents(events);

        LoadResponse response = LoadService.load(request);
        assertEquals(LoadResponse.INVALID_REQUEST_DATA, response.getError().getMessage());
    }
}

