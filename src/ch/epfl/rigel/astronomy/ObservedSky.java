package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

public final class ObservedSky {

    private final ZonedDateTime observTime;
    private final GeographicCoordinates observPosition;
    private final StereographicProjection stereographicProjection;
    private final StarCatalogue starCatalogue;
    private final Sun sun;
    private final CartesianCoordinates sunCoord;
    private final Moon moon;
    private final CartesianCoordinates moonCoord;
    //private final List<Planet> planets;
    private final Map<List<CelestialObject>,double[]> celestialObjectCartesianCoordinates;




    public ObservedSky(ZonedDateTime observTime, GeographicCoordinates observPosition, StereographicProjection stereographicProjection, StarCatalogue starCatalogue) {

        this.observTime = observTime;
        this.observPosition = observPosition;
        this.stereographicProjection = stereographicProjection;
        this.starCatalogue = starCatalogue;
        double daysSinceJ2010= Epoch.J2010.daysUntil(observTime);

        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(observTime);
        Map<List<CelestialObject>,double[]> coordinatesMap = new HashMap<>();

        sun = SunModel.SUN.at(daysSinceJ2010,eclipticToEquatorialConversion);
        moon = MoonModel.MOON.at(daysSinceJ2010,eclipticToEquatorialConversion);
        sunCoord= applyFromObject(sun);
        moonCoord= applyFromObject(moon);


        // Toutes les planètes sauf la Terre
        List<CelestialObject> planet = new ArrayList<>(PlanetModel.ALL.size()-1);
        List<CelestialObject> stars = List.copyOf(starCatalogue.stars());

        for (PlanetModel planetModel: PlanetModel.ALL ) {
            if (planetModel != PlanetModel.EARTH){
                Planet p = planetModel.at(daysSinceJ2010, eclipticToEquatorialConversion);
                planet.add(p);
            } }
        coordinatesMap.put(planet,listToArray(planet));
        coordinatesMap.put(stars,listToArray(stars));
        //planets= List.copyOf(planet);
        celestialObjectCartesianCoordinates=Collections.unmodifiableMap(coordinatesMap);
    }



    private CartesianCoordinates applyFromObject ( CelestialObject celestialObject){
        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(observTime,observPosition);
        return stereographicProjection.apply(equatorialToHorizontalConversion.apply(celestialObject.equatorialPos())); }

    private double[] listToArray (List<CelestialObject> list){
        double [] array= new double[2*list.size()];
        int i=0;
        for (CelestialObject c: list){
            CartesianCoordinates coordinates = applyFromObject(c);
            array[i]= coordinates.x();
            array[i+1]=coordinates.y();
            i+=2; }
        return array; }

    public Sun sun(){ return sun; }
    public CartesianCoordinates sunPosition(){ return sunCoord;}

    public Moon moon(){ return moon; }
    public CartesianCoordinates moonPosition(){ return moonCoord;}

   // public List <Planet> planets() {return planets;}
    //public double [] planetsPositions() {return celestialObjectCartesianCoordinates.get(planets);}

    public List<Star> stars(){ return starCatalogue.stars();}
    public double [] starsPositions(){ return celestialObjectCartesianCoordinates.get(starCatalogue.stars());}

    public Set<Asterism> asterisms(){ return starCatalogue.asterisms();}

    public List<Integer> asterismIndices(Asterism asterism){ return starCatalogue.asterismIndices(asterism) ;}

    //TODO
    public CelestialObject objectClosestTo(CartesianCoordinates cartesianCoordinates,double distance){
    return null;
    }


}
