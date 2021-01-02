import java.util.Queue;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Comparator;

public class ProcessorSharing extends Scheduler {
    private Comparator<Client> cmp;
    private Queue<Client> pq;
    private Server server;
    private double speed = 1.0;

    public ProcessorSharing() {
        cmp = (x, y) -> {
            if (x.status() < y.status()) return -1;
            if (x.status() > y.status()) return 1;
            return 0;
        };
        pq = new PriorityQueue<Client>(cmp);
        server = new Server();
    }

    public void schedule(Client incoming) {
        registerEvent(Event.ARRIVAL, incoming);
        pq.add(incoming);
    }

    private double interStep(double nextStep) {
        server.speed(speed * (1.0/pq.size()));
        server.running(pq.peek());
        server.step(server.slice(nextStep));
        if (server.running().isFinished())
            return server.running().departure();
        return nextStep;
    }

    public void step(double nextStep) {
        if (pq.size() == 0) return;
        double interStep = interStep(nextStep);
        while (interStep < nextStep) {
            registerEvent(Event.DEPARTURE, pq.remove());
            server.speed(speed * (1.0/(pq.size() + 1)));
            for (Client running : pq) {
                server.running(running);
                server.step(server.slice(interStep));
            }
            if (pq.size() == 0) return;
            interStep = interStep(nextStep);
        }
        Iterator<Client> running = pq.iterator();
        if (running.hasNext()) running.next();
        server.speed(speed * 1.0/pq.size());
        while (running.hasNext()) {
            server.running(running.next());
            server.step(server.slice(nextStep));
        }
    }

    public double work() {
        double work = 0.0;
        for (Client x : pq) work += x.status();
        return work;
    }

    public int active() {
        return pq.size();
    }

    public void speed(double speed) {
        this.speed = speed;
    }
}
