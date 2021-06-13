package spool;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.*;
import static spool.Job.serviceComparator;

public class SJF extends Scheduler
{
    private PriorityQueue<Job> pq;
    private Server server;

    public SJF()
    {
        pq = new PriorityQueue<Job>(serviceComparator());
        server = new Server();
    }

    public void schedule(Job incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        if (server.running() == null)
        {
            server.running(incoming);
        }

        pq.offer(incoming);
    }

    public void step(double nextStep)
    {
        double slice = server.slice(nextStep);

        while (slice > 0.0)
        {
            server.step(slice);

            if (!server.isBusy())
                swap();

            slice = server.slice(nextStep);
        }
    }

    private void swap()
    {
        Job running = server.running();
        registerEvent(Event.DEPARTURE, running);

        if (pq.isEmpty())
        {
            server.running(null);
            return;
        }

        Job next = pq.remove();
        double wait = running.step() - next.step();
        next.step(wait);
        server.running(next);
    }

    public double remainingService()
    {
        double work = 0.0;

        for (Job x : pq)
        {
            work += x.status();
        }

        return work;
    }

    public int queueLength()
    {
        return pq.size();
    }
}
