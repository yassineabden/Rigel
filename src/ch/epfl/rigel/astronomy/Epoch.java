package ch.epfl.rigel.astronomy;


import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Enumération époque
 * Représente les moments à partir desquels les mouvement des planètes est calculé
 *
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public enum Epoch {

    J2000(ZonedDateTime.of(LocalDate.of(2000,Month.JANUARY,1),LocalTime.NOON, ZoneOffset.UTC)),
    J2010 (ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),LocalTime.MIDNIGHT,ZoneOffset.UTC));

    private final ZonedDateTime epoch ;

    private final static double MILLIS_SEC_TO_DAYS = 1.0/Duration.ofDays(1).toMillis();
    private final static double MILLIS_TO_JULIAN_CENTURY = (1.0/36525)* MILLIS_SEC_TO_DAYS;


    Epoch(ZonedDateTime of) {
        epoch = of;
    }

    /**
     * Calcul le nombre de jours précis entre l'époque et l'instant donné
     *
     * @param when instant donné
     *
     * @return Nombre de jours entre l'époque et le temps demandé
     */
   public double daysUntil(ZonedDateTime when){

       return (this.epoch.until(when, ChronoUnit.MILLIS))
               * MILLIS_SEC_TO_DAYS;
   }


    /**
     * Calcul du nombre de siècle Julien entre l'époque et l'instant donné
     *
     * @param when l'instant donné
     *
     * @return Nombre précis de siècles Julien entre l'époque et le moment
     */
    public double julianCenturiesUntil(ZonedDateTime when){

       return (this.epoch.until(when, ChronoUnit.MILLIS))
               * MILLIS_TO_JULIAN_CENTURY;
    }
}

