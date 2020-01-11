package Responses;

import Helpers.FMSError;
import Models.Event;
import Models.Person;

import java.util.List;

public class MultiplePersonsResponse extends FMSResponse {

    public static final String INVALID_PERSONID_ERROR = "Invalid personID parameter";
    public static final String REQUESTED_PERSON_DOESNT_EXIST_ERROR = "Requested person does not belong to this user";

    private List<SinglePersonResponse> data;

    public MultiplePersonsResponse(String message, Person[] persons) {
        super(message, null);
        for(Person person : persons) {
            data.add(new SinglePersonResponse(FMSResponse.GENERAL_SUCCESS_MESSAGE, person));
        }
    }

    public MultiplePersonsResponse(FMSError error) {
        super(error);
        this.data = null;
    }

    public void setData(List<SinglePersonResponse> data) {
        this.data = data;
    }

    public List<SinglePersonResponse> getData() {
        return data;
    }

    public static class MultipleEventsResponse extends FMSResponse {

        public static final String INVALID_PERSONID_ERROR = "Invalid personID parameter";
        public static final String REQUESTED_PERSON_DOESNT_EXIST_ERROR = "Requested person does not belong to this user";

        private List<SingleEventResponse> singleEventResponses;

        public MultipleEventsResponse(String message, Event[] events) {
            super(message, null);
            for(Event event : events) {
                singleEventResponses.add(new SingleEventResponse(FMSResponse.GENERAL_SUCCESS_MESSAGE, event));
            }
        }

        public MultipleEventsResponse(FMSError error) {
            super(error);
            this.singleEventResponses = null;
        }

        public void setSingleEventResponses(List<SingleEventResponse> singleEventResponses) {
            this.singleEventResponses = singleEventResponses;
        }

        public List<SingleEventResponse> getSingleEventResponses() {
            return singleEventResponses;
        }
    }
}
