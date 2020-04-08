package ch.epfl.rigel.math;


import ch.epfl.rigel.Preconditions;

/**
 * Un angle
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class Angle {

    /**
     * Constante valant 360 degré
     */
    final public static double TAU= 2*Math.PI;

    private static final RightOpenInterval INTERVAL_NORMAL_TRIGO = RightOpenInterval.of(0,TAU);
    private static final RightOpenInterval INTERVAL_SEC_MIN = RightOpenInterval.of(0, 60);

    private static final double RAD_PER_HR = TAU/24;
    private static final double HR_PER_RAD = 24/TAU;
    private static final double SEC_PER_DEG= 1.0/ (60*60);


    private Angle(){};

    /**
     * Normalise un angle en radians quelconque dans l'intrervalle [o, TAU[
     *
     * @param rad angle en radian à normaliser
     *
     * @return l'angle normalisé
     */

    public static double normalizePositive(double rad) {

        return INTERVAL_NORMAL_TRIGO.reduce(rad); }

    /**
     * Transforme un angle donné en secondes en radians
     *
     * @param sec secondes d'angle à transformer
     *
     * @return l'angle transformé en radians
     */
    public static  double ofArcsec(double sec){
        return ofDeg(sec*SEC_PER_DEG);
    }

    /**
     * Transforme un angle donné en degré, minutes, secondes en radians
     *
     * @param deg degrés de l'angle donné
     * @param min minutes de l'angle donné
     * @param sec secondes de l'angle donné
     *
     * @throws IllegalArgumentException si les secondes où les minutes ne sont pas contenues dans l'intervalle [0,60[
     * @return l'angle transformé en radians
     */
     public static double ofDMS (int deg, int min, double sec){

         Preconditions.checkInInterval(INTERVAL_SEC_MIN, min);
         Preconditions.checkInInterval(INTERVAL_SEC_MIN,sec);
         return ofDeg(deg + (min +( sec/ 60))/60); }

    /**
     * Transforme un angle donné en degré en radian
     *
     * @param deg angle en degré
     *
     * @return angle en radians
     */
    public static double ofDeg(double deg){ return Math.toRadians(deg); }

    /**
     * Transforme un angle donné en radians en degrés
     *
     * @param rad angle en radians
     *
     * @return angle en degrés
     */
    public static double toDeg (double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Transforme un angle donné en heure en radians
     *
     * @param hr heures d'angle données
     *
     * @return angle en radians
     */
    public static double ofHr (double hr){ return hr*RAD_PER_HR; }

    /**
     * transforme un angle donné en radian en heures
     *
     * @param rad angle donné en radians
     *
     * @return angle en heures
     */
    public static double toHr (double rad){
        return rad*HR_PER_RAD;
    }

}

