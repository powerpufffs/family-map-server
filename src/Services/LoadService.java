package Services;

import DAOs.EventDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoadRequest;
import Responses.LoadResponse;

import java.util.List;

public class LoadService {
    public static LoadResponse load(LoadRequest request) {
        Database db = new Database();
        List<User> users = request.getUsers();
        List<Person> persons = request.getPersons();
        List<Event> events = request.getEvents();

        try {
            db.openConnection();
            db.clearTables();

            //Load users
            UserDao userDao = new UserDao(db.getConnection());
            for(User user: users) {
                userDao.insert(user);
            }

            //Load persons
            PersonDao personDao = new PersonDao(db.getConnection());
            for(Person person: persons) {
                personDao.insert(person);
            }

            //Load events
            EventDao eventDao = new EventDao(db.getConnection());
            for(Event event: events) {
                eventDao.insert(event);
            }

            db.closeConnection(true);
            return new LoadResponse(users.size(), persons.size(), events.size());
        } catch(DataAccessException e) {
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) { }
            return new LoadResponse(new FMSError(e.getMessage().equals(
                DataAccessException.FIELD_WAS_NULL)
                ? LoadResponse.INVALID_REQUEST_DATA
                : LoadResponse.INTERNAL_SERVER_ERROR
            ));
        }
    }
}
