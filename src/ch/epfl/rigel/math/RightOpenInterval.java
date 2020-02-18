package ch.epfl.rigel.math;

import java.util.Locale;

import static java.lang.Math.floor;

public final class RightOpenInterval extends Interval{

    private RightOpenInterval(double low, double high){
        super(low,high);
    }

    public static RightOpenInterval of(double low, double high) {

        RightOpenInterval roi1 = new RightOpenInterval(low,high);

        if (low >= high) {
            throw new IllegalArgumentException();
        } else {
            return roi1;
        }
    }

    public static RightOpenInterval symmetric (double size) {

        if (size <= 0) {
            throw new IllegalArgumentException();
        } else {
            RightOpenInterval roi2 = new RightOpenInterval(-size / 2, size / 2);
            return roi2;

        }
    }

    @Override
    public boolean contains(double v) {
        if (v >= low() && v < high() ){
            return true;
        } else {
            return false;
        }
    }
    public double reduce(double v){
        return low() + (v-low()) - (size()*floor((v-low())/size()));
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%.2f , %.2f[",low(),high());


    }




}
