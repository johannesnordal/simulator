package spool;

public interface Simulation {
    Stats simulate(Distribution arrival,
            Distribution service,
            int numberOfClients);

    void simulate(Observer[] observer,
	    Distribution arrival,
	    Distribution service,
	    int numberOfClients);
}
