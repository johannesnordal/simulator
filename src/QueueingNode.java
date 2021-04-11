package spool;

public interface QueueingNode extends SyncingNode
{
    public int queueLength();
}
