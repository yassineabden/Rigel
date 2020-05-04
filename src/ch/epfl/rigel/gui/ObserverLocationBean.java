package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public final class ObserverLocationBean {

    private final ObjectProperty<Double> lonDeg;
    private final ObjectProperty<Double> latDeg;

    public ObserverLocationBean() {
        this.lonDeg = new SimpleObjectProperty<>();
        this.latDeg = new SimpleObjectProperty<>();
    }

    public ObjectProperty<Double> lonDegProperty(){ return lonDeg; }

    public double getLonDeg(){ return lonDeg.get(); }

    public void setLonDeg (double newLonDeg){ latDeg.setValue(newLonDeg); }

    public ObjectProperty<Double> latDegProperty(){ return latDeg; }

    public double getLatDeg(){ return latDeg.get(); }

    public void setLatDeg (double newLatDeg){ latDeg.setValue(newLatDeg); }





}
