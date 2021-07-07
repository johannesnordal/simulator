package spool;

import java.util.Queue;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Comparator;
import static spool.Job.statusComparator;

public class PS extends Scheduler
{
    private Queue<Job> pq;
    private Server server;
    private double speed = 1.0;

    public PS()
    {
        pq = new PriorityQueue<Job>(statusComparator());
        server = new Server();
    }

    public void schedule(Job incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);
        pq.add(incoming);
    }

    // This method checks if the job with the lowest remaining service
    // departs before the next step. It does so simply by processing the job
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

        // If a job departs before the next step, we need to process
        // all jobs up to that point, because after the job leaves,
        // we need to re-adjust the processing power for the remaining
        // jobs.
        while (interStep < nextStep)
        {
            // If we reach this point, the job with the previously
            // lowest remaining service time (or equal) has been processed
            // to completion.
            registerEvent(Event.DEPARTURE, pq.remove());

            server.speed(speed * (1.0/(pq.size() + 1)));

            for (Job running : pq)
            {
                server.running(running);
                server.step(server.slice(interStep));
            }

            if (pq.size() == 0)
                return;

            interStep = checkForDeparturesBeforeNextStep(nextStep);
        }

        // This job was processed the last time we checked for departures
        // that might occur before the next step but it didn't depart before
        // that time. However, we don't want to process it twice, so we 
        // remove it temporarily.
        Job processedInLastCheckForDepartures 
            = pq.size() > 0 ? pq.remove() : null;

        server.speed(speed * 1.0/(pq.size() + 1));

        // Process the rest of the jobs. None of them will finish before
        // the next step, as the job with the lowest remaining service
        // didn't finish either.
        for (Job running : pq)
        {
            server.running(running);
            server.step(server.slice(nextStep));
        }

        if (processedInLastCheckForDepartures != null)
        {
            // Now that we've processed the other jobs too, we should
            // put this one back into the queue.
            pq.add(processedInLastCheckForDepartures);
        }
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

    public void speed(double speed)
    {
        this.speed = speed;
    }
}
