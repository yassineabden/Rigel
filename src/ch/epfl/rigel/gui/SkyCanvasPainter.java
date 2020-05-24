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

/**
 * Classe s'occupant de dessiner le ciel, les étoiles et les planètes
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class SkyCanvasPainter {

    private final Canvas canvas;
    private final GraphicsContext graphicsContext;

    private final static ClosedInterval MAGNITUDE = ClosedInterval.of(-2, 5);
    private final static HorizontalCoordinates EQUATOR = HorizontalCoordinates.ofDeg(0, 0);

    //TODO public, private? comment on sait?
    public SkyCanvasPainter(Canvas canvas) {

        this.canvas = canvas;
        graphicsContext = canvas.getGraphicsContext2D();
    }

    /**
     * Dessine "l'arrière-plan" du ciel, remplis le canvas de noir, effaçant ainsi les étoiles et planètes existantent
     */
    public void clear() {

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    /**
     * Dessine les étoiles et constellations présentes dans le ciel osbervé sur l'écran
     *
     * @param sky           ciel observé
     * @param projection    projection stéréographique pour mettre les étoiles sur
     * @param planeToCanvas transformation utilisée pour passer d'un repère carthésien au repère du canvas
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        // List<Star> stars = sky.stars();
        double[] starsOnCanvas = sky.starsPositions();
        planeToCanvas.transform2DPoints(sky.starsPositions(), 0, starsOnCanvas, 0, starsOnCanvas.length / 2);
        Set<Asterism> asterisms = sky.asterisms();

        //Dessine les asterisms
        Bounds canvasBound = canvas.getBoundsInLocal();
        graphicsContext.setStroke(Color.BLUE);
        graphicsContext.setLineWidth(1.0);
        boolean lastStarInBounds = false, thisStarInBounds, firstStar = true;

        for (Asterism a : asterisms) {

            List<Integer> aIndex = sky.asterismIndices(a);
            graphicsContext.beginPath();

            //todo à vérifier s'il faut true ou false au début

            for (Integer index : aIndex) {
                double x = starsOnCanvas[2 * index], y = starsOnCanvas[2 * index + 1];
                if (firstStar) {
                    graphicsContext.moveTo(x, y);
                    lastStarInBounds = canvasBound.contains(new Point2D(x, y));
                    firstStar = false;
                } else {
                    if ((thisStarInBounds = canvasBound.contains(new Point2D(x, y))) || lastStarInBounds) {
                        graphicsContext.lineTo(x, y);
                    } else {
                        graphicsContext.moveTo(x, y);
                    }
                    lastStarInBounds = thisStarInBounds;
                }
            }
            firstStar = true;
            lastStarInBounds = false;
            graphicsContext.stroke();
        }

        //dessine les étoiles
        drawBlackBody(starsOnCanvas, sky.stars(), projection, planeToCanvas);

    }

    /**
     * Dessine la ligne d'horizon et les points cardinaux
     *
     * @param stereographicProjection la projection stéréographique utilisée pour projeter les coordonées sphérique en coordonées carthésiennes
     * @param planeToCanvas           transformation utilisée pour passer d'un repère carthésien au repère du canvas
     */
    public void drawHorizon(StereographicProjection stereographicProjection, Transform planeToCanvas) {

        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(2.0);

        Point2D equator = carthesianCoordOnCanvas(planeToCanvas, stereographicProjection.circleCenterForParallel(EQUATOR));
        double equatorD = diameterOnCanvas(stereographicProjection.circleRadiusForParallel(EQUATOR), planeToCanvas);

        graphicsContext.strokeOval(equator.getX() - equatorD / 2, equator.getY() - equatorD / 2, equatorD, equatorD);
        graphicsContext.setFill(Color.RED);
        graphicsContext.setTextBaseline(VPos.TOP);
        graphicsContext.setTextAlign(TextAlignment.CENTER);

        //dessine les points cardinaux
        for (int deg = 0; deg < 360; deg += 45) {
            HorizontalCoordinates cardinalPointCoord = HorizontalCoordinates.ofDeg(deg, -0.5);
            Point2D cardinalOnCanvas = carthesianCoordOnCanvas(planeToCanvas, stereographicProjection.apply(cardinalPointCoord));
            graphicsContext.fillText(cardinalPointCoord.azOctantName("N", "E", "S", "W")
                    , cardinalOnCanvas.getX()
                    , cardinalOnCanvas.getY());
        }

    }

    /**
     * Dessine les planètes du systèmes solaire
     *
     * @param sky           ciel observé contenant un catalogue de planètes
     * @param projection    la projection stéréographique utilisée pour projeter les coordonées sphérique en coordonées carthésiennes
     * @param planeToCanvas transformation utilisée pour passer d'un repère carthésien au repère du canvas
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        double[] planetsOnCanvas = sky.planetsPositions();
        planeToCanvas.transform2DPoints(sky.planetsPositions(), 0, planetsOnCanvas, 0, planetsOnCanvas.length / 2);

        drawBlackBody(planetsOnCanvas, sky.planets(), projection, planeToCanvas);

    }

    /**
     * Dessine le soleil dans le ciel
     *
     * @param sky           ciel observé contenant un catalogue de planètes
     * @param projection    la projection stéréographique utilisée pour projeter les coordonées sphérique en coordonées carthésiennes
     * @param planeToCanvas transformation utilisée pour passer d'un repère carthésien au repère du canvas
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        double d = projection.applyToAngle(sky.sun().angularSize());

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

        //troisième disque
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(sunX - dTransformed / 2, sunY - dTransformed / 2, dTransformed, dTransformed);

    }

    /**
     * Dessine la lune dans le ciel
     *
     * @param sky           ciel observé contenant un catalogue de planètes
     * @param projection    la projection stéréographique utilisée pour projeter les coordonées sphérique en coordonées carthésiennes
     * @param planeToCanvas transformation utilisée pour passer d'un repère carthésien au repère du canvas
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        double d = projection.applyToAngle(sky.moon().angularSize());
        double dTransformed = diameterOnCanvas(d, planeToCanvas);

        Point2D moonCoord = carthesianCoordOnCanvas(planeToCanvas, sky.moonPosition());

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(moonCoord.getX() - dTransformed / 2, moonCoord.getY() - dTransformed / 2, dTransformed, dTransformed);

    }

    /**
     * Méthode dessinant une étoile ou une planète
     *
     * @param positionsOnCanvas tableaux contenant les posiitons des étoiles ou des planètes à dessiner
     * @param list              list contenant les étoiles ou les planètes à dessiner
     * @param projection        la projection stéréographique utilisée pour projeter les coordonées sphérique en coordonées carthésiennes
     * @param planeToCanvas     transformation utilisée pour passer d'un repère carthésien au repère du canvas
     */
    private void drawBlackBody(double[] positionsOnCanvas, List<? extends CelestialObject> list, StereographicProjection projection, Transform planeToCanvas) {

        int i = 0;
        for (CelestialObject celestialObject : list) {
            double dTransformed = diameterWithMagnitude(celestialObject, projection, planeToCanvas);

            if (celestialObject instanceof Star) {
                // détermine la couleur de l'étoile en fonction de la température de cette dernière
                Color starColor = BlackBodyColor.colorForTemperature(((Star) (celestialObject)).colorTemperature());
                graphicsContext.setFill(starColor);
            } else {
                // les planètes sont grises
                graphicsContext.setFill(Color.GRAY);
            }
            graphicsContext.fillOval(positionsOnCanvas[i] - dTransformed / 2, positionsOnCanvas[i + 1] - dTransformed / 2
                    , dTransformed
                    , dTransformed);

            i += 2;
        }

    }

    /**
     * Calcule le diamètre d'un object celeste ayant une magnitude (une étoile ou une planète)
     *
     * @param object object dont on veut calculer le diamètre
     * @param projection la projection stéréographique utilisée pour projeter les coordonées sphérique en coordonées carthésiennes
     * @param planeToCanvas transformation utilisée pour passer d'un repère carthésien au repère du canvas
     * @return le diamètre de l'object célèste projeté sur le canvas par rapport à sa magnitude
     */
    private double diameterWithMagnitude(CelestialObject object, StereographicProjection projection, Transform planeToCanvas) {

        double mDash = MAGNITUDE.clip(object.magnitude());
        double f = (99 - 17 * mDash) / 140;
        double d = f * projection.applyToAngle(Angle.ofDeg(0.5));
        return diameterOnCanvas(d, planeToCanvas);
    }

    /**
     * Applique la transformation d'un repère carthésien au repère du canvas à des coordonées cathésiennes
     *
     * @param planeToCanvas transformation utilisée pour passer d'un repère carthésien au repère du canvas
     * @param coordinates coordonées carthésiennes à transformer
     * @return les nouvelles coordonées sous forme d'un point 2D
     */
    private Point2D carthesianCoordOnCanvas(Transform planeToCanvas, CartesianCoordinates coordinates) {
        return planeToCanvas.transform(coordinates.x(), coordinates.y());
    }

    /**
     * Applique la transformation d'un repère carthésien au repère du canvas à un diamètre
     * @param d diamètre à transformer
     * @param planeToCanvas transformation utilisée pour passer d'un repère carthésien au repère du canvas
     * @return diamètre transformé
     */
    private double diameterOnCanvas(double d, Transform planeToCanvas) {
        return 2 * planeToCanvas.deltaTransform(d, 0).getX();
    }

}
