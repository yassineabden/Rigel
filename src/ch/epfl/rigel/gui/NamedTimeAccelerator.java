package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * Enumération représentant un accélérateur de temps constitué d'une paire (nom, accélérateur)
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

    /**
     * Constructeur des accélérateurs
     *
     * @param name        le nom de la paire
     * @param accelerator le type de l'accélérateur de la paire
     */
    NamedTimeAccelerator(String name, TimeAccelerator accelerator) {

        this.name = name;
        this.accelerator = accelerator;
    }

    /**
     * Retourne l'accélérateur de la paire
     *
     * @return l'accélérateur de la paire
     */
    public TimeAccelerator getAccelerator() {
        return accelerator;
    }

    /**
     * Retourne le nom de la paire: l'accélérateur et son nom sous forme textuelle
     *
     * @return la paire sous forme (nom, accélérateur)
     */
    public String getName() {

        String type = (this == DAY || this == SIDERAL_DAY) ? "discrete" : "continuous";
        return ("(" + name + "," + type + ")");
    }

    /**
     * Retourne la paire de l'accélérateur et son nom assococié sous forme textuelle
     *
     * @return la paire sous forme (nom, accélérateur)
     */
    @Override
    public String toString() {
        return getName();
    }
}
