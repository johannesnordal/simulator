package spool;

import java.util.Queue;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Comparator;
import static spool.Client.statusComparator;

public class PS extends Scheduler {
    
    private Queue<Client> pq;
    private Server server;
    private double speed = 1.0;

    public PS() {
        pq = new PriorityQueue<Client>(statusComparator());
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
            return server.running().step();
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

    public Client[] flush() {
        Client[] client = new Client[pq.size()];
        for (int i = 0; !pq.isEmpty(); i++) {
            client[i] = pq.poll();
        }
        return client;
    }

    public static void main(String[] args) {
        PS ps = new PS();
        Client[] client = new Client[4];
        for (int i = 0; i < 4; i++) {
            client[i] = new Client(0.0, 1.0, i);
            // System.out.println(client[i]);
            // System.out.println();
            ps.schedule(client[i]);
        }
        ps.step(4.0);
        for (Client x : client) {
            System.out.println(x);
            System.out.println();
        }
    }
}
