package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.Arrays;
import java.util.List;
/**
 * Enum modélisant une planète
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public enum PlanetModel implements CelestialObjectModel<Planet> {

    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 271.063148, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    private final String name;
    private final double periodRevol;
    private final double lonAtJ2010;
    private final double lonAtPerigee;
    private final double excOrbite;
    private final double a;
    private final double orbitIncl;
    private final double lonOrbitalNode;
    private final double angularSize;
    private final double magnitude;

    private final double cos_i, sin_i;

    public static List<PlanetModel> ALL = Arrays.asList(PlanetModel.values());
    final private static double TROPICAL_YEAR = 365.242191;

    /**
     *
     * @param name nom de la planète
     * @param periodRevol période de révolution
     * @param longAtJ2010 longitude à J2010
     * @param longAtPerigee longitude au périgrée
     * @param excOrbite Excentricité de l'robite
     * @param a demi grand-axe de l'orbite
     * @param inclinationOfOrbiteAtEcl Inclinaison de l'orbite à l'écliptique
     * @param lonOrbitalNode longitude du noeud ascendant
     * @param angularSize taille angualire
     * @param magnitude magnitude
     */

    private PlanetModel(String name, double periodRevol, double longAtJ2010, double longAtPerigee, double excOrbite, double a,
                        double inclinationOfOrbiteAtEcl, double lonOrbitalNode, double angularSize, double magnitude){
        this.name=name;
        this.periodRevol = periodRevol;
        this.lonAtJ2010 = Angle.ofDeg(longAtJ2010);
        this.lonAtPerigee = Angle.ofDeg(longAtPerigee);
        this.excOrbite= excOrbite;
        this.a = a;
        this.orbitIncl = Angle.ofDeg(inclinationOfOrbiteAtEcl);
        cos_i = Math.cos(orbitIncl);
        sin_i = Math.sin(orbitIncl);
        this.lonOrbitalNode = Angle.ofDeg(lonOrbitalNode);
        this.angularSize = Angle.ofArcsec(angularSize);
        this.magnitude= magnitude;

    }

    /**
     *
     * @param daysSinceJ2010 nombre de jour après J2010 double
     * @param eclipticToEquatorialConversion coordonées équatoriales à J2010
     * @return la planète
     */


    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double meanAnomaly = (Angle.TAU*daysSinceJ2010)/(TROPICAL_YEAR*periodRevol)+ lonAtJ2010 - lonAtPerigee;
        double realAnomaly = meanAnomaly + 2*excOrbite*Math.sin(meanAnomaly);

        double radiusToSun = a*(1-excOrbite*excOrbite) / ( 1 + excOrbite*Math.cos(realAnomaly));
        double helioLon = realAnomaly + lonAtPerigee;
        double helioEclLat = Math.asin(Math.sin(helioLon - lonOrbitalNode)*sin_i);

        double radiusProjOnEcliptic = radiusToSun*Math.cos(helioEclLat);
        double helioEclLon = Math.atan2(Math.sin(helioLon - lonOrbitalNode)*cos_i,
                                                Math.cos(helioLon - lonOrbitalNode))
                                        + lonOrbitalNode;

        double earthM = (Angle.TAU*daysSinceJ2010)/(TROPICAL_YEAR*EARTH.periodRevol) + EARTH.lonAtJ2010 - EARTH.lonAtPerigee;
        double earthV = earthM + 2*EARTH.excOrbite*Math.sin(earthM);

        double earthR = EARTH.a*(1 - EARTH.excOrbite*EARTH.excOrbite) / (1 + EARTH.excOrbite*Math.cos(earthV));
        double earthL = earthV + EARTH.lonAtPerigee;

        double earthDistance= Math.sqrt(earthR*earthR + radiusToSun*radiusToSun - 2*earthR*radiusToSun
                                        *Math.cos(helioLon-earthL)*Math.cos(helioEclLat));
        double newAngularSize = angularSize/earthDistance;

        double latDenom = earthR * Math.sin(helioEclLon - earthL);
        if (periodRevol<1) {
            double lonInf = Angle.normalizePositive( Math.PI + earthL +
                    Math.atan2(radiusProjOnEcliptic * Math.sin(earthL - helioEclLon),
                    earthR - radiusProjOnEcliptic * Math.cos(earthL - helioEclLon)));

            double newLat = Math.atan((radiusProjOnEcliptic * Math.tan(helioEclLat) * Math.sin(lonInf - helioEclLon))
                    / latDenom);

            EclipticCoordinates eclInf = EclipticCoordinates.of(lonInf, newLat);
            EquatorialCoordinates eqInf = eclipticToEquatorialConversion.apply(eclInf);

            double phase = ((1+Math.cos(lonInf-helioLon))/2);
            double newMagnitude = magnitude+ 5* Math.log10(radiusToSun*earthDistance/Math.sqrt(phase));

            return new Planet(name, eqInf, (float) (newAngularSize), (float) (newMagnitude));

        } else {
                double lonSup = Angle.normalizePositive(helioEclLon + Math.atan2(latDenom,
                        radiusProjOnEcliptic - earthR*Math.cos(helioEclLon - earthL)));

                double newLat1 = Math.atan((radiusProjOnEcliptic*Math.tan(helioEclLat)*Math.sin(lonSup-helioEclLon))
                        / latDenom);

                EclipticCoordinates eclSup = EclipticCoordinates.of(lonSup,newLat1);
                EquatorialCoordinates eqSup = eclipticToEquatorialConversion.apply(eclSup);

                double phase = ((1+Math.cos(lonSup-helioLon))/2);
                double newMagnitude = magnitude+ 5* Math.log10(radiusToSun*earthDistance/Math.sqrt(phase));

            return new Planet(name, eqSup,(float)(newAngularSize),(float)(newMagnitude)); }
    }
}

