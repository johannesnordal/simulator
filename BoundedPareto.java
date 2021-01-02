import static java.lang.Math.pow;
import static java.lang.Math.log;

public class BoundedPareto extends Distribution {
    private double shape;
    private double low;
    private double high;

    public BoundedPareto(double shape, double low, double high) {
        this.shape = shape;
        this.low = low;
        this.high = high;
    }

    public double sample() {
        double r = rnd.nextDouble();
        double numerator = -(r*pow(high, shape) - r*pow(low, shape) - pow(high, shape));
        double denominator = pow(high, shape) * pow(low, shape);
        double base = numerator / denominator;
        double exponent = -(1 / shape);
        return pow(base, exponent);
    }

    public double density(double x) {
        double numerator = shape * pow(low, shape) * pow(x, -shape - 1);
        double denominator = 1 - pow(low/high, shape);
        return numerator / denominator;
    }

    protected double mgf(int moment) {
        double m = Double.valueOf(moment);
        if (m == shape) {
            double numerator = (shape * pow(low, shape)) * log(high/low);
            double denominator = 1 - pow(low/high, shape);
            return numerator / denominator;
        } else {
            double numerator = shape * (pow(high, shape) * pow(low, m)
                - pow(high, m) * pow(low, shape));
            double denominator = (shape - m) * (pow(high, shape) - pow(low, shape));
            return numerator / denominator;
        }
    }
}
