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


    abstract ZonedDateTime adjust(ZonedDateTime simulatedTime, long nanoSeconds);

    public static TimeAccelerator continuous(int alpha) {
        return (simulatedTime, nanoSeconds) -> simulatedTime.plusNanos(alpha*nanoSeconds);

    }

    public static TimeAccelerator discrete (int lambda, Duration steps) {
        long r = 5;
        long variation = (long) Math.floor(lambda*5);
        return ((simulatedTime, nanoSeconds) -> simulatedTime.plusNanos(steps.toNanos()*(long)Math.floor(lambda*nanoSeconds)));
    }


}
