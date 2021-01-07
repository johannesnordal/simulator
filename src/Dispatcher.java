package spool;

import java.util.ArrayDeque;
import java.util.stream.*;
import java.util.Arrays;
import java.util.function.Supplier;
import static java.util.stream.Collectors.toList;

public abstract class Dispatcher implements EventSource, Simulator {
    private ArrayDeque<Observer> observer;
    protected Scheduler[] scheduler;
    
    public Dispatcher(Supplier<Scheduler> scheduler, int n) {
        observer = new ArrayDeque<>();
        this.scheduler = Stream
            .generate(scheduler)
            .limit(n)
            .collect(toList())
            .toArray(new Scheduler[n]);
    }

    public Dispatcher(Scheduler[] scheduler) {
        this.scheduler = scheduler;
    }

    public abstract void dispatch(Client incoming);

    public void registerObserver(Observer observer) {
        this.observer.add(observer);
    }

    public void registerEvent(Event event, Client client) {
        for (Observer x : observer)
            x.update(event, client);
    }

    protected void sim(Distribution arrival,
            Distribution service,
            int numberOfClients)
    {
        ClientFactory client = new ClientFactory(arrival,
                service, numberOfClients);
        while (client.hasNext())
            dispatch(client.next());
    }

    public Stats[] simulate(Distribution arrival,
            Distribution service,
            int numberOfClients)
    {
        Stats[] stats = new Stats[scheduler.length];
        for (int i = 0; i < scheduler.length; i++)
            scheduler[i].registerObserver((stats[i] = new Stats()));
        sim(arrival, service, numberOfClients);
        return stats;
    }
}
