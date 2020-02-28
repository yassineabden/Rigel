package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates,EquatorialCoordinates> {

        private final double cosOblik;
        private final double sinOblik;
        private final double oblik;
        private final Polynomial obliquity;


        public EclipticToEquatorialConversion(ZonedDateTime when){
        this.obliquity = Polynomial.of(Angle.ofArcsec(0.00181),-Angle.ofArcsec(0.0006),-Angle.ofArcsec(46.815)+
                Angle.ofDMS(23,26,21.45));
        this.oblik = obliquity.at(Epoch.J2000.julianCenturiesUntil(when));
        this.cosOblik= Math.cos(oblik);
        this.sinOblik= Math.cos(oblik); }

        @Override
        public EquatorialCoordinates apply(EclipticCoordinates ecl){
            double alpha= Math.atan2(Math.sin(ecl.lon())* cosOblik- Math.tan(ecl.lat())*sinOblik,Math.cos(ecl.lon()));
            double delta= Math.asin(Math.sin(ecl.lat())*cosOblik+Math.cos(ecl.lat())*sinOblik*Math.sin(ecl.lat()));
            return EquatorialCoordinates.of(alpha,delta); }

        @Override
        public final boolean equals(Object obj) {
        super.equals(obj);
        throw new UnsupportedOperationException(); }

        @Override
        public final int hashCode (){
        super.hashCode();
        throw new UnsupportedOperationException(); }





























}











