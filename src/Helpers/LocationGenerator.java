package Helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonParseException;

/**
 * Provides methods to generate random locations.
 */
public class LocationGenerator {

    private static ArrayList<Location> locations;
    private static Random random = new Random();

    static {
        try {
            loadLocations();
        } catch (IOException e) {
            System.out.println("failed to load locations");
        }
    }

    public static Location randomLocation() {
        return locations.get(random.nextInt(locations.size()));
    }

    private static void loadLocations() throws IOException {
        final File LOCATIONS_FILE = new File("data/places.json");

        try(InputStream locationStream = new FileInputStream(LOCATIONS_FILE)) {
            Locations locationsVar =
                    (Locations) GsonHelper.deserialize(locationStream, Locations.class);

            locations = locationsVar.data;
        } catch (JsonParseException e) {
           throw e;
        }
    }

    private class Locations {
        private ArrayList<Location> data;
    }

    public class Location {
        private float longitude;
        private float latitude;
        private String country;
        private String city;

        public float getLongitude() {
            return longitude;
        }

        public float getLatitude() {
            return latitude;
        }

        public String getCountry() {
            return country;
        }

        public String getCity() {
            return city;
        }

    }
}