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
}
