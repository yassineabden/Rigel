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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
final public class SkyCanvasPainter {

    private static final int INTERCARDINAL_DEG_STEP = 45;
    private static final double ANGULAR_SIZE_BLACK_BODY = Angle.ofDeg(0.5);

    private final Canvas canvas;
    private final GraphicsContext graphicsContext;

    private final static ClosedInterval MAGNITUDE = ClosedInterval.of(-2, 5);
    private final static HorizontalCoordinates EQUATOR = HorizontalCoordinates.ofDeg(0, 0);
    private final static double ASTERISMS_LINE_WIDTH = 1.0;
    private final static double HORIZON_LINE_WIDTH = 2.0;
    private final static double SUN_FIRST_DISC_OPACITY = 0.25;
    private final static double SUN_FIRST_DISC_DIAMETER_EXPANSION = 2.2;
    private final static double SUN_SECOND_DISC_DIAMETER_EXPANSION = 2.0;
    private static final double MAGNITUDE_FOR_BRIGHTEST_CELESTIAL_OBJECTS = 0.63;
    private static final double NAME_DISTANCE_TO_OBJECT = 5.0;



    public SkyCanvasPainter(Canvas canvas) {

        this.canvas = canvas;
        graphicsContext = canvas.getGraphicsContext2D();
    }

    /**
     * Dessine "l'arrière-plan" du ciel, remplit le canvas de noir, effaçant ainsi les étoiles et planètes existantes
     */
    public void clear() {

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    /**
     * Dessine les étoiles et asterimes présentes dans le ciel osbervé sur l'écran
     *
     * @param sky           ciel observé
     * @param projection    projection stéréographique pour mettre les étoiles sur un repère carthésien
     * @param planeToCanvas transformation utilisée pour passer d'un repère carthésien au repère du canvas
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas, boolean drawAsterisms) {

        // Créer un tableau contenant les coordonées des étoiles sur le canvas
        double[] starsOnCanvas = sky.starsPositions();
        planeToCanvas.transform2DPoints(sky.starsPositions(), 0, starsOnCanvas, 0, starsOnCanvas.length / 2);
        Set<Asterism> asterisms = sky.asterisms();

        //Dessine les asterisms
        if(drawAsterisms) {
            Bounds canvasBound = canvas.getBoundsInLocal();
            graphicsContext.setStroke(Color.BLUE);
            graphicsContext.setLineWidth(ASTERISMS_LINE_WIDTH);
            boolean lastStarInBounds = false, thisStarInBounds, firstStar = true;

            for (Asterism a : asterisms) {

                List<Integer> aIndex = sky.asterismIndices(a);
                graphicsContext.beginPath();

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
        }
        //dessine les étoiles
        double diameterOnCanvas = planeToCanvas.deltaTransform(projection.applyToAngle(ANGULAR_SIZE_BLACK_BODY), 0).getX();
        drawBlackBody(starsOnCanvas, sky.stars(), diameterOnCanvas);

    }

    /**
     * Dessine la ligne d'horizon, les points cardinaux et intercardinaux
     *
     * @param stereographicProjection la projection stéréographique utilisée pour projeter les coordonées sphérique en coordonées carthésiennes
     * @param planeToCanvas           transformation utilisée pour passer d'un repère carthésien au repère du canvas
     */
    public void drawHorizon(StereographicProjection stereographicProjection, Transform planeToCanvas) {

        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(HORIZON_LINE_WIDTH);

        // Dessine la ligne d'horizon
        Point2D equator = carthesianCoordOnCanvas(planeToCanvas, stereographicProjection.circleCenterForParallel(EQUATOR));
        double equatorD = 2 * radiusOnCanvas(stereographicProjection.circleRadiusForParallel(EQUATOR), planeToCanvas);

        graphicsContext.strokeOval(equator.getX() - equatorD / 2, equator.getY() - equatorD / 2, equatorD, equatorD);

        setGraphicsContextForName().setFill(Color.RED);

        //dessine les points cardinaux et intercardinaux
        for (int deg = 0; deg < 360; deg += INTERCARDINAL_DEG_STEP) {
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

        double diameterOnCanvas = planeToCanvas.deltaTransform(projection.applyToAngle(ANGULAR_SIZE_BLACK_BODY), 0).getX();
        drawBlackBody(planetsOnCanvas, sky.planets(), diameterOnCanvas);

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
        double dTransformed = radiusOnCanvas(d, planeToCanvas);

        Point2D sunCoord = carthesianCoordOnCanvas(planeToCanvas, sky.sunPosition());

        double sunX = sunCoord.getX();
        double sunY = sunCoord.getY();

        // premier cercle à 25% d'opacité
        graphicsContext.setFill(Color.YELLOW.deriveColor(0, 0, 1, SUN_FIRST_DISC_OPACITY));
        graphicsContext.fillOval(sunX - (dTransformed * SUN_FIRST_DISC_DIAMETER_EXPANSION) / 2, sunY - (dTransformed * SUN_FIRST_DISC_DIAMETER_EXPANSION) / 2
                ,dTransformed * SUN_FIRST_DISC_DIAMETER_EXPANSION, dTransformed * SUN_FIRST_DISC_DIAMETER_EXPANSION);

        //deuxième disque
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.fillOval(sunX - (dTransformed + SUN_SECOND_DISC_DIAMETER_EXPANSION) / 2, sunY - (dTransformed + SUN_SECOND_DISC_DIAMETER_EXPANSION) / 2
                ,dTransformed + SUN_SECOND_DISC_DIAMETER_EXPANSION, dTransformed + SUN_FIRST_DISC_DIAMETER_EXPANSION);

        //troisième disque
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(sunX - dTransformed / 2, sunY - dTransformed / 2
                ,dTransformed, dTransformed);

        //texte
        setGraphicsContextForName().setFill(Color.LIGHTSALMON);
        graphicsContext.fillText(sky.sun().name(), sunX + NAME_DISTANCE_TO_OBJECT, sunY + NAME_DISTANCE_TO_OBJECT);

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
        double dTransformed = radiusOnCanvas(d, planeToCanvas);

        Point2D moonCoord = carthesianCoordOnCanvas(planeToCanvas, sky.moonPosition());
        double x = moonCoord.getX(), y = moonCoord.getY();

        setGraphicsContextForName().setFill(Color.LIGHTSALMON);
        graphicsContext.fillText(sky.moon().info(),x + NAME_DISTANCE_TO_OBJECT, y + NAME_DISTANCE_TO_OBJECT);

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(x - dTransformed / 2, y - dTransformed / 2
                ,dTransformed, dTransformed);

    }


    // Méthode dessinant une étoile ou une planète
    private void drawBlackBody(double[] positionsOnCanvas, List<? extends CelestialObject> list, double diameterOnCanvas) {

        ClosedInterval brighestStarsMagnitude = ClosedInterval.of(-1.5, 0.65);
        int i = 0;
        for (CelestialObject celestialObject : list) {
            double dTransformed = diameterWithMagnitude(celestialObject, diameterOnCanvas);

            double x = positionsOnCanvas[i], y = positionsOnCanvas[i + 1];

            //Bonus
            if (celestialObject instanceof Star) {
                // Affiche le nom de l'étoile si cette dernière est assez lumineuse et détermine sa couleur en fonction de sa température
                if(celestialObject.magnitude() <= MAGNITUDE_FOR_BRIGHTEST_CELESTIAL_OBJECTS){
                    setGraphicsContextForName().setFill(Color.WHITE);
                    graphicsContext.fillText(celestialObject.name(), x + NAME_DISTANCE_TO_OBJECT, y + NAME_DISTANCE_TO_OBJECT);
                }
                Color starColor = BlackBodyColor.colorForTemperature(((Star) (celestialObject)).colorTemperature());
                graphicsContext.setFill(starColor);
            } else {
                // Affiche le nom de la planète et la dessine
                setGraphicsContextForName().setFill(Color.LIGHTSALMON);
                graphicsContext.fillText(celestialObject.name(), x + NAME_DISTANCE_TO_OBJECT, y + NAME_DISTANCE_TO_OBJECT);
                graphicsContext.setFill(Color.GRAY);
            }
            // Dessine l'object céleste
            graphicsContext.fillOval(x - dTransformed / 2, y - dTransformed / 2
                    , dTransformed
                    , dTransformed);

            i += 2;
        }
    }


     //Transforme le diamètre d'un object celeste ayant une magnitude (une étoile ou une planète) de coordonées sphérique au repère du canvas
    private double diameterWithMagnitude(CelestialObject object, double diameterOnCanvas) {

        double mDash = MAGNITUDE.clip(object.magnitude());
        double f = (99 - 17 * mDash) / 140;
        return f * diameterOnCanvas;
    }

    // Applique la transformation d'un repère carthésien au repère du canvas à des coordonées cathésiennes
    private Point2D carthesianCoordOnCanvas(Transform planeToCanvas, CartesianCoordinates coordinates) {

        return planeToCanvas.transform(coordinates.x(), coordinates.y());
    }

   // Applique la transformation d'un repère carthésien au repère du canvas à un diamètre
    private double radiusOnCanvas(double d, Transform planeToCanvas) {

        return  planeToCanvas.deltaTransform(d, 0).getX();
    }

    // Applique le style au contexte graphic pour l'affichage de texte
    private GraphicsContext setGraphicsContextForName (){

        graphicsContext.setTextBaseline(VPos.TOP);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        return graphicsContext;
    }

}
