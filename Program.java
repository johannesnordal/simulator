import spool.*;

public class Program implements Observer {

    private double a;
    private double b;
    private Stats.Builder builder;

    public Program(double a, double b) {
        this.a = a;
        this.b = b;
        builder = new Stats.Builder();
    }

    public void update(Event event, Client client) {
        if (event == Event.ARRIVAL) {
            if (a <= client.service() && client.service()) {
                builder.update(event, client);
            }
        }
    }

    public Stats 

    public static void main(String[] args) {
        
    }
}
