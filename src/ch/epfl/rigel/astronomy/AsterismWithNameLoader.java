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
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public enum AsterismWithNameLoader implements StarCatalogueWithName.Loader {

    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogueWithName.Builder builder) throws IOException {

        List<Star> starsBuilded = builder.stars();
        HashMap<Integer, Star> hipToStar = new HashMap<>();

        for (Star star : starsBuilded)
            hipToStar.put(star.hipparcosId(), star);

        String a;
        AsterismWithName.Builder asterism = new AsterismWithName.Builder();


        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
            while ((a = bufferedReader.readLine()) != null) {

                if (a.startsWith("*")) {
                    if(asterism.name()!= null) builder.addAsterism(asterism.build());
                    asterism.setName(a);
                }

                if (a.startsWith("[")) {

                    String[] stars = a.substring(2, a.length() - 2)
                            .split("\", \"");

                    List<Star> aStars = new ArrayList<>(stars.length);

                    for (String hipparcosId : stars) {
                       // System.out.println(hipparcosId);
                        aStars.add(hipToStar.get(Integer.parseInt(hipparcosId)));
                    }

                    System.out.println(aStars.toString());
                    asterism.addStars(aStars);

                }

            }

            builder.addAsterism(asterism.build());

        }
    }
}



