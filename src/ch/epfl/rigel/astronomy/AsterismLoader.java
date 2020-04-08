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

    /**
     * Charge les étoiles et/ou asterism du flot d'entrée et les ajoute au catalogue en cours de construction du bâtisseur
     * ou lève une IOException s'il y a une erreur
     *
     * @param inputStream Flot d'entrée contenant des asterism et/ou des étoiles
     * @param builder     Catalogue en cours de construction
     *
     * @throws IOException s'il y'a une erreur d'entrée/sortie.
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        try ( BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,US_ASCII))){

            List<Star> starsBuilded = builder.stars();
            HashMap <Integer, Star> hipToStar = new HashMap<>();

            for (Star star  : starsBuilded)
                hipToStar.put(star.hipparcosId(), star);

            String a;
            while ((a = bufferedReader.readLine()) != null){

                String [] stars = a.split(",");
                List<Star>  aStars = new ArrayList<>(stars.length);

                for (String hipparcosId : stars){
                    aStars.add(hipToStar.get(Integer.parseInt(hipparcosId))); }

                builder.addAsterism(new Asterism(aStars)); }
        }
    }
}
