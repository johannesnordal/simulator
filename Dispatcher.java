import java.util.ArrayDeque;

public abstract class Dispatcher implements EventSource<Client> {
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
}
