package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Classe de conversion de coordonées ecliptiques en coordonées équatoriales
 * @author Yassine Abdennadher (299273)
 * @author Juliette Aerni (296670)
 */

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates,EquatorialCoordinates> {

        private final double cosOblik;
        private final double sinOblik;
        private final double oblik;
        private final Polynomial obliquity;

    /**
     * Constructeurs de coordonées , définit le moment d'observation pour la conversion
     * @param when moment d'observation pour la conversion
     */
        public EclipticToEquatorialConversion(ZonedDateTime when){
        this.obliquity = Polynomial.of(Angle.ofArcsec(0.00181),-Angle.ofArcsec(0.0006),-Angle.ofArcsec(46.815),
                Angle.ofDMS(23,26,21.45));
        this.oblik = obliquity.at(Epoch.J2000.julianCenturiesUntil(when));
        this.cosOblik= Math.cos(oblik);
        this.sinOblik= Math.sin(oblik); }

    /**
     * Effectue la conversion de coordonées ecliptiques en coordonées équatoriales
     *
     * @param ecl les coordonnées ecliptiques à converser
     * @return les coordonées conversées en coordonées équatoriales
     */
    @Override
        public EquatorialCoordinates apply(EclipticCoordinates ecl){
            double alpha= Angle.normalizePositive(Math.atan2(Math.sin(ecl.lon())* cosOblik- Math.tan(ecl.lat())*sinOblik,Math.cos(ecl.lon())));
            double delta= Math.asin(Math.sin(ecl.lat())*cosOblik+Math.cos(ecl.lat())*sinOblik*Math.sin(ecl.lon())) ;
            System.out.println(Angle.toDeg(delta));
            return EquatorialCoordinates.of(alpha,delta); }

    /**
     * TODO Vérifer
     * @param obj
     * @return
     */
    @Override
        public final boolean equals(Object obj) {
            super.equals(obj);
            throw new UnsupportedOperationException(); }

    /**
     * TODO Vérifer
     * @return
     */
    @Override
    public final int hashCode (){
        super.hashCode();
        throw new UnsupportedOperationException(); }





























}











