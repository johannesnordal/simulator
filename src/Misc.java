package spool;

import java.lang.IllegalArgumentException;
import static java.lang.Math.abs;

public class Misc {

    public static double EPSILON = 1.0E-5;

    public static long factorial(long n) {
        if (n < 0) {
            String err = "Negative input to factorial";
            throw new IllegalArgumentException(err);
        }
        long res = 1;
        for (long i = 2; i <= n; i++)
            res *= i;
        return res;
    }

    public static double logGamma(double x) {
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser =
        1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
            + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
            +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
        return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
    }

    public static double gamma(double x) { 
        return Math.exp(logGamma(x));
    }

    public static boolean compareDouble(double x, double y) {
        return abs(x - y) < EPSILON;
    }

    public static double kingman(double load, double scva, double scvs, double ms) {
        return (load/(1 - load)) * ((scva + scvs)/2) * ms;
    } 
}