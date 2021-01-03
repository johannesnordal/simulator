import java.util.function.DoubleUnaryOperator;
import java.util.OptionalDouble;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class BisectionSolver {
    private final int MAX_ITER = 10000;
    private DoubleUnaryOperator fn;
    private double lower;
    private double upper;

    public BisectionSolver(DoubleUnaryOperator fn, double lower,
            double upper) {
        this.fn = fn;
        this.lower = lower;
        this.upper = upper;
    }

    public BisectionSolver(DoubleUnaryOperator fn, Bracket bracket) {
        this.fn = fn;
        this.lower = bracket.lower().getAsDouble();
        this.upper = bracket.upper().getAsDouble();
    }

    private boolean oppositeSign(double x, double y) {
        return
        signum(fn.applyAsDouble(x)) + signum(fn.applyAsDouble(y)) == 0;
    }

    public OptionalDouble solve() {
        double mid;
        for (int i = 0; i < MAX_ITER; i++) {
            mid = (upper + lower)/2.0;
            if (abs(fn.applyAsDouble(mid)) < 1.0E-4) {
                return OptionalDouble.of(mid);
            }
            if (oppositeSign(lower, mid))
                upper = mid;
            else
                lower = mid;
        }
        return OptionalDouble.empty();
    }

    public static void main(String[] args) {
        DoubleUnaryOperator fn = x -> (x * x) - 5;
        Bracket bracket = new Bracket(fn);
        BisectionSolver solver = new BisectionSolver(fn, bracket);
        System.out.println(solver.solve().getAsDouble());
    }
}
