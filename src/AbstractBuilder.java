package spool;

public abstract class AbstractBuilder<T>
{
    protected Node[] node                   = new Node[0];
    protected SyncingNode[] syncingNode     = new SyncingNode[0];
    protected QueueingNode[] queueingNode   = new QueueingNode[0];
    protected ServicingNode[] servicingNode = new ServicingNode[0];
    protected Dispatcher[] dispatcher       = new Dispatcher[0];
    protected Scheduler[] scheduler         = new Scheduler[0];
    protected Observer[] observer           = new Observer[0];

    public AbstractBuilder<T> observer(Observer[] observer)
    {
        this.observer = observer;
        return this;
    }

    public abstract T build();
}
