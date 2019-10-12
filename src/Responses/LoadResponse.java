package Responses;

import Helpers.FMSError;

public class LoadResponse {

    private String message;
    private FMSError error;

    public LoadResponse(String message) {
        this.message = message;
    }

    public LoadResponse(FMSError error) {
        this.error = error;
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
