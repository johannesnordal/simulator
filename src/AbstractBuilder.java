package spool;


public abstract class AbstractBuilder<T>
{
    protected Dispatcher[] dispatcher = new Dispatcher[0];
    protected Scheduler[] scheduler = new Scheduler[0];
    protected Node[] node = new Node[0];
    protected Observer[] observer = new Observer[0];

    public AbstractBuilder<T> dispatcher(Dispatcher[] dispatcher)
    {
        this.dispatcher = dispatcher;
        return this;
    }

    public AbstractBuilder<T> scheduler(Scheduler[] scheduler)
    {
        this.scheduler = scheduler;
        return this;
    }


    public AbstractBuilder<T> node(Node[] node)
    {
        this.node = node;
        return this;
    }

    public AbstractBuilder<T> observer(Observer[] observer)
    {
        this.observer = observer;
        return this;
    }

    public abstract T build();
}
