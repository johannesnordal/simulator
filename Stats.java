public class Stats implements Observer {
    private Moment interarrival;
    private double lastArrival;
    private Moment service;
    private Moment waiting;
    private Moment departure;
    private Moment response;
    private Moment slowdown;
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

    public void registerArrival() {
        arrivals += 1;
    }

    public void registerDeparture() {
        departures += 1;
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

    public void arriving(Client client) {
        interarrival(client);
        registerArrival();
    }

    public void departing(Client client) {
        service(client);
        waiting(client);
        response(client);
        slowdown(client);
        departure(client);
        registerDeparture();
    }
}
