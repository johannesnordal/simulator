package spool;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;

public abstract class Scheduler implements Receiver, Simulator
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

    public Stats simulate(Distribution arrival,
                          Distribution service,
                          int numberOfJobs)
    {
        Stats.Builder builder = new Stats.Builder();
        this.registerObserver(builder);
        Job.streamOf(arrival, service, numberOfJobs).forEach(this::receive);
        return builder.build();
    }

    public <T extends Observer> T simulate(Supplier<T> supplier,
                                           Distribution arrival,
                                           Distribution service,
                                           int numberOfJobs)
    {
        T observer = supplier.get();
        this.registerObserver(observer);
        Job.streamOf(arrival, service, numberOfJobs).forEach(this::receive);
        return observer;
    }

}
