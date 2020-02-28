package ch.epfl.rigel.math;


/**
 * Angle
 * permet la gestion des angles
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class Angle {

    final public static double TAU= 2*Math.PI;

    private static final RightOpenInterval INTERVAL_NORMAL_TRIGO = RightOpenInterval.of(0,TAU);

    private static final double DEG_PER_RAD = 360.0 / TAU;
    private static final double RAD_PER_DEG = TAU / 360.0;
    private static final double RAD_PER_HR = TAU/24;
    private static final double HR_PER_RAD = 24/TAU;
    private static final double SEC_PER_DEG= 1.0/ (60*60);



    /**
     * constructeur privé et sans argument pour que la classe soit ininstantiable
     */
    private Angle(){};

    /**
     * Normalise un angle en radians quelquonque dans l'intrervalle [o, TAU[
     * @param rad angle en radian à normaliser
     * @return l'angle normalisé
     */

    public static double normalizePositive(double rad) {

        return INTERVAL_NORMAL_TRIGO.reduce(rad);

    }

    /**
     * Transforme un angle donné en secondes en radians
     * @param sec secondes à transformer
     * @return l'angle transformé de radians
     */
    public static  double ofArcsec(double sec){
        return ofDeg(sec*SEC_PER_DEG);
    }

    /**
     * transforme un angle donné en degré, minutes, secondes en radians
     * @param deg degrés de l'angle donné
     * @param min minutes de l'angle donné
     * @param sec secondes de l'angle donné
     * @return l'angle transformé en radian
     */
     public static double ofDMS (int deg, int min, double sec){
        if(!(min<60 && min>=0 )|| !(sec<60 && sec>=0)) {
            throw new IllegalArgumentException();
        } else {
            return ofDeg(deg+ (min+ (sec/60))/60);

        }
     }

    /**
     * transforme un angle donné en degré en radian
     * @param deg angle en degré
     * @return angle en radians
     */
    public static double ofDeg(double deg){
        return deg*RAD_PER_DEG;
    }

    /**
     * transforme un angle donné en radians en degés
     * @param rad angle en radians
     * @return angle en dgré
     */
    public static double toDeg (double rad) {
        return rad*DEG_PER_RAD;
    }

    /**
     * transforme un angle donné en heure en radians
     * @param hr angle en heures
     * @return angles en radians
     */
    public static double ofHr (double hr){
        return hr*RAD_PER_HR;
    }

    /**
     * transforme un angle donné en radian en heures
     * @param rad angle en radians
     * @return angle en heure
     */
    public static double toHr (double rad){
        return rad*HR_PER_RAD;
    }





     }

