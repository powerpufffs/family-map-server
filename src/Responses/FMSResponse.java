package Responses;

import Helpers.FMSError;

import java.util.List;

public class FMSResponse {

    public static final String INVALID_AUTH_TOKEN_ERROR = "Invalid auth token";
    public static final String INVALID_REQUEST_DATA_ERROR =
            "Request contains invalid/incomplete data";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String USER_NOT_FOUND =
            "No user exists with that userName";
    public static final String GENERAL_SUCCESS_MESSAGE = "Success";

    private String message;
    private transient FMSError error;

    public FMSResponse(String message, FMSError error) {
        this.message = message;
        this.error = error;
    }

    public FMSResponse(FMSError error) {
        this.message = null;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return this.error != null;
    }

    public void setError(FMSError error) {
        this.error = error;
    }

    public FMSError getError() {
        return this.error;
    }

    public static <T>boolean allAreNonNull(List<T> items) {
        for(T item : items) {
            if(item == null) return false;
        }
        return true;
    }
}