public interface Observer<T> {
    public void update(Event event, T type);
    public boolean isObserving(Event event);
}
