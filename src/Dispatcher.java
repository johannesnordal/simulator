package spool;

import java.util.List;
import java.util.ArrayList;

public abstract class Dispatcher implements EventSource, Receiver
{
    private List<Observer> observers = new ArrayList<>();

    public abstract void dispatch(Job job);

    public void receive(Job job)
    {
        dispatch(job);
    }

    public void registerObserver(Observer observer)
    {
        this.observers.add(observer);
    }

    public void registerEvent(Event event, Job job)
    {
        for (Observer x : observers)
        {
            x.update(event, job);
        }
    }
}
