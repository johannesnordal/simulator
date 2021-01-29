package spool;

import java.util.ArrayDeque;
import java.util.stream.*;
import java.util.Arrays;
import java.util.function.Supplier;
import static java.util.stream.Collectors.toList;

public abstract class Dispatcher implements EventSource, Simulation {

    public abstract void dispatch(Client incoming);

    private ArrayDeque<Observer> observer;
    protected Scheduler[] scheduler;
    
    public Dispatcher(Supplier<Scheduler> scheduler, int n) {
        observer = new ArrayDeque<>();
        this.scheduler = Stream.generate(scheduler)
            .limit(n)
            .toArray(Scheduler[]::new);
    }

    public Dispatcher(Scheduler[] scheduler) {
        this.scheduler = scheduler;
    }

    public boolean requiresSpecialInitialization() {
        return false;
    }

    public void registerObserver(Observer observer) {
        this.observer.add(observer);
    }

    public Dispatcher register(Observer observer, int i) {
        scheduler[i].registerObserver(observer);
        return this;
    }

    public Dispatcher register(Observer[] observer) {
        for (int i = 0; i < scheduler.length; i++) {
            scheduler[i].registerObserver(observer[i]);
        }
        return this;
    }

    public void registerEvent(Event event, Client client) {
        for (Observer x : observer)
            x.update(event, client);
    }

    protected void sim(Distribution arrival, Distribution service, int n) {
        Client.streamOf(arrival, service, n).forEach(this::dispatch);
    }

    public Stats simulate(Distribution arrival,
            Distribution service,
            int numberOfClients)
    {
        Stats.Builder[] builder = new Stats.Builder[scheduler.length];
        for (int i = 0; i < scheduler.length; i++)
            scheduler[i].registerObserver((builder[i] = new Stats.Builder()));
        sim(arrival, service, numberOfClients);
        Stats[] stats = new Stats[builder.length];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = builder[i].build();
        }
        return Stats.merge(stats);
    }

    public void simulate(Observer[] observer,
	    Distribution arrival,
	    Distribution service,
	    int numberOfClients)
    {
	for (int i = 0; i < scheduler.length; i++) {
	    scheduler[i].registerObserver(observer[i]);
	}
	sim(arrival, service, numberOfClients);
    }
}
