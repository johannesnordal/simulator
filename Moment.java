public class Moment {
    private double first;
    private double second;
    private double third;
    private double fourth;
    private int n;

    public void accum(double x) {
        first   += x;
        second  += Math.pow(x,2);
        // third   += Math.pow(x,3);
        // fourth  += Math.pow(x,4);
    }

    public Moment denom(int n) {
        this.n = n;
        return this;
    }

    public double first() {
        return first/n;
    }

    public double second() {
        return second/n;
    }

    public double variance() {
        return second() - Math.pow(first(),2);
    }

    public double third() {
        return third/n;
    }

    public double fourth() {
        return fourth/n;
    }
}
