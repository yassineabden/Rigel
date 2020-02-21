package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

public final class ClosedInterval extends Interval {


    private ClosedInterval(double low, double high) {
        super(low, high);
    }

    public static ClosedInterval of(double low, double high) {
        Preconditions.checkArgument(low<high);
            ClosedInterval ci1 = new ClosedInterval(low,high);

            return ci1;
        }


    public static ClosedInterval symmetric (double size) {

        Preconditions.checkArgument(size>0);
            ClosedInterval ci2 = new ClosedInterval(-size / 2, size / 2);
            return ci2;

        }


    @Override
    public boolean contains(double v) {
        if (v >= low() && v <= high()) {
            return true;
        } else {
            return false;
        }
    }

    public double clip (double v) {
        if (v>=high()) {
            v=high(); }
        if (v<=low()) {
            v=low(); }
      return v;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,"[%.2f , %.2f]",low(),high());


    }














        }

















