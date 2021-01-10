package spool;

import java.util.ArrayDeque;
import static spool.Stats.StatsBuilder;

public abstract class Scheduler implements EventSource, Simulation {

    public abstract void step(double nextStep);    
    public abstract void schedule(Client incoming);
    public abstract double work();
    public abstract int active();

    private ArrayDeque<Observer> observer;

    public Scheduler() {
        observer = new ArrayDeque<Observer>();
    }

    public void receive(Client incoming) {
        step(incoming.arrival());
        schedule(incoming);
    }

    public void registerObserver(Observer observer) {
        this.observer.add(observer);
    }

    public void registerEvent(Event event, Client client) {
        for (Observer x : observer) {
            x.update(event, client);
        }
    }

    public Stats[] simulate(Distribution arrival,
            Distribution service,
            int numberOfClients) {
        StatsBuilder builder = new StatsBuilder();
        registerObserver(builder);
        sim(arrival, service, numberOfClients);
        return new Stats[] { builder.build() };
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
