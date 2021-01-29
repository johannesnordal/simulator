package spool;

import static java.lang.Math.pow;
import static java.lang.Math.exp;
import static spool.Misc.factorial;

public class Exponential extends Distribution 
{
    private double scale;

    public Exponential(double scale) {
        this.scale = scale;
    }

    public double sample() {
        return (-Math.log(1 - rnd.nextDouble())) / scale;
    }

    protected double mgf(int m) {
        return Double.valueOf(Misc.factorial(m)) / pow(scale, m);
    }

    public double density(double x) {
        return scale * Math.exp(-scale * x);
    }
}
