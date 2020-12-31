import java.util.Queue;
import java.util.LinkedList;

public class FirstComeFirstServe extends Scheduler {
    private Queue<Client> pq;
    private Server server;
    private double work;

    public FirstComeFirstServe() {
        pq = new LinkedList<Client>();
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
        registerArrival(incoming);
        if (server.running() == null)
            server.running(incoming);
        else
            pq.offer(incoming);
    }

    public void step(double nextStep) {
        var slice = server.slice(nextStep);
        while (slice > 0.0) {
            server.step(slice);
            if (!server.isBusy())
                swap(pq.poll());
            slice = server.slice(nextStep);
        }
    }

    private void swap(Client next) {
        Client running = server.running();
        registerDeparture(running);
        if (next == null) {
            server.running(null);
            return;
        }
        var wait = running.departure() - next.step();
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
        int n = Integer.parseInt(args[0]);
        Distribution X = new Exponential(2.0);
        Distribution Y = new Exponential(1.0);
        Stats stats = new Stats();
        Scheduler scheduler = new FirstComeFirstServe();
        scheduler.observer(stats);
        double clock = 0.0;
        for (int i = 0; i < n; i++) {
            clock += Y.draw();
            scheduler.step(clock);
            scheduler.schedule(new Client(clock, X.draw()));
        }
        System.out.println(stats.waiting().first());
        System.out.println(stats.response().first());
    }
}
