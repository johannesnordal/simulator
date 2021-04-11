package spool;

public class BlockingServer implements Node, ServicingNode
{
    private Server server = new Server();

    public StatusCode receive(Client client)
    {
        double slice = server.slice(client.step());

        server.step(slice);

        if (server.isBusy())
        {
            return StatusCode.BLOCK;
        }
        else
        {
            server.running(client);
        }

        return StatusCode.ACCEPT;
    }

    public Server server()
    {
        return server;
    }

    public void sync(double nextStep)
    {
        if (!server.isBusy())
            return;

        double slice = server.slice(nextStep);

        server.step(slice);
    }

    public boolean admit(Client client)
    {
        if (server.isBusy())
            return false;

        server.running(client);

        return true;
    }

    public double remainingService()
    {
        if (!server.isBusy())
            return 0.0;

        return server.running().status();
    }
}
