package spool;

import static spool.Simulation.simulate;

public class MainController {
    public static void main(String[] args) {
        int m = 4;
        Simulation simulator = new SITA(FCFS::new, m);
        Distribution arrival = new Exponential(m);
        Distribution service = new Exponential(2.0);

        int n = 10_000_000;
        Stats stats = simulate(simulator, arrival, service, n);
        System.out.println(stats.waiting().first());
        System.out.println(stats.response().first());
        System.out.println(stats.slowdown().first());
    }
}
