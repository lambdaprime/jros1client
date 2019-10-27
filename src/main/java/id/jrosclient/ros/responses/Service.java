package id.jrosclient.ros.responses;

import java.util.List;

public class Service {

    public String service;
    public List<String> serviceProvider;

    @Override
    public String toString() {
        return String.format("{ \"topic\": \"%s\", \"topicSubscriber\": %s }", service,
                Utils.asArrayOfStrings(serviceProvider));
    }
}
