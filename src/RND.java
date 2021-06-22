package spool;

import java.util.Random;
import java.util.List;
import java.util.function.Supplier;

public class RND extends Dispatcher
{
    private Receiver[] receiver;
    private Random rnd;

    public RND(List<Supplier<Receiver>> supplierList)
    {
        receiver = new Receiver[supplierList.size()];

        for (int i = 0; i < supplierList.size(); i++)
        {
            receiver[i] = supplierList.get(i).get();
        }
    }

    public RND(Supplier<Receiver> supplier, int n)
    {
        receiver = new Receiver[n];

        for (int i = 0; i < n; i++)
        {
            receiver[i] = supplier.get();
        }
    }

    public RND(Receiver[] receiver)
    {
        this.receiver = receiver;
        rnd = new Random();
    }

    public void dispatch(Job incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        int i = rnd.nextInt(receiver.length);
        receiver[i].receive(incoming);
    }

    public String toString()
    {
        return "Random";
    }
}
