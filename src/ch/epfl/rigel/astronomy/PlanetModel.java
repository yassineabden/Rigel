package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.Arrays;
import java.util.List;

/**
 * Enumération modélisant une planète
 *
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
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    private final String name;
    private final double periodRevol, lonAtJ2010, lonAtPerigee, excOrbite, orbitAxis,
            orbitIncl, lonOrbitalNode, angularSize, magnitude;

    // Sinus et cosinus de l'inclinaison de l'orbite, vitesse angulaire moyenne
    private final double cos_i, sin_i, avAngularVelocity;

    /**
     * Liste contenant toute les planètes
     */
    public static List<PlanetModel> ALL = Arrays.asList(PlanetModel.values());


    PlanetModel(String name, double periodRevol, double longAtJ2010, double longAtPerigee, double excOrbite, double orbitAxis,
                double inclinationOfOrbiteAtEcl, double lonOrbitalNode, double angularSize, double magnitude) {

        this.name = name;
        this.periodRevol = periodRevol;
        this.lonAtJ2010 = Angle.ofDeg(longAtJ2010);
        this.lonAtPerigee = Angle.ofDeg(longAtPerigee);
        this.excOrbite = excOrbite;
        this.orbitAxis = orbitAxis;
        this.orbitIncl = Angle.ofDeg(inclinationOfOrbiteAtEcl);
        cos_i = Math.cos(orbitIncl);
        sin_i = Math.sin(orbitIncl);
        this.lonOrbitalNode = Angle.ofDeg(lonOrbitalNode);
        this.angularSize = Angle.ofArcsec(angularSize);
        this.magnitude = magnitude;
        avAngularVelocity = Angle.TAU / (365.242191 * periodRevol);
    }

    /**
     * Retourne la planète après un nombre de jours donné et une conversion de coordonéées ecliptiques en coordoonées équatoriales
     *
     * @param daysSinceJ2010                 nombre de jours après J2010
     * @param eclipticToEquatorialConversion conversion de coordonnées à appliquer
     * @return la nouvelle planète
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        // Calcul les données pour la planète
        double meanAnomaly = meanAnomaly(avAngularVelocity, daysSinceJ2010, lonAtJ2010, lonAtPerigee);
        double realAnomaly = realAnomaly(meanAnomaly,excOrbite );

        double radiusToSun = orbitAxis * (1 - excOrbite * excOrbite) /
                (1 + excOrbite * Math.cos(realAnomaly));
        double helioLon = realAnomaly + lonAtPerigee;
        double helioMinusNode = helioLon - lonOrbitalNode;
        double helioEclLat = Math.asin(Math.sin(helioMinusNode) * sin_i);

        double radiusProjOnEcliptic = radiusToSun * Math.cos(helioEclLat);
        double helioEclLon = Math.atan2(Math.sin(helioMinusNode) * cos_i, Math.cos(helioMinusNode))
                + lonOrbitalNode;

        // Calcul les données pour la Terre
        double earthM = meanAnomaly(EARTH.avAngularVelocity, daysSinceJ2010 ,EARTH.lonAtJ2010, EARTH.lonAtPerigee);
        double earthV = realAnomaly(earthM ,EARTH.excOrbite);

        double earthR = EARTH.orbitAxis * (1 - EARTH.excOrbite * EARTH.excOrbite) / (1 + EARTH.excOrbite * Math.cos(earthV));
        double earthL = earthV + EARTH.lonAtPerigee;

        double earthDistance = Math.sqrt(earthR * earthR + radiusToSun * radiusToSun -
                2 * earthR * radiusToSun * Math.cos(helioLon - earthL) * Math.cos(helioEclLat));
        double newAngularSize = angularSize / earthDistance;

        double latDenom = earthR * Math.sin(helioEclLon - earthL);


        //Calcul de la longitude em fonction des planètes (inférieures ou supérieures)
        double lon = ((periodRevol < 1)) ?
                Angle.normalizePositive(Math.PI + earthL
                        + Math.atan2(radiusProjOnEcliptic * Math.sin(earthL - helioEclLon)
                             ,earthR - radiusProjOnEcliptic * Math.cos(earthL - helioEclLon)))
                : Angle.normalizePositive(helioEclLon
                        + Math.atan2(latDenom
                                ,radiusProjOnEcliptic - earthR * Math.cos(helioEclLon - earthL)));


        // Calcul le reste des coordonées, la phase et la nouvelle magnitude
        double newLat = Math.atan((radiusProjOnEcliptic * Math.tan(helioEclLat) * Math.sin(lon - helioEclLon))
                / latDenom);
        EquatorialCoordinates newEqCoord = eclipticToEquatorialConversion.
                apply(EclipticCoordinates.of(lon, newLat));

        double phaseSqrt = Math.sqrt((1 + Math.cos(lon - helioLon)) / 2);
        double newMagnitude = magnitude + 5 * Math.log10(radiusToSun * earthDistance / phaseSqrt);

        return new Planet(name, newEqCoord, (float) (newAngularSize), (float) (newMagnitude));
    }

    // Calcul l'anomalie moyenne d'une planète
    private double meanAnomaly(double avAngularVelocity,double daysSinceJ2010, double lonAtJ2010, double lonAtPerigee) {
        return avAngularVelocity*daysSinceJ2010 + lonAtJ2010 - lonAtPerigee;
    }

    // Calcul l'anomalie réelle d'une planète
    private double realAnomaly(double meanAnomaly, double excOrbite) {
        return meanAnomaly + 2 * excOrbite * Math.sin(meanAnomaly);
    }
}

