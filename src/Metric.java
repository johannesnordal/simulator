package spool;

public interface Metric<T> extends Observer
{
    public void update(Event event, Job job);

    public T getMetric();
}
