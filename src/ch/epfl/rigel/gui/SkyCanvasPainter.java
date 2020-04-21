package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import java.util.Arrays;
import java.util.List;

public final class SkyCanvasPainter {

    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final static ClosedInterval PLANETS_MAGNITUDE = ClosedInterval.of(-2,5);

    private final static HorizontalCoordinates EQUATOR = HorizontalCoordinates.ofDeg(180,0);
   /*
    private final static HorizontalCoordinates NORTH_TEXT = HorizontalCoordinates.of(0,-0.5);
    private final static HorizontalCoordinates NORTH_EAST_TEXT = HorizontalCoordinates.of(45,-0.5);
    private final static HorizontalCoordinates EAST_TEXT = HorizontalCoordinates.of(90,-0.5);
    private final static HorizontalCoordinates SOUTH_EAST_TEXT = HorizontalCoordinates.of(135,-0.5);
    private final static HorizontalCoordinates SOUTH_TEXT = HorizontalCoordinates.of(180,-0.5);
    private final static HorizontalCoordinates SOUTH_WEST_TEXT = HorizontalCoordinates.of(225,-0.5);
    private final static HorizontalCoordinates WEST_TEXT = HorizontalCoordinates.of(270,-0.5);
    private final static HorizontalCoordinates NORTH_WEST_TEXT = HorizontalCoordinates.of(315,-0.5);
*/
    private enum CardinalPoints{
        NORTH_TEXT (HorizontalCoordinates.of(0,-0.5)),
        NORTH_EAST_TEXT (HorizontalCoordinates.of(45,-0.5)),
        EAST_TEXT (HorizontalCoordinates.of(90,-0.5)),
        SOUTH_EAST_TEXT (HorizontalCoordinates.of(135,-0.5)),
        SOUTH_TEXT (HorizontalCoordinates.of(180,-0.5)),
        SOUTH_WEST_TEXT (HorizontalCoordinates.of(225,-0.5)),
        WEST_TEXT (HorizontalCoordinates.of(270,-0.5)),
        NORTH_WEST_TEXT (HorizontalCoordinates.of(315,-0.5));

        private final HorizontalCoordinates coordinates;
        private final static List<CardinalPoints> ALL = Arrays.asList(CardinalPoints.values());

        CardinalPoints(HorizontalCoordinates coordinates) {
            this.coordinates = coordinates; }

    }

    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        graphicsContext = canvas.getGraphicsContext2D();
    }


    public void clear(){
        graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getWidth()); }

    public void drawStars(ObservedSky sky, StereographicProjection stereographicProjection, Transform planeToCanvas) {
    }

    public void drawHorizon (StereographicProjection stereographicProjection, Transform planeToCanvas){

        graphicsContext.setFill(Color.RED);
        graphicsContext.setLineWidth(2.0);

        Point2D equator = transformCarthesianCoord(planeToCanvas, stereographicProjection.circleCenterForParallel(EQUATOR));
        double equatorD = transformedDiameter(planeToCanvas, stereographicProjection.circleRadiusForParallel(EQUATOR));
        graphicsContext.strokeOval(equator.getX(),equator.getY(),equatorD,equatorD);

        for ( CardinalPoints cardinalPoint : CardinalPoints.ALL) {
            Point2D cardinalOnCanvas = transformCarthesianCoord(planeToCanvas,stereographicProjection.apply(cardinalPoint.coordinates));
            String name = cardinalPoint.coordinates.azOctantName("N","E","S","W");
            graphicsContext.fillText(name,cardinalOnCanvas.getX(),cardinalOnCanvas.getY());
        }

    }

    public void drawPlanets(ObservedSky sky, StereographicProjection stereographicProjection, Transform planeToCanvas){

        planeToCanvas.transform2DPoints(sky.planetsPositions(),0,sky.planetsPositions(),0,sky.planetsPositions().length/2);

        for (int i = 0; i < sky.planetsPositions().length ; i+=2) {

            for (Planet p : sky.planets()) {
                double mDash = PLANETS_MAGNITUDE.clip(p.magnitude());
                double f = (99 - 17 * mDash) / 140;
                double d = f * stereographicProjection.applyToAngle(Angle.ofDeg(0.5));
                double dTransformed = transformedDiameter(planeToCanvas, d);

                Point2D planetCoord = transformCoord(planeToCanvas,sky.planetsPositions()[i],sky.planetsPositions()[i+1]);
                graphicsContext.setFill(Color.LIGHTGRAY);
                graphicsContext.fillOval(planetCoord.getX(),planetCoord.getY(),dTransformed,dTransformed);
            }

        }
    }

    public void drawSun(ObservedSky sky, StereographicProjection stereographicProjection, Transform planeToCanvas){

        double d  = stereographicProjection.applyToAngle(sky.sun().angularSize());

        //vecteur diamètre du soleil
        Point2D sunD = planeToCanvas.deltaTransform(new Point2D(d, 0));
        double dTransformed = transformedDiameter(planeToCanvas,d);

        Point2D sunCoord = transformCarthesianCoord(planeToCanvas,sky.sunPosition());

        double sunX = sunCoord.getX();
        double sunY = sunCoord.getY();

        // premier cercle à 25% d'opacité
        graphicsContext.setFill(Color.YELLOW.deriveColor(0,0,1,0.25));
        graphicsContext.fillOval(sunX,sunY,dTransformed*2.2,dTransformed*2.2);


        //deuxième disque
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.fillOval(sunX,sunY,dTransformed+2,dTransformed+2);

        //troisième  disque
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(sunX,sunY, dTransformed, dTransformed);

    }


    public void drawMoon(ObservedSky sky, StereographicProjection stereographicProjection, Transform planeToCanvas){

        double d = stereographicProjection.applyToAngle(sky.moon().angularSize());

        Point2D moonD = planeToCanvas.deltaTransform(new Point2D(d,0));
        double dTransformed = transformedDiameter(planeToCanvas,d);

        Point2D moonCoord = transformCarthesianCoord(planeToCanvas,sky.moonPosition());

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(moonCoord.getX(),moonCoord.getY(),dTransformed,dTransformed);
    }


    private Point2D transformCarthesianCoord(Transform planeToCanvas, CartesianCoordinates coordinates){
        return planeToCanvas.transform(coordinates.x(), coordinates.y());
    }

    private Point2D transformCoord(Transform planeToCanvas, double x, double y){
        return planeToCanvas.transform(x, y);
    }

    private double transformedDiameter(Transform planeToCanvas, double d){
        return planeToCanvas.transform(d,0).magnitude();
    }

}
