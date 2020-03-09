package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

public class Star extends CelestialObject {


    private static final ClosedInterval HIPPARCOS_INTERVAL = ClosedInterval.of(-0.5,5.5);
    private final int hipparcosId;
    private final float colorIndex;


    /**
     * Unique constructeur visible dans le package.
     *
     * @param name          nom de l'objet céleste
     * @param equatorialPos Position de l'objet en cordonées equatoriales
     * @param magnitude     magnitude
     * @throws IllegalArgumentException si la taille angulaire est négative
     * @throws NullPointerException     si le nom ou la position equatoriale sont nuls
     */
   public  Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
    super(name, equatorialPos,0f,magnitude);

    Preconditions.checkInInterval(HIPPARCOS_INTERVAL, colorIndex);

    if (!(hipparcosId <=0)){ throw new IllegalArgumentException();}

    this.colorIndex = colorIndex;
    this.hipparcosId = hipparcosId;
    }

    /**
     * Retourne le numéro d'Hipparcos
     *
     * @return numéro d'Hipparcos, un int
     */
   public int hipparcosId(){return hipparcosId;}

    /**
     *Calcul la couleur de la température
     *
     * @return la coulour de la température, la valeur entière en int
     */
   public int colorTemperature(){
       double c = 0.92*colorIndex;
       double T = 4600*(1/(c + 1.7) + 1/(c + 0.62));

       return (int)(Math.floor(T));
   }
}
