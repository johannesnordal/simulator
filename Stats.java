public class Stats implements Observer<Client> {
    private Moment interarrival;
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

    private double lastArrival;
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

    public boolean isObserving(Event event) {
        if (event == Event.ARRIVAL) return true;
        if (event == Event.DEPARTURE) return true;
        return false;
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
