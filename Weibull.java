import static java.lang.Math.pow;
import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;
import java.util.function.DoubleUnaryOperator;
import java.util.OptionalDouble;

public class Weibull extends Distribution {
    private double scale;
    private double shape;

    public Weibull(double scale, double shape) {
        this.scale = scale;
        this.shape = shape;
    }

    public double sample() {
        double r = rnd.nextDouble();
        return scale * pow((-log(1 - r)), (1.0/shape));
    }

    public double density(double x) {
        if (x < 0) return 0;
        final double xscale = x/scale;
        final double xscalepow = pow(xscale, shape-1);
        final double xscalepowshape = xscalepow * xscale;
        return (shape/scale) * xscalepow * exp(-xscalepowshape);
    }

    protected double mgf(int m) {
        double moment = Double.valueOf(m);
        return Math.pow(scale, moment) * Misc.gamma(1 + (moment/shape));
    }    

    protected static double coefficientOfVariation(double shape) {
        return
        sqrt(
            (Misc.gamma((2 + shape)/shape) / pow(Misc.gamma(1 + 1/shape), 2)) - 1
        );
    }

    public static double fitShapeToCoefficientOfVariation(double cov) {
        if (cov <= 0.0) return 1.0E20;
        DoubleUnaryOperator fn = (shape) -> {
            return coefficientOfVariation(shape) - cov;
        };
        Bracket bracket = new Bracket(fn);
        BisectionSolver solver = new BisectionSolver(fn, bracket);
        return solver.solve().getAsDouble();
    }

    public static double fitScaleToMeanAndShape(double mean, double shape) {
        return mean / Misc.gamma(1 + (1/shape));
    }
}
