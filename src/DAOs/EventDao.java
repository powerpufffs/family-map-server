package DAOs;

import Models.AuthToken;
import Models.Event;

public class EventDao {

    public static void createEvent(
        String eventId,
        String associatedUsername,
        String personId,
        String latitude,
        String longitude,
        String country,
        String city,
        String eventType,
        String year) throws Error {

        return;
    }

    public static Event readOneEvent(String eventId) throws Error { return null; }
    public static Event[] readAllEvents(AuthToken authToken) throws Error { return null; }

    public static String deleteAllEvents() { return null; }

}
