package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public enum AsterismsNameLoader implements StarCatalogue.Loader {
    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        try ( BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,US_ASCII))){

        }

    }
}
