package spool;

import java.util.Deque;
import java.util.ArrayDeque;

public class LCFS extends Scheduler {
    private ArrayDeque<Client> stack;
    private Server server;

    public LCFS() {
        stack = new ArrayDeque<Client>();
        server = new Server();
    }

    public void schedule(Client incoming) {
        registerEvent(Event.ARRIVAL, incoming);
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
        registerEvent(Event.DEPARTURE, running);
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
    }
}
