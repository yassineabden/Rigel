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
    //private final Map <CelestialObject,CartesianCoordinates> celestialObjectToCoordinates;
    private final Map <Objects , double[]> objectsToCoordinates;

    public ObservedSky(ZonedDateTime observTime, GeographicCoordinates observPosition, StereographicProjection stereographicProjection, StarCatalogue starCatalogue) {

        this.observTime = observTime;
        this.observPosition = observPosition;
        this.stereographicProjection = stereographicProjection;
        this.starCatalogue = starCatalogue;
        double daysSinceJ2010= Epoch.J2010.daysUntil(observTime);

        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(observTime);
       // Map<CelestialObject,CartesianCoordinates> coordinatesMap = new HashMap<>();
        Map<Objects,double []> coordMap = new HashMap<>();

        sun = SunModel.SUN.at(daysSinceJ2010,eclipticToEquatorialConversion);
        moon = MoonModel.MOON.at(daysSinceJ2010,eclipticToEquatorialConversion);
        sunCoord = applyFromObject(sun);
        moonCoord = applyFromObject(moon);
        coordMap.put(Objects.SUN, new double []{sunCoord.x(),sunCoord.y()});
        coordMap.put(Objects.MOON, new double [] {moonCoord.x(),moonCoord.y()});
        //coordinatesMap.put(sun,sunCoord);
        //coordinatesMap.put(moon,moonCoord);

        planets = List.copyOf(planetsList(daysSinceJ2010,eclipticToEquatorialConversion));
        coordMap.put(Objects.PLANETS, coordinatesInArray(planets));
        List<Star> stars = List.copyOf(starCatalogue.stars());
        coordMap.put(Objects.STARS, coordinatesInArray(stars));
        objectsToCoordinates = Collections.unmodifiableMap(coordMap);

        /**
        for (PlanetModel planetModel: PlanetModel.ALL ) {
            if (planetModel != PlanetModel.EARTH){
                Planet p = planetModel.at(daysSinceJ2010, eclipticToEquatorialConversion);
                CartesianCoordinates coordinates=applyFromObject(p);
                planet.add(p);
                coordinatesMap.put(p,coordinates); }
        }

        for (Star s: stars){
            CartesianCoordinates coordinates= applyFromObject(s);
            coordinatesMap.put(s,coordinates); }
         */
       // celestialObjectToCoordinates =Collections.unmodifiableMap(coordinatesMap);
    }
    private List <Planet> planetsList(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion){
        List<Planet> planet = new ArrayList<>(PlanetModel.ALL.size()-1);
        for (PlanetModel planetModel: PlanetModel.ALL ) {
            if (planetModel != PlanetModel.EARTH) {
                Planet p = planetModel.at(daysSinceJ2010, eclipticToEquatorialConversion);
                planet.add(p);
            }
        }
        return planet;

    }
    private CartesianCoordinates applyFromObject(CelestialObject celestialObject){

        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(observTime,observPosition);
        return stereographicProjection.apply(equatorialToHorizontalConversion.apply(celestialObject.equatorialPos())); }

    private double[] coordinatesInArray(List<? extends CelestialObject> list){
        double [] array = new double[2*list.size()];
        int i=0;
        for (CelestialObject c: list){
            CartesianCoordinates coordinates = applyFromObject(c);
            array[i]= coordinates.x();
            array[i+1]= coordinates.y();
            i+=2; }
        return array; }

    private CelestialObject indexOfCoordinatesToObject (Objects object, int index){
       //TODO pour debug si jamais
        if(index % 2 != 0) System.out.println("ObservedSky.indexOfCoordinatesToObject - mauvais index");

        switch (object){
            case SUN:
                return sun();
            case MOON:
                return moon();
            case PLANETS:
                return planets().get(index/2);
            case STARS:
                return stars().get(index/2);
            default:
                throw new NoSuchElementException(); }
    }


    public Sun sun(){ return sun; }
    public CartesianCoordinates sunPosition(){
        //return sunCoord;
        CartesianCoordinates coord = CartesianCoordinates.of(objectsToCoordinates.get(Objects.SUN)[0], objectsToCoordinates.get(Objects.SUN)[1]);
        return coord ;}

    public Moon moon(){ return moon; }
    public CartesianCoordinates moonPosition(){
        //return moonCoord;
        CartesianCoordinates coord = CartesianCoordinates.of(objectsToCoordinates.get(Objects.MOON)[0], objectsToCoordinates.get(Objects.MOON)[1]);
        return coord;}

    public List <Planet> planets() {return planets;}

    public double [] planetsPositions() {
       // return coordinatesInArray(planets);
        //TODO faire copie défensive
          return objectsToCoordinates.get(Objects.PLANETS) ; }

    public List<Star> stars(){ return starCatalogue.stars();}

    public double [] starsPositions(){
        //return coordinatesInArray(starCatalogue.stars());
        //TODO faire copie défensive
        return objectsToCoordinates.get(Objects.STARS) ;}

    public Set<Asterism> asterisms(){ return starCatalogue.asterisms();}

    public List<Integer> asterismIndices(Asterism asterism){ return starCatalogue.asterismIndices(asterism) ;}


    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates cartesianCoordinates, double distance){

        double x0 = cartesianCoordinates.x();
        double y0 = cartesianCoordinates.y();
        double min = Double.MAX_VALUE;
        double objectDistance;

        Optional<CelestialObject> cc = Optional.empty();
        for (Objects object : Objects.ALL) {
            double[] coord = objectsToCoordinates.get(object);
            for (int i = 0; i < coord.length; i = +2) {
                double x = coord[i];
                double y = coord[i + 1];

                if (!(Math.abs(x - x0) >= distance || Math.abs(y - y0) >= distance)) {
                   objectDistance = Math.hypot(x-x0, y-y0);

                   if (objectDistance < min && objectDistance < distance){
                       min = objectDistance;
                       cc = Optional.of(indexOfCoordinatesToObject(object, i));
                   }
                }
            }
        }
        return  cc;

/**
        for (CelestialObject celestialObject: celestialObjectToCoordinates.keySet()) {
             CartesianCoordinates c = celestialObjectToCoordinates.get(celestialObject);

            if (!(Math.abs(c.x() - x0) >= distance || Math.abs(c.y() - y0) >= distance)) {
                minDistance = Math.sqrt((c.x() - x0) * (c.x() - x0) + (c.y() - y0) * (c.y() - y0));
                // est ce mieux de calculer distance*distance
                if (minDistance < min && minDistance < distance) {
                    min = minDistance;
                    cc = Optional.of(celestialObject);
                }
            }
        }

        return  cc;
 */
    }

        private enum Objects {
            MOON, PLANETS, STARS, SUN;
            private final static List<Objects> ALL = Arrays.asList(Objects.values());

        }


    }
