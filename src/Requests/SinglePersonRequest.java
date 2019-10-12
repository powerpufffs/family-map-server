package Requests;

public class SinglePersonRequest {

    private String personId;

    public SinglePersonRequest(String personId) {
        this.personId = personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonId() {
        return personId;
    }

}
