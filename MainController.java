import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.integration.*;

public class MainController {
    public static void main(String[] args) {
        Distribution d = new Weibull(0.68, 2.1);
        System.out.println(d.getNumericalMean());
        UnivariateFunction fn = x -> x * d.density(x);
        UnivariateIntegrator integrator = new SimpsonIntegrator();
        System.out.println(integrator.integrate(100000, fn, 0.0, 10));
    }
}
