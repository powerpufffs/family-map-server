package Handlers;

import Helpers.DataAccessException;
import Helpers.GsonHelper;
import Helpers.RequestMethod;
import Requests.RegisterRequest;
import Responses.FMSResponse;
import Responses.LoginResponse;
import Services.RegisterService;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class RegisterHandler extends FMSHandler2 {
    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException, JsonParseException {
        RegisterRequest request = (RegisterRequest) GsonHelper.deserialize(exchange.getRequestBody(), RegisterRequest.class);
        exchange.getRequestBody().close();
        return RegisterService.register(request);
    }

    @Override
    public boolean isValidRequestMethod(String method) {
        return method.equals(RequestMethod.POST.name());
    }

    @Override
    public boolean isValidEndpoint(String endpoint) {
        return endpoint.equals("/user/register");
    }

    @Override
    public boolean requiresAuth() {
        return false;
    }

}
