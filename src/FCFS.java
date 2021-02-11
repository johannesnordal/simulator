package spool;

import java.util.ArrayDeque;

public class FCFS extends Scheduler {
    private ArrayDeque<Client> pq;
    private Server server;
    private double work;

    public FCFS() {
        pq = new ArrayDeque<Client>();
        server = new Server();
    }

    public FCFS register(Observer observer) {
        registerObserver(observer);
        return this;
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
        double wait = running.step() - next.step();
        next.step(wait);
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
        int n = 10_000_000;
        Stats stats;
        double cv, k, w, x, y;
        for (int i = 0; i <= 24; i++) {
            cv = i/8.0;
            Distribution arrival = Weibull.fitToMeanAndCV(1.0/0.6, cv);
            for (int j = 0; j <= 24; j++) {
                cv = j/8.0;
                Distribution service = Weibull.fitToMeanAndCV(1.0, cv);
                w = new FCFS().simulate(arrival, service, n).waiting().first();
                x = Math.sqrt(arrival.variance())/arrival.mean();
                y = Math.sqrt(service.variance())/service.mean();
                k = Misc.kingman(0.6, x, y, 1.0);
                System.out.printf("%f %f %f\t%f\n", x, y, w, k);
            }
	    System.out.println();
        }
    }
}
