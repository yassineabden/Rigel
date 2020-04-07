package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Enumération modélisant le soleil
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public enum SunModel implements CelestialObjectModel<Sun> {

    SUN;

    final private static double AV_ANGULAR_VELOCITY = Angle.TAU / 365.242191;
    final private static double ORBIT_ECCENTRICITY_AT_J2010 = 0.016705;

    final private static double ECLIPTIC_LON_OF_PERIGEE_AT_J2010 = Angle.ofDeg(283.112438);
    final private static double ECLIPTIC_LON_AT_J2010 = Angle.ofDeg(279.557208);
    final private static double ANGULAR_DIAMETER =  Angle.ofDeg(0.533128);

    /**
     * Retourne le soleil après un jnombre de jour donnés et une conversion de coordonées ecliptic en coordonées équatoriales
     *
     * @param daysSinceJ2010 nombre de jour après J2010 double
     * @param eclipticToEquatorialConversion conversion de coordonées ecliptic en coordonées équatoriales
     *
     * @return le nouveau soleil
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double meanAnomaly = AV_ANGULAR_VELOCITY*daysSinceJ2010 + ECLIPTIC_LON_AT_J2010 - ECLIPTIC_LON_OF_PERIGEE_AT_J2010;
        double realAnomaly = meanAnomaly + 2*ORBIT_ECCENTRICITY_AT_J2010*Math.sin(meanAnomaly);

        double lonEclipticGeocentric = Angle.normalizePositive(realAnomaly + ECLIPTIC_LON_OF_PERIGEE_AT_J2010);
        double angularSize = ANGULAR_DIAMETER*(1+ ORBIT_ECCENTRICITY_AT_J2010*Math.cos(realAnomaly)) /
                                 (1 - ORBIT_ECCENTRICITY_AT_J2010*ORBIT_ECCENTRICITY_AT_J2010);

        EclipticCoordinates eclPos = EclipticCoordinates.of(lonEclipticGeocentric,0.0);
        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply(eclPos);

        return new Sun(eclPos,equatorialPos,(float)(angularSize),(float)(meanAnomaly)); }
}
