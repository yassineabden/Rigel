package ch.epfl.rigel.gui;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

@FunctionalInterface
public interface TimeAccelerator {

     ZonedDateTime adjust(ZonedDateTime simulatedTime, long nanoSeconds);

     static TimeAccelerator continuous(int alpha) {
         //todo c'est juste plus(alpha*nanoSeconds ou plus(alpha*(simulatedTime-nanoSeconds)
        return (simulatedTime, nanoSeconds) -> simulatedTime.plusNanos(alpha*nanoSeconds);

    }

     static TimeAccelerator discrete (long lambda, Duration step) {
         return ((simulatedTime, nanoSeconds) -> simulatedTime.plus(step.multipliedBy((long) (lambda*nanoSeconds/1_000_000_000))));
    }

}
