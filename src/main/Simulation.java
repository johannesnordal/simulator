package spool;

public interface Simulation<T> {
    T simulate(Distribution arrival,
            Distribution service,
            int numberOfClients);
}
