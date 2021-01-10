package spool;

public class Stats implements Observer {
    private Moment interarrival;
    private Moment service;
    private Moment waiting;
    private Moment response;
    private Moment slowdown;
    private double lastArrival;
    private int arrivals;
    private int departures;

    public Stats() {
        interarrival = new Moment();
        service = new Moment();
        waiting = new Moment();
        response = new Moment();
        slowdown = new Moment();
    }

    public static Stats merge(Stats[] stats) {
        Moment interarrival = new Moment();
        Moment service = new Moment();
        Moment waiting = new Moment();
        Moment response = new Moment();
        Moment slowdown = new Moment();
        int arrivals = 0;
        int departures = 0;
        for (Stats x : stats) {
            interarrival.merge(x.interarrival());
            service.merge(x.service());
            waiting.merge(x.waiting());
            response.merge(x.response());
            slowdown.merge(x.slowdown());
            arrivals += x.arrivals;
            departures += x.departures;
        }
        return new Stats(interarrival, service, waiting, response,
                slowdown, arrivals, departures);
    }

    private Stats(Moment interarrival,
            Moment service,
            Moment waiting,
            Moment response,
            Moment slowdown,
            int arrivals,
            int departures) {
        this.interarrival = interarrival;
        this.service = service;
        this.waiting = waiting;
        this.response = response;
        this.slowdown = slowdown;
        this.arrivals = arrivals;
        this.departures = departures;
    }

    public void interarrival(Client x) {
        interarrival.accum(x.arrival() - lastArrival);
        lastArrival = x.arrival();
    }

    public void service(Client x) {
        service.accum(x.service());
    }

    public void waiting(Client x) {
        waiting.accum(x.waiting());
    }

    public void response(Client x) {
        response.accum(x.response());
    }

    public void slowdown(Client x) {
        slowdown.accum(x.slowdown());
    }

    public Moment interarrival() {
        return interarrival.denom(arrivals);
    }

    public Moment service() {
        return service.denom(departures);
    }

    public Moment waiting() {
        return waiting.denom(departures);
    }

    public Moment response() {
        return response.denom(departures);
    }

    public Moment slowdown() {
        return slowdown.denom(departures);
    }

    public double arrivals() {
        return arrivals;
    }

    public double departures() {
        return departures;
    }

    public void update(Event event, Client client) {
        if (event == Event.ARRIVAL)
            registerArrival(client);
        else
            registerDeparture(client);
    }

    private void registerArrival(Client client) {
        interarrival(client);
        arrivals++;
    }

    private void registerDeparture(Client client) {
        service(client);
        waiting(client);
        response(client);
        slowdown(client);
        departures++;
    }
}
