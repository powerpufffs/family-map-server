package Responses;

import Helpers.FMSError;
import Models.Event;

import java.util.List;

/**
 * A Class detailing the attributes and methods of a MultipleEventsResponse.
 */
public class MultipleEventsResponse extends FMSResponse {

    public static final String INVALID_PERSONID_ERROR = "Invalid personID parameter";
    public static final String REQUESTED_PERSON_DOESNT_EXIST_ERROR = "Requested person does not belong to this user";

    private List<SingleEventResponse> data;

    /**
     * Constructs a success MultipleEventsResponse
     * @param message message sent when adding was successful.
     * @param events a List of events that were added successful.
     */
    public MultipleEventsResponse(String message, Event[] events) {
        super(message, null);
        for(Event event : events) {
            data.add(new SingleEventResponse(FMSResponse.GENERAL_SUCCESS_MESSAGE, event));
        }
    }

    /**
     * Constructs an error MultipleEventsResponse
     * @param error error sent when adding was unsuccessful.
     */
    public MultipleEventsResponse(FMSError error) {
        super(error);
        this.data = null;
    }

    public void setData(List<SingleEventResponse> data) {
        this.data = data;
    }
    public List<SingleEventResponse> getData() {
        return data;
    }
}