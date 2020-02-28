package ch.epfl.rigel.astronomy;


import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Enumération époque
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public enum Epoch {


    J2000(ZonedDateTime.of(LocalDateTime.of(2000,Month.JANUARY,1,12,0), ZoneId.of("UTC"))),
    J2010(ZonedDateTime.of(LocalDateTime.of(2010,Month.JANUARY,1,0,0).minusDays(1), ZoneId.of("UTC")));

    private ZonedDateTime epoch ;


    final static double MILLIS_SEC_PER_DAYS = 1.0/Duration.ofDays(1).toMillis();
    final static double HOURS_PER_JULIAN_CENTURY = (1.0/Duration.ofDays(36525).toMillis());



    private Epoch(ZonedDateTime of) {
    this.epoch = of;
   }

    /**

     * Calcul le nombre de jours précis entre une des époque et le temps voulu
     *
     * @param when Moment de calcul(?!)
     *
     * @return Nombre de jours entre l'époque et le temps demandé
     */
   public  double daysUntil(ZonedDateTime when){

       return (this.epoch.until(when, ChronoUnit.MILLIS))* MILLIS_SEC_PER_DAYS;

       }


    /**
     * Calcul du nombre de siècle Julien entre l'époque et le moment voulu
     *
     * @param when moment à calculer
     *
     * @return Nombre précis de siècles Julien entre l'époque et le moment
     */
    public double julianCenturiesUntil(ZonedDateTime when){

       return (this.epoch.until(when, ChronoUnit.MILLIS))*MILLIS_SEC_PER_DAYS*HOURS_PER_JULIAN_CENTURY;
    }
}

