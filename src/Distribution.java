package spool;

import static java.lang.Math.pow;
import java.util.Random;

public abstract class Distribution {
    protected Random rnd;
    protected long seed;

    public abstract double sample();
    public abstract double density(double x);
    protected abstract double mgf(int m);

    public Distribution() {
        rnd = new Random();
    }

    public Distribution seed(long seed) {
        rnd.setSeed(seed);
        return this;
    }

    public long seed() {
        return seed;
    }

    public double getNumericalMean() {
        return mgf(1);
    }

    public double getNumericalVariance() {
        return mgf(2) - pow(mgf(1), 2);
    }

    public double[] support() {
        // return new double[] {1.0E-100, Double.POSITIVE_INFINITY};
        return new double[] {1.0E-50, Double.POSITIVE_INFINITY};
        // return new double[] {0.0, Double.POSITIVE_INFINITY};
    }
}
