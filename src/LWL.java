package spool;

import java.util.Random;
import java.util.ArrayList;
import java.util.function.Supplier;

public class LWL extends Dispatcher
{
    public static class Builder extends AbstractBuilder<LWL>
    {
        public Builder(Scheduler[] scheduler)
        {
            this.scheduler = scheduler;
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

        ArrayList<Integer> x = new ArrayList<>(scheduler.length);
        double min = Double.MAX_VALUE;

        for (int i = 0; i < scheduler.length; i++)
        {
            scheduler[i].sync(incoming.arrival());

            double work = scheduler[i].remainingService();

            if (work == min) 
                x.add(i);

            if (work < min)
            {
                min = work;
                x = new ArrayList<>(scheduler.length);
                x.add(i);
            }
        }

        int i = new Random().nextInt(x.size());

        boolean received = scheduler[x.get(i)].admit(incoming);

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
