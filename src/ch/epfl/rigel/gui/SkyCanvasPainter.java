package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class SkyCanvasPainter {

    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final static ClosedInterval MAGNITUDE = ClosedInterval.of(-2,5);

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
        //graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getWidth());
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    public void drawStars(ObservedSky sky, StereographicProjection stereographicProjection, Transform planeToCanvas) {

        List <Star> stars = sky.stars();
        double [] starPositions = sky.starsPositions();
        Set<Asterism> asterisms = sky.asterisms();

        //Dessine les asterisms
        Bounds canvasBound = canvas.getBoundsInLocal();
        for (Asterism a : asterisms) {
            List <Integer> aIndex = sky.asterismIndices(a);
            graphicsContext.setFill(Color.BLUE);
            graphicsContext.beginPath();
            for (Integer index : aIndex) {
                Point2D starCoord = transformCoord(planeToCanvas,starPositions[index*2],starPositions[index*2 + 1]);
                if (canvasBound.contains(starCoord)) {
                    graphicsContext.lineTo(starCoord.getX(), starCoord.getY());
                    graphicsContext.stroke();
                } else {
                    graphicsContext.moveTo(starCoord.getX(),starCoord.getY());
                }
            }
            //Fait retourner le trait d'asterism à la première étoile
            Point2D star1Coord = transformCoord(planeToCanvas,starPositions[0],starPositions[1]);
            if (canvasBound.contains(star1Coord)) {
                graphicsContext.lineTo(star1Coord.getX(), star1Coord.getY());
                graphicsContext.stroke();
            } else {
                graphicsContext.moveTo(star1Coord.getX(),star1Coord.getY());
            }
            graphicsContext.closePath();

        }


        //dessine les étoiles
       int i = 0;
        for (Star s : stars) {
            double mDash = MAGNITUDE.clip(s.magnitude());
            double sizeFactor = (99 - 17*mDash)/140.0;
            double d = sizeFactor*2*Math.tan(Angle.ofDeg(0.5)/4);
            double dTransformed = transformedDiameter(planeToCanvas,d);
            Point2D starCoord = transformCoord(planeToCanvas,starPositions[i],starPositions[i+1]);
            i += 2;
            Color starColor = BlackBodyColor.colorForTemperature( s.colorTemperature());
            graphicsContext.setFill(starColor);
            graphicsContext.fillOval(starCoord.getX()-dTransformed/2, starCoord.getY()-dTransformed/2, dTransformed,dTransformed);
        }

    }

    public void drawHorizon (StereographicProjection stereographicProjection, Transform planeToCanvas){

        graphicsContext.setFill(Color.RED);
        graphicsContext.setLineWidth(2.0);

        Point2D equator = transformCarthesianCoord(planeToCanvas, stereographicProjection.circleCenterForParallel(EQUATOR));
        double equatorD = transformedDiameter(planeToCanvas, stereographicProjection.circleRadiusForParallel(EQUATOR));
        graphicsContext.strokeOval(equator.getX()-equatorD/2,equator.getY()-equatorD/2,equatorD,equatorD);

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
                double mDash = MAGNITUDE.clip(p.magnitude());
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
        double dTransformed = transformedDiameter(planeToCanvas,d);

        Point2D sunCoord = transformCarthesianCoord(planeToCanvas,sky.sunPosition());

        double sunX = sunCoord.getX();
        double sunY = sunCoord.getY();

        // premier cercle à 25% d'opacité
        graphicsContext.setFill(Color.YELLOW.deriveColor(0,0,1,0.25));
        graphicsContext.fillOval(sunX-(dTransformed*2.2)/2,sunY-(dTransformed*2.2)/2,dTransformed*2.2,dTransformed*2.2);


        //deuxième disque
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.fillOval(sunX-(dTransformed+2)/2,sunY-(dTransformed+2)/2,dTransformed+2,dTransformed+2);

        //troisième  disque
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(sunX-dTransformed/2,sunY-dTransformed/2, dTransformed, dTransformed);

    }


    public void drawMoon(ObservedSky sky, StereographicProjection stereographicProjection, Transform planeToCanvas){

        double d = stereographicProjection.applyToAngle(sky.moon().angularSize());

        double dTransformed = transformedDiameter(planeToCanvas,d);

        Point2D moonCoord = transformCarthesianCoord(planeToCanvas,sky.moonPosition());

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(moonCoord.getX()-dTransformed/2,moonCoord.getY()-dTransformed/2,dTransformed,dTransformed);
    }


    private Point2D transformCarthesianCoord(Transform planeToCanvas, CartesianCoordinates coordinates){
        return planeToCanvas.transform(coordinates.x(), coordinates.y());
    }

    private Point2D transformCoord(Transform planeToCanvas, double x, double y){
        return planeToCanvas.transform(x, y);
    }

    private double transformedDiameter(Transform planeToCanvas, double d){
        return planeToCanvas.deltaTransform(d,0).magnitude();
    }

}
