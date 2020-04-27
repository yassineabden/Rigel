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
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public class BlackBodyColor {

    private final static RightOpenInterval DEG = RightOpenInterval.of(10,15);
    private final static RightOpenInterval COLOR_RGB = RightOpenInterval.of(80,87);
    private final static ClosedInterval TEMPERATURE_RANGE = ClosedInterval.of(1000, 40000);

    private final static List < Color> COLOR_LIST = colorList("/bbr_color.txt");


    private BlackBodyColor(){}

    /**
     * Retourne la couleur correspondant à la température donnée
     *
     * @param kelvin température donnée en degrés kelvin
     *
     * @throws IllegalArgumentException si la température donée n'est pas comprise dans
     * @return la couleur correspondant à la température donnée
     */
    public static Color colorForTemperature (float kelvin){

        Preconditions.checkInInterval(TEMPERATURE_RANGE,kelvin);
        return COLOR_LIST.get(tempToIndex(validKelvinTemprature(kelvin)));
    }

    private static int validKelvinTemprature (float kelvin){

        int temp = Math.round(kelvin/100);
        return temp*100;
    }

    private static int tempToIndex (float kelvin) {return (int) (kelvin - 1000) / 100;}



    private static List <Color> colorList(String fileName)  {

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(BlackBodyColor.class.getResourceAsStream(fileName), US_ASCII))) {



            return bufferedReader.lines()
                    .filter(l -> ( !l.startsWith("#") &&  (l.regionMatches((int) DEG.low(), "10deg", 0, (int) DEG.size()))))
                    .map(l -> Color.web(l.substring((int) COLOR_RGB.low(), (int) COLOR_RGB.high())))
                    .collect(Collectors.toUnmodifiableList());



/*
           String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    //ne regarde que les lignes ne commençant pas part "#", ce sont des commentaires

                    if (line.regionMatches((int) DEG.low(), "10deg", 0, (int) DEG.size())) {
                        // ne regarde que les lignes contenant "10deg" à la position donée et transforme en instance de Color

                        Color colorFX = Color.web(line.substring((int) COLOR_RGB.low(), (int) COLOR_RGB.high()));
                        kelvinToColorMap.put(i, colorFX);
                        i++;
                    }
                }
            }
            return Collections.unmodifiableMap(kelvinToColorMap);

 */
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
