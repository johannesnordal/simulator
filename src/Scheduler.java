package spool;

import java.util.List;
import java.util.ArrayList;

public abstract class Scheduler implements EventSource, Receiver
{
    private List<Observer> observers = new ArrayList<>();

    public abstract void step(double nextStep);
    public abstract void schedule(Job incoming);
    public abstract int queueLength();
    public abstract double remainingService();

    public void registerObserver(Observer observer)
    {
        this.observers.add(observer);
    }

    public void receive(Job incoming)
    {
        step(incoming.step());
        schedule(incoming);
    }

    public void registerEvent(Event event, Job job)
    {
        for (Observer x : this.observers)
        {
            x.update(event, job);
        }
    }
}
