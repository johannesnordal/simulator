package spool;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.function.Supplier;
import java.util.function.DoubleUnaryOperator;
import java.util.OptionalDouble;
import java.util.Optional;

public class SITA extends Dispatcher
{
    private double[] interval;

    public SITA(Supplier<Scheduler> scheduler, double[] interval)
    {
        super(scheduler, interval.length + 1);
        this.interval = interval;
    }

    public SITA(Scheduler[] scheduler, double[] interval)
    {
        super(scheduler);
        this.interval = interval;
    }

    public SITA(Supplier<Scheduler> scheduler, int n)
    {
        super(scheduler, n);
    }

    public SITA(Scheduler[] scheduler)
    {
        super(scheduler);
    }

    public void dispatch(Client incoming)
    {
        for (int i = 0; i < interval.length; i++)
        {
            if (incoming.status() < interval[i])
            {
                scheduler[i].receive(incoming);
                return;
            }          
        }

        scheduler[interval.length].receive(incoming); 
    } 

    @Override
    public boolean requiresSpecialInitialization()
    {
        return true;
    }

    private static boolean coefficientOfVariationIsTooSmall(Distribution service)
    {
        double std = sqrt(service.variance());
        double mean = service.mean();

        return std/mean < 0.001;
    }

    public static Optional<double[]> split(int numberOfServers, Distribution service)
    {
        if (coefficientOfVariationIsTooSmall(service))
        {
            return Optional.empty();
        }

        double[] interval = new double[numberOfServers - 1];
        double rangeOfJobSize = service.mean()/numberOfServers;
        double a = service.support()[0];

        for (int i = 0; i < numberOfServers - 1; i++)
        {
            double nthRange = rangeOfJobSize * (i + 1);

            DoubleUnaryOperator fn = x -> {
                return new Integrator(b -> {
                    return b * service.density(b);
                }).integrate(a, x) - nthRange;
            };    

            Bracket bracket = new Bracket(fn);

            if (!bracket.isBracketing())
            {
                return Optional.empty();
            }

            BisectionSolver solver = new BisectionSolver(fn, bracket);
            OptionalDouble res = solver.solve();

            if (!res.isPresent())
            {
                return Optional.empty();
            }

            interval[i] = res.getAsDouble();
        }

        return Optional.of(interval);
    }

    public void interval(double[] interval)
    {
        this.interval = interval;
    }

    public double[] interval()
    {
        return this.interval;
    }

    @Override
    public Stats simulate(Distribution arrival,
            Distribution service,
            int numberOfClients)
    {
        Optional<double[]> optional = split(scheduler.length, service);

        if (!optional.isPresent())
        {
            return new RND(scheduler).simulate(arrival, service, numberOfClients);
        }

        interval = optional.get();
        Stats.Builder[] builder = new Stats.Builder[scheduler.length];

        for (int i = 0; i < scheduler.length; i++)
        {
            scheduler[i].registerObserver((builder[i] = new Stats.Builder()));
        }

        sim(arrival, service, numberOfClients);
        Stats[] stats = new Stats[builder.length];

        for (int i = 0; i < stats.length; i++)
        {
            stats[i] = builder[i].build();
        }

        return Stats.merge(stats);
    }

    @Override
    public void simulate(Observer[] observer,
            Distribution arrival,
            Distribution service,
            int numberOfClients)
    {
        Optional<double[]> optional = split(scheduler.length, service);

        if (!optional.isPresent())
        {
            new RND(scheduler).simulate(observer, arrival, service, numberOfClients);

            return;
        }

        interval = optional.get();

        for (int i = 0; i < scheduler.length; i++)
        {
            scheduler[i].registerObserver(observer[i]);
        }

        sim(arrival, service, numberOfClients);
    }

    public String toString()
    {
        return "Size Interval Task Assignment";
    }

    public static void main(String[] args)
    {
        Dispatcher[] dispatcher = new Dispatcher[5]; 

        int m = 4;
        dispatcher[0] = new JSQ(FCFS::new, m);
        dispatcher[1] = new LWL(FCFS::new, m);
        dispatcher[2] = new RND(FCFS::new, m);
        dispatcher[3] = new RR(FCFS::new, m);

        Optional<double[]> interval = SITA.split(m, new Exponential(2.0));

        if (!interval.isPresent())
        {
            dispatcher[4] = new RND(FCFS::new, m);
        }
        else
        {
            dispatcher[4] = new SITA(FCFS::new, interval.get());
        }

        for (Dispatcher x : dispatcher)
        {
            if (x.requiresSpecialInitialization())
            {
                System.out.println(x + " needs special initalization.");
            }
            else
            {
                System.out.println(x + " can be initialized normally.");
            }
        }
    }
}
