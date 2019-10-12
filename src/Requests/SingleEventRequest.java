package Requests;

public class SingleEventRequest {

    private String eventId;

    public SingleEventRequest(String eventId) {
        this.eventId = eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

}
