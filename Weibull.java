import static java.lang.Math.pow;
import static java.lang.Math.exp;
import static java.lang.Math.log;

public class Weibull extends Distribution {
    private double scale;
    private double shape;

    public Weibull(double scale, double shape) {
        this.scale = scale;
        this.shape = shape;
    }

    public double sample() {
        double r = rnd.nextDouble();
        return scale * pow((-log(1 - r)), (1.0/shape));
    }

    public double density(double x) {
        if (x < 0) return 0;
        return shape/scale * pow(x/scale, shape) * pow(exp(-x/scale), shape);
    }

    protected double mgf(int m) {
        double moment = Double.valueOf(m);
        return Math.pow(scale, moment) * Misc.gamma(1 + (moment/shape));
    }    
}
