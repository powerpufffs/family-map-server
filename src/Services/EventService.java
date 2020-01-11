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
import Responses.FMSResponse;
import Responses.MultipleEventsResponse;
import Responses.SingleEventResponse;

public class EventService {

    public static SingleEventResponse getSingleEvent(EventRequest request) {
        if(request.getAuthToken() == null) {
            return new SingleEventResponse(new FMSError(MultipleEventsResponse.INVALID_AUTH_TOKEN_ERROR));
        }
        if(request.getEventId() == null) {
            return new SingleEventResponse(new FMSError(MultipleEventsResponse.INVALID_PERSONID_ERROR));
        }

        Database db = new Database();
        SingleEventResponse response = null;

        try {
            db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = authTokenDao.readAuthToken(request.getAuthToken());

            if(authToken.getUserName() == null) {
                return new SingleEventResponse(new FMSError(FMSResponse.INVALID_AUTH_TOKEN_ERROR));
            }

            EventDao eventDao = new EventDao(db.getConnection());
            Event event = eventDao.readOneEvent(authToken.getPersonId());

            if(event == null) {
                return new SingleEventResponse(new FMSError(FMSResponse.INTERNAL_SERVER_ERROR));
            }

            response = new SingleEventResponse(FMSResponse.GENERAL_SUCCESS_MESSAGE, event);
            if(response != null) {
                db.closeConnection(true);
            } else {
                db.closeConnection(false);
            }
        } catch(DataAccessException e) {
            return new SingleEventResponse(new FMSError(FMSResponse.INTERNAL_SERVER_ERROR));
        }
        return response;
    }

    public static MultipleEventsResponse getAllEvents(EventRequest request) {
        if(request.getAuthToken() == null) {
            return new MultipleEventsResponse(new FMSError(MultipleEventsResponse.INVALID_AUTH_TOKEN_ERROR));
        }
        Database db = new Database();
        MultipleEventsResponse response = null;

        try {
            db.openConnection();

            AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = authTokenDao.readAuthToken(request.getAuthToken());

            if(authToken.getUserName() == null) {
                return new MultipleEventsResponse(new FMSError(FMSResponse.INVALID_AUTH_TOKEN_ERROR));
            }

            EventDao eventDao = new EventDao(db.getConnection());
            Event[] events = eventDao.readAllEvents(authToken);

            if(events == null) {
                return new MultipleEventsResponse(new FMSError(FMSResponse.INTERNAL_SERVER_ERROR));
            }

            response = new MultipleEventsResponse(FMSResponse.GENERAL_SUCCESS_MESSAGE, events);
            if(response != null) {
                db.closeConnection(true);
            } else {
                db.closeConnection(false);
            }
        } catch(DataAccessException e) {
            return new MultipleEventsResponse(new FMSError(FMSResponse.INTERNAL_SERVER_ERROR));
        }
        return response;
    }

}
