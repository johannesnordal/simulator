package spool;

import java.util.ArrayDeque;

public class FCFS extends Scheduler
{
    private ArrayDeque<Job> pq;
    private Server server;

    public FCFS()
    {
        pq = new ArrayDeque<Job>();
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
            pq.offer(incoming);
        }
    }

    public void step(double nextStep)
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

    private void swap(Job next)
    {
        Job running = server.running();
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
        return server.isBusy() ? pq.size() + 1 : 0;
    }

    public double remainingService()
    {
        if (server.running() == null)
            return 0.0;

        double work = server.running().status();

        for (Job x : pq)
        {
            work += x.status();
        }

        return work;
    }

    public static void main(String[] args) 
    {
        Stats.Builder statsBuilder = new Stats.Builder();
        Scheduler scheduler = new FCFS();
        scheduler.registerObserver(statsBuilder);

        int n = 10_000_000;
        Distribution arrival = new Uniform(0.5, 1.5);
        Distribution service = new Uniform(0, 1.0);
        Job.streamOf(arrival, service, n).forEach(scheduler::receive);

        Stats stats = statsBuilder.build();
        System.out.println(stats.waiting().first());
        System.out.println(stats.response().first());
        System.out.println(stats.slowdown().first());
    }
}
