package Services;

import Helpers.FMSError;
import Responses.FMSResponse;

/**
 * A Class detailing the attributes and methods of a FillResponse.
 */
public class FillResponse extends FMSResponse {

    public static String INVALID_USER_OR_GENERATIONS_ERROR = "Invalid userName " +
            "or generations parameter";

    /**
     * Constructs an error FillResponse
     * @param error an FMSError object
     */
    public FillResponse(FMSError error) {
        super(null, error);
    }

    /**
     * Constructs a success FillResponse.
     * @param personsCreated the number of people that were created.
     * @param eventsCreated the number of events that were created.
     */
    public FillResponse(int personsCreated, int eventsCreated) {
        super("Successfully added " +
                personsCreated + " persons and " +
                eventsCreated + " events to the database.", null);
    }

}
