package Services;

import DAOs.*;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Responses.ClearResponse;

// Deletes all data from DB. Includes user accounts, auth tokens and generated persons and events.
public class ClearService {

    public static ClearResponse clear(Boolean testing) {
        Database db = new Database();

        try {
            db.openConnection(testing);
            UserDao userDao = new UserDao(db.getConnection());
            PersonDao personDao = new PersonDao(db.getConnection());
            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            EventDao eventDao = new EventDao(db.getConnection());

            userDao.deleteAllUsers();
            personDao.deleteAllPersons();
            authTokenDao.deleteAllAuthTokens();
            eventDao.deleteAllEvents();

            db.closeConnection(true);
            return new ClearResponse(ClearResponse.CLEAR_SUCCESSFUL_MESSAGE);
        } catch (DataAccessException e) {
            return new ClearResponse(new FMSError(ClearResponse.INTERNAL_SERVER_ERROR));
        } finally {
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
            }
        }
    }

    public static ClearResponse clear() {
        return clear(false);
    }
}
