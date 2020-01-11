package Handlers;

import Requests.EventRequest;
import Responses.FMSResponse;
import Responses.MultipleEventsResponse;
import Services.EventService;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class EventHandler extends FMSHandler {
    @Override
    public FMSResponse handleRequest(HttpExchange exchange)
            throws IOException, JsonParseException {

        String[] endpoints = exchange.getRequestURI().toString().split("/");
        String tokenId = getAuthToken(exchange);

        if(endpoints.length == 4) {
            return EventService.getSingleEvent(new Requests.EventRequest(
                endpoints[3],
                tokenId
            ));
        } else {
            return EventService.getAllEvents(new EventRequest(
                endpoints[2],
                tokenId
            ));
        }
    }

    @Override
    public boolean isValidMethod(String requestMethod) {
        return requestMethod.toLowerCase() == "get";
    }

    @Override
    public boolean isValidEndPoint(String endpoint) {
        String[] components = endpoint.split("/");

        if(components.length < 3 && components[1] == "event") {
            return true;
        }
        if(components.length == 3) {
            return true;
        }
        return false;
    }

    @Override
    public boolean requiresAuthToken() {
        return true;
    }

    @Override
    public int getStatusCode(FMSResponse response) {
        String message = response.getMessage();
        if( message.startsWith(FMSResponse.INVALID_AUTH_TOKEN_ERROR) ||
            message.startsWith(MultipleEventsResponse.INVALID_PERSONID_ERROR)) {
            return HttpURLConnection.HTTP_BAD_REQUEST;
        }
        if( message.startsWith(MultipleEventsResponse.REQUESTED_PERSON_DOESNT_EXIST_ERROR)) {
            return HttpURLConnection.HTTP_FORBIDDEN;
        }
        return HttpURLConnection.HTTP_INTERNAL_ERROR;
    }


}