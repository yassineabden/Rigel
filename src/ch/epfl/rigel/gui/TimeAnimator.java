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
    ZonedDateTime simulatedZoneDateTime;
    private final ObjectProperty <TimeAccelerator> accelerator = new SimpleObjectProperty<>();
    private final BooleanProperty running = new SimpleBooleanProperty();
    private long initialTime;

    /**
     *
     * @param dateTimeBean
     */
    //todo private-public?
    public TimeAnimator (DateTimeBean dateTimeBean){
        instantBean = dateTimeBean;
        running.set(false);
        accelerator.set(null);
        //initialisation du "temps zéro" à -1 par défault
        initialTime = -1;
    }

    /**
     *
     */
    @Override
    public void start(){
        //todo set running avant ou après start?
        running.set(true);
        super.start();
    }

    /**
     *
     * @param realTime
     */
    @Override
    public void handle(long realTime) {

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
     *
     */
    @Override
    public void stop() {
        super.stop();
        running.set(false);
    }

    /**
     * Retourne
     * @return
     */
    public ReadOnlyBooleanProperty isRunning(){return running;}

    /**
     *
     * @return
     */
    public TimeAccelerator getAccelerator() {
        return accelerator.get();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return accelerator;
    }

    /**
     *
     * @param accelerator
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }
}
