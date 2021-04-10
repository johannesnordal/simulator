package spool;

import java.util.function.Supplier;

public class RR extends Dispatcher
{
    public static class Builder extends AbstractBuilder<RR>
    {
        public RR build()
        {
            return new RR(this);
        }
    }

    private int i = 0;

    private RR(Builder builder)
    {
        super(builder);
    }
    
    public void receive(Client incoming)
    {
        super.scheduler[i].step(incoming.arrival());
        super.scheduler[i].schedule(incoming);
        i = (i += 1) % scheduler.length;
    }

    public String toString()
    {
        return "Round Robin";
    }

    public static void main(String[] args)
    {

    }
}
