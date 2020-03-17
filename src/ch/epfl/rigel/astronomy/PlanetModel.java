package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

import java.security.AllPermission;
import java.util.ArrayList;
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
    private final double periodeRevol;
    private final double longToJ2010;
    private final double longAuPerigee;
    private final double excOrbite;
    private final double ua;
    private final double inclinaison;
    private final double lonNoeud;
    private final double tailleAng;
    private final double magnitude;

    public static List<PlanetModel> ALL = Arrays.asList(PlanetModel.values());


    private PlanetModel(String name, double periodeRevol, double longToJ2010, double longAuPerigee, double excOrbite, double ua,
                        double inclinaison, double lonNoeud, double tailleAng, double magnitude){
        this.name=name;
        this.periodeRevol= periodeRevol;
        this.longToJ2010= Angle.ofDeg(longToJ2010);
        this.longAuPerigee= Angle.ofDeg(longAuPerigee);
        this.excOrbite= excOrbite;
        this.ua= ua;
        this.inclinaison= Angle.ofDeg(inclinaison);
        this.lonNoeud= Angle.ofDeg(lonNoeud);
        this.tailleAng= Angle.ofDMS(0,0,tailleAng);
        this.magnitude= magnitude; }




    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        return null;



    }

}

