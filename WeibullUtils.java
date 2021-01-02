import java.util.function.DoubleUnaryOperator;
import java.util.OptionalDouble;
import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

public class WeibullUtils {
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

    public static void main(String[] args) {
        double[] cov = {0.5, 1.0, 1.5, 2.0, 2.5, 3.0};
        for (double x : cov) {
            double shape = fitShapeToCoefficientOfVariation(x);
            double scale = fitScaleToMeanAndShape(0.6, shape);
            Distribution service = new Weibull(scale, shape);
            System.out.println(service.getNumericalMean());
        }
    }
}
