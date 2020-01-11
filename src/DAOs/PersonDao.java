package DAOs;

import Helpers.DataAccessException;
import Models.AuthToken;
import Models.Person;

import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.util.List;
import java.sql.*;

public class PersonDao {

    private final Connection connection;

    public PersonDao(Connection connection) {
        this.connection = connection;
    }

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

    public void deleteAllPersons() throws DataAccessException {
        String sqlToDrop = "DROP TABLE IF EXISTS ?";
        String createSql = "CREATE TABLE ? (" +
                "person_id TEXT PRIMARY KEY," +
                "associated_userName TEXT NOT NULL," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "gender TEXT NOT NULL," +
                "father_id TEXT," +
                "mother_id TEXT," +
                "spouse_id TEXT);";

        try(
            PreparedStatement stmt = connection.prepareStatement(sqlToDrop);
            PreparedStatement createStmt = connection.prepareStatement(createSql)
        ) {
            stmt.setString(1, "person");
            createStmt.setString(1, "person");
            stmt.executeUpdate();
            createStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while attempting to delete all persons.");
        }
    }

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
