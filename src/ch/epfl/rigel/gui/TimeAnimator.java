package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;

import java.time.ZonedDateTime;

/**
 * Classe représentant une animation du temps
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class TimeAnimator extends AnimationTimer {

    private final DateTimeBean instantBean;
    private ZonedDateTime simulatedZoneDateTime;
    private final ObjectProperty <TimeAccelerator> accelerator = new SimpleObjectProperty<>();
    private final BooleanProperty running = new SimpleBooleanProperty();
    private long initialTime;

    /**
     * Constructeur de la classe
     *
     * @param dateTimeBean l'instant d'observation
     */
    public TimeAnimator (DateTimeBean dateTimeBean){
        instantBean = dateTimeBean;
        running.set(false);
        accelerator.set(null);
        //initialisation du "temps zéro" à -1 par défault
        initialTime = -1;
    }

    /**
     * Démarre l'animation du temps
     */
    @Override
    public void start(){
        super.start();
        running.set(true);
    }

    /**
     * Cette méthode va être appelée afin de faire progresser l'animation
     *
     * @param realTime nombre de nanosecondes écoulées depuis un instant de départ
     */
    @Override
    public void handle(long realTime) {

        // Initialise l'instant de début de l'animation
        if (initialTime == -1){
            initialTime = realTime;
            simulatedZoneDateTime = instantBean.getZonedDateTime();
        }
        else {
            instantBean.setZonedDateTime( getAccelerator()
                    .adjust(simulatedZoneDateTime,realTime - initialTime));
        }
    }

    /**
     * Arrête l'animation du temps
     */
    @Override
    public void stop() {
        super.stop();
        running.set(false);
    }

    /**
     * Retourne la propriété de l'état de l'animateur
     *
     * @return la propriété de l'état de l'animateur
     */
    public ReadOnlyBooleanProperty isRunning(){
        return running;
    }

    /**
     * Retourne le contenu de l'accélérateur de temps
     *
     * @return le contenu de l'accélérateur de temps
     */
    public TimeAccelerator getAccelerator() {
        return accelerator.get();
    }

    /**
     * Retourne la propriété de l'accélérateur de temps
     *
     * @return la propriété de l'accélérateur de temps
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    /**
     * Modifie le contenu de l'accélérateur de temps
     *
     * @param accelerator nouvel accélérateur
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }
}
