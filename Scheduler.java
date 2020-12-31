import java.util.ArrayList;

public abstract class Scheduler {

    public abstract void step(double nextStep);    
    public abstract void schedule(Client incoming);
    public abstract int active();
    public abstract double work();

    private ArrayList<Observer> observer;

    public Scheduler() {
        observer = new ArrayList<Observer>();
    }

    public void receive(Client incoming) {
        step(incoming.arrival());
        schedule(incoming);
    }

    public void observer(Observer observer) {
        this.observer.add(observer);
    }

    public void registerArrival(Client client) {
        for (Observer s : observer) {
            s.arriving(client);
        }
    }

    public void registerDeparture(Client client) {
        for (Observer s : observer) {
            s.departing(client);
        }
    }
}
