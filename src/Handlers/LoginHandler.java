package Handlers;

import Helpers.GsonHelper;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.FMSResponse;
import Responses.LoginResponse;
import Services.LoginService;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoginHandler extends FMSHandler {
    /**
     * Receives a POST request, checks database for user, checks password,
     * and returns an auth token.
     *
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O exception is thrown
     * @return the result of the request, or null if a response was already sent
     */
    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException, JsonParseException {
        LoginRequest request = (LoginRequest) GsonHelper.deserialize(exchange.getRequestBody(), LoginRequest.class);
        return LoginService.login(request.getUserName(), request.getPassword());
    }

    @Override
    public boolean isValidMethod(String requestMethod) {
        return requestMethod.toLowerCase() == "post";
    }

    @Override
    public boolean isValidEndPoint(String endpoint) {
        return endpoint == "/user/login";
    }

    @Override
    public boolean requiresAuthToken() {
        return false;
    }

    @Override
    public int getStatusCode(FMSResponse response) {
        if(response.getMessage() != null) {
            return HttpURLConnection.HTTP_OK;
        }
        String message = response.getMessage();
        if(message.startsWith(LoginResponse.INVALID_OR_MISSING_INPUT_ERROR)) {
            return HttpURLConnection.HTTP_BAD_REQUEST;
        }
        return HttpURLConnection.HTTP_INTERNAL_ERROR;
    }
}
