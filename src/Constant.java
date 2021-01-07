package spool;

public class Constant extends Distribution 
{
    private double scale;

    public Constant(double scale) {
        this.scale = scale;
    }

    public double sample() {
        return scale;
    }

    protected double mgf(int m) {
        return Math.pow(scale, m);
    }

    public double density(double x) {
        return 0.0;
    }

    public static void main(String[] args) {
        Distribution X = new Constant(1.0);
        System.out.println(X.sample());
        System.out.println(X.getNumericalMean());
        System.out.println(X.density(0.6));
    }
}
