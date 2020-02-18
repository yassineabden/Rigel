package ch.epfl.rigel.math;

import java.util.Locale;

public final class ClosedInterval extends Interval {


    private ClosedInterval(double low, double high) {
        super(low, high);
    }

    public static ClosedInterval of(double low, double high) {

        ClosedInterval ci1 = new ClosedInterval(low,high);

        if (low >= high) {
        throw new IllegalArgumentException();
        } else {
            return ci1;
        }
    }

    public static ClosedInterval symmetric (double size) {

        if (size <= 0) {
            throw new IllegalArgumentException();
        } else {
            ClosedInterval ci2 = new ClosedInterval(-size / 2, size / 2);
            return ci2;

        }
    }

    @Override
    public boolean contains(double v) {
        if (v >= high() && v <= low()) {
            return false;
        } else {
            return true;
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

















