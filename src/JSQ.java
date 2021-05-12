package spool;

import java.util.Random;
import java.util.ArrayList;
import java.util.function.Supplier;

public class JSQ extends Dispatcher
{
    public static class Builder extends AbstractBuilder<JSQ>
    {
        public Builder(Scheduler[] scheduler)
        {
            this.scheduler = scheduler;
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
        scheduler[0].sync(incoming.arrival());
        int min = scheduler[0].queueLength();
        int j = 0;

        for (int i = 1; i < scheduler.length; i++)
        {
            scheduler[i].sync(incoming.arrival()); 

            if (scheduler[i].queueLength() < min)
            {
                min = scheduler[i].queueLength();
                j = i;
            }
        }

        ArrayList<Integer> x = new ArrayList<Integer>(scheduler.length);

        for (int i = 0; i < scheduler.length; i++)
        {
            if (scheduler[i].queueLength() == min) 
            {
                x.add(i);
            }
        }

        int i = random.nextInt(x.size());
        scheduler[x.get(i)].admit(incoming);

        return StatusCode.ACCEPT;
    }
}
