package Responses;

import Helpers.FMSError;
import Models.Event;
import Models.Person;
import Models.User;

import java.util.List;

/**
 * A Class detailing the attributes and methods of a LoadResponse.
 */
public class LoadResponse extends FMSResponse {

    private List<User> users;
    private List<Person> persons;
    private List<Event> events;

    /**
     * Constructs an error LoadResponse
     * @param error error sent when loading was unsuccessful.
     */
    public LoadResponse(FMSError error) {
        super(error);
    }

    /**
     * Constructs a success LoadReponse
     * @param users a List of Users that were added
     * @param persons a List of Persons that were added
     * @param events a List of Events that were added
     */
    public LoadResponse(List<User> users, List<Person> persons, List<Event> events) {
        super(String.format("Successfully added %d users, " +
                        "%d persons, " +
                        "and %d events to the database.",
                users.size(), persons.size(), events.size()), null);

        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    /**
     * Creates a message detailing the success of the operation.
     * @return a message containing the number of users, persons and events that were added
     */
    public String getSuccessMessage() {
        return String.format("Successfully added %d users, " +
                        "%d persons, " +
                        "and %d events to the database.",
                users.size(), persons.size(), events.size());
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
    public List<User> getUsers() {
        return users;
    }
    public List<Person> getPersons() {
        return this.persons;
    }
    public List<Event> getEvents() {
        return events;
    }
}
