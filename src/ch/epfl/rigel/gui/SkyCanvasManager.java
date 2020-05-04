package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.concurrent.Callable;

public final class SkyCanvasManager {

    private final ObserverLocationBean observerLocationBean;
    private final DateTimeBean dateTimeBean;
    private final ViewingParamtersBean viewingParamtersBean;
    private final ObjectBinding<StereographicProjection> projection;
    private final DoubleBinding planeToCanvas;
    private final ObservableValue <ObservedSky> observedSky; // Observablevalue
    private final ObjectProperty<Point2D> mousePosition;
    private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;



    public SkyCanvasManager(StarCatalogue starCatalogue, ObserverLocationBean observerLocationBean, DateTimeBean dateTimeBean, ViewingParamtersBean viewingParamtersBean) {

        this.observerLocationBean = observerLocationBean;
        this.dateTimeBean = dateTimeBean;
        this.viewingParamtersBean = viewingParamtersBean;
        Canvas canvas = new Canvas();

        projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParamtersBean.getCenter()));
        planeToCanvas = Bindings.createDoubleBinding(() -> canvas.getWidth()/ projection.get().applyToAngle(viewingParamtersBean.getDouble()));
        observedSky = Bindings.createObjectBinding(()-> new ObservedSky(dateTimeBean.getZonedDateTime(),
                GeographicCoordinates.ofDeg(observerLocationBean.getLonDeg(),
                observerLocationBean.getLatDeg()),
                projection.get(), starCatalogue));
        mousePosition = new SimpleObjectProperty<>();
        mouseHorizontalPosition = Bindings.createObjectBinding(()-> projection.get().inverseApply(CartesianCoordinates.of(mousePosition.get().getX(),mousePosition.get().getY())));
        mousePosition.addListener((observableValue, point2D, t1) -> mousePosition.setValue(t1));






    }
}
