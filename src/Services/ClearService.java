package Services;

import DAOs.*;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Responses.ClearResponse;

/**
 * A Class that details the attributes and methods of a ClearResponse.
 */
public class ClearService {

    /**
     * Clears the contents of the database entirely.
     * @return a ClearResponse containing the results of the operation.
     */
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
