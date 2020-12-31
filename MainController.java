public class MainController {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int clients = Integer.parseInt(args[1]);
        Scheduler[] scheduler = new Scheduler[n];
        Stats[] stats = new Stats[n];
        for (int i = 0; i < n; i++) {
            scheduler[i] = new ShortestJobFirst();
            stats[i] = new Stats();
            scheduler[i].registerObserver(stats[i]);
        }
        Dispatcher dispatcher = new LeastWorkLeft(scheduler);
        Distribution X = new Exponential(2.0);
        Distribution Y = new Exponential(n);
        double clock = 0.0;
        for (int i = 0; i < clients; i++) {
            clock += Y.draw();
            dispatcher.dispatch(new Client(clock, X.draw()));
        }
        for (int i = 0; i < n; i++) {
            System.out.println(stats[i].response().first());
        }
    }
}
