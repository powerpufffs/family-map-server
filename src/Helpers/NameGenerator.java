package Helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class that generates random names
 */
public class NameGenerator {

    private static ArrayList<String> maleNames;
    private static ArrayList<String> femaleNames;
    private static ArrayList<String> lastNames;
    private static Random random = new Random();

    static {
        try {
            loadNames();
        } catch (IOException e) {
            System.out.println("failed to load locations");
        }
    }

    public static String randomMaleName() {
        return maleNames.get(random.nextInt(maleNames.size()));
    }

    public static String randomFemaleName() {
        return femaleNames.get(random.nextInt(femaleNames.size()));
    }

    public static String randomLastName() {
        return lastNames.get(random.nextInt(lastNames.size()));
    }

    private static void loadNames() throws IOException {
        final File MALE_NAME_FILE = new File("data/maleNames.json");
        final File FEMALE_NAME_FILE = new File("data/femaleNames.json");
        final File LASTNAME_FILE = new File("data/lastNames.json");

        try ( FileInputStream maleStream = new FileInputStream(MALE_NAME_FILE);
              FileInputStream femaleStream = new FileInputStream(FEMALE_NAME_FILE);
              FileInputStream lastNameStream = new FileInputStream(LASTNAME_FILE); ) {
            maleNames = ((Names) GsonHelper.deserialize(maleStream, Names.class)).data;
            femaleNames = ((Names) GsonHelper.deserialize(femaleStream, Names.class)).data;
            lastNames = ((Names) GsonHelper.deserialize(lastNameStream, Names.class)).data;
        }

    }

    private class Names {
        private ArrayList<String> data;
    }

}