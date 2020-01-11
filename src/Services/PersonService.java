package Services;

import DAOs.AuthTokenDao;
import DAOs.EventDao;
import DAOs.PersonDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Requests.EventRequest;
import Requests.PersonRequest;
import Responses.*;

public class PersonService {

    public static SinglePersonResponse getSinglePerson(PersonRequest request) {
        if(request.getAuthToken() == null) {
            return new SinglePersonResponse(new FMSError(MultipleEventsResponse.INVALID_AUTH_TOKEN_ERROR));
        }
        if(request.getPersonId() == null) {
            return new SinglePersonResponse(new FMSError(MultipleEventsResponse.INVALID_PERSONID_ERROR));
        }

        Database db = new Database();
        SinglePersonResponse response = null;

        try {
            db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = authTokenDao.readAuthToken(request.getAuthToken());

            if(authToken.getUserName() == null) {
                return new SinglePersonResponse(new FMSError(FMSResponse.INVALID_AUTH_TOKEN_ERROR));
            }

            PersonDao personDao = new PersonDao(db.getConnection());
            Person person = personDao.readOnePersons(authToken.getPersonId());

            if(person == null) {
                return new SinglePersonResponse(new FMSError(FMSResponse.INTERNAL_SERVER_ERROR));
            }

            response = new SinglePersonResponse(FMSResponse.GENERAL_SUCCESS_MESSAGE, person);
            if(response != null) {
                db.closeConnection(true);
            } else {
                db.closeConnection(false);
            }
        } catch(DataAccessException e) {
            return new SinglePersonResponse(new FMSError(FMSResponse.INTERNAL_SERVER_ERROR));
        }
        return response;
    }

    public static MultiplePersonsResponse getAllPersons(PersonRequest request) {
        if(request.getAuthToken() == null) {
            return new MultiplePersonsResponse(new FMSError(MultipleEventsResponse.INVALID_AUTH_TOKEN_ERROR));
        }
        Database db = new Database();
        MultiplePersonsResponse response = null;

        try {
            db.openConnection();

            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = authTokenDao.readAuthToken(request.getAuthToken());

            if(authToken.getUserName() == null) {
                return new MultiplePersonsResponse(new FMSError(FMSResponse.INVALID_AUTH_TOKEN_ERROR));
            }

            PersonDao personDao = new PersonDao(db.getConnection());
            Person[] person = personDao.readAllPersons(authToken);

            if(person == null) {
                return new MultiplePersonsResponse(new FMSError(FMSResponse.INTERNAL_SERVER_ERROR));
            }

            response = new MultiplePersonsResponse(FMSResponse.GENERAL_SUCCESS_MESSAGE, person);
            if(response != null) {
                db.closeConnection(true);
            } else {
                db.closeConnection(false);
            }
        } catch(DataAccessException e) {
            return new MultiplePersonsResponse(new FMSError(FMSResponse.INTERNAL_SERVER_ERROR));
        }
        return response;
    }

}
