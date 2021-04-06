import spool.*;

import java.util.function.Predicate;

public class Program implements Observer {

    private Predicate<Client> p;
    private int e = 0;
    private int s = 0;

    public Program(Predicate<Client> p) {
        this.p = p;
    }

    public void update(Event event, Client client) {
        if (event == Event.DEPARTURE) {
            if (p.test(client)) {
                e++;
            }
            s++;
        }
    }

    public double probability() {
        return Double.valueOf(e)/Double.valueOf(s);
    }

    public int getNumberOfTailClients() {
        return e;
    }

    public int getNumberOfClients() {
        return s;
    }

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int n = 10_000_000;

        int p = Integer.parseInt(args[1]);
        double q = p/3.0;
        for (int i = 0; i <= p; i++) {
            double x = i/q;
            Distribution arrival = Weibull.fitToMeanAndCV(1.0/0.6, i/q);
            for (int j = 0; j <= p; j++) {
                double y = j/q;
                Distribution service = Weibull.fitToMeanAndCV(1.0, j/q);
                Program program = new Program((Client client) -> {
                    return Client.response(client) > x * k;
                });
                Scheduler scheduler = new SRPT();
                scheduler.registerObserver(program);
                Client.streamOf(arrival, service, n).forEach(scheduler::receive);
                System.out.printf("%f %f %f\n", x, y, program.probability());
            }
            System.out.println();
        }
    }
}
