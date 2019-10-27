package id.jrosclient.ros.impl;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static <T> String asArray(List<T> l) {
        return "[" + l.stream()
                .map(T::toString)
                .collect(Collectors.joining(", ")) + "]";
    }

    public static <T> String asArrayOfStrings(List<T> l) {
        return "[" + l.stream()
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(", ")) + "]";
    }
}
