package spool;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

public abstract class Scheduler implements EventSource, Simulator {
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
        Stats[] stats = Stream
            .generate(Stats::new)
            .limit(1)
            .collect(toList())
            .toArray(new Stats[1]);
        Arrays.stream(stats).forEach(this::registerObserver);
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
