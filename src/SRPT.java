package spool;

import java.util.Comparator;
import java.util.PriorityQueue;
import static spool.Client.statusComparator;
import static spool.Client.stepComparator;

public class SRPT extends Scheduler
{
    private PriorityQueue<Client> pq;
    private Server server;

    public SRPT()
    {
        Comparator<Client> cmp = (x, y) -> {

            Comparator<Client> status = statusComparator();
            Comparator<Client> step = stepComparator();

            int ord = status.compare(x, y);

            if (ord == 0) {
                ord = step.compare(x, y);
            }

            return ord;
        };

        pq = new PriorityQueue<Client>(cmp);
        server = new Server();
    }

    public boolean admit(Client incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);
        pq.offer(incoming);

        server.running(pq.peek());

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
        Client running = pq.remove();

        registerEvent(Event.DEPARTURE, running);

        if (pq.isEmpty())
        {
            server.running(null);
            return;
        }

        Client next = pq.peek();

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
