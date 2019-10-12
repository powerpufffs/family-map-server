package Responses;

import Helpers.FMSError;

public class FillResponse {

    private String message;
    private FMSError error;

    public FillResponse(String message) {
        this.message = message;
    }

    public FillResponse(FMSError error) {
        this.error = error;
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
