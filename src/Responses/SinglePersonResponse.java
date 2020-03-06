package Responses;

import Helpers.FMSError;
import Models.Person;

public class SinglePersonResponse extends FMSResponse {

    public static final String PERSON_DOESNT_EXIST_ERROR = "Person doesn't exist in the database";
    public static final String REQUESTED_PERSON_DOESNT_BELONG_TO_USER = "Requested person does not belong to this user";

    private String associatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    public SinglePersonResponse(FMSError error, int code) {
        super(error, code);
    }
    public SinglePersonResponse(FMSError error) {
        super(error);
    }

    public SinglePersonResponse(String message, Person person) {
        super(null, null);
        this.personID = person.getPersonId();
        this.associatedUsername = person.getAssociatedUsername();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.gender = person.getGender();
        this.fatherID = person.getFatherId();
        this.motherID = person.getMotherId();
        this.spouseID = person.getSpouseId();
    }

    public String getId() {
        return personID;
    }
    public void setId(String id) {
        this.personID = id;
    }
    public String getAssociatedUsername() {
        return associatedUsername;
    }
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getFather() {
        return fatherID;
    }
    public void setFather(String father) {
        this.fatherID = father;
    }
    public String getMother() {
        return motherID;
    }
    public void setMother(String mother) {
        this.motherID = mother;
    }
    public String getSpouse() {
        return spouseID;
    }
    public void setSpouse(String spouse) {
        this.spouseID = spouse;
    }

}