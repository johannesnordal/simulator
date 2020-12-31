public interface Collector {
    public void observer(Observer observer);
    public void arriving(Client client);
    public void departing(Client client);
}
