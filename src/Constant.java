package spool;

public class Constant extends Distribution 
{
    private double scale;

    public Constant(double scale)
    {
        this.scale = scale;
    }

    public double sample()
    {
        return scale;
    }

    protected double mgf(int m)
    {
        return Math.pow(scale, m);
    }

    public double density(double x)
    {
        return 0.0;
    }
}
