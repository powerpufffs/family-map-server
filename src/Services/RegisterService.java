package Services;

import DAOs.AuthTokenDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Models.AuthToken;
import Models.Person;
import Models.User;
import Requests.FillRequest;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.FMSResponse;
import Responses.LoginResponse;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A Class that details the attributes and methods of a RegisterService
 */
public class RegisterService {

    /**
     * Registers a new user and create 4 generations of ancestor data.
     * @param request a RegisterRequest containing requisite data for the operation
     * @return a LoginResponse containing the results of the operation.
     */
    public static LoginResponse register(RegisterRequest request) {
        Database db = new Database();

        ArrayList<Object> list = new ArrayList<>();
        list.add(request.getUserName());
        list.add(request.getPassword());
        list.add(request.getEmail());
        list.add(request.getFirstName());
        list.add(request.getLastName());
        list.add(request.getGender());

        if(!FMSResponse.allAreNotNull(list)) {
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
            userDao.insert(user);
            personDao.insert(new Person(user));

            // Generate 4 generations of ancestor data
            FillResponse fillRes = FillService.fill(new FillRequest(
                user.getUserName(),
  4
            ));

            return LoginService.login(new LoginRequest(
                user.getUserName(),
                user.getPassword()
            ));
        } catch(DataAccessException e) {
            return new LoginResponse(new FMSError(e.getMessage()));
        } finally {
            try {
                db.closeConnection(false);
            } catch(DataAccessException e) {}
        }
    }
}
