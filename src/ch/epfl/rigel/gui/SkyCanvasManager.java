package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import javax.swing.text.MutableAttributeSet;
import java.util.Optional;

import static javafx.scene.input.KeyCode.*;


public final class SkyCanvasManager {

    private final Canvas canvas;
    private final DateTimeBean dateTimeBean;
    private final ObserverLocationBean observerLocationBean;
    private final ViewingParametersBean viewingParametersBean;

    //todo lien ou propriété?
    private final ObservableDoubleValue mouseAzDeg;
    private final ObservableDoubleValue mouseAltDeg;
    //private final ObjectProperty <Optional <CelestialObject>> objectUnderMouse;

    private final ObservableValue <StereographicProjection> projection;
    private final ObservableValue <Transform> planeToCanvas;
    private final ObservableValue <ObservedSky> observedSky;
    private final ObjectProperty <CartesianCoordinates> mousePosition;
    private final ObservableValue <HorizontalCoordinates> mouseHorizontalPosition;


    public SkyCanvasManager(StarCatalogue starCatalogue,  DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) throws NonInvertibleTransformException {

        canvas = new Canvas();
        this.dateTimeBean = dateTimeBean;
        this.observerLocationBean = observerLocationBean;
        this.viewingParametersBean = viewingParametersBean;

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
          //rendre plus joli
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

          canvas.setOnScroll( scrollEvent -> {
              double x = Math.abs(scrollEvent.getDeltaX());
              double y = Math.abs(scrollEvent.getDeltaY());
              double z = Math.max(x,y) == x? scrollEvent.getDeltaX(): scrollEvent.getDeltaY();
              viewingParametersBean.setFieldOfViewDeg(z);
          });

          // TODO traiter les cas limites !! ( Poser la question aux assistants)
          canvas.setOnKeyPressed(keyEvent -> {

              keyEvent.consume();

              switch (keyEvent.getCode()) {
                  case LEFT:
                      viewingParametersBean.setCenter( HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg()-10,
                              viewingParametersBean.getCenter().altDeg()));
                  case RIGHT:
                      viewingParametersBean.setCenter( HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg()+10,
                              viewingParametersBean.getCenter().altDeg()));
                  case UP:
                      viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg(),
                              viewingParametersBean.getCenter().altDeg()+5));
                  case DOWN:
                      viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg(),
                              viewingParametersBean.getCenter().altDeg()-5));
                  default:
                      throw new Error();
              }
          });








          /**
          objectUnderMouse = new SimpleObjectProperty<>();

          objectUnderMouse.bind(observedSky.getValue().objectClosestTo(CartesianCoordinates.of(newCoord.getX(),newCoord.getY()),10));

          objectUnderMouse = Bindings.createObjectBinding(() -> observedSky.getValue().objectClosestTo(CartesianCoordinates.of(newCoord.getX(),newCoord.getY()), 10)
                  ,observedSky
                  ,planeToCanvas
                  ,mousePosition);
*/

    }

}
