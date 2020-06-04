package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Ensemble d'objets célestes projetés dans le plan
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class ObservedSkyWithName {

    private final ZonedDateTime observTime;
    private final GeographicCoordinates observPosition;
    private final StereographicProjection stereographicProjection;
    private final StarCatalogueWithName starCatalogue;

    private final List<Planet> planets;
    private final Sun sun;
    private final Moon moon;

    private final CartesianCoordinates sunCoord;
    private final CartesianCoordinates moonCoord;

    private final Map<Objects, double[]> objectsToCoordinates;

    /**
     * Constructeur de la classe
     *
     * @param observTime              l'instant d'observation
     * @param observPosition          la position d'observation
     * @param stereographicProjection projection stéréographique à utiliser
     * @param starCatalogue           le catalogue contenant les étoiles et les astérismes
     */

    public ObservedSkyWithName(ZonedDateTime observTime, GeographicCoordinates observPosition, StereographicProjection stereographicProjection, StarCatalogueWithName starCatalogue) {

        this.observTime = observTime;
        this.observPosition = observPosition;
        this.stereographicProjection = stereographicProjection;
        this.starCatalogue = starCatalogue;

        double daysSinceJ2010 = Epoch.J2010.daysUntil(observTime);
        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(observTime);

        Map<Objects, double[]> coordMap = new HashMap<>();

        sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        moon = MoonModel.MOON.at(daysSinceJ2010, eclipticToEquatorialConversion);
        sunCoord = coordinatesFromObject(sun);
        moonCoord = coordinatesFromObject(moon);

        coordMap.put(Objects.SUN, new double[]{sunCoord.x(), sunCoord.y()});
        coordMap.put(Objects.MOON, new double[]{moonCoord.x(), moonCoord.y()});

        planets = List.copyOf(planetsList(daysSinceJ2010, eclipticToEquatorialConversion));
        coordMap.put(Objects.PLANETS, coordinatesInArray(planets));

        List<Star> stars = List.copyOf(starCatalogue.stars());
        coordMap.put(Objects.STARS, coordinatesInArray(stars));

        objectsToCoordinates = Collections.unmodifiableMap(coordMap);
    }

    // Construit la liste des planètes
    private List<Planet> planetsList(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        List<Planet> planet = new ArrayList<>(PlanetModel.ALL.size() - 1);
        for (PlanetModel planetModel : PlanetModel.ALL) {
            if (planetModel != PlanetModel.EARTH) {
                Planet p = planetModel.at(daysSinceJ2010, eclipticToEquatorialConversion);
                planet.add(p);
            }
        }
        return planet;
    }

    // Retourne les coordonnées cartésiennes après la projection
    private CartesianCoordinates coordinatesFromObject(CelestialObject celestialObject) {

        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(observTime, observPosition);
        return stereographicProjection.apply(equatorialToHorizontalConversion.apply(celestialObject.equatorialPos()));
    }

    // Remplit le tableau de coordonnées cartésiennes à partir d'une liste qui hérite de Celestial Object
    private double[] coordinatesInArray(List<? extends CelestialObject> list) {
        double[] array = new double[2 * list.size()];
        int i = 0;
        for (CelestialObject c : list) {
            CartesianCoordinates coordinates = coordinatesFromObject(c);
            array[i] = coordinates.x();
            array[i + 1] = coordinates.y();
            i += 2;
        }
        return array;
    }

    // Retourne l'objcet céleste correspondant à l'index de sa coordonée x
    private CelestialObject indexOfCoordinatesToObject(Objects object, int index) {

        if (index % 2 != 0) System.out.println("ObservedSky.indexOfCoordinatesToObject - mauvais index");

        switch (object) {
            case SUN:
                return sun();
            case MOON:
                return moon();
            case PLANETS:
                return planets().get(index / 2);
            case STARS:
                return stars().get(index / 2);
            default:
                throw new NoSuchElementException();
        }
    }

    /**
     * Retourne le soleil
     *
     * @return le soleil
     */
    public Sun sun() { return sun; }

    /**
     * Retourne les coordonnées cartésiennes du Soleil
     *
     * @return les coordonnées cartésiennes du Soleil
     */
    public CartesianCoordinates sunPosition() { return sunCoord; }

    /**
     * Retourne la Lune
     *
     * @return la Lune
     */
    public Moon moon() { return moon; }

    /**
     * Retourne les coordonnées cartésiennes de la Lune
     *
     * @return les coordonnées cartésiennes de la Lune
     */
    public CartesianCoordinates moonPosition() { return moonCoord; }

    /**
     * Retourne la liste des planètes
     *
     * @return la liste des planètes
     */
    public List<Planet> planets() { return planets; }

    /**
     * Retourne les coordonnées cartésiennes des planètes sous forme de tableau
     *
     * @return les coordonnées cartésiennes des planètes sous forme de tableau
     */
    public double[] planetsPositions() {

        return objectsToCoordinates.get(Objects.PLANETS).clone();
    }

    /**
     * Retourne la liste des étoiles
     *
     * @return la liste des étoiles
     */
    public List<Star> stars() { return starCatalogue.stars(); }

    /**
     * Retourne les coordonnées cartésiennes des étoiles sous forme de tableau
     *
     * @return les coordonnées cartésiennes des étoiles sous forme de tableau
     */
    public double[] starsPositions() {

        return objectsToCoordinates.get(Objects.STARS).clone();
    }

    /**
     * Retourne les astérismes
     *
     * @return les astérismes
     */
    public Set<AsterismWithName> asterisms() { return starCatalogue.asterisms(); }

    /**
     * Retourne la liste des index des étoiles d'un astérisme donné
     *
     * @param asterism un Astérisme
     *
     * @return la liste des index des étoiles d'un astérisme donné
     */
    public List<List<Integer>> asterismIndices(AsterismWithName asterism) {

        return starCatalogue.asterismIndices(asterism);
    }


    /**
     * Retourne l'objet céleste le plus proche de ce point qui se trouve à une distance inférieure à la distance maximale
     *
     * @param cartesianCoordinates des coordonnées cartésiennes
     * @param distance             une distance maximale
     *
     * @return l'objet céleste le plus proche de ce point qui se trouve à une distance inférieure à la distance maximale
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates cartesianCoordinates, double distance) {

        double x0 = cartesianCoordinates.x();
        double y0 = cartesianCoordinates.y();
        double min = Double.MAX_VALUE;
        double objectDistance;

        Optional<CelestialObject> closestObject = Optional.empty();

        for (Objects object : Objects.ALL) {
            double[] coord = objectsToCoordinates.get(object);

            for (int i = 0; i < coord.length; i = i + 2) {
                double x = coord[i];
                double y = coord[i + 1];

                if (!(Math.abs(x - x0) >= distance || Math.abs(y - y0) >= distance)) {
                    objectDistance = Math.hypot(x - x0, y - y0);

                    if ((objectDistance < min) && (objectDistance < distance)) {
                        min = objectDistance;
                        closestObject = Optional.of(indexOfCoordinatesToObject(object, i));
                    }
                }
            }
        }
        return closestObject;
    }

    // Enum représentant les objets du Ciel
    private enum Objects {
        MOON, PLANETS, STARS, SUN;
        private final static List<Objects> ALL = Arrays.asList(Objects.values());

    }

}
