package Responses;

import Helpers.FMSError;
import Models.AuthToken;

public class LoginResponse extends FMSResponse {

    public static final String USER_ALREADY_EXISTS_ERROR = "Username already taken by another user";
    public static final String INVALID_OR_MISSING_INPUT_ERROR = "Request property missing or has invalid value";
    public static final String USER_DOESNT_EXIST_ERROR = "Internal server error";
    public static final String GENERAL_LOGIN_FAILURE_ERROR = "Internal server error";
    public static final String SUCCESSFUL_LOGIN_MESSAGE = "Login successful";

    private String authToken;
    private String userName;
    private String personID;

    public LoginResponse(FMSError error) {
        super(null, error);
    }
    public LoginResponse(FMSError error, int code) { super(error, code); }
    public LoginResponse(String message, AuthToken token) {
        super(null, null);
        this.authToken = token.getTokenId();
        this.userName = token.getUserName();
        this.personID = token.getPersonId();
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
