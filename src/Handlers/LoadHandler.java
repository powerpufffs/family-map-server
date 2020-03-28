package Handlers;

import Helpers.GsonHelper;
import Helpers.RequestMethod;
import Requests.LoadRequest;
import Responses.FMSResponse;
import Services.LoadService;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class LoadHandler extends FMSHandler {

    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException, JsonParseException {
        LoadRequest request = (LoadRequest) GsonHelper.deserialize(exchange.getRequestBody(), LoadRequest.class);
        exchange.getRequestBody().close();
        request.replaceNull();
        return LoadService.load(request);
    }

    @Override
    public boolean isValidRequestMethod(String method) {
        return method.equals(RequestMethod.POST.name());
    }

    @Override
    public boolean isValidEndpoint(String endpoint) {
        String[] endpoints = endpoint.split("/");
        return endpoints.length == 2 ? endpoints[1].equals("load") : false;
    }

    @Override
    public boolean requiresAuth() {
        return false;
    }
}
