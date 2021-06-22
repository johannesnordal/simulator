package spool;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;

public class SchedulerPrototype
{
    private Supplier<Scheduler> supplier;
    private List<Supplier<Observer>> list;

    public SchedulerPrototype(Supplier<Scheduler> supplier)
    {
        this.supplier = supplier;
        this.list = new ArrayList<>();
    }

    public void registerObserver(Supplier<Observer> supplier)
    {
        list.add(supplier);
    }

    public Scheduler get()
    {
        Scheduler scheduler = supplier.get();

        for (Supplier<Observer> x : list)
        {
            scheduler.registerObserver(x.get());
        }

        return scheduler;
    }
}
