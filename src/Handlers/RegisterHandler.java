package Handlers;

import Helpers.DataAccessException;
import Helpers.GsonHelper;
import Requests.RegisterRequest;
import Responses.FMSResponse;
import Responses.LoginResponse;
import Services.RegisterService;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class RegisterHandler extends FMSHandler {
    /**
     * Receives a POST request, creates a new user, generates data for the user,
     * logs the user in, and returns an auth token.
     *
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O exception is thrown
     * @return the result of the request, or null if a response was already sent
     */
    @Override
    public FMSResponse handleRequest(HttpExchange exchange)
            throws IOException, JsonParseException {

        RegisterRequest request = (RegisterRequest)
                GsonHelper.deserialize(exchange.getRequestBody(), RegisterRequest.class);

        return RegisterService.register(request);
    }

    /**
     * Determines if the request method is valid.
     *
     * @param requestMethod the request method, in all caps
     * @return whether the request method is valid
     */
    @Override
    public boolean isValidMethod(String requestMethod) {
        return requestMethod.toLowerCase() == "post";
    }

    /**
     * Determines if the request URI is valid.
     *
     * @param endpoint the request URI
     * @return whether the URI is valid
     */
    public boolean isValidEndPoint(String endpoint) {
        return endpoint == "/user/register";
    }

    /**
     * Determines if the request requires an auth token.
     *
     * @return whether the request requires an auth token
     */
    @Override
    public boolean requiresAuthToken() {
        return false;
    }

    /**
     * Returns the appropriate status code depending on the message
     * @return a status code that is consistent with the result
     */
    @Override
    public int getStatusCode(FMSResponse response) {
        if(response.getError() == null) {
            return HttpURLConnection.HTTP_OK;
        }
        String message = response.getError().getMessage();
        if( message.startsWith(LoginResponse.USER_ALREADY_EXISTS_ERROR) ||
            message.startsWith(LoginResponse.USER_DOESNT_EXIST_ERROR) ||
            message.startsWith(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR) ) {

            return HttpURLConnection.HTTP_BAD_REQUEST;
        }
        if(message.startsWith(LoginResponse.GENERAL_LOGIN_FAILURE_ERROR)) {
            return HttpURLConnection.HTTP_UNAUTHORIZED;
        }
        return HttpURLConnection.HTTP_INTERNAL_ERROR;
    }

}
