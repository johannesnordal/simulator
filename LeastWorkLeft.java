import java.util.Random;
import java.util.ArrayList;

public class LeastWorkLeft {
    private Scheduler[] scheduler;

    public LeastWorkLeft(Scheduler[] scheduler) {
        this.scheduler = scheduler;    
    }

    public void dispatch(Client incoming) {
        ArrayList<Integer> x = new ArrayList<Integer>(scheduler.length);
        double min = Double.MAX_VALUE;
        for (int i = 0; i < scheduler.length; i++) {
            scheduler[i].step(incoming.arrival());
            double work = scheduler[i].work();
            if (work == min)
                x.add(i);
            if (work < min) {
                min = work;
                x = new ArrayList<Integer>(scheduler.length);
                x.add(i);
            }
        }
        int i = new Random().nextInt(x.size());
        scheduler[x.get(i)].schedule(incoming);
    }
}
