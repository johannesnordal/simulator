package spool;

import java.util.Comparator;
import java.util.PriorityQueue;
import static spool.Job.statusComparator;
import static spool.Job.stepComparator;

public class SRPT extends Scheduler
{
    private PriorityQueue<Job> pq;
    private Server server;

    public SRPT()
    {
        Comparator<Job> cmp = (x, y) -> {

            Comparator<Job> status = statusComparator();
            Comparator<Job> step = stepComparator();

            int ord = status.compare(x, y);

            if (ord == 0) {
                ord = step.compare(x, y);
            }

            return ord;
        };

        pq = new PriorityQueue<Job>(cmp);
        server = new Server();
    }

    public void schedule(Job incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);
        pq.offer(incoming);

        server.running(pq.peek());
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
        Job running = pq.remove();

        registerEvent(Event.DEPARTURE, running);

        if (pq.isEmpty())
        {
            server.running(null);
            return;
        }

        Job next = pq.peek();

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

    public static void main(String[] args)
    {
        Scheduler scheduler = new SRPT();

        Job x = new Job(0, 8);    // arrival=0, service=8
        Job y = new Job(1, 1);    // arrival=1, service=1

        scheduler.step(x.arrival());    // no effect
        scheduler.schedule(x);          // puts x in server

        scheduler.step(y.arrival());    // step 0..1
        scheduler.schedule(y);          // preempt x from server
        double status = x.status();     // 7

        scheduler.step(2);              // step 1..2
        status = y.status();            // 0
        y.isFinished();                 // true

    }
}
