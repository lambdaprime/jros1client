package id.jrosclient.melodic;

import java.util.ArrayList;
import java.util.List;

public class SystemState extends Response {

    public List<Publisher> publishers = new ArrayList<>();
    public List<SubScriber> subscribers = new ArrayList<>();
    public List<Service> services = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(super.toString());
        b.append("Publishers: " + publishers).append('\n');
        b.append("Subscribers: " + subscribers).append('\n');
        b.append("Services: " + services).append('\n');
        return b.toString();
    }
}
