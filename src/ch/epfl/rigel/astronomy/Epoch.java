package ch.epfl.rigel.astronomy;


import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Enumération époque
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */
public enum Epoch {
    //TODO est-ce que Epoch doit implémenter Temporal?

    // ça joue comme ça ???!

    J2000(ZonedDateTime.of(LocalDateTime.of(2000,Month.JANUARY,1,12,0), ZoneId.of("UTC"))),
    J2010(ZonedDateTime.of(LocalDateTime.of(2010,Month.JANUARY,1,0,0).minusDays(1), ZoneId.of("UTC")));
    // TODO est-ce que double suffit où c'est pas assez gros?
    final static double MILLIS_SEC_PER_HOUR = (1/(60*60*1000));
    final static double HOURS_PER_JULIAN_CENTURY = (1/24*36525);



    private Epoch(ZonedDateTime of) {

   }

    /**
     * TODO finir la méthode et comprendre quelle méthohde de ZoneDateTime utiliser, comment mettre l'unité?
     * Calcul le nombre de jours précis entre une des époque et le temps voulu
     *
     * @param when Moment de calcul(?!)
     *
     * @return Nombre de jours entre l'époque et le temps demandé
     */
   public  double daysUntil(ZonedDateTime when){
       // il y a pas besoin de faire un switch en fait
       // TODO il faut un moins car on veut de J2000/J2010 à when et pas de when à J...

       return -(when.until(this,ChronoUnit.MILLIS))* MILLIS_SEC_PER_HOUR;

       }


    /**
     * TODO Vérifier la méthode, me paraît trop petite - j'ai mis un moins devant pour les même raisons qu'avant
     * Calcul du nombre de siècle Julien entre l'époque et le moment voulu
     *
     * @param when moment à calculer
     *
     * @return Nombre précis de siècles Julien entre l'époque et le moment
     */
    public double julianCenturiesUntil(ZonedDateTime when){

       return -(when.until(this, ChronoUnit.MILLIS))*MILLIS_SEC_PER_HOUR*HOURS_PER_JULIAN_CENTURY;
    }
}

