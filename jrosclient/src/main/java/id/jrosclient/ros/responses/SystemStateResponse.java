package id.jrosclient.ros.responses;

import java.util.ArrayList;
import java.util.List;

import id.jrosclient.ros.entities.Publisher;
import id.jrosclient.ros.entities.Service;
import id.jrosclient.ros.entities.Subscriber;
import id.xfunction.XJson;

public class SystemStateResponse extends Response {

    public List<Publisher> publishers = new ArrayList<>();
    public List<Subscriber> subscribers = new ArrayList<>();
    public List<Service> services = new ArrayList<>();

    @Override
    public String toString() {
        return XJson.merge(super.toString(),
                XJson.asString(
                        "publishers", publishers,
                        "subscribers", subscribers,
                        "services", services));
    }
}
