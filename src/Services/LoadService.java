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
import Responses.FMSResponse;
import Responses.LoadResponse;

import java.util.List;

public class LoadService {

    public static LoadResponse load(LoadRequest request) {
        Database db = new Database();
        List<User> users = request.getUsers();
        List<Person> persons = request.getPersons();
        List<Event> events = request.getEvents();
        LoadResponse response = null;

        try {
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

            response = new LoadResponse(users, persons, events);
        } catch(DataAccessException e) {
            return new LoadResponse(new FMSError(FMSResponse.INTERNAL_SERVER_ERROR));
        }
        return response;
    }
}
