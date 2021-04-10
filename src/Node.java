package spool;

public interface Node
{
    public void receive(Client client);

    public void step(double nextStep);

    public void schedule(Client incoming);

    public double work();

    public int active();
}
