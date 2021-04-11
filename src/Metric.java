package spool;

public interface Metric<T> extends Observer
{
    public void update(Event event, Client client);

    public T getMetric();
}
