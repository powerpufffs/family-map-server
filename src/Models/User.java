package Models;

/**
 * A Class that defines the attributes and methods of a User.
 */
public class User {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    /**
     * A constructor that creates a User.
     * @param userName the username of the User.
     * @param password the password of the User.
     * @param personID the id of the Person that is associated to this Event.
     * @param email of the user
     * @param firstName the first name of the Person.
     * @param lastName the last name of the Person.
     * @param gender the gender of the Person.
     */
    public User(
        String userName,
        String password,
        String email,
        String firstName,
        String lastName,
        String gender,
        String personID
    ) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof User) {
            User compareUser = (User) obj;
            return (
                this.userName.equals(compareUser.userName) &&
                this.password.equals(compareUser.password) &&
                this.email.equals(compareUser.email) &&
                this.firstName.equals(compareUser.firstName) &&
                this.lastName.equals(compareUser.lastName) &&
                this.gender.equals(compareUser.gender) &&
                this.personID.equals(compareUser.personID)
            );
        }
        return false;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
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
    public void setPersonId(String personID) {
        this.personID = personID;
    }

    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
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
    public String getPersonId() {
        return personID;
    }
}
