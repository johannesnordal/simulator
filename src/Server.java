package spool;

public class Server
{
    private Job running = null;
    private double speed = 1.0;

    public Server()
    {
        running = null;
        speed = 1.0;
    }

    public Server(double speed)
    {
        this.speed = speed;
    }

    public void step(double slice)
    {
        if (running == null)
            return;

        if (running.status() <= 0.0)
            return;

        if (running.status()/speed - slice <= 0.0)
        {
            running.step(running.status()/speed);
            running.finish();
            return;
        }

        running.step(slice);
        running.status(slice * speed);
    }

    public double slice(double nextStep)
    {
        if (running == null) 
        {
            return 0.0;
        }

        double slice = nextStep - running.step();

        return slice > 0.0 ? slice : 0.0;
    }

    public boolean isBusy()
    {
        if (running == null)
        {
            return false;
        }

        return running.status() > 0.0;
    }

    public void finish()
    {
        if (running == null || running.isFinished())
            return;

        step(running.status() * speed);
    }

    public Job running()
    {
        return running;
    }

    public void running(Job running)
    {
        this.running = running;
    }

    public void speed(double speed)
    {
        this.speed = speed;
    }

    public double speed()
    {
        return speed;
    }

    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);
        for (int i = 0; i < n; i++)
        {
            Distribution arrival = new Exponential(1.0);
            Distribution service = new Exponential(2.0);
            Job job = new Job(arrival.sample(), service.sample());

            Server server = new Server();
            server.running(job);
            server.finish();

            if (server.isBusy())
                System.out.println(server.isBusy());
        }
    }
}
