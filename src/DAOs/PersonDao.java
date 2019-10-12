package DAOs;

import Models.AuthToken;
import Models.Person;

public class PersonDao {

    public static void createPerson(
        String personId,
        String associatedUsername,
        String firstName,
        String lastName,
        String gender,
        String fatherId,
        String motherId,
        String spouseId
    ) throws Error {

        return;
    }

    public static Person readOnePersons(String personId) throws Error { return null; }
    public static Person[] readAllPersons(AuthToken authToken) throws Error { return null; }

    public static String deleteAllPersons() { return null; }

}
