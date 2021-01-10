package spool;

import java.util.function.Supplier;

public class RR extends Dispatcher {
    private int i = 0;
    
    public RR(Scheduler[] scheduler) {
        super(scheduler);
    }

    public RR(Supplier<Scheduler> scheduler, int n) {
        super(scheduler, n);
    }

    public void dispatch(Client incoming) {
        super.scheduler[i].step(incoming.arrival());
        super.scheduler[i].schedule(incoming);
        i = (i += 1) % scheduler.length;
    }

    public static void main(String[] args) {
        Dispatcher dispatcher = new RR(SRPT::new, 4);
    }
}
