package spool;

import java.util.Random;
import java.util.function.Supplier;

public class RND extends Dispatcher {
    private Random rnd;

    public RND(Scheduler[] scheduler) {
        super(scheduler);
        rnd = new Random();
    }

    public RND(Supplier<Scheduler> scheduler, int n) {
        super(scheduler, n);
        rnd = new Random();
    }

    public void dispatch(Client incoming) {
        int i = rnd.nextInt(super.scheduler.length);
        super.scheduler[i].receive(incoming);
    }

    public String toString() {
        return "Random";
    }
}
