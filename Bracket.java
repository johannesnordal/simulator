import java.util.function.DoubleUnaryOperator;
import java.util.function.DoublePredicate;
import java.util.OptionalDouble;
import static java.lang.Math.abs;

public class Bracket {
    private final int MAX_ITER = 10000;
    private DoubleUnaryOperator fn;
    private OptionalDouble lower;
    private OptionalDouble upper;

    public Bracket(DoubleUnaryOperator fn) {
        this.fn = fn;
        lower = calculateBound(x -> x <= 0);
        upper = calculateBound(x -> x >= 0); 
    }

    private OptionalDouble calculateBound(DoublePredicate p) {
        DoubleUnaryOperator[] parameters = {
            x ->  x/1.0, x ->  1.0/x,
            x -> -x/1.0, x -> -1.0/x
        };
        for (int i = 1; i <= MAX_ITER; i += 10) {
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
        double shape = Weibull.fitShapeToCoefficientOfVariation(0.001);
        double scale = Weibull.fitScaleToMeanAndShape(0.6, shape);
        System.out.println(shape);
        System.out.println(scale);
        Distribution dist = new Weibull(scale, shape);
        System.out.println(dist.getNumericalMean());
    }
}
