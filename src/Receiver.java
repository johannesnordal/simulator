package spool;

public interface Receiver extends EventSource
{
    void receive(Job job);
}
