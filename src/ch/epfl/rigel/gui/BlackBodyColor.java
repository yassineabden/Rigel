package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Classe de couleur d'un corps noir dépendamment de sa température
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public class BlackBodyColor {

    //TODO comme j'ai fait une enum il y a pas besoin
    private static ColorCatalogue colorCatalogue;

    /**
     * classe non instanciable mais je peux faire ça ou pas?
     */
    private BlackBodyColor(){
       //TODO comme j'ai fait une enum il y a pas besoin
        /**
        try (InputStream bbrColor = BlackBodyColor.class.getResourceAsStream("/bbr_color.txt")){
            colorCatalogue = new ColorCatalogue(bbrColor);
        }catch (IOException e) {
            throw new UncheckedIOException(e);
        }
         */
    }

    /**
     * Retourne la couleur correspondant à la température donnée
     *
     * @param kelvin température donnée en degrés kelvin
     *
     * @throws IllegalArgumentException si la température donée n'est pas comprise dans
     * @return la couleur correspondant à la température donnée
     */
    public Color colorForTemperature (float kelvin){

        Preconditions.checkArgument(ColorCatalogue.CATALOGUE.isValidKelvinTemperature(kelvin));
        return ColorCatalogue.CATALOGUE.kelvinToColor(kelvin); }

    /**
        //TODO à tester -> je l'ai mis dans l'enum
        private float validKelvinTemprature (float kelvin){
            float temp = Math.round(kelvin/100);
            return temp*100;
        }
     */

    /**
     * Test ENUM
     */
    private enum ColorCatalogue{

        CATALOGUE;
        private  Map < Float, Color> kelvinToColor;
        private  RightOpenInterval KELVIN_TEMPERATURE = RightOpenInterval.of(1,6);
        private  RightOpenInterval DEG  = RightOpenInterval.of(10,15);
        private  RightOpenInterval  COLOR_RGB = RightOpenInterval.of(80,87);
        private String deg = "10deg";


         ColorCatalogue (){

            Map <Float, Color> kelvinToColorMap = new HashMap<>();

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ColorCatalogue.class.getResourceAsStream("/bbr_color.txt"), US_ASCII))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (! line.startsWith("#")){
                        //ne regarde que les lignes ne commençant pas part "#", ce sont des commentaires
                        if (line.regionMatches((int)DEG.low(), deg, 0, (int)DEG.size())){
                            // ne regarde que les lignes contenant "10deg" à la position donée et transforme les kelvin en
                            // float et la temperature en instance de Color
                            float kelvinTemp = Float.parseFloat(line.substring((int)KELVIN_TEMPERATURE.low(),(int)KELVIN_TEMPERATURE.high()));
                            Color colorFX = Color.web(line.substring((int)COLOR_RGB.low(), (int)COLOR_RGB.high()));

                            kelvinToColorMap.put(kelvinTemp,colorFX); }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            kelvinToColor = Collections.unmodifiableMap(kelvinToColorMap);
        }

        private Color kelvinToColor(float kelvin) { return kelvinToColor.get(validKelvinTemprature(kelvin)); }

        private boolean isValidKelvinTemperature(float kelvin) { return kelvinToColor.containsKey(validKelvinTemprature(kelvin)); }

        //TODO à tester
        private float validKelvinTemprature (float kelvin){
            float temp = Math.round(kelvin/100);
            return temp*100; }

    }

/**
    private class ColorCatalogue{

        private  Map < Float, Color> kelvinToColor;
        private  RightOpenInterval KELVIN_TEMPERATURE = RightOpenInterval.of(1,6);
        private  RightOpenInterval DEG  = RightOpenInterval.of(10,15);
        private  RightOpenInterval  COLOR_RGB = RightOpenInterval.of(80,87);
        private String deg = "10deg";


        private ColorCatalogue(InputStream inputStream) throws IOException {

            Map <Float, Color> kelvinToColorMap = new HashMap<>();

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    if (! line.startsWith("#")){
                        //ne regarde que les lignes ne commençant pas part "#", ce sont des commentaires
                         if (line.regionMatches((int)DEG.low(), deg, 0, (int)DEG.size())){
                            // ne regarde que les lignes contenant "10deg" à la position donée et transforme les kelvin en
                             // float et la temperature en instance de Color
                             float kelvinTemp = Float.parseFloat(line.substring((int)KELVIN_TEMPERATURE.low(),(int)KELVIN_TEMPERATURE.high()));
                             Color colorFX = Color.web(line.substring((int)COLOR_RGB.low(), (int)COLOR_RGB.high()));

                            kelvinToColorMap.put(kelvinTemp,colorFX); }
                    }
                 }
             }
            kelvinToColor = Collections.unmodifiableMap(kelvinToColorMap);
        }


    }
 */
}
