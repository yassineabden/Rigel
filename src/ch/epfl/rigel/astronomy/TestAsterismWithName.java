package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public class TestAsterismWithName {

    private static InputStream resourceStream(String resourceName) {
        return TestAsterismWithName.class.getResourceAsStream(resourceName);
    }


    public static void main(String[] args) throws IOException {


        InputStream hs = resourceStream("/hygdata_v3.csv");
        InputStream as = resourceStream("/constellation_lines_rey.txt");

        StarCatalogue catalogue = new StarCatalogue.Builder()
                .loadFrom(hs, HygDatabaseLoader.INSTANCE)
                .loadFrom(as, AsterismWithNameLoader.INSTANCE)
                .build();
    }
}
