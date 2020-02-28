package Requests;

/**
 * A Class that details the attributes and methods of an PersonRequest.
 */
public class PersonRequest {

    private String personId;
    private String authToken;

    /**
     * Constructs a PersonRequest
     * @param personId the id of a person
     * @param authToken an auth token
     */
    public PersonRequest(String personId, String authToken) {
        this.personId = personId;
        this.authToken = authToken;
    }

    public String getPersonId() {
        return this.personId;
    }
    public String getAuthToken() {
        return this.authToken;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
