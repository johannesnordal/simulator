import java.util.Deque;
import java.util.ArrayDeque;

public class LastComeFirstServe extends Scheduler {
    private ArrayDeque<Client> stack;
    private Server server;

    public LastComeFirstServe() {
        stack = new ArrayDeque<Client>();
        server = new Server();
    }

    public void schedule(Client incoming) {
        registerArrival(incoming);
        if (server.running() == null) {
            server.running(incoming);
        } else {
            stack.push(server.running());
            server.running(incoming);
        }
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
        Client running = server.running();
        registerDeparture(running);
        if (stack.isEmpty()) {
            server.running(null);
            return;
        }
        Client next = stack.pop();
        double wait = running.departure() - next.step();
        next.step(wait);
        next.waiting(wait);
        server.running(next);
    }

    public double work() {
        if (server.running() == null) return 0.0;
        double work = server.running().status();
        for (Client x : stack) {
            work += x.status();
        }
        return work;
    }

    public int active() {
        if (server.running() == null) return 0;
        return stack.size() + 1;
    }

    public static void main(String[] args) {
        int n = 100000000;
        Distribution X = new Exponential(3.0);
        Distribution Y = new Exponential(1.0);
        Stats stats = new Stats();
        Scheduler scheduler = new LastComeFirstServe();
        scheduler.observer(stats);
        double clock = 0.0;
        for (int i = 0; i < n; i++) {
            clock += Y.draw();
            scheduler.step(clock);
            scheduler.schedule(new Client(clock, X.draw()));
        }
        System.out.println(stats.response().first());
    }
}
