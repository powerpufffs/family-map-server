package Requests;

/**
 * A Class that details the attributes and methods of an EventRequest.
 */
public class EventRequest {

    private String eventId;
    private String authToken;

    /**
     * Constructs an EventRequest
     * @param eventId an id for the event
     * @param authToken an authToken for the event
     */
    public EventRequest(String eventId, String authToken) {
        this.eventId = eventId;
        this.authToken = authToken;
    }

    public String getEventId() {
        return this.eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAuthToken() {
        return this.authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
