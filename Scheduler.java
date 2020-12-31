import java.util.ArrayDeque;

public abstract class Scheduler implements EventSource<Client> {

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
}
