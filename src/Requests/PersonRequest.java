package Requests;

public class PersonRequest {

    private String personId;
    private String authToken;

    public PersonRequest(String personId, String authToken) {
        this.personId = personId;
        this.authToken = authToken;
    }

    public String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
