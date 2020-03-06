package Services;

import DAOs.AuthTokenDao;
import DAOs.EventDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Models.AuthToken;
import Models.Event;
import Requests.EventRequest;
import Responses.FMSResponse;
import Responses.MultipleEventsResponse;
import Responses.SingleEventResponse;

/**
 * A Class that details the attributes and methods of a EventService.
 */
public class EventService {
    /**
     * Returns a single Event that's associated with the AuthToken and EventId contained in the EventRequest.
     * @return a SingleEventResponse containing the results of the method.
     */
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

            if(authToken == null) {
                db.closeConnection(false);
                return new SingleEventResponse(new FMSError(SingleEventResponse.INVALID_AUTH_TOKEN_ERROR));
            }

            EventDao eventDao = new EventDao(db.getConnection());
            Event event = eventDao.readOneEvent(request.getEventId());

            if(event == null) {
                db.closeConnection(false);
                return new SingleEventResponse(new FMSError(SingleEventResponse.INVALID_EVENT_ID));
            }

            if(!event.getAssociatedUsername().equals(authToken.getUserName())) {
                db.closeConnection(false);
                return new SingleEventResponse(new FMSError(SingleEventResponse.REQUESTED_EVENT_DOESNT_BELONG_TO_USER));
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

    /**
     * Returns all Events that's associated with the AuthToken contained in EventRequest.
     * @return a MultipleEventResponse containing the results of the method.
     */
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

            if(authToken == null) {
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
