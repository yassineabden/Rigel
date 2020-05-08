package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public final class ObserverLocationBean {

    private final DoubleProperty lonDeg;
    private final DoubleProperty latDeg;
    //todo assistant avait dit abservable value ça pouvait être une bonne idée mais je capte pas comment
    //private final ObservableValue <GeographicCoordinates> coordinates;
    private final ObjectBinding <GeographicCoordinates> coordinates;


    public ObserverLocationBean() {
        this.lonDeg = new SimpleDoubleProperty();
        this.latDeg = new SimpleDoubleProperty();
        //todo on peut caster le bind ??
        //coordinates = Bindings.createObjectBinding( ()-> coordinatesProperty().setValue(GeographicCoordinates.ofDeg(getLonDeg(),getLatDeg())) ,lonDeg,latDeg );
       //todo ça marche comme ça?
        coordinates = Bindings.createObjectBinding(()-> GeographicCoordinates.ofDeg(getLonDeg(),getLatDeg()), lonDeg,latDeg);
    }

    public DoubleProperty lonDegProperty(){ return lonDeg; }

    public double getLonDeg(){ return lonDeg.get(); }

    public void setLonDeg (double newLonDeg){ latDeg.set(newLonDeg); }

    public DoubleProperty latDegProperty(){ return latDeg; }

    public double getLatDeg(){ return latDeg.get(); }

    public void setLatDeg (double newLatDeg){ latDeg.set(newLatDeg); }

    //todo pas tout compris le délire de observable value
    public ObservableValue<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }
    public GeographicCoordinates getCoordinates() {
        return coordinates.get();
    }
    public void setCoordinates (GeographicCoordinates geographicCoordinates){
        setLonDeg(geographicCoordinates.lonDeg());
        setLatDeg(geographicCoordinates.latDeg());
    }




}
