package spool;

import java.util.Random;
import java.util.ArrayList;
import java.util.function.Supplier;

public class JSQ extends Dispatcher
{
    private Random random;

    public JSQ(Supplier<Scheduler> scheduler, int n)
    {
        super(scheduler, n);
        random = new Random();
    }

    public JSQ(Scheduler[] scheduler)
    {
        super(scheduler);
    }

    public String toString()
    {
        return "Join Shortest Queue";
    }

    public void dispatch(Client incoming)
    {
        scheduler[0].step(incoming.arrival());
        int min = scheduler[0].active();
        int j = 0;

        for (int i = 1; i < scheduler.length; i++)
        {
            scheduler[i].step(incoming.arrival()); 

            if (scheduler[i].active() < min)
            {
                min = scheduler[i].active();
                j = i;
            }
        }

        ArrayList<Integer> x = new ArrayList<Integer>(scheduler.length);

        for (int i = 0; i < scheduler.length; i++)
        {
            if (scheduler[i].active() == min) 
            {
                x.add(i);
            }
        }

        int i = random.nextInt(x.size());
        scheduler[x.get(i)].schedule(incoming);
    }
}
