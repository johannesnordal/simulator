import java.util.NoSuchElementException;
import org.apache.commons.math3.distribution.*;

public class ClientFactory {
    private RealDistribution arrival;
    private RealDistribution service;
    private double clock = 0.0;
    private long n = Long.MAX_VALUE;
    private long i = 0;

    public ClientFactory(RealDistribution arrival, RealDistribution service) {
        this.arrival = arrival;
        this.service = service;
    }

    public ClientFactory(RealDistribution arrival,
            RealDistribution service,
            int n) {
        this.arrival = arrival;
        this.service = service;
        this.n = n;
    }

    public boolean hasNext() {
        if (n == Long.MAX_VALUE) return true;
        return i < n;
    }

    public Client next() {
        if (!hasNext()) throw new NoSuchElementException();
        clock += arrival.sample();
        i++;
        return new Client(clock, service.sample());
    }
}
