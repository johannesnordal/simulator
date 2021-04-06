import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.function.*;
import spool.*;

public class Tail implements Observer {

    private Stats.Builder builder = new Stats.Builder();
    private Stats stats = null;

    private PriorityQueue<Client> queue;
    private Comparator<Client> cmp;
    private int n;

    public Tail(Comparator<Client> cmp, int n) {
        queue = new PriorityQueue<>(cmp);
        this.cmp = cmp;
        this.n = n;
    }

    public void update(Event event, Client client) {
        if (event == Event.DEPARTURE) {
            filter(client);
        }
    }

    private void filter(Client client) {
        if (queue.isEmpty() || cmp.compare(client, queue.peek()) > 0) {
            queue.add(client);
            if (queue.size() > n) queue.remove();
        }
    }

    public Stats stats() {
        if (stats == null) {
            for (Client client : queue) {
                builder.update(Event.DEPARTURE, client);
            }
            stats = builder.build();
        }
        return stats;
    }

    public static void main(String[] args) {
        Tail tail = new Tail(Client.serviceComparator(), 500_000);
        Scheduler scheduler = new FCFS().register(tail);

        int n = 10_000_000;
        Distribution arrival = new Exponential(1);
        Distribution service = new Exponential(2);
        Client.streamOf(arrival, service, n).forEach(scheduler::receive);

        Stats stats = tail.stats();
        System.out.println(stats.response().first());
    }
}
