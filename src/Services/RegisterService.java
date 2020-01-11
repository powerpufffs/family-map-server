package Services;

import DAOs.AuthTokenDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Models.AuthToken;
import Models.User;
import Requests.RegisterRequest;
import Responses.FMSResponse;
import Responses.LoginResponse;

import java.util.ArrayList;
import java.util.UUID;

public class RegisterService {

    public static LoginResponse register(RegisterRequest request) {
        Database db = new Database();

        ArrayList<Object> list = new ArrayList<>();
        list.add(request.getUserName());
        list.add(request.getPassword());
        list.add(request.getEmail());
        list.add(request.getFirstName());
        list.add(request.getLastName());
        list.add(request.getGender());

        if(!FMSResponse.allAreNonNull(list)) {
            return new LoginResponse(new FMSError(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR));
        }

        try {
            db.openConnection();
            UserDao userDao = new UserDao(db.getConnection());
            PersonDao personDao = new PersonDao(db.getConnection());

            if(userDao.readOneUser(request.getUserName()) != null) {
               return new LoginResponse(new FMSError(LoginResponse.USER_ALREADY_EXISTS_ERROR));
            }

            String personID = null;
            do {
                personID = UUID.randomUUID().toString();
            } while (personDao.readOnePersons(personID) != null);

            User user = new User(
                request.getUserName(),
                request.getPassword(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getGender(),
                UUID.randomUUID().toString()
            );

            // Generate 4 generations of ancestor data
            // TODO: generate ancestor data on register

            //Log user in
            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            String tokenId = null;
            do {
                tokenId = UUID.randomUUID().toString();
            } while (authTokenDao.readAuthToken(tokenId) != null);

            db.closeConnection(true);

            return new LoginResponse(
                LoginResponse.SUCCESSFUL_LOGIN_MESSAGE,
                new AuthToken(tokenId, user.getPersonId(), user.getUserName())
            );
        } catch(DataAccessException e) {
            return new LoginResponse(new FMSError(e.getMessage()));
        } finally {
            try {
                db.closeConnection(false);
            } catch(DataAccessException e) {}
        }
    }
}
