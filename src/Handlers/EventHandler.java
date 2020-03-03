package Handlers;

import Helpers.RequestMethod;
import Requests.EventRequest;
import Responses.FMSResponse;
import Responses.MultipleEventsResponse;
import Services.EventService;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class EventHandler extends FMSHandler2 {
    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException, JsonParseException {
        String[] endpoints = exchange.getRequestURI().toString().split("/");
        String token = getAuthToken(exchange);

        if(endpoints.length == 3) {
            return EventService.getSingleEvent(new Requests.EventRequest(
                endpoints[2],
                token
            ));
        } else {
            return EventService.getAllEvents(new EventRequest(
                "",
                token
            ));
        }
    }

    @Override
    public boolean isValidRequestMethod(String method) {
        return method.equals(RequestMethod.GET.name());
    }

    @Override
    public boolean isValidEndpoint(String endpoint) {
        String[] endpoints = endpoint.split("/");
        if (endpoints.length == 2 && endpoints[1].equals("person")) {
            return true;
        } else if (endpoints.length == 3) {
            return true;
        }
        return false;
    }

    @Override
    public boolean requiresAuth() {
        return true;
    }
}