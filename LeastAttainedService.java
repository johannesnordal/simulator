import java.util.Comparator;
import java.util.PriorityQueue;

public class LeastAttainedService extends Scheduler {
    private Comparator<Client> cmp;
    private PriorityQueue<Client> pq;
    private Server server;
    private double clock = 0.0;

    public LeastAttainedService() {
        cmp = (x, y) -> {
            double a = x.service() - x.status();
            double b = y.service() - y.status();
            if (a < b) return -1;
            if (a > b) return 1;
            return 0;
        };
        pq = new PriorityQueue<Client>(cmp);
        server = new Server();
    }

    private double age(Client x) {
        return x.service() - x.status();
    }

    private Client sync(Client x) {
        double wait = clock - x.step();
        x.step(wait);
        x.waiting(wait);
        return x;
    }
    
    private PriorityQueue<Client> smallastAgeGroup() {
        Comparator<Client> cmp = (x, y) -> {
            if (x.status() < y.status()) return -1;
            if (x.status() < y.status()) return 1;
            return 0;
        };
        PriorityQueue<Client> sag = new PriorityQueue(cmp);
        sag.offer(sync(pq.poll()));
        while (!pq.isEmpty()) {
            if (age(pq.peek()) > age(sag.peek())) break;
            else sag.offer(sync(pq.poll()));
        }
        return sag;
    }

    private double interStep(double nextStep) {
        if (pq.isEmpty()) return nextStep;
        double interStep = age(pq.peek()) + clock;
        return interStep < nextStep ? interStep : nextStep;
    }

    public void schedule(Client incoming) {
        pq.offer(incoming);
    }

    public void step(double nextStep) {
        if (pq.isEmpty()) return;
        PriorityQueue<Client> sag = smallestAgeGroup();
        double interStep = interStep(nextStep);
        while (interStep < nextStep) {
            
        }
    }

    public static void main(String[] args) {
        int n = 10000000;
        Distribution X = new Exponential(2.0);
        Distribution Y = new Exponential(1.0);
        Stats stats = new Stats();
        Scheduler scheduler = new LeastAttainedService();
        scheduler.observer(stats);
        double clock = 0.0;
        for (int i = 0; i < n; i++) {
            clock += Y.draw();
            scheduler.step(clock);
            scheduler.schedule(new Client(clock, X.draw(), i));
        }
        System.out.println(stats.waiting().first());
        System.out.println(stats.response().first());
        System.out.println(stats.service().first());
    }
}
