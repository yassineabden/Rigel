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
    private final List<Planet> planets;
    private final Map<CartesianCoordinates,CelestialObject> celestialObjectToCoordinates;


    public ObservedSky(ZonedDateTime observTime, GeographicCoordinates observPosition, StereographicProjection stereographicProjection, StarCatalogue starCatalogue) {

        this.observTime = observTime;
        this.observPosition = observPosition;
        this.stereographicProjection = stereographicProjection;
        this.starCatalogue = starCatalogue;
        double daysSinceJ2010= Epoch.J2010.daysUntil(observTime);

        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(observTime);
        Map<CartesianCoordinates,CelestialObject> coordinatesMap = new HashMap<>();

        sun = SunModel.SUN.at(daysSinceJ2010,eclipticToEquatorialConversion);
        moon = MoonModel.MOON.at(daysSinceJ2010,eclipticToEquatorialConversion);
        sunCoord= applyFromObject(sun);
        moonCoord= applyFromObject(moon);
        coordinatesMap.put(sunCoord,sun);
        coordinatesMap.put(moonCoord,moon);

        List<Planet> planet = new ArrayList<>(PlanetModel.ALL.size()-1);
        List<Star> stars = List.copyOf(starCatalogue.stars());

        for (PlanetModel planetModel: PlanetModel.ALL ) {
            if (planetModel != PlanetModel.EARTH){
                Planet p = planetModel.at(daysSinceJ2010, eclipticToEquatorialConversion);
                CartesianCoordinates coordinates=applyFromObject(p);
                planet.add(p);
                coordinatesMap.put(coordinates,p); }
        }
        for (Star s: stars){
            CartesianCoordinates coordinates= applyFromObject(s);
            coordinatesMap.put(coordinates,s); }

        planets= List.copyOf(planet);
        celestialObjectToCoordinates =Collections.unmodifiableMap(coordinatesMap);
    }

    private CartesianCoordinates applyFromObject(CelestialObject celestialObject){
        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(observTime,observPosition);
        return stereographicProjection.apply(equatorialToHorizontalConversion.apply(celestialObject.equatorialPos())); }

    private double[] listToArray (List<? extends CelestialObject> list){
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

    public List <Planet> planets() {return planets;}

    public double [] planetsPositions() {return listToArray(planets);}

    public List<Star> stars(){ return starCatalogue.stars();}

    public double [] starsPositions(){ return listToArray(starCatalogue.stars()); }

    public Set<Asterism> asterisms(){ return starCatalogue.asterisms();}

    public List<Integer> asterismIndices(Asterism asterism){ return starCatalogue.asterismIndices(asterism) ;}


    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates cartesianCoordinates,double distance){
        double x0 = cartesianCoordinates.x();
        double y0 = cartesianCoordinates.y();
        double min = Double.MAX_VALUE;
        double minDistance = 0.0;
        Optional<CelestialObject> cc = Optional.empty();
        for (CartesianCoordinates c: celestialObjectToCoordinates.keySet()) {
            if (!(Math.abs(c.x() - x0) >= distance || Math.abs(c.y() - y0) >= distance)) {
                minDistance = Math.sqrt((c.x() - x0) * (c.x() - x0) - (c.y() - y0) * (c.y() - y0));
                // est ce mieux de calculer distance*distance
                if (minDistance < min && minDistance < distance) {
                    min = minDistance;
                    cc = Optional.of(celestialObjectToCoordinates.get(c));
                }
            }
        }
        return (min==Double.MAX_VALUE)? Optional.empty(): cc;
    }










}
