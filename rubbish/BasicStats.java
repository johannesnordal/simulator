package spool;

import static java.lang.Math.pow;

public class BasicStats {
    public static class Builder implements Observer {
        private final int moments = 2;
        private final int mean = 0;
        private final int secondMoment = 1;
        private double lastArrival = 0.0;
        private double[] interarrival = new double[moments];
        private double[] service = new double[moments];
        private double[] response = new double[moments];
        private int arrivals = 0;
        private int departures = 0;

        public Builder accumulate(Event event, Client client) {
            update(event, client);
            return this;
        }

        public void update(Event event, Client client) {
            if (event == Event.ARRIVAL) {
                double interarrival = client.arrival() - lastArrival;
                this.interarrival[mean] += interarrival;
                this.interarrival[secondMoment] += pow(interarrival, 2);
                lastArrival = client.arrival();
            } else {
                service[mean] += client.service();
                service[secondMoment] += pow(client.service(), 2);
                double response = client.step() - client.arrival();
                this.response[mean] += response;
                this.response[secondMoment] += pow(response, 2);
            }
        }

        public BasicStats build() {
            return new BasicStats(this);
        }
    }

    private final Moment interarrival;
    private final Moment service;
    private final Moment waiting;
    private final Moment response;
    private final Moment slowdown;
    private final int arrivals;
    private final int departures;

    private BasicStats(Builder builder) {
        arrivals    = builder.arrivals;
        departures  = builder.departures;
        this.interarrival = new Moment(
                builder.interarrival[0],
                builder.interarrival[1],
                arrivals);
        this.service = new Moment(
                builder.service[0],
                builder.service[1],
                departures);
        this.waiting = new Moment(
                builder.response[0] - builder.service[0],
                builder.response[1] - builder.service[1],
                departures);
        this.response = new Moment(
                builder.response[0],
                builder.response[1],
                departures);
        this.slowdown = new Moment(
                builder.response[0] / builder.service[0],
                builder.response[1] / builder.service[1],
                departures);
    }

    public Moment interarrival()    { return interarrival; }
    public Moment service()         { return service; }
    public Moment waiting()         { return waiting; }
    public Moment response()        { return response; }
    public Moment slowdown()        { return slowdown; }
    public int arrivals()           { return arrivals; }
    public int departures()         { return departures; }
}
