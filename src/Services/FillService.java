package Services;

import DAOs.EventDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Helpers.*;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.FillRequest;

import java.util.*;

/**
 * A Class detailing the attributes and methods of a FillService.
 */
public class FillService {

    private static final Random random = new Random();
    private static final int MAX_AGE = 120;
    private static final int ADULT_AGE = 13;
    private static final int MAX_BIRTH_GIVING_AGE = 50;
    private static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    /**
     * Populates the database with X number of generated data.
     * @param request a FillRequest containing the user for which the generations will be generated
     *                and the number of generations to generate.
     * @return a FillResponse
     */
    public static FillResponse fill(FillRequest request) {
        Database db = new Database();
        User user = null;
        FillResponse response = null;

        try {
            System.out.println("Begin Filling");
            db.openConnection();
            UserDao userDao = new UserDao(db.getConnection());
            user = userDao.readOneUser(request.getUserName());

            if(user == null) {
                db.closeConnection(true);
                return new FillResponse(new FMSError(FillResponse.INVALID_USER_OR_GENERATIONS_ERROR));
            }
            if(request.getGenerations() < 0) {
                db.closeConnection(true);
                return new FillResponse(new FMSError(FillResponse.INVALID_REQUEST_DATA_ERROR));
            }

            PersonDao personDao = new PersonDao(db.getConnection());
            Person person = personDao.readOnePersons(user.getPersonId());

            if(person == null) {
                db.closeConnection(true);
                return new FillResponse(new FMSError(FillResponse.INTERNAL_SERVER_ERROR));
            }

            // create events

            Event birthEvent = createBirthEvent(person,  CURRENT_YEAR - random.nextInt(MAX_AGE - ADULT_AGE));
            Collection<Event> userEvents =
                    createOtherEvents(person, birthEvent.getYear(), CURRENT_YEAR + 1);

            // create ancestor data
            Collection<Object> ancestorData =
                    generateAncestorData(person, birthEvent.getYear(), request.getGenerations());

            // add to db

            int personsCreated = 1;
            int eventsCreated = userEvents.size();

            EventDao eventDao = new EventDao(db.getConnection());

            personDao.deleteAllPersonsForUser(user.getUserName());
            eventDao.deleteAllEventsForUser(user.getUserName());

            personDao.insert(person);
            userDao.updatePersonIdForUser(user);

            for(Event event: userEvents) {
                eventDao.insert(event);
            }

            for(Object object: ancestorData) {
                if(object instanceof Event) {
                    eventsCreated++;
                    System.out.println(eventsCreated);
                    eventDao.insert((Event) object);
                } else if(object instanceof Person) {
                    personsCreated++;
                    personDao.insert((Person) object);
                }
            }

            System.out.println("End Filling");
            db.closeConnection(true);
            return new FillResponse(personsCreated, eventsCreated);
        } catch(DataAccessException e) {
            return new FillResponse(new FMSError(FillResponse.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * Calculates a realistic birth year for a person based on his/her year of marriage.
     * @param marriageYear the person's year of marriage
     * @return an appropriate birth year for the person
     */
    private static int calculateBirthYear(int marriageYear) {
        return marriageYear - ADULT_AGE - random.nextInt(10);
    }

    /**
     * Generates ancestral data recursively.
     * @param person the person to which the generated data will be associated with
     * @param personBirthYear the birth year of the person.
     * @param generations the number of generations to continue to generate data for.
     * @return a Collection of Objects
     */
    private static Collection<Object> generateAncestorData(Person person, int personBirthYear, int generations) {
        Collection<Object> generatedData = new ArrayList<>();

        if(generations == 0) {
            return generatedData;
        }

        // create parents and link to each other and child

        Person dadPerson = generatePerson(person.getAssociatedUsername(), "m",
                person.getLastName());
        Person momPerson = generatePerson(person.getAssociatedUsername(), "f",
                null);

        dadPerson.setSpouseId(momPerson.getPersonId());
        momPerson.setSpouseId(dadPerson.getPersonId());

        person.setFatherId(dadPerson.getPersonId());
        person.setMotherId(momPerson.getPersonId());

        generatedData.add(dadPerson);
        generatedData.add(momPerson);

        // create marriage events

        Event dadMarriage = createMarriageEvent(dadPerson, momPerson, personBirthYear);
        dadMarriage.setPersonId(dadPerson.getPersonId());
        generatedData.add(dadMarriage);

        Event momMarriage = dadMarriage.clone();
        momMarriage.setPersonId(momPerson.getPersonId());
        generatedData.add(momMarriage);

        int marriageYear = dadMarriage.getYear();

        // create other events

        int dadBirthYear = calculateBirthYear(marriageYear);
        generatedData.addAll(createOtherEvents(dadPerson, dadBirthYear,
                marriageYear));
        int momBirthYear = calculateBirthYear(marriageYear);
        generatedData.addAll(createOtherEvents(momPerson, momBirthYear,
                marriageYear));

        // recursively add father's and mother's ancestors' data
        generatedData.addAll(generateAncestorData(dadPerson, dadBirthYear,
                generations - 1));
        generatedData.addAll(generateAncestorData(momPerson, momBirthYear,
                generations - 1));

        return generatedData;
    }

    /**
     * Generates a new Person.
     * @param associatedUserName the username that will be associated for the Person
     * @param gender the gender to be assigned to the Person
     * @param lastName the lastname to be assigned to the Person
     * @return the generated Person
     */
    private static Person generatePerson(String associatedUserName, String gender,
                                         String lastName) {
        return new Person(
            UUID.randomUUID().toString(),
            associatedUserName,
            gender == "m"
                ? NameGenerator.randomMaleName()
                : NameGenerator.randomFemaleName(),
            lastName == null
                ? NameGenerator.randomLastName()
                : lastName,
            gender,
    null,
   null,
   null
        );
    }

    /**
     * Creates all other types of events (birth, baptism, graduation) for a person.
     * @param person the person for which the events will be created for
     * @param birthYear the birth year of the person
     * @param minDeathYear a reasonable year by which this person should be dead by
     * @return a Collection of Events
     */
    private static Collection<Event> createOtherEvents(Person person, int birthYear, int minDeathYear) {
        Collection<Event> events = new ArrayList<>();

        Event birth = createBirthEvent(person, birthYear);
        events.add(birth);

        Event baptism = createBaptism(person, birth.getYear(), CURRENT_YEAR);
        events.add(baptism);

        Event graduation = createGraduationEvent(person, birth.getYear());
        events.add(graduation);

        if (minDeathYear <= CURRENT_YEAR) {
            Event death = createDeathEvent(person, minDeathYear);

            if (death.getYear() <= CURRENT_YEAR) {
                events.add(death);
            }
        }

        return events;
    }

    /**
     * Creates a marriage event.
     * @param husband the Person representing the husband in this event
     * @param wife the Person representing the wife in this event
     * @param maxYear the
     * @return the created Event
     */
    private static Event createMarriageEvent(Person husband, Person wife, int maxYear) {
        LocationGenerator.Location location = LocationGenerator.randomLocation();
        return new Event(
            UUID.randomUUID().toString(),
            husband.getAssociatedUsername(),
            husband.getPersonId(),
            location.getLatitude(),
            location.getLongitude(),
            location.getCountry(),
            location.getCity(),
            "Marriage",
            maxYear - random.nextInt(5)
        );
    }

    /**
     * Creates a birth event.
     * @param person the person associated with the event
     * @param year the year of the event
     * @return created Event
     */
    private static Event createBirthEvent(Person person, int year) {
        LocationGenerator.Location location = LocationGenerator.randomLocation();
        return new Event(
            UUID.randomUUID().toString(),
            person.getAssociatedUsername(),
            person.getPersonId(),
            location.getLatitude(),
            location.getLongitude(),
            location.getCountry(),
            location.getCity(),
        "Birth",
            year
        );
    }

    /**
     * Creates a death event.
     * @param person the person associated with the event
     * @param minYear a reasonable year by which this person should be dead by
     */
    private static Event createDeathEvent(Person person, int minYear) {
        LocationGenerator.Location location = LocationGenerator.randomLocation();
        return new Event(
            UUID.randomUUID().toString(),
            person.getAssociatedUsername(),
            person.getPersonId(),
            location.getLatitude(),
            location.getLongitude(),
            location.getCountry(),
            location.getCity(),
            "Death",
            minYear + random.nextInt(MAX_AGE - ADULT_AGE)
        );
    }

    /**
     * Creates a baptism event.
     * @param person the Person representing the husband in this event
     * @param minYear the minimum age required before a Person can participate in a baptism
     * @param maxYear a reasonable maximum age by which a Person should be baptized by
     * @return created Event
     */
    private static Event createBaptism(Person person, int minYear, int maxYear) {
        LocationGenerator.Location location = LocationGenerator.randomLocation();
        return new Event(
            UUID.randomUUID().toString(),
            person.getAssociatedUsername(),
            person.getPersonId(),
            location.getLatitude(),
            location.getLongitude(),
            location.getCountry(),
            location.getCity(),
            "Baptism",
            minYear + random.nextInt(maxYear - minYear)
        );
    }

    /**
     * Creates a graduation event.
     * @param person the Person representing the husband in this event
     * @param birthYear the year in which the Person was born
     * @return Event
     */
    private static Event createGraduationEvent(Person person, int birthYear) {
        LocationGenerator.Location location = LocationGenerator.randomLocation();
        return new Event(
            UUID.randomUUID().toString(),
            person.getAssociatedUsername(),
            person.getPersonId(),
            location.getLatitude(),
            location.getLongitude(),
            location.getCountry(),
            location.getCity(),
  "Graduation",
       birthYear + ADULT_AGE - random.nextInt(3)
        );
    }

}