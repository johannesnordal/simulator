package spool;

public interface EventSource
{
    public Observer[] getObservers();
    public void registerObserver(Observer observer);
    public void registerEvent(Event event, Client client);
}
