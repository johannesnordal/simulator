public class Server {
    private Client running = null;
    private double speed = 1.0;

    public void step(double slice) {
        if (running.status() <= 0.0)
            return;
        if (running.status()/speed - slice <= 0.0) {
            running.step(running.status()/speed);
            running.finish();
            return;
        }
        running.step(slice);
        running.status(slice * speed);
    }

    public double slice(double nextStep) {
        if (running == null) return 0.0;
        double slice = nextStep - running.step();
        return slice > 0.0 ? slice : 0.0;
    }

    public boolean isBusy() {
        if (running == null) return false;
        return running.status() > 0.0;
    }

    public Client running() {
        return running;
    }

    public void running(Client running) {
        this.running = running;
    }

    public void speed(double speed) {
        this.speed = speed;
    }

    public double speed() {
        return speed;
    }

    public static void main(String[] args) {
        Server s1 = new Server();
        Server s2 = new Server();

        s1.running(new Client(0.0, 2.0));
        s2.running(new Client(1.0, 3.0));
        
        double slice1 = s1.slice(1.5);
        double slice2 = s2.slice(1.5);
        System.out.println(slice1);
        System.out.println(slice2);
        System.out.println(s1.running().step());
        System.out.println(s2.running().step());
        s1.step(slice1);
        s2.step(slice2);
        System.out.println(s1.running().step());
        System.out.println(s2.running().step());
    }
}
