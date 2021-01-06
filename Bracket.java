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
        // lower = calculateBound(x -> x < 0);
        // upper = calculateBound(x -> x > 0); 
    }

    private OptionalDouble calculateBound(DoublePredicate p) {
        DoubleUnaryOperator[] parameters = {
            x ->  x/1.0, x ->  1.0/x,
            // x -> -x/1.0, x -> -1.0/x
        };
        for (int i = 1; i <= MAX_ITER; i += 10) {
            for (DoubleUnaryOperator param : parameters) {
                double u = param.applyAsDouble(i);
                if (p.test(fn.applyAsDouble(u)))
                    return OptionalDouble.of(u);
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
        Distribution d = new BoundedPareto(0.3, 0.01, 2.0);
        double target = d.getNumericalMean()/2.0;
        DoubleUnaryOperator fn = x -> {
            return x * d.density(x) - target;
        };
        System.out.println(d.getNumericalMean());
        System.out.println(2 * d.density(2));
        System.out.println("target: " + target);
        /*
        double x = Double.parseDouble(args[0]);
        System.out.println(fn.applyAsDouble(x));
        System.out.println(fn.applyAsDouble(1.0/x));
        */
        Bracket bracket = new Bracket(fn);
        if (bracket.isBracketing()) {
            double lo = bracket.lower().getAsDouble();
            double hi = bracket.upper().getAsDouble();
            System.out.println("lo: " + lo);
            System.out.println("hi: " + hi);
            System.out.println("f(lo): " + fn.applyAsDouble(lo));
            System.out.println("f(hi): " + fn.applyAsDouble(hi));
        } else {
            System.out.println("Doesn't bracket.");
            if (bracket.lower().isPresent()) {
                double lo = bracket.lower().getAsDouble();
                System.out.println("lo: " + lo);
                System.out.println("f(lo): " + fn.applyAsDouble(lo));
            } else {
                System.out.println("Lower bound missing.");
            }
            if (bracket.upper().isPresent()) {
                double hi = bracket.upper().getAsDouble();
                System.out.println("hi: " + hi);
                System.out.println("f(hi): " + fn.applyAsDouble(hi));
            } else {
                System.out.println("Upper bound missing.");
            }
        }
    }
}
