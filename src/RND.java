package spool;

import java.util.Random;
import java.util.function.Supplier;

public class RND extends Dispatcher
{
    private Receiver[] receiver;
    private Random rnd;

    private RND(Receiver[] receiver)
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
