package spool;

/*
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.OptionalDouble;
import static spool.Misc.compareDouble;
import static spool.Job.serviceAgeComparator;
import static spool.Job.statusComparator;
import static spool.Job.streamOf;

// Work in progress.

public class LAS extends Scheduler {
    private PriorityQueue<Job> pq;
    private Server server;
    private double clock = 0.0;

    public LAS() { 
        pq = new PriorityQueue<>(serviceAgeComparator());
        server = new Server();
    }

    public boolean admit(Job incoming) { 
        return true;
    }

    public void sync(double nextStep) {
    
    }

    public double remainingService() {
        return 0.0;
    }

    public int queueLength() {
        return 0;
    }

    public static void main(String[] args) {
        int n = 10;
        Distribution arrival = new Exponential(1.0);
        Distribution service = new Exponential(2.0);
        Job[] job = streamOf(arrival, service, n).toArray(Job[]::new);
    }
}
*/
