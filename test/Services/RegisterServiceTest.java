package Services;

import DAOs.EventDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.RegisterRequest;
import Responses.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
    private Database db;

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

    @Test
    public void registerPositive() throws DataAccessException {
        RegisterRequest request = new RegisterRequest(
  "suzy",
  "password1",
     "great@gmail.com",
 "Suz",
  "Kim",
    "f"
        );

        try {
            LoginResponse response = RegisterService.register(request);

            // Validate Response json
            assertNotNull(response.getAuthToken());
            assertEquals(response.getUserName(), "suzy");
            assertNotNull(response.getPersonID());

            // Check that user is actually in db
            UserDao userDao = new UserDao(db.getConnection());
            User user = userDao.readOneUser(response.getUserName());

            assertEquals(user.getUserName(), request.getUserName());
            assertEquals(user.getPassword(), request.getPassword());
            assertEquals(user.getEmail(), request.getEmail());
            assertEquals(user.getFirstName(), request.getFirstName());
            assertEquals(user.getLastName(), request.getLastName());
            assertEquals(user.getGender(), request.getGender());

            // Check that person was created
            PersonDao personDao = new PersonDao(db.getConnection());
            Person person = personDao.readOnePersons(response.getPersonID());

            assertEquals(person.getAssociatedUsername(), user.getUserName());

            //Check that generations were generated
            AuthToken authToken = new AuthToken(response.getAuthToken(), response.getPersonID(), response.getUserName());
            Person[] persons = personDao.readAllPersons(authToken);
            assertEquals(31, persons.length);

            EventDao eventDao = new EventDao(db.getConnection());
            Event[] events = eventDao.readAllEvents(authToken);
            assertTrue(events.length >= 31*3);

            Map<String, Integer> eventsPerPerson = new HashMap<>();
            for (Event e : events) {
                if (!eventsPerPerson.containsKey(e.getPersonId())) {
                    eventsPerPerson.put(e.getPersonId(), 1);
                } else {
                    eventsPerPerson.put(
                        e.getPersonId(),
                        eventsPerPerson.get(e.getPersonId()) + 1
                    );
                }
            }

            assertEquals(persons.length, eventsPerPerson.size());

            for(String personId : eventsPerPerson.keySet()) {
                assertTrue(eventsPerPerson.get(personId) >= 3);
            }
            db.closeConnection(true);
        } catch(DataAccessException e) {
            db.closeConnection(false);
            throw e;
        } catch (Exception e) {
            db.closeConnection(false);
            throw e;
        }
    }

    @Test
    public void registerFailsIfUsernameExists() {
        LoginResponse response = RegisterService.register(new RegisterRequest(
            "suzy",
            "password1",
            "great@gmail.com",
            "Suz",
            "Kim",
            "f"
        ));

        assertNull(response.getError());

        LoginResponse response2 = RegisterService.register(new RegisterRequest(
            "suzy",
            "password1",
            "great@gmail.com",
            "Suz",
            "Kim",
            "f"
        ));

        assertEquals(response2.getError().getMessage(), LoginResponse.USER_ALREADY_EXISTS_ERROR);
    }

    @Test
    public void registerFailsIfMissingParams() {
        LoginResponse response = RegisterService.register(new RegisterRequest(
            "suzy",
            null,
            "great@gmail.com",
            "Suz",
            "Kim",
            "f"
        ));

        assertEquals(response.getError().getMessage(), LoginResponse.INVALID_OR_MISSING_INPUT_ERROR);
    }
}

