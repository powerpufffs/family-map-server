package Helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a singleton that handles all connections with the browser
 */
public class Database {
    private Connection conn;

    //Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions
    public Connection openConnection() throws DataAccessException {
        return this.openConnection(false);
    }


    public Connection openConnection(boolean testing) throws DataAccessException {
        try {
            //The Structure for this Connection is driver:language:path
            //The path assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = testing
                    ? "jdbc:sqlite:familymapTest.sqlite"
                    : "jdbc:sqlite:familymap.sqlite";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    public Connection getConnection() throws DataAccessException {
        if(conn == null) {
            return openConnection();
        } else {
            return conn;
        }
    }

    //When we are done manipulating the database it is important to close the connection. This will
    //End the transaction and allow us to either commit our changes to the database or rollback any
    //changes that were made before we encountered a potential error.

    //IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
    //DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
    //OR PROBLEMS YOU ENCOUNTER
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                //This will commit the changes to the database
                conn.commit();
            } else {
                //If we find out something went wrong, pass a false into closeConnection and this
                //will rollback any changes we made during this connection
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void createTables() throws DataAccessException {

        try (Statement stmt = conn.createStatement()){
            //First lets open our connection to our database.

            //We pull out a statement from the connection we just established
            //Statements are the basis for our transactions in SQL
            //Format this string to be exactly like a sql create table command
            stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS user " +
                "(" +
                "userName TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "first_name TEXT NOT NULL, " +
                "last_name TEXT, " +
                "gender TEXT, " +
                "person_id TEXT NOT NULL UNIQUE " +
                ");"
            );
            stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS person " +
                "(" +
                "person_id TEXT PRIMARY KEY, " +
                "associated_userName TEXT NOT NULL, " +
                "first_name TEXT NOT NULL, " +
                "last_name TEXT NOT NULL, " +
                "gender TEXT NOT NULL, " +
                "father_id TEXT, " +
                "mother_id TEXT, " +
                "spouse_id TEXT " +
                ");"
            );
            stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS event " +
                "(" +
                "event_id TEXT PRIMARY KEY, " +
                "associated_userName TEXT NOT NULL, " +
                "person_id TEXT NOT NULL, " +
                "latitude REAL NOT NULL, " +
                "longitude REAL NOT NULL, " +
                "country TEXT NOT NULL, " +
                "city TEXT NOT NULL, " +
                "event_type TEXT NOT NULL, " +
                "year TEXT NOT NULL " +
                ");"
            );
            stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS authToken " +
                "(" +
                "token_id TEXT PRIMARY KEY, " +
                "person_id TEXT NOT NULL, " +
                "userName TEXT NOT NULL " +
                ");"
            );
            //if we got here without any problems we successfully created the table and can commit
        } catch (SQLException e) {
            //if our table creation caused an error, we can just not commit the changes that did happen
            throw new DataAccessException("SQL Error encountered while creating tables");
        }

    }

    public void clearTables() throws DataAccessException
    {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DROP TABLE IF EXISTS";
            stmt.executeUpdate(sql + " user");
            stmt.executeUpdate(sql + " person");
            stmt.executeUpdate(sql + " event");
            stmt.executeUpdate(sql + " authToken");
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}

