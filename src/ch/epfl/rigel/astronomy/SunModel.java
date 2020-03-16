package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.math.Angle;

public enum SunModel implements CelestialObjectModel<Sun> {


    SUN;

    final private static double TROPICAL_YEAR = 365.242191;
    final private static double ORBIT_ECCENTRICITY_AT_J2010 = 0.016705;

    final private static double ECLIPTIC_LON_OF_PERIGEE_AT_J2010 = Angle.ofDeg(283.112438);
    final private static double ECLIPTIC_LON_AT_J2010 = Angle.ofDeg(279.557208);
    final private static double ANGULAR_DIAMETER =  Angle.ofDeg(0.533128);


    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double N = Angle.normalizePositive(Angle.TAU*daysSinceJ2010 / TROPICAL_YEAR);

        double meanAnomaly = N + ECLIPTIC_LON_AT_J2010 + ECLIPTIC_LON_OF_PERIGEE_AT_J2010;
        double realAnomaly = meanAnomaly + 2*ORBIT_ECCENTRICITY_AT_J2010*Math.sin(meanAnomaly);

        double longEclipticGeocentric = realAnomaly + ECLIPTIC_LON_OF_PERIGEE_AT_J2010;
        double angularSize = ANGULAR_DIAMETER*( (1+ ORBIT_ECCENTRICITY_AT_J2010*Math.cos(realAnomaly)) / (1 - ORBIT_ECCENTRICITY_AT_J2010*ORBIT_ECCENTRICITY_AT_J2010));

        EclipticCoordinates eclPos= EclipticCoordinates.of(longEclipticGeocentric,0.0);
        EquatorialCoordinates equatorialPos = eclipticToEquatorialConversion.apply(eclPos);

        Sun newSun = new Sun(eclPos,equatorialPos,(float)(angularSize),(float)(meanAnomaly));

        return newSun;
    }
}
