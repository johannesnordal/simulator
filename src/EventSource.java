package spool;

import java.lang.RuntimeException;

public interface EventSource
{
    void registerEvent(Event event, Job job);
    void registerObserver(Observer observer);

    static void registerObservers(EventSource[] source, Observer[] observer)
    {
        for (int i = 0; i < source.length && i < observer.length; i++)
        {
            if (observer[i] == null)
            {
                continue;
            }

            source[i].registerObserver(observer[i]);
        }
    }
}
