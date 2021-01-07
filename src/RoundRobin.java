package spool;

import java.util.function.Supplier;

public class RoundRobin extends Dispatcher {
    private int i = 0;
    
    public RoundRobin(Scheduler[] scheduler) {
        super(scheduler);
    }

    public RoundRobin(Supplier<Scheduler> scheduler, int n) {
        super(scheduler, n);
    }

    public void dispatch(Client incoming) {
        super.scheduler[i].step(incoming.arrival());
        super.scheduler[i].schedule(incoming);
        i = (i += 1) % scheduler.length;
    }

    public static void main(String[] args) {
        Dispatcher dispatcher
            = new RoundRobin(ShortestRemainingProcessing::new, 4);
    }
}
