package spool;

import java.util.ArrayDeque;

public class FCFS extends Scheduler
{
    private ArrayDeque<Client> pq;
    private Server server;
    private double work;

    public FCFS()
    {
        pq = new ArrayDeque<Client>();
        server = new Server();
    }

    public FCFS register(Observer observer)
    {
        registerObserver(observer);
        return this;
    }

    public void schedule(Client incoming)
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

    public int active()
    {
        return server.running() == null ? 0 : pq.size() + 1;
    }

    public double work()
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

    public static void main(String[] args)
    {
        Simulation simulator = new FCFS();

        Distribution arrival = new Exponential(1.0);
        Distribution service = new Exponential(2.0);
        int numberOfClients = 10_000_000;

        Stats stats = simulator.simulate(arrival, service, numberOfClients);

        System.out.println(stats.response().first());
    }
}
