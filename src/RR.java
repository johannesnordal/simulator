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
    
    public StatusCode receive(Client incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        StatusCode code = node[i].receive(incoming);
        
        if (code == StatusCode.BLOCK)
        {
            registerEvent(Event.BLOCK, incoming);
        }

        i = (i += 1) % node.length;

        return StatusCode.ACCEPT;
    }

    public String toString()
    {
        return "Round Robin";
    }
}
