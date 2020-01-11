package Handlers;

import Requests.PersonRequest;
import Responses.FMSResponse;
import Responses.MultipleEventsResponse;
import Responses.MultiplePersonsResponse;
import Services.PersonService;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class PersonHandler extends FMSHandler {

    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException, JsonParseException {
        String[] components = exchange.getRequestURI().toString().split("/");

        String tokenId = getAuthToken(exchange);
        if(components.length == 4) {
            return PersonService.getSinglePerson(new PersonRequest(
                components[3],
                tokenId
            ));
        } else {
            return PersonService.getAllPersons(new PersonRequest(
                components[2],
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

        if(components.length < 3 && components[1] == "person") {
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
            message.startsWith(MultiplePersonsResponse.INVALID_PERSONID_ERROR)) {
            return HttpURLConnection.HTTP_BAD_REQUEST;
        }
        if( message.startsWith(MultiplePersonsResponse.REQUESTED_PERSON_DOESNT_EXIST_ERROR)) {
            return HttpURLConnection.HTTP_FORBIDDEN;
        }
        return HttpURLConnection.HTTP_INTERNAL_ERROR;
    }

}
