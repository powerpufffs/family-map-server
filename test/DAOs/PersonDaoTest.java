package DAOs;

import DAOs.PersonDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Models.Person;
//import org.junit.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {
    private Database db;
    private Person person;

//    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        person = new Person(
            "1234",
            "samplePersonname",
            "Hello",
            "World",
            "M",
            "John123",
            "Julie123",
            "Janessa123"
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
        Person comparePerson = null;

        try {
            Connection connection = db.openConnection();
            PersonDao personDao = new PersonDao(connection);
            personDao.insert(person);
            comparePerson = personDao.readOnePersons(person.getPersonId());

            assertNotNull(comparePerson);
            assertEquals(person.getPersonId(), comparePerson.getPersonId());

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
        PersonDao personDao = null;

        try {
            connection = db.openConnection();
            personDao = new PersonDao(connection);
            personDao.insert(person);

            // Test that assertion is thrown in duplicate insert
            PersonDao finalPersonDao = personDao;
            assertThrows(DataAccessException.class, () -> {
                finalPersonDao.insert(person);
            });
            // Test that an non-existing user doesn't exist
            assertNull(personDao.readOnePersons("thisShouldn'tWork"));
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) {

        }
    }

    @Test
    public void findPositive() throws DataAccessException {
        setup();
        Person comparePerson = null;

        try {
            Connection connection = db.openConnection();
            PersonDao personDao = new PersonDao(connection);

            personDao.insert(person);
            comparePerson = personDao.readOnePersons(person.getPersonId());
            assertNotNull(comparePerson);

            comparePerson.setPersonId("somethingElse");
            personDao.insert(comparePerson);
            assertNotNull(personDao.readOnePersons(comparePerson.getPersonId()));

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
        Person comparePerson = null;

        try {
            Connection connection = db.openConnection();
            PersonDao personDao = new PersonDao(connection);
            comparePerson = personDao.readOnePersons("any");

            personDao.insert(person);
            personDao.deleteAllPersons();

            comparePerson = personDao.readOnePersons(person.getPersonId());
            assertNull(comparePerson);
            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) {
        }
    }

    @Test
    public void deleteAllPersonsPositive() throws DataAccessException {
        setup();
        Person comparePerson = null;

        try {
            Connection connection = db.openConnection();
            PersonDao personDao = new PersonDao(connection);

            //Insert user then delete tables
            personDao.insert(person);
            assertNotNull(personDao.readOnePersons(person.getPersonId()));
            personDao.deleteAllPersons();

            //Check that person no longer exists
            assertNull(personDao.readOnePersons(person.getPersonId()));
            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) {

        }
    }

    @Test
    public void deleteAllPersonsForUserPositive() throws DataAccessException {
        setup();
        Person secondPerson = new Person(
        "secondId",
        "secondEvent",
        "FirstHallo",
        "LastHallo",
        "m",
        "Jack",
        "Jill",
        "Gloria"
        );

        try {
            Connection connection = db.openConnection();
            PersonDao personDao = new PersonDao(connection);

            //Insert users then delete table
            personDao.insert(person);
            personDao.insert(secondPerson);
            assertNotNull(personDao.readOnePersons(person.getPersonId()));
            assertNotNull(personDao.readOnePersons(secondPerson.getPersonId()));
            personDao.deleteAllPersonsForUser(person.getAssociatedUsername());

            //Check that person for user no longer exists
            assertNull(personDao.readOnePersons(person.getPersonId()));

            //Check that other person is still in the db
            assertNotNull(personDao.readOnePersons(secondPerson.getPersonId()));
            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) { }
    }
}