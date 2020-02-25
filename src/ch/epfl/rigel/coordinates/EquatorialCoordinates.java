package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Des coordonnées équatoriales
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class EquatorialCoordinates extends SphericalCoordinates{

    private static final RightOpenInterval INTERVAL_RA_RAD = RightOpenInterval.of(0.0, Angle.TAU );
    private static final ClosedInterval INTERVAL_DEC_RAD =  ClosedInterval.symmetric(Angle.TAU/2);


    private EquatorialCoordinates(double raHr, double decDeg){

        super(Angle.ofHr(raHr),Angle.ofDeg(decDeg));

    }

    /**
     * Méthode de construction de coordonées équatoriales
     *
     * @param raHr ascension droite  en heure
     * @param decDeg declinaison en degré
     * @throws IllegalArgumentException si  l'ascension droite n'est pas inclus entre [0h, 24h[
     * @return IllegalArgumentException si la declinaison  n'est pas inclus entre [-90 deg, 90 deg]
     */
    public EquatorialCoordinates of(double raHr, double decDeg){

            Preconditions.checkArgument(INTERVAL_RA_RAD.contains(Angle.ofHr(raHr)));
            Preconditions.checkArgument(INTERVAL_DEC_RAD.contains(Angle.ofDeg(decDeg)));

            EquatorialCoordinates ec = new EquatorialCoordinates(raHr,decDeg);

            return ec;

    }

    /**
     * Vérifie qu'une heure est une ascension droite valide, comprise entre [0h, 24h[
     *
     * @param raHr ascension droite en heure
     * @return vrai si l'heure est valide, faux sinon
     */
    public static boolean isValidRaHr(double raHr){
        return INTERVAL_RA_RAD.contains(Angle.ofHr(raHr));
    }

    /**
     * Vérifie qu'un angle est une declinaison valide, contenue entre [-90 deg, 90 deg]
     *
     * @param dec latitude en degré à vérifier
     * @return vrai si l'angle est valide, faux sinon
     */
    public static boolean isValidDec(double dec){
        return INTERVAL_DEC_RAD.contains(Angle.ofDeg(dec));
    }

    /**
     * Retourne l'ascesion droite en radian
     *
     * @return l'ascension droite en radian, qui correspond à la longitude
     */
    public double ra(){
        return super.lon();
    }

    /**
     * Retourne l'ascesion droite en degrés
     *
     *  @return l'ascension droite en degrés, qui correspond à la longitude
     */

    public double raDeg(){ return super.lonDeg(); }


    /**
     * Retourne l'ascesion droite en heure
     *
     *  @return l'ascension droite en heure, qui correspond à la longitude
     */
    public double raHr(){ return Angle.toHr(super.lonDeg());
    }


    /**
     *  Retourne la déclinaison en radian
     *
     * @return declinaison en radian
     */

    public double dec(){ return super.lat(); }

    /**
     *  Retourne la déclinaison en degré
     *
     * @return declinaison en degré
     */

    public double decDeg(){
        return super.latDeg();
    }

    /**
     * Transforme en string les coordonées équatoriales
     *
     * @return une string de type (ra=6.5700°, dec=46.5200°)
     */
    @Override
    public String toString(){
    return String.format(Locale.ROOT, "(ra=%.4f.h, dec=%.4.f", raHr(), decDeg());
    }


}
