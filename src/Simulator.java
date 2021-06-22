package spool;

import java.util.function.Supplier;

public interface Simulator
{
    Stats simulate(Distribution arrival,
                   Distribution service,
                   int numberOfJob);

    <T extends Observer> T simulate(Supplier<T> supplier,
                                    Distribution arrival,
                                    Distribution service,
                                    int numberOfJobs);
}
