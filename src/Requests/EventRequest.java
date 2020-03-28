package Requests;

/**
 * A Class that details the attributes and methods of an EventRequest.
 */
public class EventRequest {

    private String eventID;
    private String authToken;

    /**
     * Constructs an EventRequest
     * @param eventID an id for the event
     * @param authToken an authToken for the event
     */
    public EventRequest(String eventID, String authToken) {
        this.eventID = eventID;
        this.authToken = authToken;
    }

    public String getEventID() {
        return this.eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAuthToken() {
        return this.authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
