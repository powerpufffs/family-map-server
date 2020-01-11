package Responses;

import Helpers.FMSError;
import Models.Event;
import Models.Person;
import Models.User;

import java.util.Arrays;
import java.util.List;

public class LoadResponse extends FMSResponse {

    private List<User> users;
    private List<Person> persons;
    private List<Event> events;

    public LoadResponse(List<User> users, List<Person> persons, List<Event> events) {
        super(String.format("Successfully added %d users, " +
                        "%d persons, " +
                        "and %d events to the database.",
                users.size(), persons.size(), events.size()), null);

        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public LoadResponse(FMSError error) {
        super(error);
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

    public String getSuccessMessage() {
        return String.format("Successfully added %d users, " +
                "%d persons, " +
                "and %d events to the database.",
                users.size(), persons.size(), events.size());
    }
}
