import org.apache.commons.math3.distribution.*;

public class MainController {
    public static void main(String[] args) {
        double shape = WeibullUtils.fitShapeToCoefficientOfVariation(1.5);
        double scale = WeibullUtils.fitScaleToShape(0.6, shape);
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        RealDistribution arrival = new ExponentialDistribution(1.0);
        RealDistribution service = new ExponentialDistribution(0.5);
        double[] interval = SizeIntervalTaskAssignment.split(n, service);
        Stats[] stats = new Stats[n];
        Scheduler[] scheduler = FirstComeFirstServe.arrayOf(n);
        for (int i = 0; i < n; i++) {
            stats[i] = new Stats();
            scheduler[i].registerObserver(stats[i]);
        }
        Dispatcher dispatcher = new SizeIntervalTaskAssignment(scheduler, interval);
        ClientFactory client = new ClientFactory(arrival, service, m);
        while (client.hasNext()) {
            dispatcher.dispatch(client.next());
        }
        for (double x : interval) System.out.println(x);
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.println(stats[i].response().first());
        }
    }
}
