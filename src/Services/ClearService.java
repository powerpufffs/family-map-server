package Services;

import DAOs.*;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Responses.ClearResponse;

// Deletes all data from DB. Includes user accounts, auth tokens and generated persons and events.
public class ClearService {

    public static ClearResponse clear() {
        Database db = new Database();

        try {
            db.openConnection();
            UserDao userDao = new UserDao(db.getConnection());
            userDao.deleteAllUsers();

            PersonDao personDao = new PersonDao(db.getConnection());
            personDao.deleteAllPersons();

            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            authTokenDao.deleteAllAuthTokens();

            EventDao eventDao = new EventDao(db.getConnection());
            eventDao.deleteAllEvents();

            db.closeConnection(true);
            return new ClearResponse(ClearResponse.CLEAR_SUCCESSFUL_MESSAGE);
        } catch (DataAccessException e) {
            return new ClearResponse(new FMSError(ClearResponse.INTERNAL_SERVER_ERROR));
        } finally {
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) { }
        }
    }
}
