/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jros1client
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.jros1client.app;

import id.jros1client.JRos1ClientConfiguration;
import id.xfunction.cli.ArgumentParsingException;
import id.xfunction.cli.SmartArgs;
import id.xfunction.logging.XLogger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class JRos1ClientApp {

    private static Optional<String> masterUrl = Optional.empty();
    private static JRos1ClientConfiguration config = new JRos1ClientConfiguration();
    private static Map<String, Consumer<String>> handlers =
            Map.of(
                    "--masterUrl",
                            url -> {
                                masterUrl = Optional.of(url);
                            },
                    "--nodePort",
                            port -> {
                                config.setNodeServerPort(Integer.parseInt(port));
                            },
                    "--truncate",
                            maxLength -> {
                                config.setMaxMessageLoggingLength(Integer.parseInt(maxLength));
                            });
    private static LinkedList<String> positionalArgs = new LinkedList<>();

    public static Stream<String> readResourceAsStream(String file) {
        try {
            return new BufferedReader(
                            new InputStreamReader(
                                    JRos1ClientApp.class
                                            .getClassLoader()
                                            .getResource(file)
                                            .openStream()))
                    .lines();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void usage() {
        readResourceAsStream("jrosclient-README.md").forEach(System.out::println);
        System.exit(1);
    }

    @SuppressWarnings("unused")
    private static <T> T withArg(Optional<T> arg) {
        if (arg.isEmpty()) {
            usage();
        }
        return arg.get();
    }

    public static void main(String[] args) {
        try {
            new SmartArgs(handlers, positionalArgs::add).parse(args);
            run();
        } catch (ArgumentParsingException e) {
            usage();
            System.exit(1);
        }
    }

    private static void run() {
        if (positionalArgs.isEmpty()) throw new ArgumentParsingException("Arguments are empty");
        var cmd = positionalArgs.removeFirst();
        switch (cmd) {
            case "rostopic":
                new RosTopic(masterUrl, config).execute(positionalArgs);
                break;
            case "--debug":
                {
                    enableDebug();
                    run();
                    break;
                }
            default:
                throw new ArgumentParsingException("Unknown command " + cmd);
        }
    }

    private static void enableDebug() {
        XLogger.load("jrosclient-debug.properties");
    }
}
