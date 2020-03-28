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
import Requests.PersonRequest;
import Responses.MultipleEventsResponse;
import Responses.MultiplePersonsResponse;
import Responses.SinglePersonResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {

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
    private Person person = new Person(user);
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
        personDao.insert(person);

        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
        authTokenDao.insertAuthToken(authToken);
        db.closeConnection(true);
    }

    @Test
    public void getSinglePersonPositive() throws DataAccessException {
        typicalSetup();

        SinglePersonResponse response = PersonService.getSinglePerson(new PersonRequest(
            person.getPersonId(),
            authToken.getTokenId()
        ));

        assertEquals(person.getPersonId(), response.getId());
    }

    @Test
    public void getSinglePersonInvalidParams() throws DataAccessException {
        typicalSetup();

        SinglePersonResponse response = PersonService.getSinglePerson(new PersonRequest(
            "wrong",
            authToken.getTokenId()
        ));

        assertEquals(SinglePersonResponse.PERSON_DOESNT_EXIST_ERROR, response.getError().getMessage());

        response = PersonService.getSinglePerson(new PersonRequest(
            person.getPersonId(),
    "mark zuckerberg"
        ));

        assertEquals(SinglePersonResponse.INVALID_AUTH_TOKEN_ERROR, response.getError().getMessage());
    }

    @Test
    public void getSinglePersonUnauthenticated() throws DataAccessException {
        typicalSetup();
        Person anotherPerson = new Person(
            "different",
            "bubbles",
            "buttercup",
            "blossom",
            "f",
            null,
            null,
            null
        );

        db.openConnection();
        PersonDao personDao = new PersonDao(db.getConnection());
        personDao.insert(anotherPerson);
        db.closeConnection(true);

        SinglePersonResponse response = PersonService.getSinglePerson(new PersonRequest(
            anotherPerson.getPersonId(),
            authToken.getTokenId()
        ));

        assertEquals(SinglePersonResponse.REQUESTED_PERSON_DOESNT_BELONG_TO_USER, response.getError().getMessage());
    }

    // Multiple persons

    @Test
    public void getMultiplePersonsPositive() throws DataAccessException {
        typicalSetup();
        Person anotherPerson = new Person(
            "anotherId",
            user.getUserName(),
            "buttercup",
            "blossom",
            "f",
            null,
            null,
            null
        );
        Person unrelatedPerson = new Person(
            "kakarot",
            "garry kasparov",
            "buttercup",
            "blossom",
            "f",
            null,
            null,
            null
        );

        db.openConnection();
        PersonDao personDao = new PersonDao(db.getConnection());
        personDao.insert(anotherPerson);
        db.closeConnection(true);

        MultiplePersonsResponse response = PersonService.getAllPersons(new PersonRequest(
            "",
            authToken.getTokenId()
        ));

        List<SinglePersonResponse> persons = response.getData();
        assertTrue(persons.size() == 2);
    }

    @Test
    public void getMultiplePersonsInvalidAuth() throws DataAccessException {
        typicalSetup();

        MultiplePersonsResponse response = PersonService.getAllPersons(new PersonRequest(
            "",
            "mojojojo"
        ));

        assertEquals(MultiplePersonsResponse.INVALID_AUTH_TOKEN_ERROR, response.getError().getMessage());
    }

}
