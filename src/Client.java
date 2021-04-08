package spool;

import java.util.Comparator;
import java.util.function.Supplier;
import java.util.function.Predicate;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;
import java.util.Optional;
import java.util.stream.Stream;

public class Client {
    private double step;
    private double status;
    private double arrival;
    private double service;
    private double waiting;
    private int id;

    public Client(double arrival, double service)
    {
        this(arrival, service, -1);
    }

    public Client(double arrival, double service, int id)
    {
        this.step = arrival;
        this.status = service;
        this.arrival = arrival;
        this.service = service;
        this.waiting = 0.0;
        this.id = id;
    }

    private Client(double step, double status, double arrival,
            double service, double waiting, int id)
    {
        this.step = step;
        this.status = status;
        this.arrival = arrival;
        this.service = service;
        this.waiting = waiting;
        this.id = id;
    }

    public Client clone()
    {
        return new Client(this.step, this.status, this.arrival, this.service,
                this.waiting, this.id);
    }

    public static Stream<Client> streamOf(Distribution arrival,
            Distribution service,
            int numberOfClients)
    {
        Supplier<Client> client = new Supplier<>() {
            private double clock = 0.0;
            private int id = 0;
            public Client get() {
                clock += arrival.sample();
                return new Client(clock, service.sample(), id++);
            }
        };

        return Stream.generate(client).limit(numberOfClients);
    }

    public void step(double slice)
    {
        if (slice <= 0) return;
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
        return status <= 0.0;
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
            .append("Client:    ").append(id)
            .append("\nArrival: ").append(arrival)
            .append("\nService: ").append(service)
            .append("\nStep:    ").append(step)
            .append("\nStatus:  ").append(status)
            .toString();
    }

    public static double waiting(Client x)
    {
        double w = response(x) - x.service();

        if (w < 0.0)
        {
            return 0.0;
        }

        return w;
    }

    public static double response(Client x)
    {
        return x.step() - x.arrival();
    }

    public static double slowdown(Client x)
    {
        return response(x) / x.service();
    }

    public static Comparator<Client> comparator(ToDoubleFunction<Client> fn)
    {
        Comparator<Client> cmp = (x, y) -> {
            if (fn.applyAsDouble(x) < fn.applyAsDouble(y)) return -1;
            if (fn.applyAsDouble(x) > fn.applyAsDouble(y)) return 1;
            return 0;
        };

        return cmp;
    }

    public static Comparator<Client> arrivalComparator()
    {
        return comparator(x -> x.arrival());
    }

    public static Comparator<Client> serviceComparator()
    {
        return comparator(x -> x.service());
    }

    public static Comparator<Client> stepComparator()
    {
        return comparator(x -> x.step());
    }

    public static Comparator<Client> statusComparator()
    {
        return comparator(x -> x.status());
    }

    // Total received service.
    public static double serviceAge(Client x)
    {
        return x.service() - x.status();
    }

    public static Comparator<Client> serviceAgeComparator()
    {
        return comparator(x -> serviceAge(x));
    }

    public static void main(String[] args)
    {
        int m = 4;

        Stats.Builder[] builder = Stream.generate(Stats.Builder::new)
            .limit(m)
            .toArray(Stats.Builder[]::new);

        Dispatcher dispatcher = new LWL(SJF::new, m);
        dispatcher.register(builder);

        int n = 10_000_000;
        Distribution arrival = new Exponential(m);
        Distribution service = new Exponential(2);
        streamOf(arrival, service, n).forEach(dispatcher::dispatch);

        Stream.of(builder)
            .map(x -> x.build())
            .map(x -> x.response().first())
            .forEach(System.out::println);
    }
}
