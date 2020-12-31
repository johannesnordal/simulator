public class Client {
    private double step;
    private double status;
    private double arrival;
    private double service;
    private double waiting;
    private int id = -1;

    public Client(double arrival, double service) {
        this.step = arrival;
        this.status = service;
        this.arrival = arrival;
        this.service = service;
        this.waiting = 0.0;
    }

    public Client(double arrival, double service, int id) {
        this.step = arrival;
        this.status = service;
        this.arrival = arrival;
        this.service = service;
        this.waiting = 0.0;
        this.id = id;
    }

    public static Client[] batch(Distribution service, double arrival, int n) {
        Client[] batch = new Client[n];
        for (int i = 0; i < n; i++) {
            batch[i] = new Client(arrival, service.draw());
        }
        return batch;
    }

    public void step(double slice) {
        step += (slice < 0 ? 0.0 : slice);
    }

    public void status(double service) {
        status -= (status - service <= 0.0 ? 0.0 : service);
    }

    public void finish() {
        status = 0.0;
    }

    public void waiting(double wait) {
        waiting += (wait < 0.0 ? 0.0 : wait);
    }

    public void id(int id) {
        this.id = id;
    }

    public double step() {
        return step;
    }

    public double status() {
        return status;
    }

    public boolean isFinished() {
        return status <= 0.0;
    }

    public double arrival() {
        return arrival;
    }

    public double service() {
        return service;
    }

    public double waiting() {
        // return waiting; 
        return response() - service();
    }

    public double departure() {
        return step;
    }

    public double response() {
        return step - arrival; 
    }

    public double slowdown() {
        return response() / service;
    }

    public int id() {
        return id;
    }

    public static void main(String[] args) {
        Client client = new Client(0.2, 1.3);

        client.id(1);
        client.step(0.3);
        client.status(0.2);
        // client.waiting(0.1);

        System.out.println("step:\t\t" + client.step());
        System.out.println("status:\t\t" + client.status());
        System.out.println("arrival:\t" + client.arrival());
        System.out.println("service:\t" + client.service());
        System.out.println("waiting:\t" + client.waiting());
        System.out.println("departure:\t" + client.departure());
        System.out.println("id:\t\t" + client.id());
    }
}
