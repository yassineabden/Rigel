package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * Enumération représentant un accélérateur de temps
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public enum NamedTimeAccelerator {

    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(60, Duration.ofDays(1))),
    SIDERAL_DAY("jour sidéral", TimeAccelerator.discrete(60, Duration.ofHours(23).plusMinutes(56).plusSeconds(4)));

    private final String name;
    private final TimeAccelerator accelerator;

    NamedTimeAccelerator(String name, TimeAccelerator accelerator) {
        
        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     *
     * @return
     */
    public String getName(){

        String type =  (this == DAY ||this == SIDERAL_DAY) ? "discrete" : "continuous";
        return new String("(" + name + "," + type);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return getName();
    }
}
