package Services;

import DAOs.EventDao;
import DAOs.PersonDao;
import DAOs.UserDao;
import Helpers.*;
import Models.Event;
import Models.Person;
import Models.User;
import Requests.FillRequest;
import Responses.FillResponse;

import java.net.HttpURLConnection;
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

    public static FillResponse fill(FillRequest request) {
        Database db = new Database();
        User user = null;

        try {
            db.openConnection();
            UserDao userDao = new UserDao(db.getConnection());
            user = userDao.readOneUser(request.getUserName());

            if(user == null) {
                db.closeConnection(false);
                return new FillResponse(new FMSError(
                    FillResponse.INVALID_USER_OR_GENERATIONS_ERROR),
                    HttpURLConnection.HTTP_BAD_REQUEST
                );
            }
            if(request.getGenerations() < 0) {
                db.closeConnection(false);
                return new FillResponse(new FMSError(
                    FillResponse.INVALID_USER_OR_GENERATIONS_ERROR),
                    HttpURLConnection.HTTP_BAD_REQUEST
                );
            }

            PersonDao personDao = new PersonDao(db.getConnection());
            Person person = personDao.readOnePersons(user.getPersonId());

            if(person == null) {
                db.closeConnection(false);
                return new FillResponse(new FMSError(
                    FillResponse.INTERNAL_SERVER_ERROR),
                    HttpURLConnection.HTTP_BAD_REQUEST
                );
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

            // Sort through generated objects and insert into db
            for(Object object: ancestorData) {
                if(object instanceof Event) {
                    eventsCreated++;
                    eventDao.insert((Event) object);
                } else if(object instanceof Person) {
                    personsCreated++;
                    personDao.insert((Person) object);
                }
            }

            db.closeConnection(true);
            return new FillResponse(personsCreated, eventsCreated);
        } catch(DataAccessException e) {
            return new FillResponse(new FMSError(
                FillResponse.INTERNAL_SERVER_ERROR),
                HttpURLConnection.HTTP_INTERNAL_ERROR
            );
        }
    }

    private static int calculateBirthYear(int marriageYear) {
        return marriageYear - ADULT_AGE - random.nextInt(10);
    }

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

        dadPerson.setSpouseID(momPerson.getPersonId());
        momPerson.setSpouseID(dadPerson.getPersonId());

        person.setFatherID(dadPerson.getPersonId());
        person.setMotherID(momPerson.getPersonId());

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

    private static Collection<Event> createOtherEvents(Person person, int birthYear, int minDeathYear) {
        Collection<Event> events = new ArrayList<>();

        Event birth = createBirthEvent(person, birthYear);
        events.add(birth);

        Event baptism = createBaptism(person, birth.getYear(), CURRENT_YEAR);
        events.add(baptism);

        Event graduation = createGraduationEvent(person, birth.getYear());
        events.add(graduation);

        if (minDeathYear <= CURRENT_YEAR) {
            Event death;
            do {
                death = createDeathEvent(person, minDeathYear);
                System.out.println(death.getYear() - birthYear);
            } while (death.getYear() >= CURRENT_YEAR || death.getYear() - birthYear >= 120);
            events.add(death);
        }
        return events;
    }

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