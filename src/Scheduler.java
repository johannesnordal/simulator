package spool;

public abstract class Scheduler implements EventSource, Node, SyncingNode,
       QueueingNode, ServicingNode
{
    protected Node[] node;
    protected SyncingNode[] syncingNode;
    protected QueueingNode[] queueingNode;
    protected ServicingNode[] servicingNode;
    protected Dispatcher[] dispatcher;
    protected Scheduler[] scheduler;
    protected Observer[] observer;

    public Scheduler() { }

    protected Scheduler(AbstractBuilder builder)
    {
        this.node           = builder.node;
        this.syncingNode    = builder.syncingNode;
        this.queueingNode   = builder.queueingNode;
        this.servicingNode  = builder.servicingNode;
        this.dispatcher     = builder.dispatcher;
        this.scheduler      = builder.scheduler;
        this.observer       = builder.observer;
    }

    public StatusCode receive(Client incoming)
    {
        sync(incoming.arrival());
        admit(incoming);

        return StatusCode.ACCEPT;
    }

    public void registerEvent(Event event, Client client)
    {
        for (Observer x : observer)
        {
            x.update(event, client);
        }
    }
}
