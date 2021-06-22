package spool;

import java.util.function.Supplier;

public class RR extends Dispatcher
{
    private Receiver[] receiver;
    private int i = 0;

    public RR(Supplier<Receiver> supplier, int n)
    {
        receiver = new Receiver[n];

        for (int j = 0; j < n; j++)
        {
            receiver[j] = supplier.get();
        }
    }

    public RR(Receiver[] receiver)
    {
        this.receiver = receiver;
    }
    
    public void dispatch(Job incoming)
    {
        registerEvent(Event.ARRIVAL, incoming);

        receiver[i].receive(incoming);
        i = (i += 1) % receiver.length;
    }

    public String toString()
    {
        return "Round Robin";
    }
}
