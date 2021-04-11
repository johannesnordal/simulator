package spool;

public interface SyncingNode extends Node
{
    public void sync(double nextStep);

    public boolean admit(Client client);
}
