package Services;

import Helpers.FMSError;
import Responses.FMSResponse;

/**
 * The result of a request to the <code>/user/login</code> or
 * <code>/user/register</code> endpoint. The outcome of an attempt to
 * log a user into or register a new user with the server.
 */
public class FillResponse extends FMSResponse {

    public static String INVALID_USER_OR_GENERATIONS_ERROR = "Invalid userName " +
            "or generations parameter";

    /**
     * Cretaes a new error FillResponse.
     *
     * @param error an FMSError object
     */
    public FillResponse(FMSError error) {
        super(null, error);
    }

    /**
     * Creates a new success FillResponse.
     *
     * @param message string describing the successful result
     */
    public FillResponse(int personsCreated, int eventsCreated) {
        super("Successfully added " +
                personsCreated + " persons and " +
                eventsCreated + " events to the database.", null);
    }

}
