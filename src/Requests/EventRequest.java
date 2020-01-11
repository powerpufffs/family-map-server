package Requests;

public class EventRequest {

    private String eventId;
    private String authToken;

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
