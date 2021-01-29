package spool;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.OptionalDouble;
import static spool.Misc.compareDouble;
import static spool.Client.serviceAgeComparator;
import static spool.Client.statusComparator;
import static spool.Client.streamOf;

public class LAS extends Scheduler {
    private PriorityQueue<Client> pq;
    private Server server;
    private double clock = 0.0;

    public LAS() { 
        pq = new PriorityQueue<>(serviceAgeComparator());
        server = new Server();
    }

    public void schedule(Client incoming) { 

    }

    public void step(double nextStep) {
    
    }

    public double work() {
        return 0.0;
    }

    public int active() {
        return 0;
    }

    public static void main(String[] args) {
        int n = 10;
        Distribution arrival = new Exponential(1.0);
        Distribution service = new Exponential(2.0);
        Client[] client = streamOf(arrival, service, n).toArray(Client[]::new);
    }
}
