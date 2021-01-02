public class RoundRobin extends Dispatcher {
    private Scheduler[] scheduler;
    private int i = 0;
    
    public RoundRobin(Scheduler[] scheduler) {
        this.scheduler = scheduler;
    }

    public void dispatch(Client incoming) {
        scheduler[i].step(incoming.arrival());
        scheduler[i].schedule(incoming);
        i = (i += 1) % scheduler.length;
    }
}
