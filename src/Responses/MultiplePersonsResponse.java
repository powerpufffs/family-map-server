package Responses;

import Helpers.FMSError;
import Models.Event;
import Models.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * A Class detailing the attributes and methods of a MultiplePersonsResponse.
 */
public class MultiplePersonsResponse extends FMSResponse {

    public static final String INVALID_PERSONID_ERROR = "Invalid personID parameter";
    public static final String REQUESTED_PERSON_DOESNT_EXIST_ERROR = "Requested person does not belong to this user";

    private List<SinglePersonResponse> data = new ArrayList<SinglePersonResponse>();

    /**
     * Constructs an error MultiplePersonsResponse
     *
     * @param error error sent when unsuccessful.
     */
    public MultiplePersonsResponse(FMSError error) {
        super(error);
        this.data = null;
    }

    /**
     * Constructs an error MultiplePersonsResponse
     *
     * @param message message sent when successful.
     * @param persons array of Person
     */
    public MultiplePersonsResponse(String message, Person[] persons) {
        super(null, null);
        for (Person person : persons) {
            data.add(new SinglePersonResponse(FMSResponse.GENERAL_SUCCESS_MESSAGE, person));
        }
    }

    public void setData(List<SinglePersonResponse> data) {
        this.data = data;
    }

    public List<SinglePersonResponse> getData() {
        return data;
    }
}
