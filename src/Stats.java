package spool;

class Moment {
    private double first;
    private double second;
    private int denom = 0;

    public void accum(double x) {
        first   += x;
        second  += Math.pow(x,2);
    }

    public Moment denom(int denom) {
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

public class Stats implements Observer {
    private Moment interarrival;
    private Moment service;
    private Moment waiting;
    private Moment departure;
    private Moment response;
    private Moment slowdown;
    private double lastArrival;
    private int arrivals;
    private int departures;

    public Stats() {
        interarrival = new Moment();
        service = new Moment();
        waiting = new Moment();
        departure = new Moment();
        response = new Moment();
        slowdown = new Moment();
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

    public void departure(Client x) {
        departure.accum(x.departure());
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

    public Moment departure() {
        return departure.denom(departures);
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
        else if (event == Event.DEPARTURE)
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
        departure(client);
        departures++;
    }
}
