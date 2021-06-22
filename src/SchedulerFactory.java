package spool;

import java.util.List;
import java.util.function.Supplier;

public class SchedulerFactory
{
    public static Scheduler[] get(Supplier<Scheduler> supplierList, int n)
    {
        Scheduler[] scheduler = new Scheduler[n];

        for (int i = 0; i < n; i++)
        {
            scheduler[i] = supplierList.get();
        }

        return scheduler;
    }

    public static Scheduler get(String name)
    {
        switch (name.toUpperCase())
        {
            case "FCFS" :
                return new FCFS();
            case "LCFS" :
                return new LCFS();
            case "PS" :
                return new PS();
            case "SJF" :
                return new SJF();
            case "SRPT" :
                return new SRPT();
            default :
                return null;
        }
    }

    public static Scheduler[] get(String name, int n)
    {
        Scheduler[] scheduler = new Scheduler[n];

        for (int i = 0; i < n; i++)
        {
            scheduler[i] = get(name);
        }

        return scheduler;
    }

    public static Scheduler[] get(String ...name)
    {
        Scheduler[] scheduler = new Scheduler[name.length];

        for (int i = 0; i < name.length; i++)
        {
            scheduler[i] = get(name[i]);
        }

        return scheduler;
    }

    public static void main(String[] args)
    {
    }
}
