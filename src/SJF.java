package spool;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.*;
import static spool.Client.serviceComparator;

public class SJF extends Scheduler
{
    private PriorityQueue<Client> pq;
    private Server server;

    public SJF()
    {
        pq = new PriorityQueue<Client>(serviceComparator());
        server = new Server();
    }

    public boolean admit(Client incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        if (server.running() == null)
        {
            server.running(incoming);
        }

        pq.offer(incoming);

        return true;
    }

    public void sync(double nextStep)
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
        Client running = server.running();
        registerEvent(Event.DEPARTURE, running);

        if (pq.isEmpty())
        {
            server.running(null);
            return;
        }

        Client next = pq.remove();
        double wait = running.step() - next.step();
        next.step(wait);
        server.running(next);
    }

    public double remainingService()
    {
        double work = 0.0;

        for (Client x : pq)
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
