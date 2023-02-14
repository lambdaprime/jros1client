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
package id.jros1client.tests.integration;

import static id.jros1client.tests.integration.TestConstants.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import id.jros1client.JRos1ClientFactory;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.std_msgs.StringMessage;
import id.xfunction.ResourceUtils;
import id.xfunction.lang.XExec;
import id.xfunction.lang.XThread;
import id.xfunctiontests.AssertRunCommand;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JRos1ClientAppTests {

    private static final String JROSCLIENT_PATH =
            Paths.get("").toAbsolutePath().resolve("build/jros1client/jros1client").toString();
    private static final ResourceUtils resourceUtils = new ResourceUtils();
    private static final JRos1ClientFactory factory = new JRos1ClientFactory();

    @BeforeEach
    void setup() throws IOException {}

    @AfterEach
    void cleanup() throws IOException {}

    /** Test that client can reconnect successfully in case their connection was abruptly closed. */
    @Test
    public void test_client_reconnect() throws Exception {
        String topicName = "/testTopic2";
        var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topicName);
        var client = factory.createClient(URL);
        client.publish(publisher);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // publish message to the topic every second
        executor.submit(
                () -> {
                    while (!executor.isShutdown()) {
                        publisher.submit(new StringMessage().withData("Hello ROS"));
                        System.out.println("Published");
                        XThread.sleep(1000);
                    }
                });

        for (int i = 0; i < 3; i++) {
            var expected = new StringMessage().withData("Hello ROS").toString();
            new AssertRunCommand(
                            JROSCLIENT_PATH,
                            "--masterUrl",
                            URL,
                            "--nodePort",
                            "1234",
                            "rostopic",
                            "echo",
                            "-n",
                            "1",
                            "testTopic2",
                            "id.jrosmessages.std_msgs.StringMessage")
                    .assertOutput(expected)
                    .assertReturnCode(0)
                    .run();
        }

        client.unpublish(topicName, StringMessage.class);
        executor.shutdown();
        client.close();
    }

    @Test
    public void test_echo() {
        new AssertRunCommand(
                        JROSCLIENT_PATH,
                        "--masterUrl",
                        URL,
                        "--nodePort",
                        "1234",
                        "rostopic",
                        "echo",
                        "-n",
                        "5",
                        "testTopic",
                        "id.jrosmessages.std_msgs.StringMessage")
                .assertOutputFromResource("echo")
                .withOutputConsumer(System.out::println)
                .withWildcardMatching()
                .assertReturnCode(0)
                .run();

        new AssertRunCommand(
                        JROSCLIENT_PATH,
                        "rostopic",
                        "echo",
                        "-n",
                        "5",
                        "testTopic",
                        "id.jrosmessages.std_msgs.StringMessage")
                .assertOutputFromResource("echo")
                .withWildcardMatching()
                .assertReturnCode(0)
                .run();
    }

    @Test
    public void test_echo_infinity() throws Exception {
        var proc =
                new XExec(
                                JROSCLIENT_PATH,
                                "rostopic",
                                "echo",
                                "testTopic",
                                "id.jrosmessages.std_msgs.StringMessage")
                        .start();
        var future = new CompletableFuture<Void>();
        proc.forwardStderrAsync();
        var out = new ArrayList<String>();
        proc.stdoutAsync(
                line -> {
                    out.add(line);
                    if (out.size() == 5) future.complete(null);
                });
        future.get();
        proc.process().destroyForcibly();
        assertEquals(
                resourceUtils.readResource("echo").trim(),
                out.stream().collect(Collectors.joining("\n")));
    }

    @Test
    public void test_echo_missing_args() {
        new AssertRunCommand(
                        JROSCLIENT_PATH,
                        "--masterUrl",
                        URL,
                        "--nodePort",
                        "1234",
                        "rostopic",
                        "echo",
                        "testTopic")
                .assertOutput(resourceUtils.readResource("jrosclient-README.md"))
                .assertReturnCode(1)
                .run();
    }

    @Test
    public void test_wrong_args() throws Exception {
        new AssertRunCommand(JROSCLIENT_PATH)
                .assertOutput(resourceUtils.readResource("jrosclient-README.md"))
                .assertReturnCode(1)
                .run();
        new AssertRunCommand(JROSCLIENT_PATH, "rostopic")
                .assertOutput(resourceUtils.readResource("jrosclient-README.md"))
                .assertReturnCode(1)
                .run();
    }

    @Test
    public void test_debug() {
        new AssertRunCommand(
                        JROSCLIENT_PATH,
                        "--masterUrl",
                        URL,
                        "--nodePort",
                        "1234",
                        "--debug",
                        "rostopic",
                        "echo",
                        "-n",
                        "1",
                        "testTopic",
                        "id.jrosmessages.std_msgs.StringMessage")
                .assertOutputFromResource("debug")
                .withWildcardMatching()
                .assertReturnCode(0)
                .run();
    }

    @Test
    public void test_list() {
        new AssertRunCommand(
                        JROSCLIENT_PATH,
                        "--masterUrl",
                        URL,
                        "--nodePort",
                        "1234",
                        "rostopic",
                        "list")
                .assertOutputFromResource("list")
                .withWildcardMatching()
                .assertReturnCode(0)
                .run();
    }

    @Test
    public void test_truncate() {
        new AssertRunCommand(
                        JROSCLIENT_PATH,
                        "--masterUrl",
                        URL,
                        "--debug",
                        "--truncate",
                        "6",
                        "rostopic",
                        "echo",
                        "-n",
                        "1",
                        "testTopic",
                        "id.jrosmessages.std_msgs.StringMessage")
                .assertOutputFromResource("truncate")
                .withWildcardMatching()
                .assertReturnCode(0)
                .run();
    }

    @Test
    public void test_classpath() {
        new AssertRunCommand(
                        JROSCLIENT_PATH,
                        "--masterUrl",
                        URL,
                        "--nodePort",
                        "1234",
                        "rostopic",
                        "list")
                .assertOutputFromResource("list")
                .withWildcardMatching()
                .assertReturnCode(0)
                .withEnvironmentVariables(Map.of("CLASSPATH", "tmp/l.jar"))
                .run();
    }
}
