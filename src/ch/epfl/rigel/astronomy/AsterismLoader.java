package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Enumération instanciant un chargeur de catalogue d'asterisms
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public enum AsterismLoader implements StarCatalogue.Loader {

    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        try ( BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,US_ASCII))){

            List<Star> starsBuilded = builder.stars();

            HashMap<Integer, Star> hipToStar = new HashMap<>();
            for (Star star  : starsBuilded) {
                hipToStar.put(star.hipparcosId(), star);
            }


            String a = bufferedReader.readLine() ;
    //readLine
            while (! a.isEmpty() ){

                String [] stars = a.split(",");
                List<Star>  aStars = new ArrayList<>(stars.length);

                for (String n : stars) {
                    aStars.add(hipToStar.get(Integer.parseInt(n)));
                }

                builder.addAsterism(new Asterism(aStars)); }
        }
    }
}
