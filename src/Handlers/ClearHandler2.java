package Handlers;

import Helpers.RequestMethod;
import Responses.FMSResponse;
import Services.ClearService;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;


public class ClearHandler2 extends FMSHandler2 {
    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException {
        return ClearService.clear();
    }

    @Override
    public boolean isValidRequestMethod(String method) {
        return method.equals(RequestMethod.POST.name());
    }

    @Override
    public boolean isValidEndpoint(String endpoint) {
        String[] endpoints = endpoint.split("/");
        return endpoints.length == 2 ? endpoints[1].equals("clear") : false;
    }

    @Override
    public boolean requiresAuth() {
        return false;
    }
}