import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
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

    public static double[] split(int n, Distribution service) {
        double[] interval = new double[n - 1];
        double rangeOfJobSize = service.getNumericalMean()/n;
        for (int i = 0; i < n - 1; i++) {
            double nthRange = rangeOfJobSize * (i + 1);
            DoubleUnaryOperator fn = x -> {
                return
                new Integrator(a -> a * service.density(a)).integrate(0, x) - nthRange;
            };    
            Bracket bracket = new Bracket(fn);
            BisectionSolver solver = new BisectionSolver(fn, bracket);
            interval[i] = solver.solve().getAsDouble();
        }
        return interval;
    }
    public static void main(String[] args) {
        double shape = WeibullUtils.fitShapeToCoefficientOfVariation(0.001); 
        double scale = WeibullUtils.fitScaleToMeanAndShape(0.6, shape);
        Distribution service = new Weibull(scale, shape);
        double[] interval = SizeIntervalTaskAssignment.split(4, service);
        for (double x : interval)
            System.out.println(x);
    }
}
