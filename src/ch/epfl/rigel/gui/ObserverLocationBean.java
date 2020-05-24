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

/**
 * Bean représentant l'endroit d'observation du ciel
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class ObserverLocationBean {

    private final DoubleProperty lonDeg;
    private final DoubleProperty latDeg;
    private final ObservableValue<GeographicCoordinates> coordinates;


    /**
     * TODO public
     * Constructeur de bean de l'endroit d'observation
     */
    public ObserverLocationBean() {
        this.lonDeg = new SimpleDoubleProperty();
        this.latDeg = new SimpleDoubleProperty();
        coordinates = Bindings.createObjectBinding(() -> GeographicCoordinates.ofDeg(getLonDeg(), getLatDeg()), lonDeg, latDeg);
    }

    /**
     * Retourne la propriété de la longitude de l'endroit d'observation
     *
     * @return la propriété contenant longitude de l'endroit d'observation
     */
    public DoubleProperty lonDegProperty() {
        return lonDeg;
    }

    /**
     * La longitude en degrés de l'endroit d'observation contenu dans la propriété
     *
     * @return la longitude en degrés de l'endroit d'observation
     */
    public double getLonDeg() {
        return lonDeg.get();
    }

    /**
     * Permet de modifier la longitude de l'endroit d'observation contenu dans la propriété
     *
     * @param newLonDeg nouvelle longitude en degrés
     */
    public void setLonDeg(double newLonDeg) {
        lonDeg.set(newLonDeg);
    }

    /**
     * Retourne la propriété de la latitude de l'endroit d'observation
     *
     * @return la propriété de la latitude de l'endroit d'observation
     */
    public DoubleProperty latDegProperty() {
        return latDeg;
    }

    /**
     * La latitude en degré de l'endroit d'observation contenu dans la propriété
     *
     * @return la latitude en degrés de l'endroit d'observation
     */
    public double getLatDeg() {
        return latDeg.get();
    }

    /**
     * Permet de modifier la latitude de l'endroit d'observation contenu dans la propriété
     *
     * @param newLatDeg nouvelle latitude en degrés
     */
    public void setLatDeg(double newLatDeg) {
        latDeg.set(newLatDeg);
    }

    /**
     * Retourne la propriété de l'endroit d'observation en coordonnées géographiques
     *
     * @return la propriété de l'endroit d'observation en coordonnées géographiques
     */
    public ObservableValue<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    /**
     * Retourne les coordonnées géographiques de l'endroit d'observation contenu dans la propriété
     *
     * @return les coordonnées géographiques de l'endroit d'observation
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.getValue();
    }

    /**
     * Permet de modifier l'endroit d'observation du bean grâce à des coordonées géographiques
     *
     * @param geographicCoordinates coordonées géographiques du nouvel endroit d'observation
     */
    public void setCoordinates(GeographicCoordinates geographicCoordinates) {
        setLonDeg(geographicCoordinates.lonDeg());
        setLatDeg(geographicCoordinates.latDeg());
    }


}
