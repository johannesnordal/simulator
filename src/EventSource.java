package spool;

import java.lang.RuntimeException;

public interface EventSource
{
    void registerEvent(Event event, Job job);
    void registerObserver(Observer observer);
}
