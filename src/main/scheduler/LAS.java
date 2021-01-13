package spool;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.OptionalDouble;
import static spool.Misc.compareDouble;
import static spool.Client.serviceAgeComparator;
import static spool.Client.statusComparator;

public class LAS extends Scheduler {
    private PriorityQueue<Client> pq;
    private Server server;
    private double clock = 0.0;

    public LAS() { 
        pq = new PriorityQueue<>(serviceAgeComparator());
        server = new Server();
    }

    /**
     * Calculates the age, i.e. time in system, of given client.
     * @param client A client who's age will be calculated.
     * @return Client's age.
     */
    public static double age(Client client) {
        return client.service() - client.status();
    }

    /**
     * Schedules client to run in the system. Since this client's age
     * will be zero on arrival it will run immediately on the next step,
     * either solo or in parallel with another client of the same age.
     * @param incoming Lastest arriving client.
     */
    public void schedule(Client incoming) { 
        pq.offer(incoming);
    }

    public void step(double nextStep) {
        if (pq.isEmpty()) return;
        if (pq.size() == 1) {
            server.running(pq.peek());
            clock = nextStep;
            server.step(server.slice(clock));
            if (!server.isBusy()) {
                registerEvent(Event.DEPARTURE, server.running());
            }
            return;
        }

        PriorityQueue<Client> las = leastAttainedService();
        OptionalDouble op = nextLowestServiceAge();
        if (op.isPresent() && !las.isEmpty()) {
            double nextAge = op.getAsDouble();
            double interStep = interStep(las, nextAge, nextStep);
            PS ps = new PS();
            for (Client x : las)
                ps.schedule(x);
            ps.step(interStep);
            for (Client x : las) {
                if (x.isFinished())
                    registerEvent(Event.DEPARTURE, x);
                else
                    pq.offer(x);
            }
        } else {
            PS ps = new PS();
            for (Client x : las)
                ps.schedule(x);
            ps.step(nextStep);
            for (Client x : las) {
                if (x.isFinished())
                    registerEvent(Event.DEPARTURE, x);
                else
                    pq.offer(x);
            }
        }
    }

    private PriorityQueue<Client> leastAttainedService() {
        Comparator<Client> cmp = (x, y) -> {
            if (x.status() < y.status()) return -1;
            if (x.status() > y.status()) return 1;
            return 0;
        };
        PriorityQueue<Client> las = new PriorityQueue<>(cmp);

        Client client = pq.poll();
        las.offer(client);
        while (!pq.isEmpty()) {
            if (compareDouble(age(client), age(pq.peek()))) {
                client = pq.poll();
                las.offer(client);
            } else {
                break;
            }
        }
        return las;
    }
    
    private OptionalDouble nextLowestServiceAge() {
        if (pq.isEmpty()) return OptionalDouble.empty();
        return OptionalDouble.of(age(pq.peek()));
    }

    private double interStep(PriorityQueue<Client> las, double nextAge,
            double nextStep) {
        double gap = nextAge - age(las.peek());
        double interStep = gap * las.size() + clock;
        return nextStep < interStep ? nextStep : interStep;
    }

    private double slice(List<Client> las, double interStep) {
        server.running(las.get(0));
        return server.slice(interStep);
    }

    public double work() {
        return 0.0;
    }

    public int active() {
        return 0;
    }

    private static void info(Client x) {
        System.out.println(x);
        System.out.println("Age:\t" + age(x));
        System.out.println();
    }

    // PriorityQueue where all jobs have the same age and the
    // job with the least amount of work left has the highest
    // priority in the queue.
    private static double untitled(PriorityQueue<Client> las) {
        return 0.0;
    }

    public static void main(String[] args) {
        Client[] client = new Client[5];

        client[0] = new Client(0.0, 5.0, 0);
        client[1] = new Client(0.0, 3.0, 1);
        client[2] = new Client(0.0, 2.0, 2);
        client[3] = new Client(0.0, 4.0, 3);
        client[4] = new Client(0.0, 7.0, 4);

        Server server = new Server();
        for (Client x : client) {
            server.running(x);
            server.step(1);
            info(x);
        }

        Comparator<Client> cmp = statusComparator();
    }
}
