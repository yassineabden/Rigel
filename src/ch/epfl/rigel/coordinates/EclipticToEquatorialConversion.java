package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Classe de conversion de coordonées ecliptiques en coordonées équatoriales
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates,EquatorialCoordinates> {

    private final double cosObliq, sinObliq;

    //TODO on l'utilise que dans le constructeur mais comme ça il est déjé fait non?
    private final Polynomial OBLIQUITY = Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006), -Angle.ofArcsec(46.815),
                                           Angle.ofDMS(23, 26, 21.45));



    /**
     * Constructeurs de coordonées , définit le moment d'observation pour la conversion
     *
     * @param when moment d'observation pour la conversion
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {

        double obliquity = OBLIQUITY.at(Epoch.J2000.julianCenturiesUntil(when));
        this.cosObliq = Math.cos(obliquity);
        this.sinObliq = Math.sin(obliquity);
    }

    /**
     * Effectue la conversion de coordonées ecliptiques en coordonées équatoriales
     *
     * @param ecl les coordonnées ecliptiques à converser
     *
     * @return les coordonées conversées en coordonées équatoriales
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {

        double ra = Angle.normalizePositive(Math.atan2(
                        Math.sin(ecl.lon()) * cosObliq - Math.tan(ecl.lat()) * sinObliq,
                        Math.cos(ecl.lon())));
        double decl = Math.asin(Math.sin(ecl.lat()) * cosObliq +
                        Math.cos(ecl.lat())*sinObliq*Math.sin(ecl.lon()));

        return EquatorialCoordinates.of(ra, decl); }

    /**
     * Lance une exception car on ne peut pas utilié cette méthode avec une conversion de coordonées
     *
     * @throws UnsupportedOperationException car car cette méthode de peut pas être appelée pour une conversion de coordonées
     */
    @Override
    public int hashCode() {throw new UnsupportedOperationException(); }

    /**
     * Lance une exception car on ne peut pas utilié cette méthode avec une conversion de coordonées
     *
     * @throws UnsupportedOperationException car car cette méthode de peut pas être appelée pour une conversion de coordonées
     */
    @Override
    public boolean equals(Object obj) {throw new UnsupportedOperationException(); }
}









































