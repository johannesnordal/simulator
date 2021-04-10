package spool;

import java.lang.RuntimeException;

public interface EventSource
{
    public void registerObserver(Observer observer);
    public void registerEvent(Event event, Client client);

    public static void registerObserver(EventSource[] eventSource,
            Observer[] observer)
    {
        if (eventSource.length != observer.length)
        {
            String e = "Arrays of different length";

            throw new RuntimeException(e);
        }

        for (int i = 0; i < eventSource.length; i++)
        {
            eventSource[i].registerObserver(observer[i]);
        }
    }
}
