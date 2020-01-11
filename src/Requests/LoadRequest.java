package Requests;

import Models.Event;
import Models.Person;
import Models.User;

import java.util.List;

public class LoadRequest {

    private List<User> users;
    private List<Person> persons;
    private List<Event> events;

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Event> getEvents() {
        return events;
    }

    public LoadRequest(List<User> users, List<Person> persons, List<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }
}
