package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

import java.awt.*;
import java.awt.geom.Point2D;

public final class SkyCanvasManager {

    private final ObserverLocationBean observerLocationBean;
    private final DateTimeBean dateTimeBean;
    private final ViewingParametersBean viewingParametersBean;
    private final ObjectBinding<StereographicProjection> projection;
    //todo observable property
    private final DoubleBinding planeToCanvas;

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
}
