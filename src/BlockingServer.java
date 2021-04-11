package spool;

public class BlockingServer implements Node
{
    private Server server = new Server();

    public boolean receive(Client client)
    {
        double slice = server.slice(client.step());

        server.step(slice);

        if (server.isBusy())
        {
            return false;
        }
        else
        {
            server.running(client);
        }

        return true;
    }

    public Server server()
    {
        return server;
    }
}
