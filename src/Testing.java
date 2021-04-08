package spool;

import java.util.PriorityQueue;
import java.util.Optional;
import java.util.stream.*;
import static spool.Client.arrivalComparator;

class Testing
{
    public static void main(String[] args)
    {
        int n = 10_000_000;
        Distribution arrival = new Exponential(1.0);
        Distribution service = new Exponential(2.0);

        Simulation simulator = new FCFS();
        Stats stats = simulator.simulate(arrival, service, n);
        
        System.out.println(stats.waiting().first());
        System.out.println(stats.response().first());
    }
}
