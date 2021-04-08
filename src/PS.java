package spool;

import java.util.Queue;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Comparator;
import static spool.Client.statusComparator;

public class PS extends Scheduler
{
    private Queue<Client> pq;
    private Server server;
    private double speed = 1.0;

    public PS()
    {
        pq = new PriorityQueue<Client>(statusComparator());
        server = new Server();
    }

    public void schedule(Client incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);
        pq.add(incoming);
    }

    // This method checks if the client with the lowest remaining service
    // departs before the next step. It does so simply by servicing the client
    // and either returning the time of departure or, in case it doesn't
    // depart before the next step, positive infinity.
    private double checkForDeparturesBeforeNextStep(double nextStep)
    {
        server.speed(speed * (1.0/pq.size()));
        server.running(pq.peek());
        server.step(server.slice(nextStep));

        if (server.running().isFinished())
        {
            return server.running().step();
        }

        return Double.POSITIVE_INFINITY;
    }

    public void step(double nextStep)
    {
        if (pq.size() == 0)
            return;

        double interStep = checkForDeparturesBeforeNextStep(nextStep);

        // If a client departs before the next step, we need to process
        // all clients up to that point, because after the client leaves,
        // we need to re-adjust the processing power for the remaining
        // clients.
        while (interStep < nextStep)
        {
            // If we reach this point, the client with the previously
            // lowest remaining service time (or equal) has been processed
            // to completion.
            registerEvent(Event.DEPARTURE, pq.remove());

            server.speed(speed * (1.0/(pq.size() + 1)));

            for (Client running : pq)
            {
                server.running(running);
                server.step(server.slice(interStep));
            }

            if (pq.size() == 0)
                return;

            interStep = checkForDeparturesBeforeNextStep(nextStep);
        }

        // This client was processed the last time we checked for departures
        // that might occur before the next step but it didn't depart before
        // that time. However, we don't want to process it twice, so we 
        // remove it temporarily.
        Client processedInLastCheckForDepartures 
            = pq.size() > 0 ? pq.remove() : null;

        server.speed(speed * 1.0/(pq.size() + 1));

        // Process the rest of the clients. None of them will finish before
        // the next step, as the client with the lowest remaining service
        // didn't finish either.
        for (Client running : pq)
        {
            server.running(running);
            server.step(server.slice(nextStep));
        }

        if (processedInLastCheckForDepartures != null)
        {
            // Now that we've processed the other clients too, we should
            // put this one back into the queue.
            pq.add(processedInLastCheckForDepartures);
        }
    }

    public double work()
    {
        double work = 0.0;

        for (Client x : pq)
        {
            work += x.status();
        }

        return work;
    }

    public int active()
    {
        return pq.size();
    }

    public void speed(double speed)
    {
        this.speed = speed;
    }

    public Client[] flush()
    {
        Client[] client = new Client[pq.size()];

        for (int i = 0; !pq.isEmpty(); i++)
        {
            client[i] = pq.poll();
        }

        return client;
    }

    public static void main(String[] args)
    {
        PS ps = new PS();
        Client[] client = new Client[4];

        for (int i = 0; i < 4; i++)
        {
            client[i] = new Client(0.0, 1.0, i);
            ps.schedule(client[i]);
        }

        ps.step(0.5);

        for (Client x : client)
        {
            System.out.println(x);
            System.out.println();
        }
    }
}
