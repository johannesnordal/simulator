package spool;

public class Moment {
    private double first;
    private double second;
    private int denom = 0;

    protected Moment() { }

    protected Moment(double first, double second, int denom) {
        this.first = first;
        this.second = second;
        this.denom = denom;
    }

    protected void merge(Moment that) {
        this.first += that.first;
        this.second += that.second;
        this.denom += that.denom;
    }

    protected void accum(double x) {
        first   += x;
        second  += Math.pow(x,2);
    }

    protected Moment denom(int denom) {
        this.denom = denom;
        return this;
    }

    public double first() {
        return first/denom;
    }

    public double second() {
        return second/denom;
    }

    public double variance() {
        return second() - Math.pow(first(),2);
    }
}
