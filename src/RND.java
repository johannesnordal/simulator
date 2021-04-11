package spool;

import java.util.Random;
import java.util.function.Supplier;

public class RND extends Dispatcher
{
    public static class Builder extends AbstractBuilder<RND>
    {
        public Builder(Node[] node)
        {
            this.node = node;
        }

        public RND build()
        {
            return new RND(this);
        }
    }

    private Random rnd;

    private RND(Builder builder)
    {
        super(builder);
        rnd = new Random();
    }

    public StatusCode receive(Client incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        int i = rnd.nextInt(node.length);
        StatusCode code = node[i].receive(incoming);

        if (code == StatusCode.BLOCK)
        {
            registerEvent(Event.BLOCK, incoming);
        }

        return StatusCode.ACCEPT;
    }

    public String toString()
    {
        return "Random";
    }
}
