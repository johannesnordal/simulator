package spool;

import java.util.Random;
import java.util.ArrayList;
import java.util.function.Supplier;

public class LWL extends Dispatcher
{
    public static class Builder extends AbstractBuilder<LWL>
    {
        public Builder(ServicingNode[] servicingNode)
        {
            this.servicingNode = servicingNode;
        }

        public LWL build()
        {
            return new LWL(this);
        }
    }
    
    private LWL(Builder builder)
    {
        super(builder);
    }

    public StatusCode receive(Client incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        ArrayList<Integer> x = new ArrayList<>(servicingNode.length);
        double min = Double.MAX_VALUE;

        for (int i = 0; i < servicingNode.length; i++)
        {
            servicingNode[i].sync(incoming.arrival());

            double work = servicingNode[i].remainingService();

            if (work == min) 
                x.add(i);

            if (work < min)
            {
                min = work;
                x = new ArrayList<>(servicingNode.length);
                x.add(i);
            }
        }

        int i = new Random().nextInt(x.size());

        boolean received = servicingNode[x.get(i)].admit(incoming);

        if (!received)
        {
            registerEvent(Event.BLOCK, incoming);
        }

        return StatusCode.ACCEPT;
    }

    public String toString()
    {
        return "Least Work Left";
    }
}
