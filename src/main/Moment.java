package spool;

import static java.lang.Math.pow;

public interface Moment {
    public double first();
    public double second();
    public int samples();

    public static Moment of(double first, double second, int samples) {
        Moment moment = new Moment() {
            public double first()   { return first; }
            public double second()  { return second; }
            public int samples()    { return samples; }
        };
        return moment;
    }

    public static Moment merge(Moment[] collection) {
        double first, second;
        int samples;
        first = second = samples = 0;
        for (Moment x : collection) {
            first   += x.first() * x.samples();
            second  += x.second() * x.samples();
            samples += x.samples();
        }
        return Moment.of(first/samples, second/samples, samples);
    }

    public static Moment merge(Moment x, Moment y) {
        return Moment.merge(new Moment[] {x, y});
    }

    default double variance() {
        return second() - pow(first(), 2);
    }
}
