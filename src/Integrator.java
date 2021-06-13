package spool;

import java.util.function.DoubleUnaryOperator;

public class Integrator
{
    private DoubleUnaryOperator f;

    public Integrator(DoubleUnaryOperator f)
    {
        this.f = f;
    }

    // Sedgewick & Wayne
    public double integrate(double a, double b)
    {
        int n = 10000; 
        double h = (b - a) / (n - 1);
        double sum = 1.0 / 3.0 * (f.applyAsDouble(a) + f.applyAsDouble(b));

        for (int i = 1; i < n-1; i += 2)
        {
            double x = a + h * i;
            sum += 4.0 / 3.0 * f.applyAsDouble(x);
        }

        for (int i = 2; i < n-1; i += 2)
        {
            double x = a + h * i;
            sum += 2.0 / 3.0 * f.applyAsDouble(x);
        }

        return sum * h;
    }

    public static void main(String[] args) {
        Integrator integrator = new Integrator(x -> {
            return Math.sin(x) * Math.pow(Math.cos(x), 2);
        });

        System.out.println(integrator.integrate(0, Math.PI));
    }
}
