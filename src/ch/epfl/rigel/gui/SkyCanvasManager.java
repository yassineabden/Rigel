package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.transform.Transform;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

public final class SkyCanvasManager {

    private final Canvas canvas;
    private final DateTimeBean dateTimeBean;
    private final ObserverLocationBean observerLocationBean;
    private final ViewingParametersBean viewingParametersBean;
    //todo lien ou propriété?
    private final DoubleProperty mouseAzDeg;
    private final DoubleProperty mouseAltDeg;
    private final ObjectProperty<CelestialObject> objectUnderMouse;

    private final ObjectBinding <StereographicProjection> projection;
    private final ObjectBinding <Transform> planeToCanvas;
    private final ObjectBinding <ObservedSky> observedSky;
    private final ObjectBinding <HorizontalCoordinates> mouseHorizontalPosition;





    public SkyCanvasManager(StarCatalogue starCatalogue,  DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {

        canvas = new Canvas();
        this.dateTimeBean = dateTimeBean;
        this.observerLocationBean = observerLocationBean;
        this.viewingParametersBean = viewingParametersBean;

       //todo dans la lambda on défini ce que ça va être la valeur de projection?
        projection = Bindings.createObjectBinding(()-> ( new StereographicProjection(viewingParametersBean.getCenter())), viewingParametersBean);
       //todo la planeToCanvas était une Transform avant, on doit la faire comme dans l'étape 8 ou juste avec un facteur de dilatation?
        // à mettre en méthode
        double dilatation = canvas.getWidth()/ projection.get().applyToAngle(viewingParametersBean.getFieldOfViewDeg());
        // todo je comprends pas pourquoi ça joue pas... je donne bien une Transform non?
           planeToCanvas = Bindings.createObjectBinding( () -> Transform.affine(dilatation, 0, 0, -dilatation, canvas.getWidth()/2, canvas.getHeight()/2), viewingParametersBean,canvas );
           // planeToCanvas = Bindings.createObjectBinding(() -> canvas.getWidth()/ projection.get().applyToAngle(viewingParametersBean.getFieldOfViewDeg()))

        observedSky = Bindings.createObjectBinding(() -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(),projection.get(), starCatalogue), observerLocationBean,dateTimeBean,projection);
        canvas.addMouseListener();
        mouseHorizontalPosition = Bindings.createObjectBinding( () ->  );



    }





/**
    private final ObserverLocationBean observerLocationBean;
    private final DateTimeBean dateTimeBean;
    private final ViewingParametersBean viewingParametersBean;
    private final ObjectBinding<StereographicProjection> projection;
    //todo observable property
    private final ObservableValue <Transform> planeToCanvas;
   // private final DoubleBinding planeToCanvas;

    private final ObservableValue <ObservedSky> observedSky; // Observablevalue
    private final ObjectProperty<Point2D> mousePosition;
    private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;
    private final Canvas canvas;


    public SkyCanvasManager(StarCatalogue starCatalogue, ObserverLocationBean observerLocationBean, DateTimeBean dateTimeBean, ViewingParametersBean viewingParametersBean) {

        this.observerLocationBean = observerLocationBean;
        this.dateTimeBean = dateTimeBean;
        this.viewingParametersBean = viewingParametersBean;
        //todo canvas
        canvas = new Canvas();

        projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParametersBean.getCenter()), );
        planeToCanvas = Bindings.createDoubleBinding(() -> canvas.getWidth()/ projection.get().applyToAngle(viewingParametersBean.getDouble()));
        observedSky = Bindings.createObjectBinding(()-> new ObservedSky(dateTimeBean.getZonedDateTime(),
                GeographicCoordinates.ofDeg(observerLocationBean.getLonDeg()
                ,observerLocationBean.getLatDeg())
                ,projection.get()
                ,starCatalogue));
        mousePosition = new SimpleObjectProperty<>();
        mouseHorizontalPosition = Bindings.createObjectBinding(()-> projection.get().inverseApply(CartesianCoordinates.of(mousePosition.get().getX(),mousePosition.get().getY())));
        mousePosition.addListener((observableValue, point2D, t1) -> mousePosition.setValue(t1));


    }

    public Canvas getCanvas() {
        return canvas;
    }
 */

}
