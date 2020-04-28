package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;

/**
 * Classe représentant une animation du temps
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public final class TimeAnimator extends AnimationTimer {

    private final DateTimeBean instantBean;
    private long animationStartedSince;

    //todo private-public?
    public  TimeAnimator (DateTimeBean dateTimeBean){
        instantBean = dateTimeBean;

    }

    @Override
    public void start(){
        super.start();
       animationStartedSince = 0;
    }

    @Override
    public void handle(long l) {
        animationStartedSince += l;

    }
    @Override
    public void stop() {
        super.stop();
    }
}
