package spool;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.DoubleUnaryOperator;
import java.util.OptionalDouble;
import java.util.Optional;

public class SITA extends Dispatcher
{
    private Receiver[] receiver;
    private double[] interval;

    public SITA(Receiver[] receiver, double[] interval)
    {
        this.receiver = receiver;
        this.interval = interval;
    }

    public static Supplier<Dispatcher> getPrototype(
            List<Supplier<Receiver>> supplierList, Distribution service)
    {
        Supplier<Dispatcher> supplier = () -> {
            double [] interval = SITA.split(supplierList.size(), service);
            Receiver[] receiver = new Receiver[supplierList.size()];
            for (int i = 0; i < supplierList.size(); i++)
            {
                receiver[i] = supplierList.get(i).get();
            }
            return new SITA(receiver, interval);
        };

        return supplier;
    }

    public void dispatch(Job incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        for (int i = 0; i < interval.length; i++)
        {
            if (incoming.status() < interval[i])
            {
                receiver[i].receive(incoming);
            }          
        }

        receiver[interval.length].receive(incoming); 
    } 

    private static boolean coefficientOfVariationIsTooSmall(Distribution service)
    {
        double std = sqrt(service.variance());
        double mean = service.mean();

        return std/mean < 0.001;
    }

    public static double[] split(int numberOfServers, Distribution service)
    {
        if (coefficientOfVariationIsTooSmall(service))
        {
            return new double[0];
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
                return new double[0];
            }

            BisectionSolver solver = new BisectionSolver(fn, bracket);
            OptionalDouble res = solver.solve();

            if (!res.isPresent())
            {
                return new double[0];
            }

            interval[i] = res.getAsDouble();
        }

        return interval;
    }

    public double[] interval()
    {
        return this.interval;
    }

    public String toString()
    {
        return "Size Interval Task Assignment";
    }
}
