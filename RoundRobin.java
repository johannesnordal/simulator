import java.util.function.Supplier;

public class RoundRobin extends Dispatcher {
    private Scheduler[] scheduler;
    private int i = 0;
    
    public RoundRobin(Scheduler[] scheduler) {
        this.scheduler = scheduler;
    }

    public RoundRobin(Supplier<Scheduler> scheduler, int n) {
        this.scheduler = new Scheduler[n];
        for (int i = 0; i < n; i++) {
            this.scheduler[i] = scheduler.get();
        }
    }

    public void dispatch(Client incoming) {
        scheduler[i].step(incoming.arrival());
        scheduler[i].schedule(incoming);
        i = (i += 1) % scheduler.length;
    }

    public static void main(String[] args) {
        Dispatcher dispatcher
            = new RoundRobin(ShortestRemainingProcessing::new, 4);
    }
}
