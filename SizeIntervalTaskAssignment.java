import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.lang.StringBuilder;
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

    private static boolean coefficientOfVariationIsTooSmall(
            Distribution service) {
        double std = sqrt(service.getNumericalVariance());
        double mean = service.getNumericalMean();
        return std/mean < 0.001;
    }

    public static double[] split(int numberOfServers, Distribution service) {
        double[] interval = new double[numberOfServers - 1];
        if (coefficientOfVariationIsTooSmall(service)) {
            for (int i = 0; i < numberOfServers - 1; i++)
                interval[i] = service.getNumericalMean();
            return interval;
                
        }
        double rangeOfJobSize = service.getNumericalMean()/numberOfServers;
        double a = service.support()[0];
        for (int i = 0; i < numberOfServers - 1; i++) {
            double nthRange = rangeOfJobSize * (i + 1);
            DoubleUnaryOperator fn = x -> {
                return
                new Integrator(b -> b * service.density(b)).integrate(a, x) - nthRange;
            };    
            Bracket bracket = new Bracket(fn);
            BisectionSolver solver = new BisectionSolver(fn, bracket);
            OptionalDouble res = solver.solve();
            interval[i] = res.getAsDouble();
        }
        return interval;
    }

    public static void main(String[] args) {
        double[] cov = {0.001, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0};
        for (double x : cov) {
            double shape = Weibull.fitShapeToCoefficientOfVariation(x);
            double scale = Weibull.fitScaleToMeanAndShape(0.6, shape);
            Distribution dist = new Weibull(scale, shape);
            double[] interval = split(8, dist);
            for (double y : interval) {
                System.out.println(y);
            }
            System.out.println();
        }
    }
}
