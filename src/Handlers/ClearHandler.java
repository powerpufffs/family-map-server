package Handlers;

import Responses.ClearResponse;
import Responses.FMSResponse;
import Services.ClearService;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Handler that handles all functionality related to clearing data from the database.
 * Instantiated by the Server when a request arrives at the /clear endpoint.
 */
public class ClearHandler extends FMSHandler {
    /**
     * Receives a POST request, clears all data in the database
     *
     * @param exchange a HTTPExchange object for the request
     * @throws IOException if an I/O exception is thrown
     */
    @Override
    public FMSResponse handleRequest(HttpExchange exchange)
            throws IOException, JsonParseException {
        return ClearService.clear();
    }

    @Override
    public boolean isValidMethod(String requestMethod) {
        return requestMethod.toLowerCase().equals("post");
    }

    @Override
    public boolean isValidEndPoint(String endpoint) {
        return endpoint.equals("/clear");
    }

    @Override
    public boolean requiresAuthToken() {
        return false;
    }

    @Override
    public int getStatusCode(FMSResponse response) {
        if(response.getError() == null) {
            return HttpURLConnection.HTTP_OK;
        }
        return HttpURLConnection.HTTP_INTERNAL_ERROR;
    }
}
