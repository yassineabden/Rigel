package ch.epfl.rigel.astronomy;


import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.US_ASCII;
/**
 * Chargeur de catalogue HYG
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader {

    INSTANCE;

    // Index des colonnes dont les informations doivent être extraites
    private final static int HIP = 1;
    private final static int PROPER=6;
    private final static int MAG= 13;
    private final static int CI= 16;
    private final static int RARAD = 23;
    private final static int DECRAD = 24;
    private final static int BAYER = 27;
    private final static int CON = 29;


    /**
     * Charge les étoiles et/ou asterism du flot d'entrée et les ajoute au catalogue en cours de construction du bâtisseur
     * ou lève une IOException s'il y a une erreur
     *
     * @param inputStream Flot d'entrée contenant des asterism et/ou des étoiles
     * @param builder Catalogue en cours de construction
     *
     * @throws IOException s'il y'a une erreur d'entrée/sortie.
     */
    @Override
    public  void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        try ( BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,US_ASCII))){
            String line;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts= line.split(",");
                int hipparcosId= parts[HIP].isEmpty()? 0 : Integer.parseInt(parts[HIP]);
                String con = parts[CON];
                String bayer = parts[BAYER].isEmpty()? "?": parts[BAYER];
                String name = parts[PROPER].isEmpty()? bayer+ " " + con: parts[PROPER];
                System.out.print(name);
                double rared = Double.parseDouble(parts[RARAD]);
                double decred = Double.parseDouble(parts[DECRAD]);
                double magnitude = parts[MAG].isEmpty()? 0 : Double.parseDouble(parts[MAG]);
                double colorIndex = parts[CI].isEmpty()? 0 : Double.parseDouble(parts[CI]);
                EquatorialCoordinates coordinates = EquatorialCoordinates.of(rared,decred);

                Star star = new Star(hipparcosId,name,coordinates,(float) magnitude,(float) colorIndex);
                builder.addStar(star); }
        }
    }
}
