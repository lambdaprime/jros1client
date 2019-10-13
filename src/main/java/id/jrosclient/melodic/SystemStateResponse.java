package id.jrosclient.melodic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SystemStateResponse extends Response {

    public List<Publisher> publishers = new ArrayList<>();
    public List<SubScriber> subscribers = new ArrayList<>();
    public List<Service> services = new ArrayList<>();

    @Override
    public String toString() {
        return "{" + Stream.of(super.toString(), 
                "\"publishers\": " + Utils.asArray(publishers),
                "\"subscribers\": " + Utils.asArray(subscribers),
                "\"services\": " + Utils.asArray(services)).collect(Collectors.joining(", " )) + "}";
    }
}
