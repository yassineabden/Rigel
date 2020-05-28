package ch.epfl.rigel.gui;


import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Interface représentant un accélérateur de temps
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

@FunctionalInterface
public interface TimeAccelerator {

     double NANOSECONDS_PER_SECOND = 1_000_000_000;

    /**
     * Calcule le temps simulé
     *
     * @param simulatedTime le temps simulé initial (de type ZoneDateTime)
     * @param nanoSeconds   le temps réel écoulé depuis le début de l'animation (exprimé en nanosecondes)
     *
     * @return le temps simulé sous la forme d'une nouvelle instance de ZonedDateTime.
     */
    ZonedDateTime adjust(ZonedDateTime simulatedTime, long nanoSeconds);

    /**
     * Retourne un accélérateur continu
     *
     * @param alpha facteur d'accélération (entier)
     *
     * @return un accélérateur continu
     */
    static TimeAccelerator continuous(int alpha) {

        return (simulatedTime, nanoSeconds) -> simulatedTime.plusNanos(alpha * nanoSeconds);
    }

    /**
     * Retourne un accélérateur discret
     *
     * @param lambda la fréquence d'avancement du temps simulé
     * @param step   le pas discret de temps simulé
     *
     * @return un accélérateur discret
     */
    static TimeAccelerator discrete(long lambda, Duration step) {

        return ((simulatedTime, nanoSeconds) ->
                simulatedTime.plus(step.multipliedBy((long) (lambda * nanoSeconds / NANOSECONDS_PER_SECOND))));
    }

}
