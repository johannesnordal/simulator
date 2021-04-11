package spool;

import java.util.ArrayDeque;

public class FCFS extends Scheduler
{
    private ArrayDeque<Client> pq;
    private Server server;

    public static class Builder extends AbstractBuilder<FCFS>
    {
        public FCFS build()
        {
            return new FCFS(this);
        }
    }

    private FCFS(Builder builder)
    {
        super(builder);
        pq = new ArrayDeque<Client>();
        server = new Server();
    }

    public FCFS()
    {
        pq = new ArrayDeque<Client>();
        server = new Server();
    }

    public boolean admit(Client incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        if (server.running() == null)
        {
            server.running(incoming);
        }
        else
        {
            pq.offer(incoming);
        }

        return true;
    }

    public void sync(double nextStep)
    {
        double slice = server.slice(nextStep);

        while (slice > 0.0)
        {
            server.step(slice);

            if (!server.isBusy())
                swap(pq.poll());

            slice = server.slice(nextStep);
        }
    }

    private void swap(Client next)
    {
        Client running = server.running();
        registerEvent(Event.DEPARTURE, running);

        if (next == null)
        {
            server.running(null);
            return;
        }

        double wait = running.step() - next.step();
        next.step(wait);
        server.running(next);
    }

    public int queueLength()
    {
        return server.running() == null ? 0 : pq.size() + 1;
    }

    public double remainingService()
    {
        if (server.running() == null)
            return 0.0;

        double work = server.running().status();

        for (Client x : pq)
        {
            work += x.status();
        }

        return work;
    }
}
