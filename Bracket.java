import java.util.OptionalDouble;
import java.util.function.DoubleUnaryOperator;
import java.util.function.DoublePredicate;
import java.util.OptionalDouble;
import static java.lang.Math.abs;

public class Bracket {
    private final int MAX_ITER = 1000;
    private DoubleUnaryOperator fn;
    private OptionalDouble lower;
    private OptionalDouble upper;

    public Bracket(DoubleUnaryOperator fn) {
        this.fn = fn;
        lower = calculateBound(x -> x <= 0, 1.0);
        upper = calculateBound(x -> x >= 0, 1.0); 
    }

    public Bracket(DoubleUnaryOperator fn, double target) {
        this.fn = fn;
        lower = calculateBound(x -> x <= target, target);
        upper = calculateBound(x -> x >= target, target); 
    }

    private OptionalDouble calculateBound(DoublePredicate p, double target) {
        DoubleUnaryOperator[] parameters = {
            x ->  x/target, x ->  target/x,
            x -> -x/target, x -> -target/x
        };
        for (int i = 1; i <= MAX_ITER; i++) {
            for (DoubleUnaryOperator param : parameters) {
                double x = param.applyAsDouble(i);
                if (p.test(fn.applyAsDouble(x)))
                    return OptionalDouble.of(x);
            }
        }
        return OptionalDouble.empty();
    }

    public boolean isBracketing() {
        return lower.isPresent() && upper.isPresent();
    }

    public OptionalDouble lower() {
        return lower;
    }

    public OptionalDouble upper() {
        return upper;
    }

    public static void main(String[] args) {
        double shape = WeibullUtils.fitShapeToCoefficientOfVariation(0.5);
        double scale = WeibullUtils.fitScaleToMeanAndShape(0.6, shape);
        Distribution service = new Weibull(scale, shape);
        int i = Integer.parseInt(args[0]);
        double target = i * service.getNumericalMean()/8.0;
        DoubleUnaryOperator fn = x -> x * service.density(x);
        Bracket bracket = new Bracket(fn, target);
        System.out.println(bracket.lower());
        System.out.println(bracket.upper());
    }
}
