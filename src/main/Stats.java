package spool;

import static java.lang.Math.pow;

public class Stats {
    private static final int METRICS = 5;
    private int arrivals = 0;
    private int departures = 0;
    private final Moment[] moment;

    public static class StatsBuilder implements Observer {
        private double[][] moment = new double[METRICS][2];
        private int arrivals = 0;
        private int departures = 0;
        private double lastArrival;

        public void update(Event event, Client client) {
            if (event == Event.ARRIVAL) {
                double interarrival = client.arrival() - lastArrival;
                moment[0][0] += interarrival;
                moment[0][1] += pow(interarrival, 2);
                lastArrival = client.arrival();
                arrivals++;
            } else {
                moment[1][0] += client.service();
                moment[1][1] += pow(client.service(), 2);
                moment[2][0] += client.waiting();
                moment[2][1] += pow(client.waiting(), 2);
                moment[3][0] += client.response();
                moment[3][1] += pow(client.response(), 2);
                moment[4][0] += client.slowdown();
                moment[4][1] += pow(client.slowdown(), 2);
                departures++;
            }
        }

        public Stats build() {
            return new Stats(this);
        }
    }

    private Stats(StatsBuilder builder) {
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

    private Stats(Moment[] moment) {
        this.moment = moment;
    }

    public Moment interarrival()    { return moment[0]; }
    public Moment service()         { return moment[1]; }
    public Moment waiting()         { return moment[2]; }
    public Moment response()        { return moment[3]; }
    public Moment slowdown()        { return moment[4]; }

    public static Stats merge(Stats[] stats) {
        if (stats.length == 1) return stats[0];
        Moment[] moment = new Moment[METRICS];
        for (int i = 0; i < METRICS; i++) {
            moment[i] = Moment.of(0.0, 0.0, 0);
        }
        for (int i = 0; i < stats.length; i++) {
            moment[0] = Moment.merge(moment[0], stats[i].interarrival());
            moment[1] = Moment.merge(moment[1], stats[i].service());
            moment[2] = Moment.merge(moment[2], stats[i].waiting());
            moment[3] = Moment.merge(moment[3], stats[i].response());
            moment[4] = Moment.merge(moment[4], stats[i].slowdown());
        }
        return new Stats(moment);
    }
}
