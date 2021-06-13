package spool;

import java.util.Random;
import java.util.ArrayList;
import java.util.function.Supplier;

public class LWL extends Dispatcher
{
    private Scheduler[] scheduler;

    private LWL(Scheduler[] scheduler)
    {
        this.scheduler = scheduler;
    }

    public void dispatch(Job incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        ArrayList<Integer> x = new ArrayList<>(scheduler.length);
        double min = Double.MAX_VALUE;

        for (int i = 0; i < scheduler.length; i++)
        {
            scheduler[i].step(incoming.arrival());

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

        scheduler[x.get(i)].schedule(incoming);
    }

    public String toString()
    {
        return "Least Work Left";
    }
}
