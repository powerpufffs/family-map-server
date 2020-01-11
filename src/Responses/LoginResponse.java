package Responses;

import Helpers.FMSError;
import Models.AuthToken;

/**
 * The result of a request to the <code>/user/login</code> or
 * <code>/user/register</code> endpoint. The outcome of an attempt to
 * log a user into or register a new user with the server.
 */
public class LoginResponse extends FMSResponse {

    public static final String USER_ALREADY_EXISTS_ERROR = "Username already taken by another user";
    public static final String INVALID_OR_MISSING_INPUT_ERROR = "Request property missing or has invalid value";
    public static final String USER_DOESNT_EXIST_ERROR = "Internal server error";
    public static final String GENERAL_LOGIN_FAILURE_ERROR = "Internal server error";
    public static final String SUCCESSFUL_LOGIN_MESSAGE = "Login successful";

    private AuthToken authToken;

    /**
     * Cretaes a new error LoginResult.
     *
     * @param error an FMSError ebject
     */
    public LoginResponse(FMSError error) {
        super(null, error);
    }

    /**
     * Creates a new success LoginResult.
     *
     * @param authtoken an AuthToken object
     */
    public LoginResponse(String message, AuthToken authtoken) {
        super(message, null);
        this.authToken = authtoken;
    }


    /**
     * @param authToken an AuthToken object with holds the authorization token
     *                  and other information
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * @return an AuthToken object with holds the authorization token
     *                  and other information
     */
    public AuthToken getAuthToken() {
        return authToken;
    }
}
