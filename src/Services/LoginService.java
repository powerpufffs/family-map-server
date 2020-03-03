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

/**
 * A Class that specifies the attributes and methods of a LoginService.
 */
public class LoginService {
    /**
     * Attempts to log a user in.
     * @param request a request object containing a username and a password
     * @return a loginResponse object containing an error,
     *         if failed and an authToken object if succeeded
     */
    public static LoginResponse login(LoginRequest request) {
        Database db = new Database();
        User user = null;
        LoginResponse res = null;

        try {
            db.openConnection();
            UserDao userDao = new UserDao(db.getConnection());
            user = userDao.readOneUser(request.getUserName());

            if(user == null) {
                return new LoginResponse(new FMSError(LoginResponse.INTERNAL_SERVER_ERROR));
            }

            if(request.getPassword() != user.getPassword()) {
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
