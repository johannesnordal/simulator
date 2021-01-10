package spool;

import java.util.function.Supplier;

public interface Simulation {
    Stats[] simulate(Distribution arrival,
            Distribution service,
            int numberOfClients);

    static Stats simulate(Simulation simulator,
            Distribution arrival,
            Distribution service,
            int numberOfClients) {
        return
        Stats.merge(simulator.simulate(arrival, service, numberOfClients));
    }
}
