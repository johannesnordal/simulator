package spool;

import java.util.List;
import java.util.ArrayList;
import static java.lang.Math.pow;

public class Stats
{
    private static final int METRICS = 5;
    private int arrivals = 0;
    private int departures = 0;
    private final Moment[] moment;

    public static class Builder implements Observer
    {
        private double[][] moment = new double[METRICS][2];
        private int arrivals = 0;
        private int departures = 0;
        private double lastArrival;

        public void update(Event event, Job job)
        {
            if (event == Event.ARRIVAL)
            {
                double interarrival = job.arrival() - lastArrival;
                moment[0][0] += interarrival;
                moment[0][1] += pow(interarrival, 2);
                lastArrival = job.arrival();
                arrivals++;
            }
            else
            {
                moment[1][0] += job.service();
                moment[1][1] += pow(job.service(), 2);
                moment[2][0] += job.waiting();
                moment[2][1] += pow(job.waiting(), 2);
                moment[3][0] += job.response();
                moment[3][1] += pow(job.response(), 2);
                moment[4][0] += job.slowdown();
                moment[4][1] += pow(job.slowdown(), 2);
                departures++;
            }
        }

        public Stats build()
        {
            return new Stats(this);
        }
    }

    private Builder builder;

    public Stats()
    {
        this.builder = new Builder();
        this.moment = new Moment[METRICS];
    }

    private Stats(Builder builder)
    {
        arrivals = builder.arrivals;
        departures = builder.departures;
        moment = new Moment[METRICS];
        moment[0] = Moment.of(
                builder.moment[0][0]/arrivals,
                builder.moment[0][1]/arrivals,
                arrivals);
        moment[1] = Moment.of(
                builder.moment[1][0]/departures,
                builder.moment[1][1]/departures,
                departures);
        moment[2] = Moment.of(
                builder.moment[2][0]/departures,
                builder.moment[2][1]/departures,
                departures);
        moment[3] = Moment.of(
                builder.moment[3][0]/departures,
                builder.moment[3][1]/departures,
                departures);
        moment[4] = Moment.of(
                builder.moment[4][0]/departures,
                builder.moment[4][1]/departures,
                departures);
    }

    private Stats(Moment[] moment, int arrivals, int departures)
    {
        this.moment = moment;
        this.arrivals = arrivals;
        this.departures = departures;
    }

    public Moment interarrival()    { return moment[0]; }
    public Moment service()         { return moment[1]; }
    public Moment waiting()         { return moment[2]; }
    public Moment response()        { return moment[3]; }
    public Moment slowdown()        { return moment[4]; }
    public int arrivals()   { return arrivals; }
    public int departures() { return departures; }

    public static Stats merge(Stats[] stats)
    {
        if (stats.length == 1)
        {
            return stats[0];
        }

        Moment[] moment = new Moment[METRICS];

        for (int i = 0; i < METRICS; i++)
        {
            moment[i] = Moment.of(0.0, 0.0, 0);
        }

        int arrivals = 0, departures = 0;

        for (int i = 0; i < stats.length; i++)
        {
            moment[0] = Moment.merge(moment[0], stats[i].interarrival());
            moment[1] = Moment.merge(moment[1], stats[i].service());
            moment[2] = Moment.merge(moment[2], stats[i].waiting());
            moment[3] = Moment.merge(moment[3], stats[i].response());
            moment[4] = Moment.merge(moment[4], stats[i].slowdown());

            arrivals += stats[i].arrivals();
            departures += stats[i].departures();
        }

        return new Stats(moment, arrivals, departures);
    }

    public static Stats merge(Stats[] ...collection)
    {
        List<Stats> list = new ArrayList<>();

        for (int i = 0; i < collection.length; i++)
        {
            for (int j = 0; j < collection[i].length; j++)
            {
                list.add(collection[i][j]);
            }
        }

        Stats[] stats = list.toArray(new Stats[list.size()]);

        return merge(stats);
    }

    public static Stats.Builder[] getBuilders(int n)
    {
        Stats.Builder[] builder = new Stats.Builder[n];

        for (int i = 0; i < n; i++)
            builder[i] = new Stats.Builder();

        return builder;
    }

    public static Stats[] build(Stats.Builder[] builder)
    {
        Stats[] stats = new Stats[builder.length];

        for (int i = 0; i < stats.length; i++) 
        {
            stats[i] = builder[i].build();
        }

        return stats;
    }

    public static Stats[] build(Stats.Builder[] ...collection)
    {
        List<Stats.Builder> list = new ArrayList<>();

        for (int i = 0; i < collection.length; i++)
        {
            for (int j = 0; j < collection[i].length; j++)
            {
                list.add(collection[i][j]);
            }
        }

        Stats.Builder[] builder = list.toArray(new Stats.Builder[list.size()]);

        return build(builder);
    }

    public static Stats get(Stats.Builder[] ...collection)
    {
        return merge(build(collection));
    }
}
