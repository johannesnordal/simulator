package spool;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.function.Supplier;
import java.util.function.DoubleUnaryOperator;
import java.util.OptionalDouble;
import java.util.Optional;

public class SITA extends Dispatcher
{
    public static class Builder extends AbstractBuilder<SITA>
    {
        private double[] interval;

        public Builder(Node[] node, double[] interval)
        {
            this.interval = interval;
            this.node = node;
        }

        public SITA build()
        {
            return new SITA(this);
        }
    }

    private double[] interval;

    private SITA(Builder builder)
    {
        super(builder);
        this.interval = builder.interval;
    }

    private void handle(StatusCode code, Client client)
    {
        switch(code)
        {
            case BLOCK:
                registerEvent(Event.BLOCK, client);
                break;

            default:
                break;
        }
    }

    public StatusCode receive(Client incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        for (int i = 0; i < interval.length; i++)
        {
            if (incoming.status() < interval[i])
            {
                StatusCode code = node[i].receive(incoming);

                if (code != StatusCode.ACCEPT)
                {
                    handle(code, incoming);
                }

                return StatusCode.ACCEPT;
            }          
        }

        node[interval.length].receive(incoming); 

        return StatusCode.ACCEPT;
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
