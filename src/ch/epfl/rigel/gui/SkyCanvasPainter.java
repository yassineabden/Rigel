package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.List;
import java.util.Set;

public final class SkyCanvasPainter {

    private final Canvas canvas;
    private final GraphicsContext graphicsContext;

    private final static ClosedInterval MAGNITUDE = ClosedInterval.of(-2, 5);
    private final static HorizontalCoordinates EQUATOR = HorizontalCoordinates.ofDeg(0, 0);


    public SkyCanvasPainter(Canvas canvas) {

        this.canvas = canvas;
        graphicsContext = canvas.getGraphicsContext2D();
    }


    public void clear() {

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        System.out.println("clear()");
    }

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        // List<Star> stars = sky.stars();
        double[] starsOnCanvas = sky.starsPositions();
        planeToCanvas.transform2DPoints(sky.starsPositions(), 0, starsOnCanvas, 0, starsOnCanvas.length / 2);
        Set<Asterism> asterisms = sky.asterisms();

        //Dessine les asterisms
        Bounds canvasBound = canvas.getBoundsInLocal();
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(1.0);

        for (Asterism a : asterisms) {

            List<Integer> aIndex = sky.asterismIndices(a);
            graphicsContext.beginPath();

            //todo à vérifier s'il faut true ou false au début
            boolean lastStarInBounds = true;

            for (Integer index : aIndex) {
                double x = starsOnCanvas[2 * index], y = starsOnCanvas[2 * index + 1];

                if (lastStarInBounds || (lastStarInBounds = canvasBound.contains(new Point2D(x, y)))) {
                    graphicsContext.lineTo(x, y);
                } else {
                    graphicsContext.moveTo(x, y);
                }
            }
            graphicsContext.stroke();
        }

        //dessine les étoiles
        drawBlackBody(starsOnCanvas, sky.stars(), projection, planeToCanvas);

    }

    public void drawHorizon(StereographicProjection stereographicProjection, Transform planeToCanvas) {

        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(2.0);

        Point2D equator = carthesianCoordOnCanvas(planeToCanvas, stereographicProjection.circleCenterForParallel(EQUATOR));
        double equatorD = diameterOnCanvas(stereographicProjection.circleRadiusForParallel(EQUATOR), planeToCanvas);

        graphicsContext.strokeOval(equator.getX() - equatorD / 2, equator.getY() - equatorD / 2, equatorD, equatorD);
        graphicsContext.setFill(Color.RED);
        graphicsContext.setTextBaseline(VPos.TOP);
        graphicsContext.setTextAlign(TextAlignment.CENTER);

        for (int deg = 0; deg < 360; deg += 45) {
            HorizontalCoordinates cardinalPointCoord = HorizontalCoordinates.ofDeg(deg, - 0.5);
            Point2D cardinalOnCanvas = carthesianCoordOnCanvas(planeToCanvas, stereographicProjection.apply(cardinalPointCoord));
            graphicsContext.fillText(cardinalPointCoord.azOctantName("N", "E", "S", "W"), cardinalOnCanvas.getX(), cardinalOnCanvas.getY());
        }

    }

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        double[] planetsOnCanvas = sky.planetsPositions();
        planeToCanvas.transform2DPoints(sky.planetsPositions(), 0, planetsOnCanvas, 0, planetsOnCanvas.length / 2);

        drawBlackBody(planetsOnCanvas, sky.planets(), projection, planeToCanvas);

    }

    public void drawSun(ObservedSky sky, StereographicProjection stereographicProjection, Transform planeToCanvas) {

        double d = stereographicProjection.applyToAngle(sky.sun().angularSize());

        //vecteur diamètre du soleil
        double dTransformed = diameterOnCanvas(d, planeToCanvas);

        Point2D sunCoord = carthesianCoordOnCanvas(planeToCanvas, sky.sunPosition());

        double sunX = sunCoord.getX();
        double sunY = sunCoord.getY();

        // premier cercle à 25% d'opacité
        graphicsContext.setFill(Color.YELLOW.deriveColor(0, 0, 1, 0.25));
        graphicsContext.fillOval(sunX - (dTransformed * 2.2) / 2, sunY - (dTransformed * 2.2) / 2, dTransformed * 2.2, dTransformed * 2.2);

        //deuxième disque
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.fillOval(sunX - (dTransformed + 2) / 2, sunY - (dTransformed + 2) / 2, dTransformed + 2, dTransformed + 2);

        //troisième  disque
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(sunX - dTransformed / 2, sunY - dTransformed / 2, dTransformed, dTransformed);

    }


    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        double d = projection.applyToAngle(sky.moon().angularSize());
        double dTransformed = diameterOnCanvas(d, planeToCanvas);

        Point2D moonCoord = carthesianCoordOnCanvas(planeToCanvas, sky.moonPosition());

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(moonCoord.getX() - dTransformed / 2, moonCoord.getY() - dTransformed / 2, dTransformed, dTransformed);

    }

    private void drawBlackBody(double[] positionsOnCanvas, List<? extends CelestialObject> list, StereographicProjection projection, Transform planeToCanvas) {

        int i = 0;
        for (CelestialObject celestialObject : list) {
            double dTransformed = diameterWithMagnitude(celestialObject, projection, planeToCanvas);

            if (celestialObject instanceof Star) {
                Color starColor = BlackBodyColor.colorForTemperature(((Star) (celestialObject)).colorTemperature());
                //todo check la magnitude pour dessiner le nom
                graphicsContext.setFill(starColor);
            } else {
                graphicsContext.setFill(Color.GRAY);
            }
            graphicsContext.fillOval(positionsOnCanvas[i] - dTransformed / 2, positionsOnCanvas[i + 1] - dTransformed / 2, dTransformed, dTransformed);

            i += 2;
        }

    }

    private double diameterWithMagnitude(CelestialObject object, StereographicProjection projection, Transform planeToCanvas) {

        double mDash = MAGNITUDE.clip(object.magnitude());
        double f = (99 - 17 * mDash) / 140;
        double d = f * projection.applyToAngle(Angle.ofDeg(0.5));
        return diameterOnCanvas(d, planeToCanvas);
    }

    private Point2D carthesianCoordOnCanvas(Transform planeToCanvas, CartesianCoordinates coordinates) {

        return planeToCanvas.transform(coordinates.x(), coordinates.y());
    }

    private double diameterOnCanvas(double d, Transform planeToCanvas) {

        return 2 * planeToCanvas.deltaTransform(d, 0).getX();
    }

}
