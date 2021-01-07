package spool;

import java.util.Comparator;
import java.util.PriorityQueue;

public class LeastAttainedService extends Scheduler {
    private Comparator<Client> cmp;
    private PriorityQueue<Client> pq;
    private Server server;

    public LeastAttainedService() { }

    public void step(double nextStep) { }

    public void schedule(Client incoming) { }

    public double work() {
        return 0.0;
    }

    public int active() {
        return 0;
    }
}
