package spool;

import java.util.ArrayDeque;

public abstract class Dispatcher implements EventSource, Node
{
    protected Dispatcher[] dispatcher;
    protected Scheduler[] scheduler;
    protected Node[] node;
    protected Observer[] observer;

    protected Dispatcher(AbstractBuilder builder)
    {
        this.dispatcher = builder.dispatcher;
        this.scheduler = builder.scheduler;
        this.node = builder.node;
        this.observer = builder.observer;
    }

    public void registerObserver(Observer observer) { }

    public void registerEvent(Event event, Client client)
    {
        for (int i = 0; i < observer.length; i++)
        {
            observer[i].update(event, client);
        }
    }

    public void schedule(Client client) { }

    public void step(double nextStep) { }

    public double work()
    {
        return 0.0;
    }

    public int active()
    {
        return 0;
    }
}
