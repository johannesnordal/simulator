import java.util.Comparator;
import java.util.PriorityQueue;

public class ShortestRemainingProcessing extends Scheduler {
    private Comparator<Client> cmp;
    private PriorityQueue<Client> pq;
    private Server server;

    public ShortestRemainingProcessing() {
        cmp = (x, y) -> {
            if (x.status() < y.status()) return -1;
            if (x.status() > y.status()) return 1;
            return 0;
        };
        pq = new PriorityQueue<Client>(cmp);
        server = new Server();
    }

    public void schedule(Client incoming) {
        registerArrival(incoming);
        pq.offer(incoming);
        server.running(pq.peek());
    }

    public void step(double nextStep) {
        double slice = server.slice(nextStep);    
        while (slice > 0.0) {
            server.step(slice);
            if (!server.isBusy()) swap();
            slice = server.slice(nextStep);
        }
    }

    private void swap() {
        Client running = pq.remove();
        registerDeparture(running);
        if (pq.isEmpty()) {
            server.running(null);
            return;
        }
        Client next = pq.peek();
        double wait = running.departure() - next.step();
        next.step(wait);
        next.waiting(wait);
        server.running(next);
    }

    public double work() {
        double work = 0.0;
        for (Client x : pq) work += x.status();
        return work;
    }

    public int active() {
        return pq.size();
    }
}
