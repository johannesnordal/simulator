package spool;

import java.util.Comparator;
import java.util.PriorityQueue;

public class LeastAttainedService extends Scheduler {
    private Comparator<Client> cmp;
    private PriorityQueue<Client> pq;
    private Server server;

    public LeastAttainedService() { 
        cmp = (x, y) -> {
            if (age(x) < age(y)) return -1;
            if (age(x) > age(y)) return 1;
            return 0;
        };
        pq = new PriorityQueue<>();
        server = new Server();
    }

    /**
     * Calculates the age, i.e. time in system, of given client.
     * @param client A client who's age will be calculated.
     * @return Client's age.
     */
    public double age(Client client) {
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
            server.step(server.slice(nextStep));
        }
        Client[] client = gatherClientsWithLeastAttainedService();
        double ageOfClientsInNextSet = findNextToLeastAttainedService();
    }

    // Given a set of clients with the least attained service,
    // the age of clients the next to least attained service,
    // and the next step, calculate the time slice for the 
    // clients with the least attained service before they
    // either a) belong to the next age group or b) the next time
    // step occurs.

    public double work() {
        return 0.0;
    }

    public int active() {
        return 0;
    }

    public static void main(String[] args) {
        int n = 10;
        Client[] client = new Client[n];
        Distribution arrival = new Exponential(1.0);
        Distribution service = new Exponential(2.0);
        double clock = 0.0;
        for (int i = 0; i < n; i++) {
            clock += arrival.sample();
            client[i] = new Client(clock, service.sample(), i);
        }
        for (Client x : client) {
            System.out.println(x);
            System.out.println();
        }
    }
}
