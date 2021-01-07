package spool;

import java.util.function.Supplier;

public interface Simulator {
    public Stats[] simulate(Distribution arrival,
            Distribution service,
            int numberOfClients);
}
