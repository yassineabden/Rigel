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
    //TODO on fait ce qui intelliJ ?
    private final DateTimeBean dateTimeBean;
    private final ObserverLocationBean observerLocationBean;
    private final ViewingParametersBean viewingParametersBean;
    private final SkyCanvasPainter skyCanvasPainter;

    private final ObservableDoubleValue mouseAzDeg;
    private final ObservableDoubleValue mouseAltDeg;
    private final ObservableValue<CelestialObject> objectUnderMouse;

    private final ObservableValue<StereographicProjection> projection;
    private final ObservableValue<Transform> planeToCanvas;
    private final ObservableValue<ObservedSky> observedSky;
    private final ObjectProperty<Point2D> mousePosition;
    private final ObservableValue<HorizontalCoordinates> mouseHorizontalPosition;

    private final static int AZIMUT_DEG_STEP = 10;
    private final static int ALTITUDE_DEG_STEP = 5;
    private final static RightOpenInterval AZIMUT_DEG_INTERVAL = RightOpenInterval.of(0, 360);
    private final static ClosedInterval ALTITUDE_DEG_INTERVAL = ClosedInterval.of(5, 90);
    private final static ClosedInterval FOV_INTERVAL_DEG = ClosedInterval.of(30, 150);

    /**
     * TODO public?
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
        this.dateTimeBean = dateTimeBean;
        this.observerLocationBean = observerLocationBean;
        this.viewingParametersBean = viewingParametersBean;
        skyCanvasPainter = new SkyCanvasPainter(canvas);


        HorizontalCoordinates viewingParametersBeanCenter = viewingParametersBean.getCenter();

        projection = Bindings.createObjectBinding(() -> (new StereographicProjection(viewingParametersBeanCenter))
                , this.viewingParametersBean.centerProperty());

        StereographicProjection stereographicProjection = projection.getValue();

        double viewingParametersBeanFoVDeg = viewingParametersBean.getFieldOfViewDeg();

        planeToCanvas = Bindings.createObjectBinding(() -> {
                    double dilatation = canvas.getWidth()
                            / stereographicProjection.applyToAngle(Angle.ofDeg(viewingParametersBeanFoVDeg));

                    return Transform.affine(dilatation, 0, 0, -dilatation, canvas.getWidth() / 2.0, canvas.getHeight() / 2.0);
                }
                , this.viewingParametersBean.fieldOfViewDegProperty()
                //todo avant il y avait projection.getValue() ...
                , projection
                , canvas.heightProperty()
                , canvas.widthProperty());

        observedSky = Bindings.createObjectBinding(() -> new ObservedSky(dateTimeBean.getZonedDateTime()
                        , observerLocationBean.getCoordinates()
                        , stereographicProjection
                        , starCatalogue)
                , this.observerLocationBean.coordinatesProperty()
                , this.dateTimeBean.dateProperty()
                , this.dateTimeBean.timeProperty()
                , this.dateTimeBean.zoneProperty()
                , projection);

        mousePosition = new SimpleObjectProperty<>(new Point2D(0, 0));
        canvas.setOnMouseMoved(event -> mousePosition.set(new Point2D(event.getX(), event.getY())));

        mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
                    Point2D newPosition;

                    if ((newPosition = inversePlaneToCanvas(mousePosition.get())) != null) {
                        return stereographicProjection.inverseApply(CartesianCoordinates.of(newPosition.getX(), newPosition.getY()));
                    } else {
                        return null;
                    }
                }
/**
 return ((newPosition = inversePlaneToCanvas(mousePosition.get()) )!= null) ?
 projection.getValue().inverseApply(CartesianCoordinates.of(newPosition.getX(), newPosition.getY()))
 : newPosition;
 */

                , mousePosition
                , projection
                , planeToCanvas);

        mouseAzDeg = createDoubleBinding(() -> (mouseHorizontalPosition.getValue() == null) ? 0.0 : mouseHorizontalPosition.getValue().azDeg()
                , mouseHorizontalPosition);
        mouseAltDeg = createDoubleBinding(() -> (mouseHorizontalPosition.getValue() == null) ? 0.0 : mouseHorizontalPosition.getValue().altDeg()
                , mouseHorizontalPosition);

        canvas.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown())
                canvas.requestFocus();
        });

        objectUnderMouse = Bindings.createObjectBinding(() -> {
                    Point2D newPosition;
                    if((newPosition = inversePlaneToCanvas(mousePosition.get())) != null) {
                        return observedSky.getValue()
                                .objectClosestTo(CartesianCoordinates.of(newPosition.getX(), newPosition.getY()), 10)
                                .orElse(null);
                    } else {
                        return null;
                    }

                    /**
                     Point2D newCoord;
                     try {
                     newCoord = planeToCanvas.getValue().inverseTransform(mousePosition.get().getX(), mousePosition.get().getY());
                     return observedSky.getValue().objectClosestTo(CartesianCoordinates.of(newCoord.getX(), newCoord.getY()), 10).orElse(null);

                     } catch (NonInvertibleTransformException e) {
                     return null;
                     }
                     */
                }
                , observedSky
                , planeToCanvas
                , mousePosition);

        canvas.setOnScroll(scrollEvent -> {

            double currentFoV = viewingParametersBeanFoVDeg;
            double scrollDeltaX = scrollEvent.getDeltaX();
            double scrollDeltaY = scrollEvent.getDeltaY();

            double x = Math.abs(scrollDeltaX);
            double y = Math.abs(scrollDeltaY);
            double z = Math.max(x, y) == x ? currentFoV + scrollDeltaX : currentFoV + scrollDeltaY;

            viewingParametersBean.setFieldOfViewDeg(FOV_INTERVAL_DEG.clip(z));
        });


        canvas.setOnKeyPressed(keyEvent -> {

            double centerALtDeg = viewingParametersBeanCenter.altDeg();
            double centerAzDeg = viewingParametersBeanCenter.azDeg();

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

        planeToCanvas.addListener(e -> drawSky(stereographicProjection));
        observedSky.addListener(e -> drawSky(stereographicProjection));

    }

    /**
     * Retourne le canvas utilisé
     *
     * @return le canvas utilisé
     */
    public Canvas canvas() {
        return canvas;
    }

    /**TODO on utilise pas?
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

    /**TODO on utilise pas?
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

    private void drawSky(StereographicProjection stereographicProjection) {

        ObservedSky sky = observedSky.getValue();
        Transform transform = planeToCanvas.getValue();

        skyCanvasPainter.clear();
        skyCanvasPainter.drawStars(sky, stereographicProjection, transform);
        skyCanvasPainter.drawPlanets(sky, stereographicProjection, transform);
        skyCanvasPainter.drawSun(sky, stereographicProjection, transform);
        skyCanvasPainter.drawMoon(sky, stereographicProjection, transform);
        skyCanvasPainter.drawHorizon(stereographicProjection, transform);

    }
    //TODO c'est quoi le mieux/diff entre try catch et throws Exception?

    /**
     * private Point2D inversePlaneToCanvas (Point2D canvasCoordinates) throws NonInvertibleTransformException {
     * return planeToCanvas.getValue().inverseTransform(canvasCoordinates);
     * }
     */

    private Point2D inversePlaneToCanvas(Point2D canvasCoordinates) {
        try {
            return planeToCanvas.getValue().inverseTransform(canvasCoordinates);
        } catch (NonInvertibleTransformException e) {
            return null;
        }
    }


}

