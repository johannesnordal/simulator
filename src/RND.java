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

    public void receive(Client incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        int i = rnd.nextInt(scheduler.length);
        node[i].receive(incoming);
    }

    public String toString()
    {
        return "Random";
    }
}
