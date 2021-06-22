package spool;

import java.util.Comparator;
import java.util.function.Supplier;
import java.util.function.Predicate;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;
import java.util.Optional;
import java.util.stream.Stream;

public class Job {
    private double step;
    private double status;
    final private double arrival;
    final private double service;
    private int id;

    public Job(double arrival, double service)
    {
        this(arrival, service, -1);
    }

    public Job(double arrival, double service, int id)
    {
        this.step = arrival;
        this.status = service;
        this.arrival = arrival;
        this.service = service;
        this.id = id;
    }

    private Job(double step, double status, double arrival,
            double service, int id)
    {
        this.step = step;
        this.status = status;
        this.arrival = arrival;
        this.service = service;
        this.id = id;
    }

    public Job clone()
    {
        return new Job(this.step, this.status, this.arrival, this.service,
                this.id);
    }

    public static Stream<Job> streamOf(Distribution arrival,
            Distribution service,
            int numberOfJobs)
    {
        Supplier<Job> job = new Supplier<>() {
            private double clock = 0.0;
            private int id = 0;
            public Job get() {
                clock += arrival.sample();
                return new Job(clock, service.sample(), id++);
            }
        };

        return Stream.generate(job).limit(numberOfJobs);
    }

    public void step(double slice)
    {
        if (isFinished()) return;
        if (slice <= 0)   return;
        step += slice;
    }

    public void status(double serviceSlice)
    {
        status -= serviceSlice;

        if (status < 0)
            status = 0;
    }

    public void finish()
    {
        status = 0.0;
    }

    public void id(int id)
    {
        this.id = id;
    }

    public double step()
    {
        return step;
    }

    public double status()
    {
        return status;
    }

    public boolean isFinished()
    {
        return Misc.compareDouble(status, 0.0);
    }

    public double arrival()
    {
        return arrival;
    }

    public double service()
    {
        return service;
    }

    public int id()
    {
        return id;
    }

    public String toString()
    {
        String id = "?";

        if (this.id != -1)
        {
            id = id.valueOf(this.id);
        }

        return new StringBuilder()
            .append("Job:    ").append(id)
            .append("\nArrival: ").append(arrival)
            .append("\nService: ").append(service)
            .append("\nStep:    ").append(step)
            .append("\nStatus:  ").append(status)
            .toString();
    }

    public static double waiting(Job x)
    {
        double w = response(x) - x.service();

        if (w < 0.0)
        {
            return 0.0;
        }

        return w;
    }

    public static double response(Job x)
    {
        return x.step() - x.arrival();
    }

    public static double slowdown(Job x)
    {
        return response(x) / x.service();
    }

    public static Comparator<Job> comparator(ToDoubleFunction<Job> fn)
    {
        Comparator<Job> cmp = (x, y) -> {
            if (fn.applyAsDouble(x) < fn.applyAsDouble(y)) return -1;
            if (fn.applyAsDouble(x) > fn.applyAsDouble(y)) return 1;
            return 0;
        };

        return cmp;
    }

    public static Comparator<Job> arrivalComparator()
    {
        return comparator(x -> x.arrival());
    }

    public static Comparator<Job> serviceComparator()
    {
        return comparator(x -> x.service());
    }

    public static Comparator<Job> stepComparator()
    {
        return comparator(x -> x.step());
    }

    public static Comparator<Job> statusComparator()
    {
        return comparator(x -> x.status());
    }

    // Total received service.
    public static double serviceAge(Job x)
    {
        return x.service() - x.status();
    }

    public static Comparator<Job> serviceAgeComparator()
    {
        return comparator(x -> serviceAge(x));
    }

    public static void main(String[] args)
    {
    }
}
