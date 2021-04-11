package spool;

import java.util.ArrayDeque;

public abstract class Dispatcher implements EventSource, Node
{
    protected Node[] node;
    protected SyncingNode[] syncingNode;
    protected QueueingNode[] queueingNode;
    protected ServicingNode[] servicingNode;
    protected Dispatcher[] dispatcher;
    protected Scheduler[] scheduler;
    protected Observer[] observer;

    protected Dispatcher(AbstractBuilder builder)
    {
        this.node           = builder.node;
        this.syncingNode    = builder.syncingNode;
        this.queueingNode   = builder.queueingNode;
        this.servicingNode  = builder.servicingNode;
        this.dispatcher     = builder.dispatcher;
        this.scheduler      = builder.scheduler;
        this.observer       = builder.observer;
    }

    public void registerEvent(Event event, Client client)
    {
        for (int i = 0; i < observer.length; i++)
        {
            observer[i].update(event, client);
        }
    }
}
