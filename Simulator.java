public interface Simulator {
    public void simulate(Observer<Client> observer,
            Distribution arrival,
            Distribution service,
            int numberOfClients);

    public Stats simulate(Distribution arrival,
            Distribution service,
            int numberOfClients);
}
