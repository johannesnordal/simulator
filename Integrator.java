import java.util.function.DoubleUnaryOperator;

public class Integrator {
    private DoubleUnaryOperator f;

    public Integrator(DoubleUnaryOperator f) {
        this.f = f;
    }

    // Sedgewick & Wayne
    public double integrate(double a, double b) {
        int n = 10000; 
        double h = (b - a) / (n - 1);
        double sum = 1.0 / 3.0 * (f.applyAsDouble(a) + f.applyAsDouble(b));
        for (int i = 1; i < n-1; i += 2) {
            double x = a + h * i;
            sum += 4.0 / 3.0 * f.applyAsDouble(x);
        }
        for (int i = 2; i < n-1; i += 2) {
            double x = a + h * i;
            sum += 2.0 / 3.0 * f.applyAsDouble(x);
        }
        return sum * h;
    }

    public static void main(String[] args) { 
        double shape = Weibull.fitShapeToCoefficientOfVariation(1.5);
        double scale = Weibull.fitScaleToMeanAndShape(0.6, shape);
        System.out.println("Shape : " + shape);
        System.out.println("Scale : " + scale);
        Distribution dist = new Weibull(scale, shape);
        double mean = dist.getNumericalMean();
        double range = mean/8.0;
        System.out.println("Range : " + range);
        Integrator integrator = new Integrator(x -> dist.density(x) - range);
        double a = dist.support()[0];
        double x = Double.parseDouble(args[0]);
        System.out.println(integrator.integrate(a, x));
    }
}
