import spool.*;

int n = 10_000_000;
Distribution arrival = new Exponential(1.0)
Distribution service = new Exponential(2.0)

Simulation simulator = new FCFS();
Stats stats = simulator.simulate(arrival, service, n);

println(stats.response().first());
