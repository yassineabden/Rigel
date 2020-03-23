package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.Arrays;
import java.util.List;

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
    private final double inclinationOfOrbiteAtEcl;
    private final double lonOrbitalNode;
    private final double angularSize;
    private final double magnitude;

    public static List<PlanetModel> ALL = Arrays.asList(PlanetModel.values());

    final private static double TROPICAL_YEAR = 365.242191;



    private PlanetModel(String name, double periodRevol, double longAtJ2010, double longAtPerigee, double excOrbite, double a,
                        double inclinationOfOrbiteAtEcl, double lonOrbitalNode, double angularSize, double magnitude){
        this.name=name;
        this.periodRevol = periodRevol;
        this.lonAtJ2010 = Angle.ofDeg(longAtJ2010);
        this.lonAtPerigee = Angle.ofDeg(longAtPerigee);
        this.excOrbite= excOrbite;
        this.a = a;
        this.inclinationOfOrbiteAtEcl = Angle.ofDeg(inclinationOfOrbiteAtEcl);
        this.lonOrbitalNode = Angle.ofDeg(lonOrbitalNode);
        this.angularSize = Angle.ofArcsec(angularSize);
        this.magnitude= magnitude;
    }




    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        double N =  Angle.normalizePositive((Angle.TAU*daysSinceJ2010)/(TROPICAL_YEAR*periodRevol));
        double meanAnomaly = N + lonAtJ2010 - lonAtPerigee;
        double realAnomaly = meanAnomaly + 2*excOrbite*Math.sin(meanAnomaly);

        double rNum = a*(1-excOrbite*excOrbite);
        double radiusToSun = rNum / ( 1 + excOrbite*Math.cos(realAnomaly));

        double heliocentricLon = realAnomaly + lonAtPerigee;

        double sinLOmega = Math.sin(heliocentricLon - lonOrbitalNode);
        double cosLOmega = Math.cos(heliocentricLon - lonOrbitalNode);


        double heliocentricEclLat = Math.asin(sinLOmega*Math.sin(inclinationOfOrbiteAtEcl));

        double projectionOfRadiusOnEcliptic = radiusToSun*Math.cos(heliocentricEclLat);
        double heliocentricEclLon = Math.atan2(sinLOmega*Math.cos(inclinationOfOrbiteAtEcl), cosLOmega) + lonOrbitalNode;

        double earthN = Angle.normalizePositive((Angle.TAU*daysSinceJ2010)/(TROPICAL_YEAR*EARTH.periodRevol));
        double earthM = earthN + EARTH.lonAtJ2010 - EARTH.lonAtPerigee;
        double earthV = earthM + 2*EARTH.excOrbite*Math.sin(earthM);

        double earthRNum = EARTH.a*(1 - EARTH.excOrbite*EARTH.excOrbite);
        double earthR = earthRNum / (1 + EARTH.excOrbite*Math.cos(earthV));

        double earthL = earthV + EARTH.lonAtPerigee;

        double sinLL = Math.sin(earthL - heliocentricLon);
        double cosLL = Math.cos(earthL - heliocentricLon);

        double newLat = Math.atan2(projectionOfRadiusOnEcliptic*Math.tan(heliocentricEclLat)*sinLL, earthR - projectionOfRadiusOnEcliptic*cosLL);



        switch (this){
            case MERCURY:
            case VENUS:
                double aTanInf = Math.atan2(projectionOfRadiusOnEcliptic*sinLL, earthR - projectionOfRadiusOnEcliptic*cosLL);
                double lonInf = Math.PI + earthL + aTanInf;
                EclipticCoordinates eclInf = EclipticCoordinates.of(lonInf,newLat);
                EquatorialCoordinates eqInf = eclipticToEquatorialConversion.apply(eclInf);

                return new Planet(name, eqInf,(float)(angularSize),(float)(magnitude));

            case MARS:
            case SATURN:
            case URANUS:
            case JUPITER:
            case NEPTUNE:
                double aTanSup = Math.atan2(earthR*sinLL,projectionOfRadiusOnEcliptic-earthR*cosLL);
                double lonSup = heliocentricEclLon + aTanSup;
                EclipticCoordinates eclSup = EclipticCoordinates.of(lonSup,newLat);
                EquatorialCoordinates eqSup = eclipticToEquatorialConversion.apply(eclSup);

                return new Planet(name, eqSup,(float)(angularSize),(float)(magnitude));

            default:
                throw new IllegalArgumentException();


        }

    }

}

