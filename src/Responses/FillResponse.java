package Responses;

import Helpers.FMSError;

/**
 * A Class detailing the attributes and methods of a FillResponse
 */
public class FillResponse {

    private String message;
    private FMSError error;

    /**
     * Constructs an error FillResponse
     * @param error error sent when filling was unsuccessful.
     */
    public FillResponse(FMSError error) {
        this.error = error;
    }

    /**
     * Constructs a success FillResponse
     * @param message sent when filling was successful.
     */
    public FillResponse(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setError(FMSError error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }
    public FMSError getError() {
        return error;
    }

}
