package spool;

import java.util.Random;
import java.util.ArrayList;
import java.util.function.Supplier;

public class JSQ extends Dispatcher
{
    public static class Builder extends AbstractBuilder<JSQ>
    {
        public Builder(QueueingNode[] queueingNode)
        {
            this.queueingNode = queueingNode;
        }

        public JSQ build()
        {
            return new JSQ(this);
        }
    }

    private Random random;

    private JSQ(Builder builder)
    {
        super(builder);
        random = new Random();
    }

    public String toString()
    {
        return "Join Shortest Queue";
    }

    public StatusCode receive(Client incoming)
    {
        queueingNode[0].sync(incoming.arrival());
        int min = queueingNode[0].queueLength();
        int j = 0;

        for (int i = 1; i < queueingNode.length; i++)
        {
            queueingNode[i].sync(incoming.arrival()); 

            if (queueingNode[i].queueLength() < min)
            {
                min = queueingNode[i].queueLength();
                j = i;
            }
        }

        ArrayList<Integer> x = new ArrayList<Integer>(queueingNode.length);

        for (int i = 0; i < queueingNode.length; i++)
        {
            if (queueingNode[i].queueLength() == min) 
            {
                x.add(i);
            }
        }

        int i = random.nextInt(x.size());
        queueingNode[x.get(i)].admit(incoming);

        return StatusCode.ACCEPT;
    }
}
