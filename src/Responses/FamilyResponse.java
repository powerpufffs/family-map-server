package Responses;

import Helpers.FMSError;

public class FamilyResponse {

    private String message;
    private FMSError error;

    public FamilyResponse(String message) {
        this.message = message;
    }

    public FamilyResponse(FMSError error) {
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