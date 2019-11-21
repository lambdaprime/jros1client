package id.jrosclient.ros.responses;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import id.jrosclient.ros.entities.Publisher;
import id.jrosclient.ros.entities.Service;
import id.jrosclient.ros.entities.Subscriber;
import id.jrosclient.ros.impl.Utils;

public class SystemStateResponse extends Response {

    public List<Publisher> publishers = new ArrayList<>();
    public List<Subscriber> subscribers = new ArrayList<>();
    public List<Service> services = new ArrayList<>();

    @Override
    public String toString() {
        return "{" + Stream.of(super.toString(), 
                "\"publishers\": " + Utils.asArray(publishers),
                "\"subscribers\": " + Utils.asArray(subscribers),
                "\"services\": " + Utils.asArray(services)).collect(Collectors.joining(", " )) + "}";
    }
}
