import spool.*;

public class ContourMetrics {
    public static void main(String[] args) {
        int n = 10_000_000;
        for (int y = 0; y <= 6; y += 1) {
            Distribution arrival = Weibull.fitToMeanAndCV(1.0/0.6, y/2.0);
            for (int x = 0; x <= 6; x += 1) {
                Distribution service = Weibull.fitToMeanAndCV(1.0, x/2.0);
                double res = new SRPT().simulate(arrival, service, n).waiting().first();
                System.out.printf("%f %f %f\n", x/2.0, y/2.0, res);
            }
            System.out.println();
        }
    }
}
