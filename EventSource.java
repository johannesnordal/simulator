public interface EventSource<T> {
    public void registerObserver(Observer<T> observer);
    public void registerEvent(Event event, T type);
}
