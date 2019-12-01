package id.jrosclient.ros.entities;

import java.util.List;

import id.jrosclient.ros.api.impl.Utils;

public class Service implements Entity {

    public String service;
    public List<String> serviceProvider;

    @Override
    public String toString() {
        return String.format("{ \"topic\": \"%s\", \"topicSubscriber\": %s }", service,
                Utils.asArrayOfStrings(serviceProvider));
    }
}
