package Services;

import DAOs.AuthTokenDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoginRequest;
import Responses.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    Database db;
    private User user = new User(
            "Hello",
            "IncrediblePassword",
            "johnny@gmail.com",
            "John",
            "Gailey",
            "m",
            "idid"
    );
    private Person person = new Person(user);
    private AuthToken authToken = new AuthToken(
            "authTokenId1",
            user.getPersonId(),
            user.getUserName()
    );

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        db.openConnection();
        db.clearTables();

        UserDao userDao = new UserDao(db.getConnection());
        userDao.insert(user);

        PersonDao personDao = new PersonDao(db.getConnection());
        personDao.insert(person);

        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
        authTokenDao.insertAuthToken(authToken);

        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void loginPositive() {
        LoginResponse response = LoginService.login(new LoginRequest(
            user.getUserName(),
            user.getPassword()
        ));

        assertNotNull(response.getAuthToken());
        assertEquals(user.getPersonId(), response.getPersonID());
        assertNotNull(user.getUserName(), response.getUserName());
    }

    @Test
    public void loginInvalidOrMissingUsername() {
        LoginResponse response = LoginService.login(new LoginRequest(
            "wrong",
            user.getPassword()
        ));
        assertEquals(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR, response.getError().getMessage());

        response = LoginService.login(new LoginRequest(
            null,
            user.getPassword()
        ));
        assertEquals(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR, response.getError().getMessage());

    }

    @Test
    public void loginInvalidOrMissingPassword() {
        LoginResponse response = LoginService.login(new LoginRequest(
                user.getUserName(),
                "wrong"
        ));
        assertEquals(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR, response.getError().getMessage());

        response = LoginService.login(new LoginRequest(
                user.getUserName(),
                "wrong"
        ));
        assertEquals(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR, response.getError().getMessage());
    }
}
