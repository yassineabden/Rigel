package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import static javafx.beans.binding.Bindings.createDoubleBinding;

/**
 * Classe s'ocuupant de gérer les actions et mises à jour du canvas
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class SkyCanvasManager {

    private final Canvas canvas;
    private final SkyCanvasPainter skyCanvasPainter;
    private final ObservableValue<StereographicProjection> projection;

    private final ObservableDoubleValue mouseAzDeg;
    private final ObservableDoubleValue mouseAltDeg;
    private final ObservableValue<CelestialObject> objectUnderMouse;

    private final ObservableValue<Transform> planeToCanvas;
    private final ObservableValue<ObservedSky> observedSky;
    private final ObjectProperty<Point2D> mousePosition;
    private final ObservableValue<HorizontalCoordinates> mouseHorizontalPosition;

    private final static int AZIMUT_DEG_STEP = 10;
    private final static int ALTITUDE_DEG_STEP = 5;
    private static final int OBJECT_CLOSETST_TO_DISTANCE = 10;
    private final static double DEFAULT_ZERO = 0.0;


    private final static RightOpenInterval AZIMUT_DEG_INTERVAL = RightOpenInterval.of(0, 360);
    private final static ClosedInterval ALTITUDE_DEG_INTERVAL = ClosedInterval.of(5, 90);
    private final static ClosedInterval FOV_INTERVAL_DEG = ClosedInterval.of(30, 150);

    /**
     * Constructeur du manager de canvas, initialise les propriétés essentielles
     *
     * @param starCatalogue         catalogue contenant les étoiles et planètes du ciel observé
     * @param dateTimeBean          bean contenant l'instant d'observation du ciel
     * @param observerLocationBean  bean contenant la position d'observation du ciel
     * @param viewingParametersBean bean contenant les paramètres du vue de l'écran
     */
    public SkyCanvasManager(StarCatalogue starCatalogue, DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {

        // Initialisation des beans et attributs
        canvas = new Canvas();
        skyCanvasPainter = new SkyCanvasPainter(canvas);

        projection = Bindings.createObjectBinding(() -> (new StereographicProjection(viewingParametersBean.getCenter()))
                , viewingParametersBean.centerProperty());


        planeToCanvas = Bindings.createObjectBinding(() -> {
                    double dilatation = canvas.getWidth()
                            / projection.getValue().applyToAngle(Angle.ofDeg(viewingParametersBean.getFieldOfViewDeg()));

                    return Transform.translate(canvas.getWidth() / 2.0, canvas.getHeight() / 2.0).createConcatenation(Transform.scale(dilatation, -dilatation));
                }
                , viewingParametersBean.fieldOfViewDegProperty()
                , projection
                , canvas.heightProperty()
                , canvas.widthProperty());

        observedSky = Bindings.createObjectBinding(() -> new ObservedSky(dateTimeBean.getZonedDateTime()
                        , observerLocationBean.getCoordinates()
                        , projection.getValue()
                        , starCatalogue)
                , observerLocationBean.coordinatesProperty()
                , dateTimeBean.dateProperty()
                , dateTimeBean.timeProperty()
                , dateTimeBean.zoneProperty()
                , projection);

        // Initialisation des attributs et actions de la souris
        mousePosition = new SimpleObjectProperty<>(Point2D.ZERO);
        canvas.setOnMouseMoved(event -> mousePosition.set(new Point2D(event.getX(), event.getY())));

        mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
                    Point2D newPosition;
                    try {
                        newPosition = planeToCanvas.getValue().inverseTransform(mousePosition.get());
                        return projection.getValue().inverseApply(CartesianCoordinates.of(newPosition.getX(), newPosition.getY()));
                    } catch (NonInvertibleTransformException e) {
                        return null;
                    }

                }
                , mousePosition
                , projection
                , planeToCanvas);

        mouseAzDeg = createDoubleBinding(() -> (mouseHorizontalPosition.getValue() == null) ? DEFAULT_ZERO : mouseHorizontalPosition.getValue().azDeg()
                , mouseHorizontalPosition);
        mouseAltDeg = createDoubleBinding(() -> (mouseHorizontalPosition.getValue() == null) ? DEFAULT_ZERO : mouseHorizontalPosition.getValue().altDeg()
                , mouseHorizontalPosition);

        canvas.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown())
                canvas.requestFocus();
        });

        objectUnderMouse = Bindings.createObjectBinding(() -> {
                    Point2D newPosition;
                    Point2D newObjectDistance;
                    try {
                        newPosition = planeToCanvas.getValue().inverseTransform(mousePosition.get());
                        newObjectDistance = planeToCanvas.getValue().inverseDeltaTransform(OBJECT_CLOSETST_TO_DISTANCE,0);
                        return observedSky.getValue()
                                .objectClosestTo(CartesianCoordinates.of(newPosition.getX(), newPosition.getY()), newObjectDistance.getX())
                                .orElse(null);
                    } catch (NonInvertibleTransformException e) {
                        return null;
                    }

                }
                , observedSky
                , planeToCanvas
                , mousePosition);

        // Zoom lorsqu'on touche le trackpad (change le champ de vue)
        canvas.setOnScroll(scrollEvent -> {

            double currentFoV = viewingParametersBean.getFieldOfViewDeg();
            double scrollDeltaX = scrollEvent.getDeltaX();
            double scrollDeltaY = scrollEvent.getDeltaY();

            double x = Math.abs(scrollDeltaX);
            double y = Math.abs(scrollDeltaY);
            double z = x > y ? currentFoV + scrollDeltaX : currentFoV + scrollDeltaY;

            viewingParametersBean.setFieldOfViewDeg(FOV_INTERVAL_DEG.clip(z));
        });

        // Change la position horizontale de la vue lorsque les touches sont appuyées
        canvas.setOnKeyPressed(keyEvent -> {

            HorizontalCoordinates viewingCenter = viewingParametersBean.getCenter();
            double centerALtDeg = viewingCenter.altDeg();
            double centerAzDeg = viewingCenter.azDeg();

            switch (keyEvent.getCode()) {
                case LEFT:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    AZIMUT_DEG_INTERVAL.reduce(centerAzDeg - AZIMUT_DEG_STEP)
                                    , centerALtDeg));
                    break;

                case RIGHT:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    AZIMUT_DEG_INTERVAL.reduce(centerAzDeg + AZIMUT_DEG_STEP)
                                    , centerALtDeg));
                    break;

                case UP:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(centerAzDeg
                                    , ALTITUDE_DEG_INTERVAL.clip(centerALtDeg + ALTITUDE_DEG_STEP)));
                    break;

                case DOWN:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(centerAzDeg
                                    , ALTITUDE_DEG_INTERVAL.clip(centerALtDeg - ALTITUDE_DEG_STEP)));

                    break;
                default:
                    return;
            }
            keyEvent.consume();

        });

        // Redessine le ciel lorsqu'on change de position d'observation, de champ de vue ou d'instant d'observation
        planeToCanvas.addListener(e -> drawSky());
        observedSky.addListener(e -> drawSky());

    }

    // Dessine le ciel en entier
    private void drawSky() {
        StereographicProjection stereographicProjection = projection.getValue();
        ObservedSky sky = observedSky.getValue();
        Transform transform = planeToCanvas.getValue();

        skyCanvasPainter.clear();
        skyCanvasPainter.drawStars(sky, stereographicProjection, transform);
        skyCanvasPainter.drawPlanets(sky, stereographicProjection, transform);
        skyCanvasPainter.drawSun(sky, stereographicProjection, transform);
        skyCanvasPainter.drawMoon(sky, stereographicProjection, transform);
        skyCanvasPainter.drawHorizon(stereographicProjection, transform);

    }

    /**
     * Retourne le canvas utilisé
     *
     * @return le canvas utilisé
     */
    public Canvas canvas() {
        return canvas;
    }

    /**
     * Retourne l'azimut en degrés de la position horizontal de la souris
     *
     * @return l'azimut en degrés de la position horizontal de la souris
     */
    public double getMouseAzDeg() {
        return mouseAzDeg.get();
    }

    /**
     * Retourne la propriété contenant l'azimut en degrés de la position horizontal de la souris
     *
     * @returnla propriété contenant l'azimut en degrés de la position horizontal de la souris
     */
    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }

    /**
     * Retourne l'altitude en degrés de la position horizontal de la souris
     *
     * @return l'altitude en degrés de la position horizontal de la souris
     */
    public double getMouseAltDeg() {
        return mouseAltDeg.get();
    }

    /**
     * Retourne la propriété contenant l'altitude en degrés de la position horizontal de la souris
     *
     * @returnla propriété contenant l'altitude en degrés de la position horizontal de la souris
     */
    public ObservableDoubleValue mouseAltDegProperty() {
        return mouseAltDeg;
    }

    /**
     * Retourne l'object celeste se trouvant sous la souris
     *
     * @return l'object celeste se trouvant sous la souris
     */
    public CelestialObject getObjectUnderMouse() {
        return objectUnderMouse.getValue();
    }

    /**
     * Retourne la propriété de l'object celeste se trouvant sous la souris
     *
     * @return la propriété del'object celeste se trouvant sous la souris
     */
    public ObservableValue<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }


}

