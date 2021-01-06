import java.util.ArrayDeque;

public class FirstComeFirstServe extends Scheduler {
    private ArrayDeque<Client> pq;
    private Server server;
    private double work;

    public FirstComeFirstServe() {
        pq = new ArrayDeque<Client>();
        server = new Server();
    }

    public static FirstComeFirstServe[] arrayOf(int n) {
        FirstComeFirstServe[] a = new FirstComeFirstServe[n];
        for (int i = 0; i < n; i++) {
            a[i] = new FirstComeFirstServe();
        }
        return a;
    }

    public void schedule(Client incoming) {
        registerEvent(Event.ARRIVAL, incoming);
        if (server.running() == null)
            server.running(incoming);
        else
            pq.offer(incoming);
    }

    public void step(double nextStep) {
        double slice = server.slice(nextStep);
        while (slice > 0.0) {
            server.step(slice);
            if (!server.isBusy())
                swap(pq.poll());
            slice = server.slice(nextStep);
        }
    }

    private void swap(Client next) {
        Client running = server.running();
        registerEvent(Event.DEPARTURE, running);
        if (next == null) {
            server.running(null);
            return;
        }
        double wait = running.departure() - next.step();
        next.step(wait);
        // next.waiting(wait);
        server.running(next);
    }

    public int active() {
        return server.running() == null ? 0 : pq.size() + 1;
    }

    public double work() {
        if (server.running() == null) return 0.0;
        double work = server.running().status();
        for (Client x : pq) work += x.status();
        return work;
    }

    public static void main(String[] args) {
        Simulator simulator = new FirstComeFirstServe();
        Stats stats = simulator.simulate(
                new Exponential(1.0),
                new Exponential(2.0),
                10000000);
        System.out.println(stats.waiting().first());
        System.out.println(stats.response().first());
    }
}
