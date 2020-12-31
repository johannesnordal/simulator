import java.util.NoSuchElementException;

public class ClientFactory {
    private Distribution arrival;
    private Distribution service;
    private double clock = 0.0;
    private long n = Long.MAX_VALUE;
    private long i = 0;

    public ClientFactory(Distribution arrival, Distribution service) {
        this.arrival = arrival;
        this.service = service;
    }

    public ClientFactory(Distribution arrival, Distribution service, int n) {
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
        clock += arrival.draw();
        i++;
        return new Client(clock, service.draw());
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        JoinShortestQueue jsq = new JoinShortestQueue(
            FirstComeFirstServe.arrayOf(n));
        ClientFactory client = new ClientFactory(
            new Exponential(1.0), 
            new Exponential(2.0),
            10000000);
        while (client.hasNext()) {
            jsq.dispatch(client.next());
        }
    }
}
