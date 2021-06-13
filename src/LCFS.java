package spool;

import java.util.Deque;
import java.util.ArrayDeque;

public class LCFS extends Scheduler
{
    private ArrayDeque<Job> stack;
    private Server server;

    public LCFS()
    {
        stack = new ArrayDeque<Job>();
        server = new Server();
    }

    public void schedule(Job incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        if (server.running() == null)
        {
            server.running(incoming);
        }
        else
        {
            stack.push(server.running());
            server.running(incoming);
        }
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

        if (stack.isEmpty())
        {
            server.running(null);
            return;
        }

        Job next = stack.pop();
        double wait = running.step() - next.step();
        next.step(wait);
        server.running(next);
    }

    public double remainingService()
    {
        if (server.running() == null) 
        {
            return 0.0;
        }

        double work = server.running().status();

        for (Job x : stack)
        {
            work += x.status();
        }

        return work;
    }

    public int queueLength()
    {
        if (server.running() == null) 
        {
            return 0;
        }

        return stack.size() + 1;
    }
}
