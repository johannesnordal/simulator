package spool;

import static java.lang.Math.pow;

public class Uniform extends Distribution
{
    private double lower = 0.0;
    private double upper = 1.0;

    public Uniform() { }

    public Uniform(double upper)
    {
        this.upper = upper;
    }

    public Uniform(double lower, double upper)
    {
        this.lower = lower;
        this.upper = upper;
    }

    public double sample()
    {
        return lower + rnd.nextDouble() * (upper - lower);
    }

    protected double mgf(int m)
    {
        double n = Double.valueOf(m);
        double x = pow(upper, n + 1) - pow(lower, n + 1);
        double y = (n + 1) * (upper - lower);

        return x / y;
    }

    public double density(double x)
    {
        return 1.0 / (upper - lower);
    }

    @Override
    public double[] support()
    {
        return new double[] {lower, upper};
    }
}
