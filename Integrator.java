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
        Distribution d1 = new Exponential(0.6);
        Distribution d2 = new Weibull(0.68, 2.1);
        Distribution d3 = new Uniform(1.0, 4.0);
        System.out.println(d1.getNumericalMean());
        System.out.println(d2.getNumericalMean());
        System.out.println(d3.getNumericalMean());
        Integrator i1 = new Integrator(x -> x * d1.density(x));
        Integrator i2 = new Integrator(x -> x * d2.density(x));
        Integrator i3 = new Integrator(x -> x * d3.density(x));
        System.out.println(i1.integrate(0.0, 50));
        System.out.println(i2.integrate(0.0, 50));
        System.out.println(i3.integrate(1.0, 4.0));
    }
}
