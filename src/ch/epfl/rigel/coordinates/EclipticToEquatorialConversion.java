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
    private final static Polynomial OBLIQUITY = Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006), -Angle.ofArcsec(46.815),
                                           Angle.ofDMS(23, 26, 21.45));

    /**
     * Constructeur de changement de coordonnées pour le couple date/heure donné
     *
     * @param when moment d'observation pour la conversion
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {

        double obliquity = OBLIQUITY.at(Epoch.J2000.julianCenturiesUntil(when));
        cosObliq = Math.cos(obliquity);
        sinObliq = Math.sin(obliquity); }

    /**
     * Effectue la conversion de coordonées ecliptiques en coordonnées équatoriales
     *
     * @param ecl les coordonnées ecliptiques à convertir
     *
     * @return les coordonnées écliptiques converties en coordonnées équatoriales
     */
    @Override
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {

        double ra = Angle.normalizePositive(Math.atan2(
                        Math.sin(ecl.lon())*cosObliq - Math.tan(ecl.lat())*sinObliq,
                        Math.cos(ecl.lon())));
        double decl = Math.asin(Math.sin(ecl.lat())*cosObliq +
                        Math.cos(ecl.lat())*sinObliq*Math.sin(ecl.lon()));

        return EquatorialCoordinates.of(ra, decl); }

    /**
     * Lance une exception car on ne peut pas utiliser cette méthode avec une conversion de coordonnées
     *
     * @throws UnsupportedOperationException car cette méthode ne peut pas être appelée pour une conversion de coordonnées
     */
    @Override
    public int hashCode() {throw new UnsupportedOperationException(); }

    /**
     * Lance une exception car on ne peut pas utilié cette méthode avec une conversion de coordonées
     * @param obj objet arbiitraire
     *
     * @throws UnsupportedOperationException car cette méthode ne peut pas être appelée pour une conversion de coordonnées
     */
    @Override
    public boolean equals(Object obj) {throw new UnsupportedOperationException(); }
}









































