public class RoundRobin {
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

    public static void main(String[] args) {
        int n = 4;
        Scheduler[] scheduler = new FirstComeFirstServe[n];
        Stats[] stats = new Stats[n];
        for (int i = 0; i < n; i++) {
            stats[i] = new Stats();
            scheduler[i] = new FirstComeFirstServe();
            scheduler[i].observer(stats[i]);
        }
        RoundRobin dispatcher = new RoundRobin(scheduler);
        Distribution X = new Exponential(2.0);
        Distribution Y = new Exponential(n);
        double clock = 0.0;
        for (int i = 0; i < 100000000; i++) {
            clock += Y.draw();
            dispatcher.dispatch(new Client(clock, X.draw()));
        }
        for (int i = 0; i < n; i++) {
            System.out.println(
                stats[i].interarrival().first()
                + " " + stats[i].service().first()
            );
        }
    }
}
