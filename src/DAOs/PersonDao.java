package DAOs;

import Helpers.DataAccessException;
import Models.AuthToken;
import Models.Person;

import javax.swing.plaf.nimbus.State;
import java.util.List;
import java.sql.*;


/**
 * A Class that defines the attributes and methods of a PersonDao.
 */
public class PersonDao {

    private final Connection connection;

    /**
     * Constructs a PersonDao
     * @param connection a connection to the database.
     */
    public PersonDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Saves a Person into the database.
     * @param person the Person to be saved.
     * @throws DataAccessException
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO person (person_id, associated_userName, first_name, last_name, gender, " +
                "father_id, mother_id, spouse_id) VALUES(?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonId());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherId());
            stmt.setString(7, person.getMotherId());
            stmt.setString(8, person.getSpouseId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Reads a Person from the database.
     * @param personID a unique id that will be used to retrieve the Person.
     * @throws DataAccessException
     * @return the Person that matches the id.
     */
    public Person readOnePersons(String personID) throws DataAccessException {
        if(personID == null)
            throw new DataAccessException("Error. personID was null when attempting to read one person");

        Person person = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE person_id = ?;";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if(rs.next()) {
                person = new Person(
                    rs.getString("person_id"),
                    rs.getString("associated_userName"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("gender"),
                    rs.getString("father_id"),
                    rs.getString("mother_id"),
                    rs.getString("spouse_id")
                );
                return person;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while reading person");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DataAccessException("Error encountered while closing result set while reading person");
                }
            }
        }
        return null;
    }

    /**
     * Reads all Persons from the database.
     * @param authToken an AuthToken that will be used to authorize the method.
     * @throws DataAccessException
     * @return a List of all Persons.
     */
    public Person[] readAllPersons(AuthToken authToken) throws DataAccessException {
        if(authToken == null)
            throw new DataAccessException("Error. authToken was null when attempting to read one authToken");

        List<Person> personList = null;

        try(
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM person")
        ) {
            while(rs.next()) {
                personList.add(new Person(
                    rs.getString("person_id"),
                    rs.getString("associated_userName"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("gender"),
                    rs.getString("father_id"),
                    rs.getString("mother_id"),
                    rs.getString("spouse_id")
                ));
            }

            return personList.toArray(new Person[personList.size()]);
        } catch(SQLException e) {
            throw new DataAccessException("Error countered while reading all persons");
        }
    }

    /**
     * Deletes all Persons currently in the Database.
     * @throws DataAccessException
     */
    public void deleteAllPersons() throws DataAccessException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM person");
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while attempting to delete all persons.");
        }
    }

    /**
     * Deletes all Persons associated with the User currently in the Database.
     * @param userName the username of the user.
     * @throws DataAccessException
     */
    public void deleteAllPersonsForUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM person WHERE associated_userName = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new DataAccessException("Error encountered while attempting to delete all persons for given user.");
        }
    }

}
