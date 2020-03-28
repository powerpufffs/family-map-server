package Models;

import java.util.Arrays;
import java.util.List;

/**
 * A Class that defines the attributes and methods of a Person.
 */
public class Person extends FMSModel {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID = null;
    private String motherID = null;
    private String spouseID = null;

    /**
     * A constructor that creates a Person.
     * @param personID the id of the Person that is associated to this Event.
     * @param associatedUsername the associated username for this Event.
     * @param firstName the first name of the Person.
     * @param lastName the last name of the Person.
     * @param gender the gender of the Person.
     * @param fatherID the unique identifier belonging to the father of the Person.
     * @param motherID the unique identifier belonging to the father of the Person.
     * @param spouseID the unique identifier belonging to the spouse of the Person.
     */
    public Person(
        String personID,
        String associatedUsername,
        String firstName,
        String lastName,
        String gender,
        String fatherID,
        String motherID,
        String spouseID
    ) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    //Create Person from a User object
    public Person(User user) {
        this.associatedUsername = user.getUserName();
        this.personID = user.getPersonId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
    }

    @Override
    public boolean requiredFieldsAreNotNull() {
        List<Object> list = Arrays.asList(
            this.personID,
            this.associatedUsername,
            this.firstName,
            this.lastName,
            this.gender
        );
        return super.allAreNotNull(list);
    };

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
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
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
    public String getFatherID() {
        return fatherID;
    }
    public String getMotherID() {
        return motherID;
    }
    public String getSpouseID() {
        return spouseID;
    }
}
