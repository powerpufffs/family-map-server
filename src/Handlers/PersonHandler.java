package Handlers;

import Helpers.RequestMethod;
import Requests.PersonRequest;
import Responses.FMSResponse;
import Services.PersonService;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class PersonHandler extends FMSHandler {
    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException {
        String[] endpoints = exchange.getRequestURI().toString().split("/");
        String token = getAuthToken(exchange);

        if (endpoints.length == 2) {
            return PersonService.getAllPersons(new PersonRequest(
                "",
                token
            ));
        } else {
            return PersonService.getSinglePerson(new PersonRequest(
                endpoints[2],
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
        }
        if (endpoints.length == 3) {
            return true;
        }
        return false;
    }

    @Override
    public boolean requiresAuth() {
        return true;
    }
}
