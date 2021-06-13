package spool;

import java.util.Optional;
import java.util.function.DoubleUnaryOperator;
import static spool.Misc.EPSILON;
import static spool.Misc.compareDouble;

public class MainController
{
    public static void main(String[] args)
    {
        // LAS.main(args);
        // JobUtils.main(args);
        // FCFS.main(args);
        // SRPT.main(args);
        // PS.main(args);
        // Testing.main(args);
        // Job.main(args);
        // SITA.main(args);
        // LCFS.main(args);
        // Server.main(args);
        // Weibull.main(args);
        // Integrator.main(args);
        // Bracket.main(args);
        BisectionSolver.main(args);
    }
}
