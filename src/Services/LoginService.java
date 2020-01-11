package Services;

import DAOs.AuthTokenDao;
import DAOs.UserDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Models.AuthToken;
import Models.User;
import Requests.LoginRequest;
import Responses.LoginResponse;
import java.util.UUID;


public class LoginService {
    /**
     * @param userName userName of the user seeking to log in
     * @param password password of the user seeking to log in
     * @return a loginResponse object containing an error,
     *         if failed and an authToken object if succeeded
     */
    public static LoginResponse login(String userName, String password) {
        Database db = new Database();
        User user = null;
        LoginResponse res = null;

        try {
            db.openConnection();
            UserDao userDao = new UserDao(db.getConnection());
            user = userDao.readOneUser(userName);

            if(user == null) {
                return new LoginResponse(new FMSError(LoginResponse.INTERNAL_SERVER_ERROR));
            }

            if(password != user.getPassword()) {
                return new LoginResponse(new FMSError(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR));
            }

            AuthToken authToken = new AuthToken(
                UUID.randomUUID().toString(),
                user.getPersonId(),
                user.getUserName()
            );
            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            authTokenDao.insertAuthToken(authToken);

            db.closeConnection(true);
            // Return authToken
            return new LoginResponse(
                LoginResponse.SUCCESSFUL_LOGIN_MESSAGE,
                authToken
            );

        } catch(DataAccessException e) {
            return new LoginResponse(new FMSError(LoginResponse.GENERAL_LOGIN_FAILURE_ERROR));
        } finally {
            try {
                db.closeConnection(false);
            } catch(DataAccessException e) {}
        }
    }

}
