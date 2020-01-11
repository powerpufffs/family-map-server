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
        db.openConnection(true);
        db.createTables();
        db.closeConnection(true);
    }

    //    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection(true);
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void insertPositive() throws DataAccessException {
        setup();
        Event compareEvent = null;

        try {
            Connection connection = db.openConnection(true);
            EventDao eventDao = new EventDao(connection);
            eventDao.insert(event);
            compareEvent = eventDao.readOneEvent(event.getEventId());

            assertNotNull(compareEvent);
            assertEquals(event.getEventId(), compareEvent.getEventId());

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
            connection = db.openConnection(true);
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
            Connection connection = db.openConnection(true);
            EventDao eventDao = new EventDao(connection);

            eventDao.insert(event);
            compareEvent = eventDao.readOneEvent(event.getEventId());
            assertNotNull(compareEvent);

            compareEvent.setEventId("somethingElse");
            eventDao.insert(compareEvent);
            assertNotNull(eventDao.readOneEvent(compareEvent.getEventId()));

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
            Connection connection = db.openConnection(true);
            EventDao eventDao = new EventDao(connection);
            compareEvent = eventDao.readOneEvent("any");

            eventDao.insert(event);
            eventDao.deleteAllEvents();

            compareEvent = eventDao.readOneEvent(event.getEventId());
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
            Connection connection = db.openConnection(true);
            EventDao eventDao = new EventDao(connection);

            eventDao.insert(event);
            compareEvent = eventDao.readOneEvent(event.getEventId());
            assertNotNull(compareEvent);

            compareEvent.setEventId("somethingElse");
            eventDao.insert(compareEvent);
            assertNotNull(eventDao.readOneEvent(compareEvent.getEventId()));

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
            Connection connection = db.openConnection(true);
            EventDao eventDao = new EventDao(connection);
            compareEvent = eventDao.readOneEvent("any");

            eventDao.insert(event);
            eventDao.deleteAllEvents();

            compareEvent = eventDao.readOneEvent(event.getEventId());
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
            Connection connection = db.openConnection(true);
            EventDao eventDao = new EventDao(connection);

            //Insert users then delete table
            eventDao.insert(event);
            eventDao.insert(secondEvent);
            assertNotNull(eventDao.readOneEvent(event.getEventId()));
            assertNotNull(eventDao.readOneEvent(secondEvent.getEventId()));
            eventDao.deleteAllEvents();

            //Check that both persons no longer exist
            assertNull(eventDao.readOneEvent(event.getEventId()));
            assertNull(eventDao.readOneEvent(secondEvent.getEventId()));
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
           Connection connection = db.openConnection(true);
           EventDao eventDao = new EventDao(connection);

           //Insert users then delete table
           eventDao.insert(event);
           eventDao.insert(secondEvent);
           assertNotNull(eventDao.readOneEvent(event.getEventId()));
           assertNotNull(eventDao.readOneEvent(secondEvent.getEventId()));
           eventDao.deleteAllEventsForUser(event.getAssociatedUsername());

           //Check that event for persons no longer exist
           assertNull(eventDao.readOneEvent(event.getEventId()));

           //Check that other event is still in the db
           assertNotNull(eventDao.readOneEvent(secondEvent.getEventId()));
           db.closeConnection(true);
           tearDown();
       } catch(DataAccessException e) {
           db.closeConnection(false);
       } catch (Exception e) { }
   }
}
