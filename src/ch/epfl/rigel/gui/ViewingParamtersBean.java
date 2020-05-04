package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public  final class ViewingParamtersBean {

    private final ObjectProperty<Double> fieldOfViewDeg;
    private final ObjectProperty <HorizontalCoordinates> center;

    public ViewingParamtersBean(){
        this.fieldOfViewDeg = new SimpleObjectProperty<>();
        this.center = new SimpleObjectProperty<>();
    }

    public ObjectProperty<Double> doubleProperty(){ return fieldOfViewDeg; }

    public double getDouble(){ return fieldOfViewDeg.get(); }

    public void setDouble (double newDouble) { fieldOfViewDeg.setValue(newDouble); }

    public ObjectProperty<HorizontalCoordinates> centerProperty(){ return center; }

    public HorizontalCoordinates getCenter(){ return center.get(); }

    public void setCenter ( HorizontalCoordinates newCoordinates) { center.setValue(newCoordinates); }


















}
