package id.jrosclient.melodic;

import java.util.List;

public class Service {

    public String service;
    public List<String> serviceProvider;

    @Override
    public String toString() {
        return String.format("Service: %s [%s]", service, serviceProvider);
    }
}
