package Responses;

import Helpers.FMSError;

/**
 * A Class detailing the attributes and methods of a FamilyResponse.
 */
public class FamilyResponse {

    private String message;
    private FMSError error;

    /**
     * Constructs an error FamilyResponse
     * @param error sent when unsuccessful.
     */
    public FamilyResponse(FMSError error) {
        this.error = error;
    }

    /**
     * Constructs a successful FamilyResponse
     * @param message sent when successful.
     */
    public FamilyResponse(String message) {
        this.message = message;
    }

    public void setError(FMSError error) {
        this.error = error;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public FMSError getError() {
        return error;
    }

}