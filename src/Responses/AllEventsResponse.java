package Responses;

import Helpers.FMSError;
import Models.Event;

public class AllEventsResponse {

    private Event[] data;
    private FMSError error;

    public AllEventsResponse(Event[] data, String message) {
        this.data = data;
    }

    public AllEventsResponse(FMSError error) {
        this.error = error;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public void setError(FMSError error) {
        this.error = error;
    }

    public Event[] getData() {
        return data;
    }

    public FMSError getError() {
        return error;
    }

}
