package Responses;

import Helpers.FMSError;

public class LoginResponse {

    private String authToken;
    private String userName;
    private String personId;
    private FMSError error;

    public LoginResponse(String authToken, String userName, String personId) {
        this.authToken = authToken;
        this.userName = userName;
        this.personId = personId;
    }

    public LoginResponse(FMSError error) {
        this.error = error;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setError(FMSError error) {
        this.error = error;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonId() {
        return personId;
    }

    public FMSError getError() {
        return error;
    }
}
