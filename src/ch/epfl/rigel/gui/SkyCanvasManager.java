package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
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


    public SkyCanvasManager(StarCatalogue starCatalogue, DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {

        canvas = new Canvas();
        this.dateTimeBean = dateTimeBean;
        this.observerLocationBean = observerLocationBean;
        this.viewingParametersBean = viewingParametersBean;
        skyCanvasPainter = new SkyCanvasPainter(canvas);

        projection = Bindings.createObjectBinding(() -> (new StereographicProjection(viewingParametersBean.getCenter())), this.viewingParametersBean.centerProperty());

        // à mettre en méthode
        double dilatation = canvas.getWidth()
                / projection.getValue().applyToAngle(viewingParametersBean.getFieldOfViewDeg());

        planeToCanvas = Bindings.createObjectBinding(() -> Transform.affine(dilatation, 0, 0, -dilatation, canvas.getWidth() / 2.0, canvas.getHeight() / 2.0)
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

        mousePosition = new SimpleObjectProperty<>(new Point2D(0, 0));
        canvas.setOnMouseMoved(event -> mousePosition.set(new Point2D(event.getX(), event.getY())));

        //todo il faut rajouter planeToCanvas dans les dependecies?
        //todo ça bug... -> est-ce que c'est dans le repère du ciel ou juste en coordHorizontale?


        mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
                    Point2D newCoord;
                    try {
                        newCoord = planeToCanvas.getValue().inverseTransform(mousePosition.get().getX(), mousePosition.get().getY());
                        return projection.getValue().inverseApply(CartesianCoordinates.of(newCoord.getX(), newCoord.getY()));

                    } catch (NonInvertibleTransformException e) {
                        return null;
                    }
                }
                , mousePosition
                , projection
                , planeToCanvas);

        mouseAzDeg = createDoubleBinding(() -> mouseHorizontalPosition.getValue().azDeg()
                , mouseHorizontalPosition);
        mouseAltDeg = createDoubleBinding(() -> mouseHorizontalPosition.getValue().altDeg()
                , mouseHorizontalPosition);

        canvas.setOnMousePressed(event -> {
                    if (event.isPrimaryButtonDown())
                        canvas.requestFocus();
                }
        );

//todo try catch
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
            double x = Math.abs(scrollEvent.getDeltaX());
            double y = Math.abs(scrollEvent.getDeltaY());
            double z = Math.max(x, y) == x ? scrollEvent.getDeltaX() : scrollEvent.getDeltaY();
            viewingParametersBean.setFieldOfViewDeg(z);
        });

        //todo
        canvas.setOnKeyPressed(keyEvent -> {

            keyEvent.consume();

            switch (keyEvent.getCode()) {
                case LEFT:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    AZIMUT_DEG_INTERVAL.reduce(viewingParametersBean.getCenter().azDeg() - AZIMUT_DEG_STEP)
                                    , viewingParametersBean.getCenter().altDeg()));

                case RIGHT:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(
                                    AZIMUT_DEG_INTERVAL.reduce(viewingParametersBean.getCenter().azDeg() + AZIMUT_DEG_STEP)
                                    , viewingParametersBean.getCenter().altDeg()));

                case UP:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg()
                                    , ALTITUDE_DEG_INTERVAL.clip(viewingParametersBean.getCenter().altDeg() + ALTITUDE_DEG_STEP)));

                case DOWN:
                    viewingParametersBean.setCenter(
                            HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg()
                                    , ALTITUDE_DEG_INTERVAL.clip(viewingParametersBean.getCenter().altDeg() - ALTITUDE_DEG_STEP)));


                default:
                    throw new Error();
            }
        });
        planeToCanvas.addListener(e -> drawSky());
        observedSky.addListener(e -> drawSky());

    }


    public Canvas canvas() {
        return canvas;
    }

    public Number getMouseAzDeg() {
        return mouseAzDeg.get();
    }

    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }

    public Number getMouseAltDeg() {
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
        System.out.println("drawSky()");

        skyCanvasPainter.clear();
        skyCanvasPainter.drawStars(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        skyCanvasPainter.drawPlanets(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        skyCanvasPainter.drawMoon(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        skyCanvasPainter.drawSun(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        //skyCanvasPainter.drawHorizon(projection.getValue(), planeToCanvas.getValue());

    }

}
