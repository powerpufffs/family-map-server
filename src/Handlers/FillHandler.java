package Handlers;

import DAOs.UserDao;
import Helpers.DataAccessException;
import Helpers.Database;
import Helpers.FMSError;
import Requests.FillRequest;
import Responses.FMSResponse;
import Services.FillResponse;
import Services.FillService;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import org.sqlite.util.StringUtils;

import java.io.IOException;

public class FillHandler extends FMSHandler {

    private int DEFAULT_GENERATIONS = 4;

    @Override
    public FMSResponse handleRequest(HttpExchange exchange) throws IOException, JsonParseException {
        String endpoint = exchange.getRequestURI().toString();
        return FillService.fill(new FillRequest(endpoint.split("/")[2], findGenerations(endpoint)));
    }

    @Override
    public boolean isValidMethod(String requestMethod) {
        return requestMethod.toLowerCase() == "post";
    }

    @Override
    public boolean isValidEndPoint(String endpoint) {
        String[] components = endpoint.split("/");

        if( components.length < 2 ||
            components.length > 4 ||
            components[1] != "fill" ) {
            return false;
        }
        if(components.length > 3 && !components[4].matches("-?\\d+")) {
            return false;
        }

        return true;
    }

    @Override
    public boolean requiresAuthToken() {
        return false;
    }

    @Override
    public int getStatusCode(FMSResponse response) {
        return 0;
    }

    private int findGenerations(String endpoint) {
        String[] components = endpoint.split("/");
        if(components.length < 4) {
            return DEFAULT_GENERATIONS;
        }
        return Integer.parseInt(components[3]);
    }

}


































