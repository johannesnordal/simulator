/*
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.analysis.integration.*;
*/
import static java.lang.Math.pow;
import java.util.function.DoubleUnaryOperator;
import java.util.OptionalDouble;

public class SizeIntervalTaskAssignment extends Dispatcher {
    private Scheduler[] scheduler;
    private double[] interval;

    public SizeIntervalTaskAssignment(Scheduler[] scheduler,
            double[] interval) {
        this.scheduler = scheduler;
        this.interval = interval;
    }

    public void dispatch(Client incoming) {
        for (int i = 0; i < interval.length; i++) {
            if (incoming.status() < interval[i]) {
                scheduler[i].receive(incoming);
                return;
            }          
        }
        scheduler[interval.length].receive(incoming); 
    } 

    /*
    public static double[] split(int n, Distribution service) {
        double cov = pow(service.getNumericalMean(), 2)
            / service.getNumericalVariance();
        double[] interval = new double[n - 1];
        UnivariateFunction f = (x) -> {
            return x * service.density(x);
        };
        double rangeOfJobSize = service.getNumericalMean()/n;
        for (int i = 0; i < n - 1; i++) {
            double nthRange = rangeOfJobSize * (i + 1);
            UnivariateIntegrator ui = new SimpsonIntegrator();
            UnivariateFunction g = (x) -> {
                return ui.integrate(10000, f, 0, x) - nthRange;
            };
            UnivariateSolver solver = new BisectionSolver();
            double[] bracket =
                UnivariateSolverUtils.bracket(g, 1.0, 1.E-10, 10000);
            interval[i] = solver.solve(1000, g, bracket[0], bracket[1]);
        }
        return interval;
    }
    */

    public static double[] split(int n, Distribution service) {
        double[] interval = new double[n - 1];
        DoubleUnaryOperator f = (x) -> {
            return x * service.density(x);
        };
        double rangeOfJobSize = service.getNumericalMean()/n;
        for (int i = 0; i < n - 1; i++) {
            double nthRange = rangeOfJobSize * (i + 1);
            Integrator integrator = new Integrator(f);
            DoubleUnaryOperator g = (x) -> {
                return integrator.integrate(0, x) - nthRange;
            };
            Bracket bracket = new Bracket(g);
            if (!bracket.isBracketing()) System.err.println("Oh, snap.");
            BisectionSolver solver = new BisectionSolver(g, bracket);
            interval[i] = solver.solve().getAsDouble();
        }
        return interval;
    }
    public static void main(String[] args) {
        double shape = WeibullUtils.fitShapeToCoefficientOfVariation(0.5); 
        double scale = WeibullUtils.fitScaleToMeanAndShape(0.6, shape);
        Distribution service = new Weibull(scale, shape);
        double[] interval = SizeIntervalTaskAssignment.split(5, service);
        for (double x : interval)
            System.out.println(x);
    }
}
