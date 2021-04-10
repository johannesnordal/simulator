package spool;

import static java.lang.Math.pow;

public class Moment
{
    private final double first;
    private final double second;
    private final int samples;

    private Moment(double first, double second, int samples)
    {
        this.first = first;
        this.second = second;
        this.samples = samples;
    }


    public static Moment of(double first, double second, int samples)
    {
        return new Moment(first, second, samples);
    }

    public static Moment merge(Moment[] collection) {
        double first, second;
        int samples;

        first = second = samples = 0;
        for (Moment x : collection)
        {
            first   += x.first() * x.samples();
            second  += x.second() * x.samples();
            samples += x.samples();
        }

        return new Moment(first/samples, second/samples, samples);
    }

    public static Moment merge(Moment x, Moment y)
    {
        return Moment.merge(new Moment[] {x, y});
    }

    public double first()
    {
        return first;
    }

    public double second()
    {
        return second;
    }

    public int samples()
    {
        return samples;
    }

    public double variance()
    {
        return second - pow(first, 2);
    }
}
