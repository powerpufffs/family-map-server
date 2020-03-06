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
    public static LoginResponse login(LoginRequest request) {
        Database db = new Database();
        User user = null;
        LoginResponse res = null;

        try {
            System.out.println("Beginning to Log In");
            db.openConnection();
            UserDao userDao = new UserDao(db.getConnection());
            user = userDao.readOneUser(request.getUserName());

            if(user == null) {
                db.closeConnection(true);
                return new LoginResponse(new FMSError(LoginResponse.INTERNAL_SERVER_ERROR));
            }

            if(!request.getPassword().equals(user.getPassword())) {
                db.closeConnection(true);
                return new LoginResponse(new FMSError(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR));
            }

            AuthToken authToken = new AuthToken(
                UUID.randomUUID().toString(),
                user.getPersonId(),
                user.getUserName()
            );
            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            authTokenDao.insertAuthToken(authToken);

            System.out.println("Ending Log in");
            db.closeConnection(true);
            // Return authToken
            return new LoginResponse(
                LoginResponse.SUCCESSFUL_LOGIN_MESSAGE,
                authToken
            );

        } catch(DataAccessException e) {
            return new LoginResponse(new FMSError(LoginResponse.GENERAL_LOGIN_FAILURE_ERROR));
        }
    }

}
