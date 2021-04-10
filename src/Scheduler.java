package spool;

import java.util.ArrayDeque;
import java.util.function.Supplier;

public abstract class Scheduler implements EventSource, Simulation
{
    public abstract void step(double nextStep);    
    public abstract void schedule(Client incoming);
    public abstract double work();
    public abstract int active();

    private ArrayDeque<Observer> observer;

    public Scheduler()
    {
        observer = new ArrayDeque<Observer>();
    }

    public void receive(Client incoming)
    {
        step(incoming.arrival());
        schedule(incoming);
    }

    public Observer[] getObservers()
    {
        return observer.toArray(new Observer[observer.size()]);
    }

    public void registerObserver(Observer observer)
    {
        this.observer.add(observer);
    }

    public void registerEvent(Event event, Client client)
    {
        for (Observer x : observer)
        {
            x.update(event, client);
        }
    }

    public Stats simulate(Distribution arrival,
            Distribution service,
            int numberOfClients)
    {
        Stats.Builder builder = new Stats.Builder();

        registerObserver(builder);
        sim(arrival, service, numberOfClients);

        return builder.build();
    }

    public void simulate(Observer[] observer,
            Distribution arrival,
            Distribution service,
            int numberOfClients)
    {
        for (Observer x : observer)
        {
            registerObserver(x);
        }

        sim(arrival, service, numberOfClients);
    }

    protected void sim(Distribution arrival, Distribution service, int n)
    {
        Client.streamOf(arrival, service, n).forEach(this::receive);
    }
}
