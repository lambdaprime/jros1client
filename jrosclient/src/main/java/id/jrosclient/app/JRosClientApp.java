package id.jrosclient.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import id.xfunction.ArgumentParsingException;
import id.xfunction.SmartArgs;

public class JRosClientApp {

    private static Optional<String> masterUrl = Optional.empty();
    private static Optional<Integer> nodePort = Optional.empty();
    private static Map<String, Consumer<String>> handlers = Map.of(
        "--masterUrl", url -> { masterUrl = Optional.of(url); },
        "--nodePort", port -> { nodePort = Optional.of(Integer.parseInt(port));}
    );
    private static LinkedList<String> positionalArgs = new LinkedList<>();

    public static Stream<String> readResourceAsStream(String file) {
        try {
            return new BufferedReader(new InputStreamReader(
                    JRosClientApp.class.getClassLoader().getResource(file).openStream())).lines();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void usage() {
        readResourceAsStream("usage.txt")
            .forEach(System.out::println);
        System.exit(1);
    }

    private static <T> T withArg(Optional<T> arg) {
        if (arg.isEmpty()) {
            usage();
        }
        return arg.get();
    }
    
    public static void main(String[] args) {
        try {
            new SmartArgs(handlers, positionalArgs::add).parse(args);
            if (positionalArgs.isEmpty()) throw new ArgumentParsingException();
            var cmd = positionalArgs.removeFirst();
            switch (cmd) {
            case "rostopic" : new RosTopic(withArg(masterUrl), withArg(nodePort))
                .execute(positionalArgs); 
                break;
            default: throw new ArgumentParsingException();
            }
        } catch (ArgumentParsingException e) {
            usage();
            System.exit(1);
        }
    }

}
