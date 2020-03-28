package Services;

import DAOs.EventDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.FillRequest;
import Requests.LoadRequest;
import Requests.RegisterRequest;
import Responses.FillResponse;
import Responses.LoadResponse;
import Responses.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private Database db;
    private FillRequest request;
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
    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        db.openConnection();
        db.clearTables();

        UserDao userDao = new UserDao(db.getConnection());
        userDao.insert(user);

        db.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void fillPositive() throws FileNotFoundException, DataAccessException {
        db.openConnection();
        PersonDao personDao = new PersonDao(db.getConnection());
        personDao.insert(person);
        db.closeConnection(true);

        FillResponse response = FillService.fill(new FillRequest(user.getUserName(), 4));

        // Previous data should be gone
        assertThrows(DataAccessException.class, () -> { personDao.readOnePersons(person.getPersonId()); });

        //Check that generations were generated
        assertNull(response.getError());
        assertNotNull(response.getMessage());
    }

    @Test
    public void fillInvalidParams() {
        FillResponse response = FillService.fill(new FillRequest("wrong", 4));
        assertEquals(FillResponse.INVALID_USER_OR_GENERATIONS_ERROR, response.getError().getMessage());

        response = FillService.fill(new FillRequest(user.getUserName(), -2));
        assertEquals(FillResponse.INVALID_USER_OR_GENERATIONS_ERROR, response.getError().getMessage());
    }

}

