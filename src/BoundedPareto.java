package spool;

import static java.lang.Math.pow;
import static java.lang.Math.log;

public class BoundedPareto extends Distribution {
    private final double shape;
    private final double lower;
    private final double upper;

    public BoundedPareto(double shape, double lower, double upper) {
        this.shape = shape;
        this.lower = lower;
        this.upper = upper;
    }

    public double sample() {
        final double r = rnd.nextDouble();
        final double numerator = -(r * pow(upper, shape)
                - r * pow(lower, shape)
                - pow(upper, shape));
        final double denominator = pow(upper, shape) * pow(lower, shape);
        final double base = numerator/denominator;
        final double exponent = -(1/shape);
        return pow(base, exponent);
    }

    public double density(double x) {
        if (x < lower || x > upper) return 0.0;
        final double pow = -shape - 1;
        final double shapexpow = pow(shape * x, pow);
        final double numer = lower * shape;
        final double denom = 1 - pow(lower/upper, shape);
        return shapexpow * (numer/denom);
    }

    @Override
    public double[] support() {
        return new double[] {lower, upper};
    }

    protected double mgf(int moment) {
        double m = Double.valueOf(moment);
        if (m == shape) {
            double numerator = (shape * pow(lower, shape)) * log(upper/lower);
            double denominator = 1 - pow(lower/upper, shape);
            return numerator / denominator;
        } else {
            double numerator = shape * (pow(upper, shape) * pow(lower, m)
                - pow(upper, m) * pow(lower, shape));
            double denominator = (shape - m) * (pow(upper, shape) - pow(lower, shape));
            return numerator / denominator;
        }
    }
}
