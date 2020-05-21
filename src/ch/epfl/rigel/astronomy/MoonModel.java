package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Enum modélisant une lune
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON;

    private final static double AVERAGE_LONG = Angle.ofDeg(91.929336);
    private final static double AVERAGE_LONG_PERIGREE = Angle.ofDeg(130.143076);
    private final static double LONGITUDE_NODE = Angle.ofDeg(291.682547);
    private final static double INCLIN_ORBIT = Angle.ofDeg(5.145396);
    private final static double ECCENT_ORBIT = 0.0549;

    /**
     * @param daysSinceJ2010                 nombre de jour après J2010 double
     * @param eclipticToEquatorialConversion coordonnées équatoriales à J2010
     * @return la lune
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        // Calcul de l'anomalie moyenne et la longitude écliptique du Soleil
        Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double longEclpSun = sun.eclipticPos().lon();
        double meanAnomalySun = Math.sin(sun.meanAnomaly());

        // Calcul de la longitude orbitale vraie
        double averageLongOrb = Angle.ofDeg(13.1763966) * daysSinceJ2010 + AVERAGE_LONG;
        double meanAnomaly = averageLongOrb - Angle.ofDeg(0.1114041) * daysSinceJ2010 - AVERAGE_LONG_PERIGREE;

        double evection = Angle.ofDeg(1.2739) * Math.sin(2 * (averageLongOrb - longEclpSun) - meanAnomaly);
        double correcEquAnnu = Angle.ofDeg(0.1858) * meanAnomalySun;
        double coorec3 = Angle.ofDeg(0.37) * meanAnomalySun;

        double anomalyCorr = meanAnomaly + evection - correcEquAnnu - coorec3;
        double coorecCenterEqu = Angle.ofDeg(6.2886) * Math.sin(anomalyCorr);
        double coorec4 = Angle.ofDeg(0.214) * Math.sin(2 * anomalyCorr);

        double correcOrbitLong = averageLongOrb + evection + coorecCenterEqu - correcEquAnnu + coorec4;
        double variation = Angle.ofDeg(0.6583) * Math.sin(2 * (correcOrbitLong - longEclpSun));

        double trueOrbLong = correcOrbitLong + variation;

        // Calcul de la longitude corrigée du nœud ascendant
        double averageLongNode = LONGITUDE_NODE - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double corrLongNode = averageLongNode - Angle.ofDeg(0.16) * meanAnomalySun;

        // Calcul de longitude et la latitude écliptique de la Lune grâce aux données précédentes
        double longMoon = Angle.normalizePositive(Math.atan2(Math.sin(trueOrbLong - corrLongNode) * Math.cos(INCLIN_ORBIT),
                Math.cos(trueOrbLong - corrLongNode)) + corrLongNode);
        double latMoon = Math.asin(Math.sin(trueOrbLong - corrLongNode) * Math.sin(INCLIN_ORBIT));


        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(longMoon, latMoon);
        EquatorialCoordinates coordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        // Calcul de la phase et de la taille angulaire
        double phase = (1 - Math.cos(trueOrbLong - longEclpSun)) / 2;
        double distance = (1 - ECCENT_ORBIT * ECCENT_ORBIT) / (1 + ECCENT_ORBIT * Math.cos(anomalyCorr + coorecCenterEqu));
        double angularSize = Angle.ofDeg(0.5181) / distance;

        return new Moon(coordinates, (float) angularSize, 0f, (float) phase);
    }
}

