package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Bean représentant les paramètres de vue du ciel affiché
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class ViewingParametersBean {

    private final DoubleProperty fieldOfViewDeg;
    private final ObjectProperty<HorizontalCoordinates> center;

    /**
     * Constructeur de bean des paramètres de vue
     */
    public ViewingParametersBean() {
        this.fieldOfViewDeg = new SimpleDoubleProperty();
        this.center = new SimpleObjectProperty<>();
    }

    /**
     * Retourne la propriété du champs de vue de l'écran
     *
     * @return la propriété du champs de vue de l'écran
     */
    public DoubleProperty fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * Retourne l'angle du champs de vue de l'écran en degrés
     *
     * @return l'angle du champs de vue de l'écran en degrés
     */
    public double getFieldOfViewDeg() {
        return fieldOfViewDeg.get();
    }

    /**
     * Permet de modifier l'angle du champs de vue contenu dans la propriété
     *
     * @param newFieldOfViewDeg nouvel angle du champs de vue
     */
    public void setFieldOfViewDeg(double newFieldOfViewDeg) {
        fieldOfViewDeg.set(newFieldOfViewDeg);
    }

    /**
     * Retourne la propriété du centre du champs de vue
     *
     * @return la propriété du centre du champs de vue
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }

    /**
     * Retourne les coordonées horizontales contenue dans la propriété du centre du champs de vue
     *
     * @return les coordonées horizontales du centre du champs de vue
     */
    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    /**
     * Permet de modifier les coordonées du centre contenu dans la propriété
     *
     * @param newCenterCoordinates coordonées horizontales du nouveau centre
     */
    public void setCenter(HorizontalCoordinates newCenterCoordinates) {
        center.setValue(newCenterCoordinates);
    }

}
