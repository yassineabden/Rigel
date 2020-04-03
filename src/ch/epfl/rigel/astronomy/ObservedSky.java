package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;

public final class ObservedSky {

    private final ZonedDateTime observTime;
    private final GeographicCoordinates observPosition;
    private final StereographicProjection stereographicProjection;
    private final StarCatalogue starCatalogue;
    private final Sun sun;
    private final Moon moon;


    public ObservedSky(ZonedDateTime observTime, GeographicCoordinates observPosition, StereographicProjection stereographicProjection, StarCatalogue starCatalogue) {
        this.observTime = observTime;
        this.observPosition = observPosition;
        this.stereographicProjection = stereographicProjection;
        this.starCatalogue = starCatalogue;
        double daysSinceJ2010= Epoch.J2010.daysUntil(observTime);
        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(observTime);
        this.sun= SunModel.SUN.at(daysSinceJ2010,eclipticToEquatorialConversion);
        this.moon =MoonModel.MOON.at(daysSinceJ2010,eclipticToEquatorialConversion);
        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(observTime,observPosition);
        CartesianCoordinates posSun = stereographicProjection.apply(equatorialToHorizontalConversion.apply(sun.equatorialPos()));
        CartesianCoordinates posMoon = stereographicProjection.apply(equatorialToHorizontalConversion.apply(moon.equatorialPos());
        for (PlanetModel planetModel: PlanetModel.ALL){
            Planet planet=planetModel.at(daysSinceJ2010,eclipticToEquatorialConversion);
            CartesianCoordinates posPlanet= stereographicProjection.apply(equatorialToHorizontalConversion.apply(planet.equatorialPos())); }
        for (Star s: starCatalogue.stars()){
            CartesianCoordinates posStar = stereographicProjection.apply(equatorialToHorizontalConversion.apply(s.equatorialPos())); }
    }

    public Sun sun(){
        double daysSinceJ2010= Epoch.J2010.daysUntil(observTime);
        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(observTime);
        return SunModel.SUN.at(daysSinceJ2010,eclipticToEquatorialConversion);
    }
     public CartesianCoordinates sunPosition(){

     }





}
