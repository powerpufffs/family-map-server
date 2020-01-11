package Handlers;

import Responses.ClearResponse;
import Responses.FMSResponse;
import Services.ClearService;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.HttpURLConnection;

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
        return requestMethod.toLowerCase() == "post";
    }

    @Override
    public boolean isValidEndPoint(String endpoint) {
        return endpoint == "/clear";
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
