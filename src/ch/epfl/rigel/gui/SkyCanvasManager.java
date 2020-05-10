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


public final class SkyCanvasManager {

    private final Canvas canvas;
    private final DateTimeBean dateTimeBean;
    private final ObserverLocationBean observerLocationBean;
    private final ViewingParametersBean viewingParametersBean;
    //todo attribut ou pas?
    private final ObjectProperty <SkyCanvasPainter> skyCanvasPainter;

    //todo lien ou propriété?
    private final ObservableDoubleValue mouseAzDeg;
    private final ObservableDoubleValue mouseAltDeg;
    private final ObservableValue <CelestialObject> objectUnderMouse;

    private final ObservableValue <StereographicProjection> projection;
    private final ObservableValue <Transform> planeToCanvas;
    private final ObservableValue <ObservedSky> observedSky;
    private final ObjectProperty <CartesianCoordinates> mousePosition;
    private final ObservableValue <HorizontalCoordinates> mouseHorizontalPosition;

    private final static int AZIMUT_DEG_STEP = 10;
    private final static int ALTITUDE_DEG_STEP = 5;
    private final static RightOpenInterval AZIMUT_DEG_INTERVAL = RightOpenInterval.of(0,360);
    private final static ClosedInterval ALTITUDE_DEG_INTERVAL = ClosedInterval.of(5,90);


    public SkyCanvasManager(StarCatalogue starCatalogue,  DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) throws NonInvertibleTransformException {

        canvas = new Canvas();
        this.dateTimeBean = dateTimeBean;
        this.observerLocationBean = observerLocationBean;
        this.viewingParametersBean = viewingParametersBean;
        skyCanvasPainter = new SimpleObjectProperty<>();
        skyCanvasPainter.set(new SkyCanvasPainter(canvas));


        projection = Bindings.createObjectBinding(()-> ( new StereographicProjection(viewingParametersBean.getCenter())), this.viewingParametersBean.centerProperty());

        // à mettre en méthode
        double dilatation = canvas.getWidth()
                / projection.getValue().applyToAngle(viewingParametersBean.getFieldOfViewDeg());

        planeToCanvas = Bindings.createObjectBinding( () -> Transform.affine(dilatation, 0, 0, -dilatation, canvas.getWidth()/2.0, canvas.getHeight()/2.0)
                ,this.viewingParametersBean.fieldOfViewDegProperty()
                ,projection );
           // planeToCanvas = Bindings.createObjectBinding(() -> canvas.getWidth()/ projection.get().applyToAngle(viewingParametersBean.getFieldOfViewDeg()))

        observedSky = Bindings.createObjectBinding(() -> new ObservedSky(dateTimeBean.getZonedDateTime()
                        ,observerLocationBean.getCoordinates()
                        ,projection.getValue()
                        ,starCatalogue)
                ,this.observerLocationBean.coordinatesProperty()
                ,this.dateTimeBean.dateProperty()
                ,this.dateTimeBean.timeProperty()
                ,this.dateTimeBean.zoneProperty()
                ,projection);

          mousePosition = new SimpleObjectProperty<>(CartesianCoordinates.of(0,0));
          canvas.setOnMouseMoved( event -> mousePosition.setValue(CartesianCoordinates.of(event.getX(),event.getY())));

          //todo il faut rajouter planeToCanvas dans les dependecies?
          //todo ça bug...
          Point2D newCoord = planeToCanvas.getValue().inverseTransform(mousePosition.get().x(),mousePosition.get().y());
          mouseHorizontalPosition = Bindings.createObjectBinding(() -> projection.getValue().inverseApply(CartesianCoordinates.of(newCoord.getX(),newCoord.getY()))
                  ,mousePosition
                  ,projection
                  ,planeToCanvas);

          mouseAzDeg = Bindings.createDoubleBinding(() -> mouseHorizontalPosition.getValue().azDeg()
                  ,mouseHorizontalPosition);
          mouseAltDeg = Bindings.createDoubleBinding(() -> mouseHorizontalPosition.getValue().altDeg()
                  ,mouseHorizontalPosition);

          canvas.setOnMousePressed( event -> {
              if (event.isPrimaryButtonDown())
                  canvas.requestFocus(); }
                  ) ;

        objectUnderMouse = Bindings.createObjectBinding(() -> observedSky.getValue().objectClosestTo(CartesianCoordinates.of(newCoord.getX(),newCoord.getY()), 10).get()
                ,observedSky
                ,planeToCanvas
                ,mousePosition);


        canvas.setOnScroll( scrollEvent -> {
              double x = Math.abs(scrollEvent.getDeltaX());
              double y = Math.abs(scrollEvent.getDeltaY());
              double z = Math.max(x,y) == x ? scrollEvent.getDeltaX(): scrollEvent.getDeltaY();
              viewingParametersBean.setFieldOfViewDeg(z);
          });



          // TODO traiter les cas limites !! ( Poser la question aux assistants)
          //todo je crois que c'est fait?
          canvas.setOnKeyPressed(keyEvent -> {

              keyEvent.consume();

              switch (keyEvent.getCode()) {
                  case LEFT:
                      viewingParametersBean.setCenter(
                              HorizontalCoordinates.ofDeg(
                                      viewingParametersAzimutCenter(viewingParametersBean.getCenter().azDeg() - AZIMUT_DEG_STEP)
                                            ,viewingParametersBean.getCenter().altDeg()));

                  case RIGHT:
                      viewingParametersBean.setCenter(
                              HorizontalCoordinates.ofDeg(
                                      viewingParametersAzimutCenter(viewingParametersBean.getCenter().azDeg() + AZIMUT_DEG_STEP)
                                      ,viewingParametersBean.getCenter().altDeg()));

                  case UP:
                      viewingParametersBean.setCenter(
                              HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg()
                                      ,viewingParametersAltitudeCenter(viewingParametersBean.getCenter().altDeg() + ALTITUDE_DEG_STEP)));

                  case DOWN:
                      viewingParametersBean.setCenter(
                              HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg()
                                      ,viewingParametersAltitudeCenter(viewingParametersBean.getCenter().altDeg() - ALTITUDE_DEG_STEP)));


                  default:
                      throw new Error();
              }
          });


        skyCanvasPainter.addListener((beans, oldBeans, newBeans) -> {
            skyCanvasPainter.get().clear();
            skyCanvasPainter.get().drawStars(observedSky.getValue(),projection.getValue(),planeToCanvas.getValue());
            skyCanvasPainter.get().drawPlanets(observedSky.getValue(),projection.getValue(),planeToCanvas.getValue());
            skyCanvasPainter.get().drawMoon(observedSky.getValue(),projection.getValue(),planeToCanvas.getValue());
            skyCanvasPainter.get().drawSun(observedSky.getValue(),projection.getValue(),planeToCanvas.getValue());
            skyCanvasPainter.get().drawHorizon(projection.getValue(),planeToCanvas.getValue());
        });


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


    private double viewingParametersAzimutCenter(double newAzDeg){

        double x = AZIMUT_DEG_INTERVAL.size() - newAzDeg;
        //todo ça se fait ou c'est trop moche?
        return (AZIMUT_DEG_INTERVAL.contains(x)) ? newAzDeg
                                                :  (x < 0) ?  - x
                                                          : x - AZIMUT_DEG_INTERVAL.high();
    }

    private double viewingParametersAltitudeCenter(double newAltDeg){

        double x = (ALTITUDE_DEG_INTERVAL.size() - newAltDeg);
        //todo ça se fait ou c'est trop moche?
        return (ALTITUDE_DEG_INTERVAL.contains(x)) ? newAltDeg
                                                :  (x < 0) ?  - x
                                                          : x - ALTITUDE_DEG_INTERVAL.high();
    }

}
