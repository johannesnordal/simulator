package spool;

import java.lang.RuntimeException;

public interface EventSource
{
    public void registerEvent(Event event, Client client);
}
