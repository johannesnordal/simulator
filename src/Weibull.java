package spool;

import static java.lang.Math.pow;
import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;
import static spool.Misc.gamma;
import java.util.function.DoubleUnaryOperator;

public class Weibull extends Distribution
{
    private double scale;
    private double shape;

    public Weibull(double scale, double shape)
    {
        this.scale = scale;
        this.shape = shape;
    }

    public static Distribution fitToMeanAndCV(double mean, double cov)
    {
        if (cov == 0)
        {
            return new Constant(mean);
        }

        double shape = fitShapeToCoefficientOfVariation(cov);
        double scale = fitScaleToMeanAndShape(mean, shape);

        return new Weibull(scale, shape);
    }

    public double sample()
    {
        double r = rnd.nextDouble();
        return scale * pow((-log(1 - r)), (1.0/shape));
    }

    public double density(double x)
    {
        if (x < 0) return 0;

        final double xscale = x/scale;
        final double xscalepow = pow(xscale, shape-1);
        final double xscalepowshape = xscalepow * xscale;

        return (shape/scale) * xscalepow * exp(-xscalepowshape);
    }

    protected double mgf(int m)
    {
        double moment = Double.valueOf(m);

        return pow(scale, moment) * gamma(1 + (moment/shape));
    }

    public static double coefficientOfVariation(double shape)
    {
        return
        sqrt((gamma((2 + shape)/shape) / pow(gamma(1 + 1/shape), 2)) - 1);
    }

    public static double fitShapeToCoefficientOfVariation(double cv)
    {
        if (cv <= 0.0) return 1.0E20;

        DoubleUnaryOperator fn = shape -> coefficientOfVariation(shape) - cv;

        return new BisectionSolver(fn, new Bracket(fn)).solve().getAsDouble();
    }

    public static double fitScaleToMeanAndShape(double mean, double shape)
    {
        return mean / gamma(1 + (1/shape));
    }

    public static void main(String[] args) {
        Distribution wei = Weibull.fitToMeanAndCV(1.0, 0.5);
        System.out.println(wei.mean());
        System.out.println(wei.variance());
        System.out.println(wei.coefficientOfVariation());
    }
}
