package spool;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static spool.Stats.StatsBuilder;
import java.util.function.Supplier;
import java.util.function.DoubleUnaryOperator;
import java.util.OptionalDouble;
import java.util.Optional;

public class SITA extends Dispatcher {
    private double[] interval;

    public SITA(Supplier<Scheduler> scheduler, int n) {
        super(scheduler, n);
    }

    private SITA(Scheduler[] scheduler) {
        super(scheduler);
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

    private static boolean coefficientOfVariationIsTooSmall(Distribution service) {
        double std = sqrt(service.variance());
        double mean = service.mean();
        return std/mean < 0.001;
    }

    public static Optional<double[]> split(int numberOfServers, Distribution service) {
        if (coefficientOfVariationIsTooSmall(service))
            return Optional.empty();
        double[] interval = new double[numberOfServers - 1];
        double rangeOfJobSize = service.mean()/numberOfServers;
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
        return Optional.of(interval);
    }

    public void interval(double[] interval) {
        this.interval = interval;
    }

    public double[] interval() {
        return this.interval;
    }

    @Override
    public Stats[] simulate(Distribution arrival, Distribution service,
            int numberOfClients) {
        Optional<double[]> optional = split(scheduler.length, service);
        if (!optional.isPresent())
            return new RND(scheduler).simulate(arrival, service, numberOfClients);
        interval = optional.get();
        StatsBuilder[] builder = new StatsBuilder[scheduler.length];
        for (int i = 0; i < scheduler.length; i++)
            scheduler[i].registerObserver((builder[i] = new StatsBuilder()));
        sim(arrival, service, numberOfClients);
        Stats[] stats = new Stats[builder.length];
        for (int i = 0; i < stats.length; i++)
            stats[i] = builder[i].build();
        return stats;
    }
}
