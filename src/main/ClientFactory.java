package spool;

import java.util.NoSuchElementException;

public class ClientFactory {
    private Distribution arrival;
    private Distribution service;
    private double clock = 0.0;
    private int i = 0;
    private int n;


    public ClientFactory(Distribution arrival, Distribution service, int n) {
        this.arrival = arrival;
        this.service = service;
        this.n = n;
    }

    public boolean hasNext() {
        return i < n;
    }

    public Client next() {
        if (i >= n) throw new NoSuchElementException();
        clock += arrival.sample();
        i++;
        return new Client(clock, service.sample());
    }
}
