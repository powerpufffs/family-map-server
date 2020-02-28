package Models;

/**
 * A Class that defines the attributes and methods of a Person.
 */
public class Person {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherId;
    private String motherId;
    private String spouseId;

    /**
     * A constructor that creates a Person.
     * @param personID the id of the Person that is associated to this Event.
     * @param associatedUsername the associated username for this Event.
     * @param firstName the first name of the Person.
     * @param lastName the last name of the Person.
     * @param gender the gender of the Person.
     * @param fatherId the unique identifier belonging to the father of the Person.
     * @param motherId the unique identifier belonging to the father of the Person.
     * @param spouseId the unique identifier belonging to the spouse of the Person.
     */
    public Person(
        String personID,
        String associatedUsername,
        String firstName,
        String lastName,
        String gender,
        String fatherId,
        String motherId,
        String spouseId
    ) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.spouseId = spouseId;
    }

    public void setPersonId(String personID) {
        this.personID = personID;
    }
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }
    public void setFirstName(String firstName) { this.firstName = firstName; }
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
        return personID;
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
