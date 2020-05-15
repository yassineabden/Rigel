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


public final class SkyCanvasManager {

    private final Canvas canvas;
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


    public SkyCanvasManager(StarCatalogue starCatalogue, DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {

        canvas = new Canvas();
        this.dateTimeBean = dateTimeBean;
        this.observerLocationBean = observerLocationBean;
        this.viewingParametersBean = viewingParametersBean;
        skyCanvasPainter = new SkyCanvasPainter(canvas);

        projection = Bindings.createObjectBinding(() -> (new StereographicProjection(viewingParametersBean.getCenter())), this.viewingParametersBean.centerProperty());

        planeToCanvas = Bindings.createObjectBinding(() -> {
                    double dilatation  = canvas.getWidth()
                            / projection.getValue().applyToAngle(Angle.ofDeg(viewingParametersBean.getFieldOfViewDeg()));

                    return Transform.affine(dilatation, 0, 0, -dilatation, canvas.getWidth() / 2.0, canvas.getHeight() / 2.0);
                }
                , this.viewingParametersBean.fieldOfViewDegProperty()
                , projection
                , canvas.heightProperty()
                , canvas.widthProperty());

        observedSky = Bindings.createObjectBinding(() -> new ObservedSky(dateTimeBean.getZonedDateTime()
                        , observerLocationBean.getCoordinates()
                        , projection.getValue()
                        , starCatalogue)
                , this.observerLocationBean.coordinatesProperty()
                , this.dateTimeBean.dateProperty()
                , this.dateTimeBean.timeProperty()
                , this.dateTimeBean.zoneProperty()
                , projection);

        mousePosition = new SimpleObjectProperty<>(new Point2D(0,0));
        canvas.setOnMouseMoved(event -> mousePosition.set(new Point2D(event.getX(), event.getY())));

        mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
                    Point2D newCoord;
                    try {
                        newCoord = planeToCanvas.getValue().inverseTransform(mousePosition.get());
                        return projection.getValue().inverseApply(CartesianCoordinates.of(newCoord.getX(), newCoord.getY()));

                    } catch (NonInvertibleTransformException e) {
                        System.out.println("nonInvertibleTransform");
                        return null;
                    }
                }
                , mousePosition
                , projection
                , planeToCanvas);

        mouseAzDeg = createDoubleBinding(() -> (mouseHorizontalPosition.getValue() == null) ? 5.0 : mouseHorizontalPosition.getValue().azDeg()
                , mouseHorizontalPosition);
        mouseAltDeg = createDoubleBinding(() -> (mouseHorizontalPosition.getValue() == null) ? 5.0 : mouseHorizontalPosition.getValue().altDeg()
                , mouseHorizontalPosition);

        canvas.setOnMousePressed(event -> {
                    if (event.isPrimaryButtonDown())
                        canvas.requestFocus();
                });

        objectUnderMouse = Bindings.createObjectBinding(() -> {
                    Point2D newCoord;
                    try {
                        newCoord = planeToCanvas.getValue().inverseTransform(mousePosition.get().getX(), mousePosition.get().getY());
                        return observedSky.getValue().objectClosestTo(CartesianCoordinates.of(newCoord.getX(), newCoord.getY()), 10).orElse(null);

                    } catch (NonInvertibleTransformException e) {
                        return null;
                    }
                }
                , observedSky
                , planeToCanvas
                , mousePosition);

        canvas.setOnScroll(scrollEvent -> {

            double currentFoV = viewingParametersBean.getFieldOfViewDeg();
            double scrollDeltaX = scrollEvent.getDeltaX();
            double scrollDeltaY = scrollEvent.getDeltaY();

            double x = Math.abs(scrollDeltaX);
            double y = Math.abs(scrollDeltaY);
            double z = Math.max(x, y) == x ? currentFoV + scrollDeltaX : currentFoV + scrollDeltaY;

            viewingParametersBean.setFieldOfViewDeg(FOV_INTERVAL_DEG.clip(z));
        });

        //todo
        canvas.setOnKeyPressed(keyEvent -> {

            double centerALtDeg = viewingParametersBean.getCenter().altDeg();
            double centerAzDeg = viewingParametersBean.getCenter().azDeg();

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

        planeToCanvas.addListener(e -> drawSky());
        observedSky.addListener(e -> drawSky());

    }


    public Canvas canvas() {
        return canvas;
    }

    public double getMouseAzDeg() {
        return mouseAzDeg.get();
    }

    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }

    public double getMouseAltDeg() {
        return mouseAltDeg.get();
    }

    public ObservableDoubleValue mouseAltDegProperty() {
        return mouseAltDeg;
    }

    public CelestialObject getObjectUnderMouse() {
        return objectUnderMouse.getValue();
    }

    public ObservableValue<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

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

}
