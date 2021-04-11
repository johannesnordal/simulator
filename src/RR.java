package spool;

import java.util.function.Supplier;

public class RR extends Dispatcher
{
    public static class Builder extends AbstractBuilder<RR>
    {
        public Builder(Node[] node)
        {
            this.node = node;
        }

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
        registerEvent(Event.ARRIVAL, incoming);

        node[i].receive(incoming);
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
