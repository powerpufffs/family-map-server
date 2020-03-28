package Handlers;

import Helpers.GsonHelper;
import Helpers.RequestMethod;
import Requests.LoginRequest;
import Responses.FMSResponse;
import Services.LoginService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class LoginHandler extends FMSHandler {
    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException {
        LoginRequest request = (LoginRequest) GsonHelper.deserialize(exchange.getRequestBody(), LoginRequest.class);
        exchange.getRequestBody().close();
        return LoginService.login(request);
    }

    @Override
    public boolean isValidRequestMethod(String method) {
        return method.equals(RequestMethod.POST.name());
    }

    @Override
    public boolean isValidEndpoint(String endpoint) {
        return endpoint.equals("/user/login");
    }

    @Override
    public boolean requiresAuth() {
        return false;
    }
}