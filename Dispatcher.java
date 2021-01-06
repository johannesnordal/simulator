import java.util.ArrayDeque;

public abstract class Dispatcher implements EventSource<Client>, Simulator {
    private ArrayDeque<Observer<Client>> observer;
    
    public Dispatcher() {
        observer = new ArrayDeque<>();
    }

    public abstract void dispatch(Client incoming);

    public void registerObserver(Observer<Client> observer) {
        if (observer.isObserving(Event.ARRIVAL))
            this.observer.add(observer);
    }

    public void registerEvent(Event event, Client client) {
        for (Observer<Client> x : observer)
            x.update(event, client);
    }

    private void sim(Distribution arrival,
            Distribution service,
            int numberOfClients) {
        ClientFactory client = new ClientFactory(arrival,
                service, numberOfClients);
        while (client.hasNext())
            dispatch(client.next());
    }

    public void simulate(Observer<Client> observer,
            Distribution arrival,
            Distribution service,
            int numberOfClients) {
        registerObserver(observer);
        sim(arrival, service, numberOfClients);
    }

    public Stats simulate(Distribution arrival,
            Distribution service,
            int numberOfClients) {
        Stats stats = new Stats();
        registerObserver(stats);
        sim(arrival, service, numberOfClients);
        return stats;
    }
}
