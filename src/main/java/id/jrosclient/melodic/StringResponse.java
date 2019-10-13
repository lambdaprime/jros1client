package id.jrosclient.melodic;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringResponse extends Response {

    public final String key;
    public String value = "";

    public StringResponse(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "{" + Stream.of(super.toString(),
                String.format("\"%s\": \"%s\"", key, value)).collect(Collectors.joining(", " )) + "}";
    }
}
