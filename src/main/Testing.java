package spool;

import java.util.PriorityQueue;
import java.util.Optional;
import static spool.Client.arrivalComparator;

class Testing {
    public static void main(String[] args) {
        int m = 4;
        Simulation<Stats[]> simulator = new RND(FCFS::new, m);

        int n = 1_000_000;
        Distribution arrival = new Exponential(4);
        Distribution service = new Exponential(2);

        Stats stats = Stats.merge(simulator.simulate(arrival, service, n));
        System.out.println(stats.waiting().first());
        System.out.println(stats.response().first());
    }
}
