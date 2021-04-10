package spool;

public abstract class Scheduler implements EventSource, Node
{
    protected Dispatcher[] dispatcher;
    protected Scheduler[] scheduler;
    protected Node[] node;
    protected Observer[] observer;

    public Scheduler() { }

    protected Scheduler(AbstractBuilder builder)
    {
        this.dispatcher = builder.dispatcher;
        this.scheduler = builder.scheduler;
        this.node = builder.node;
        this.observer = builder.observer;
    }

    public void receive(Client incoming)
    {
        step(incoming.arrival());
        schedule(incoming);
    }

    public void registerEvent(Event event, Client client)
    {
        for (Observer x : observer)
        {
            x.update(event, client);
        }
    }
}
