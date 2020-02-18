package ch.epfl.rigel.math;

public final class Angle {

    private Angle(){};


    final public static double TAU= 2*Math.PI;
    public static double normalizePositive(double rad) {
       //on peut pas instancié car privé mais comment capter le methodes de construction?!
        RightOpenInterval roinormal = new RightOpenInterval(0, Math.PI);

        return roinormal.reduce(rad);

    }

    public static  double ofArcsec(double sec){
        //on a considéré le cercle trigo en entier et pas [0,pi[
        return sec*TAU/(48*60*60);
    }
}
