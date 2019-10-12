package Models;

public class Person {
    private String personId;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherId;
    private String motherId;
    private String spouseId;

    public Person(String personId, String associatedUsername, String firstName, String lastName, String gender, String fatherId, String motherId, String spouseId) {
        this.personId = personId;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.spouseId = spouseId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public void setSpouseId(String spouseId) {
        this.spouseId = spouseId;
    }

    public String getPersonId() {
        return personId;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFatherId() {
        return fatherId;
    }

    public String getMotherId() {
        return motherId;
    }

    public String getSpouseId() {
        return spouseId;
    }

}
