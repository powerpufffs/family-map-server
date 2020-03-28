package Handlers;

import Helpers.RequestMethod;
import Requests.FillRequest;
import Responses.FMSResponse;
import Services.FillService;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class FillHandler extends FMSHandler {

    private int DEFAULT_GENERATIONS = 4;

    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException, JsonParseException {
        String endpoint = exchange.getRequestURI().toString();
        return FillService.fill(new FillRequest(endpoint.split("/")[2], findGenerations(endpoint)));
    }

    @Override
    public boolean isValidRequestMethod(String method) {
        return method.toLowerCase().equals(RequestMethod.POST.name().toLowerCase());
    }

    @Override
    public boolean isValidEndpoint(String endpoint) {
        String[] components = endpoint.split("/");

        if( components.length < 2 ||
            components.length > 4 ||
            !components[1].equals("fill") ) {
            return false;
        }
        if(components.length > 3 && !components[3].matches("-?\\d+")) {
            return false;
        }

        return true;
    }

    @Override
    public boolean requiresAuth() {
        return false;
    }


    private int findGenerations(String endpoint) {
        String[] components = endpoint.split("/");
        if(components.length < 4) {
            return DEFAULT_GENERATIONS;
        }
        return Integer.parseInt(components[3]);
    }

}


































