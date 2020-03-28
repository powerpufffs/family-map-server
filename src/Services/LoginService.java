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

import java.net.HttpURLConnection;
import java.util.UUID;

public class LoginService {
    public static LoginResponse login(LoginRequest request) {
        Database db = new Database();
        User user = null;
        LoginResponse res = null;

        try {
            db.openConnection();
            UserDao userDao = new UserDao(db.getConnection());
            user = userDao.readOneUser(request.getUserName());

            if(user == null) {
                db.closeConnection(false);
                return new LoginResponse(new FMSError(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR), HttpURLConnection.HTTP_BAD_REQUEST);
            }

            if(!request.getPassword().equals(user.getPassword())) {
                db.closeConnection(false);
                return new LoginResponse(new FMSError(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR), HttpURLConnection.HTTP_BAD_REQUEST);
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
            return new LoginResponse(new FMSError(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR));
        }
    }

}
