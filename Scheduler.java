import java.util.ArrayDeque;

public abstract class Scheduler implements EventSource<Client>, Simulator {
    public abstract void step(double nextStep);    
    public abstract void schedule(Client incoming);
    public abstract double work();
    public abstract int active();

    private ArrayDeque<Observer<Client>> arrivalObserver;
    private ArrayDeque<Observer<Client>> departureObserver;

    public Scheduler() {
        arrivalObserver = new ArrayDeque<Observer<Client>>();
        departureObserver = new ArrayDeque<Observer<Client>>();
    }

    public void receive(Client incoming) {
        step(incoming.arrival());
        schedule(incoming);
    }

    /********************************************************
    *   EventSource<T> interface.                           *
    ********************************************************/
    public void registerObserver(Observer<Client> observer) {
        if (observer.isObserving(Event.ARRIVAL))
            arrivalObserver.add(observer);
        if (observer.isObserving(Event.DEPARTURE))
            departureObserver.add(observer);
    }

    public void registerEvent(Event event, Client client) {
        if (event == Event.ARRIVAL) {
            for (Observer<Client> x : arrivalObserver)
                x.update(event, client);
        } else if (event == Event.DEPARTURE) {
            for (Observer<Client> x : departureObserver)
                x.update(event, client);
        }
    }

    /**************************************
    *   Simulator interface.              *
    **************************************/
    public void simulate(Observer<Client> observer,
            Distribution arrival,
            Distribution service,
            int numberOfClients) {
        registerObserver(observer);
        simulate(arrival, service, numberOfClients);
    }

    public Stats simulate(Distribution arrival,
            Distribution service,
            int numberOfClients) {
        Stats stats = new Stats();
        registerObserver(stats);
        sim(arrival, service, numberOfClients);
        return stats;
    }

    private void sim(Distribution arrival,
            Distribution service,
            int numberOfClients) {
        ClientFactory client = new ClientFactory(arrival,
                service, numberOfClients);
        while (client.hasNext())
            receive(client.next());
    }
}
