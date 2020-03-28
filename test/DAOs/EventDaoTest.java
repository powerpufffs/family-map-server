package DAOs;

import Helpers.DataAccessException;
import Helpers.Database;
import Models.Event;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventDaoTest {
    private Database db;
    private Event event;

    //    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        event = new Event(
                "myCoolEvent",
                "supCuz",
                "randomPersonId",
                12131.12f,
                1231.23f,
                "Australia",
                "Auckland",
                "Marriage",
                2010
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
        Event compareEvent = null;

        try {
            Connection connection = db.openConnection();
            EventDao eventDao = new EventDao(connection);
            eventDao.insert(event);
            compareEvent = eventDao.readOneEvent(event.getEventID());

            assertNotNull(compareEvent);
            assertEquals(event.getEventID(), compareEvent.getEventID());

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
        EventDao eventDao = null;

        try {
            connection = db.openConnection();
            eventDao = new EventDao(connection);
            eventDao.insert(event);

            // Test that assertion is thrown in duplicate insert
            EventDao finalEventDao = eventDao;
            assertThrows(DataAccessException.class, () -> {
                finalEventDao.insert(event);
            });
            // Test that an non-existing user doesn't exist
            assertNull(eventDao.readOneEvent("thisShouldn'tWork"));
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
        Event compareEvent = null;

        try {
            Connection connection = db.openConnection();
            EventDao eventDao = new EventDao(connection);

            eventDao.insert(event);
            compareEvent = eventDao.readOneEvent(event.getEventID());
            assertNotNull(compareEvent);

            compareEvent.setEventID("somethingElse");
            eventDao.insert(compareEvent);
            assertNotNull(eventDao.readOneEvent(compareEvent.getEventID()));

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
        Event compareEvent = null;

        try {
            Connection connection = db.openConnection();
            EventDao eventDao = new EventDao(connection);
            compareEvent = eventDao.readOneEvent("any");

            eventDao.insert(event);
            eventDao.deleteAllEvents();

            compareEvent = eventDao.readOneEvent(event.getEventID());
            assertNull(compareEvent);
            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) { }
    }

    //TODO: Implement these
    @Test
    public void findAllPositive() throws DataAccessException {
        setup();
        Event compareEvent = null;

        try {
            Connection connection = db.openConnection();
            EventDao eventDao = new EventDao(connection);

            eventDao.insert(event);
            compareEvent = eventDao.readOneEvent(event.getEventID());
            assertNotNull(compareEvent);

            compareEvent.setEventID("somethingElse");
            eventDao.insert(compareEvent);
            assertNotNull(eventDao.readOneEvent(compareEvent.getEventID()));

            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) {
        }
    }

    @Test
    public void findAllNegative() throws DataAccessException {
        setup();
        Event compareEvent = null;

        try {
            Connection connection = db.openConnection();
            EventDao eventDao = new EventDao(connection);
            compareEvent = eventDao.readOneEvent("any");

            eventDao.insert(event);
            eventDao.deleteAllEvents();

            compareEvent = eventDao.readOneEvent(event.getEventID());
            assertNull(compareEvent);
            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) { }
    }

    @Test
    public void deleteAllEventsPositive() throws DataAccessException {
        setup();
        Event resultEvent = null;
        Event secondEvent = new Event(
    "secondId",
"secondEvent",
    "randomPersonId",
    12321.2234f,
    12342132132.123f,
    "Japan",
        "Tokyo",
    "Birth",
        1990
        );

        try {
            Connection connection = db.openConnection();
            EventDao eventDao = new EventDao(connection);

            //Insert users then delete table
            eventDao.insert(event);
            eventDao.insert(secondEvent);
            assertNotNull(eventDao.readOneEvent(event.getEventID()));
            assertNotNull(eventDao.readOneEvent(secondEvent.getEventID()));
            eventDao.deleteAllEvents();

            //Check that both persons no longer exist
            assertNull(eventDao.readOneEvent(event.getEventID()));
            assertNull(eventDao.readOneEvent(secondEvent.getEventID()));
            db.closeConnection(true);
            tearDown();
        } catch(DataAccessException e) {
            db.closeConnection(false);
        } catch (Exception e) { }
    }

   @Test
   public void deleteAllEventsForUsePositive() throws DataAccessException {
       setup();
       Event secondEvent = new Event(
       "secondId",
       "secondEvent",
       "randomPersonId",
       12321.2234f,
       12342132132.123f,
       "Japan",
       "Tokyo",
       "Birth",
       1990
       );

       try {
           Connection connection = db.openConnection();
           EventDao eventDao = new EventDao(connection);

           //Insert users then delete table
           eventDao.insert(event);
           eventDao.insert(secondEvent);
           assertNotNull(eventDao.readOneEvent(event.getEventID()));
           assertNotNull(eventDao.readOneEvent(secondEvent.getEventID()));
           eventDao.deleteAllEventsForUser(event.getAssociatedUsername());

           //Check that event for persons no longer exist
           assertNull(eventDao.readOneEvent(event.getEventID()));

           //Check that other event is still in the db
           assertNotNull(eventDao.readOneEvent(secondEvent.getEventID()));
           db.closeConnection(true);
           tearDown();
       } catch(DataAccessException e) {
           db.closeConnection(false);
       } catch (Exception e) { }
   }
}
