package ch.epfl.rigel.math;


public final class Angle {

    private Angle(){};

    final public static double TAU= 2*Math.PI;
    private static final double DEG_PER_RAD = 360.0 / TAU;
    private static final double RAD_PER_DEG = TAU / 360.0;
    private static final double RAD_PER_HR = TAU/24;
    private static final double HR_PER_RAD = 24/TAU;


    public static double normalizePositive(double rad) {

        RightOpenInterval roiNormal = RightOpenInterval.of(0,Math.PI);
        return roiNormal.reduce(rad);

    }

    public static  double ofArcsec(double sec){
        //on a considéré le cercle trigo en entier et pas [0,pi[
        return sec*TAU/(48*60*60);
    }
     public static double ofDMS (int deg, int min, double sec){
        if(!(min<60 && min>=0 )|| !(sec<60 && sec>=0)) {
            throw new IllegalArgumentException();
        } else {
            return ofDeg(deg+ (min+ (sec/60))/60);

        }
     }
    public static double ofDeg(double deg){
        return deg*RAD_PER_DEG;
    }
    public static double toDeg (double rad) {
        return rad*DEG_PER_RAD;
    }

    public static double ofHr (double hr){
        return hr*RAD_PER_HR;
    }
    public static double toHr (double rad){
        return rad*HR_PER_RAD;
    }





     }

