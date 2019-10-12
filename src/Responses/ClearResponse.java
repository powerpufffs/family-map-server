package Responses;

import Helpers.FMSError;

public class ClearResponse {

    private String message;
    private FMSError error;

    public ClearResponse(String message) {
        this.message = message;
    }
    public ClearResponse(FMSError error) { this.error = error; }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
}
