package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Classe de couleur d'un corps noir dépendamment de sa température
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class BlackBodyColor {

    private final static RightOpenInterval DEG = RightOpenInterval.of(10, 15);
    private final static RightOpenInterval COLOR_RGB = RightOpenInterval.of(80, 87);
    private final static ClosedInterval TEMPERATURE_RANGE = ClosedInterval.of(1000, 40000);

    private final static List<Color> COLOR_LIST = colorList("/bbr_color.txt");


    private BlackBodyColor() { }

    /**
     * Retourne la couleur correspondante à la température donnée
     *
     * @param kelvin température exprimée en degrés kelvin
     *
     * @throws IllegalArgumentException si la température donnée n'est pas comprise dans la plage de température couverte par le fichier
     *
     * @return la couleur correspondante à la température donnée
     */
    public static Color colorForTemperature(float kelvin) {

        Preconditions.checkInInterval(TEMPERATURE_RANGE, kelvin);
        return COLOR_LIST.get(tempToIndex(validKelvinTemprature(kelvin)));
    }
    // TODO les nombres magiques représentent ils qqch? Si oui il faut les stocker dans des csts!!
    private static int validKelvinTemprature(float kelvin) {

        int temp = Math.round(kelvin / 100);
        return temp * 100;
    }

    private static int tempToIndex(float kelvin) {

        return (int) (kelvin - TEMPERATURE_RANGE.low()) / 100;
    }
    //TODO est-ce qu'on peut juste mettre bbr.color.txt?

    // Transforme les couleurs données dans le fichier, donné en argument sous forme de chaîne de caractères, en instances de la classe Color
    private static List<Color> colorList(String fileName) {

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(BlackBodyColor.class.getResourceAsStream(fileName), US_ASCII))) {

            return bufferedReader.lines()
                    .filter(l -> (!l.startsWith("#") && (l.regionMatches((int) DEG.low(), "10deg", 0, (int) DEG.size()))))
                    .map(l -> Color.web(l.substring((int) COLOR_RGB.low(), (int) COLOR_RGB.high())))
                    .collect(Collectors.toUnmodifiableList());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
