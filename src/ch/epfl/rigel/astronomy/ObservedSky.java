package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

public final class ObservedSky {

   // private final ZonedDateTime observTime;
   // private final GeographicCoordinates observPosition;
   // private final StereographicProjection stereographicProjection;
    private final StarCatalogue starCatalogue;
    private final Sun sun;
    private final CartesianCoordinates sunCoord;
    private final Moon moon;
    private final CartesianCoordinates moonCoord;
    private final List<Planet> planets ;
    private final double [] planetsCoord ;
    private final double [] starsCord;


    public ObservedSky(ZonedDateTime observTime, GeographicCoordinates observPosition, StereographicProjection stereographicProjection, StarCatalogue starCatalogue) {

        //this.observTime = observTime;
        //this.observPosition = observPosition;
        //this.stereographicProjection = stereographicProjection;
        this.starCatalogue = starCatalogue;
        double daysSinceJ2010= Epoch.J2010.daysUntil(observTime);

        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(observTime);
        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(observTime,observPosition);

        sun = SunModel.SUN.at(daysSinceJ2010,eclipticToEquatorialConversion);
        moon = MoonModel.MOON.at(daysSinceJ2010,eclipticToEquatorialConversion);

        sunCoord = stereographicProjection.apply(equatorialToHorizontalConversion.apply(sun.equatorialPos()));
        moonCoord = stereographicProjection.apply(equatorialToHorizontalConversion.apply(moon.equatorialPos()));


        double [] pCoord = new double[14] ;
        ArrayList <Planet> planets = new ArrayList<>(7);

        List<Star> stars = starCatalogue.stars();
        double [] starsCord = new double [2*stars.size()];

        int i = 0;

        for (PlanetModel planetModel : PlanetModel.ALL) {
           //sûrement moyen de faire ça plus joli
            if (planetModel.equals(PlanetModel.EARTH)) {
            } else {
                Planet planet = planetModel.at(daysSinceJ2010, eclipticToEquatorialConversion);
                CartesianCoordinates posPlanet = stereographicProjection.apply(equatorialToHorizontalConversion.apply(planet.equatorialPos()));

                planets.add(planet);
                pCoord[i] = posPlanet.x();
                pCoord[i + 1] = posPlanet.y();
                i++;
            }
        }

        this.planets = List.copyOf(planets);
        // Attention immuabilité
        this.planetsCoord = pCoord;

        i = 0;
        for (Star s: stars){
            CartesianCoordinates posStar = stereographicProjection.apply(equatorialToHorizontalConversion.apply(s.equatorialPos()));
            starsCord [i] = posStar.x();
            starsCord [i] = posStar.y();
            i++;
        }
        // Attention immuabilité
        this.starsCord = starsCord;
    }


    public Sun sun(){ return sun; }
    public CartesianCoordinates sunPosition(){ return sunCoord;}

    public Moon moon(){ return moon; }
    public CartesianCoordinates moonPosition(){ return moonCoord;}

    public List <Planet> planets() {return planets;}
    public double [] planetsPositions() {return planetsCoord;}

    public List<Star> stars(){ return starCatalogue.stars();}
    public double [] starsPositions(){ return starsCord;}

    public Set<Asterism> asterisms(){ return starCatalogue.asterisms();}
    public List<Integer> asterismIndices(Asterism asterism){ return starCatalogue.asterismIndices(asterism) ;}

    //TODO
    public CelestialObject objectClosestTo(CartesianCoordinates cartesianCoordinates){
        return null;
    }


}
