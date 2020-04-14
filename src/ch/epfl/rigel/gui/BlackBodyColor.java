package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Classe de couleur d'un corps noir dépendamment de sa température
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public class BlackBodyColor {


    private final static Map < Integer, Color> colorMap = colorMap();
    private final static RightOpenInterval DEG = RightOpenInterval.of(10,15);
    private final static RightOpenInterval COLOR_RGB = RightOpenInterval.of(80,87);

    private final static ClosedInterval TEMPERATURE_RANGE = ClosedInterval.of(1000, 40000);


    private BlackBodyColor(){ }

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
        return colorMap.get(tempToIndex(validKelvinTemprature(kelvin))); }

    private static int validKelvinTemprature (float kelvin){
        int temp = Math.round(kelvin/100);
        return temp*100; }

    private static int tempToIndex (float kelvin){
        return (int) (kelvin - 1000) / 100; }


    private static Map <Integer, Color> colorMap(){

        Map <Integer, Color> kelvinToColorMap = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(BlackBodyColor.class.getResourceAsStream("/bbr_color.txt"), US_ASCII))) {
            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (! line.startsWith("#")){
                    //ne regarde que les lignes ne commençant pas part "#", ce sont des commentaires

                    if (line.regionMatches((int)DEG.low(), "10deg", 0, (int)DEG.size())){
                        // ne regarde que les lignes contenant "10deg" à la position donée et transforme les kelvin en
                        // float et la temperature en instance de Color

                       // int  kelvinTemp = Integer.parseInt(line.substring((int)KELVIN_TEMPERATURE.low(),(int)KELVIN_TEMPERATURE.high()));

                        Color colorFX = Color.web(line.substring((int)COLOR_RGB.low(), (int)COLOR_RGB.high()));
                        kelvinToColorMap.put(i,colorFX);
                        i++;}
                }
            }
            return kelvinToColorMap;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }



     /**


    private enum ColorCatalogue{

        CATALOGUE;
        private final Map < Integer, Color> kelvinToColor;
        //TODO pourquoi c'est illegal d'accéder à des éléments static dans le constructeur d'une enum?
        private final RightOpenInterval KELVIN_TEMPERATURE = RightOpenInterval.of(1,6);
        private final RightOpenInterval DEG  = RightOpenInterval.of(10,15);
        private final RightOpenInterval COLOR_RGB = RightOpenInterval.of(80,87);
        private String deg = "10deg";

         ColorCatalogue (){

            Map <Integer, Color> kelvinToColorMap = new HashMap<>();

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ColorCatalogue.class.getResourceAsStream("/bbr_color.txt"), US_ASCII))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (! line.startsWith("#")){
                        //ne regarde que les lignes ne commençant pas part "#", ce sont des commentaires
                        if (line.regionMatches((int)DEG.low(), deg, 0, (int)DEG.size())){
                            // ne regarde que les lignes contenant "10deg" à la position donée et transforme les kelvin en
                            // float et la temperature en instance de Color

                            int  kelvinTemp = Integer.parseInt(line.substring((int)KELVIN_TEMPERATURE.low(),(int)KELVIN_TEMPERATURE.high()));
                            Color colorFX = Color.web(line.substring((int)COLOR_RGB.low(), (int)COLOR_RGB.high()));

                            kelvinToColorMap.put(kelvinTemp,colorFX); }
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            kelvinToColor = Collections.unmodifiableMap(kelvinToColorMap);
        }

        private Color kelvinToColor(float kelvin) { return kelvinToColor.get(validKelvinTemprature(kelvin)); }

        //TODO à tester
        private int validKelvinTemprature (float kelvin){
            int temp = Math.round(kelvin/100);
            return temp*100; }

    }
      */
}
