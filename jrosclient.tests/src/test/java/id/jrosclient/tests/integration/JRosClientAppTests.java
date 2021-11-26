/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jrosclient
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
/*
 * Authors:
 * - lambdaprime <intid@protonmail.com>
 */
/**
 * Copyright 2020 lambdaprime
 * 
 * Email: intid@protonmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jrosclient.tests.integration;

import static id.jrosclient.tests.integration.TestConstants.URL;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.std_msgs.StringMessage;
import id.xfunction.ResourceUtils;
import id.xfunction.lang.XExec;
import id.xfunction.lang.XThread;
import id.xfunction.text.WildcardMatcher;

public class JRosClientAppTests {

    private static final String JROSCLIENT_PATH = Paths.get("")
            .toAbsolutePath()
            .resolve("build/jrosclient/jrosclient")
            .toString();
    private static final ResourceUtils resourceUtils = new ResourceUtils();

    @BeforeEach
    void setup() throws IOException {
    }
    
    @AfterEach
    void cleanup() throws IOException {
    }
    
    /**
     * Test that client can reconnect successfully in case their connection
     * was abruptly closed.
     */
    @Test
    public void test_client_reconnect() throws Exception {
        String topicName = "/testTopic2";
        var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topicName);
        var client = new JRosClient(URL);
        client.publish(publisher);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // publish message to the topic every second
        executor.submit(() -> {
            while (!executor.isShutdown()) {
                publisher.submit(new StringMessage().withData("Hello ROS"));
                System.out.println("Published");
                XThread.sleep(1000);
            }
        });

        String actual, expected;
        var args = String.format("--masterUrl %s --nodePort 1234 rostopic echo -n 1 testTopic2 id.jrosmessages.std_msgs.StringMessage",
                URL);
        for (int i = 0; i < 3; i++) {
            actual = runOk(args)
                    .replace("\n", "");
            expected = new StringMessage()
                    .withData("Hello ROS")
                    .toString();
            Assertions.assertEquals(expected, actual);
        }

        client.unpublish(topicName);
        executor.shutdown();
        client.close();
    }

    @Test
    public void test_echo() {
        var args = String.format("--masterUrl %s --nodePort 1234 rostopic echo -n 5 testTopic id.jrosmessages.std_msgs.StringMessage",
                URL);
        var out = runOk(args);
        Assertions.assertTrue(new WildcardMatcher(resourceUtils.readResource("echo")).matches(out));
        args = String.format("rostopic echo -n 5 testTopic id.jrosmessages.std_msgs.StringMessage");
        out = runOk(args);
        Assertions.assertTrue(new WildcardMatcher(resourceUtils.readResource("echo")).matches(out));
    }

    @Test
    public void test_echo_missing_args() {
        var args = String.format("--masterUrl %s --nodePort 1234 rostopic echo testTopic",
                URL);
        var out = runFail(args);
        Assertions.assertEquals(resourceUtils.readResource("jrosclient-README.md") + "\n\n", out);
    }
    
    @Test
    public void test_wrong_args() throws Exception {
        var out = runFail("");
        Assertions.assertEquals(resourceUtils.readResource("jrosclient-README.md") + "\n\n", out);
        out = runFail("rostopic");
        Assertions.assertEquals(resourceUtils.readResource("jrosclient-README.md") + "\n\n", out);
    }

    @Test
    public void test_debug() {
        var args = String.format("--masterUrl %s --nodePort 1234 --debug rostopic echo -n 1 testTopic id.jrosmessages.std_msgs.StringMessage",
                URL);
        var out = runOk(args);
        Assertions.assertTrue(new WildcardMatcher(resourceUtils.readResource("debug")).matches(out));
    }

    @Test
    public void test_list() {
        var args = String.format("--masterUrl %s --nodePort 1234 rostopic list",
                URL);
        var out = runOk(args);
        Assertions.assertTrue(new WildcardMatcher(resourceUtils.readResource("list")).matches(out));
    }
    
    @Test
    public void test_truncate() {
        var args = String.format("--masterUrl %s --debug --truncate 6 rostopic echo -n 1 testTopic id.jrosmessages.std_msgs.StringMessage",
                URL);
        var out = runOk(args);
        Assertions.assertTrue(new WildcardMatcher(resourceUtils.readResource("truncate")).matches(out));
    }
    
    private String runFail(String fmt, Object...args) {
        return run(1, fmt, args);
    }

    private String runOk(String fmt, Object...args) {
        return run(0, fmt, args);
    }
    
    private String run(int expectedCode, String fmt, Object...args) {
        var proc = new XExec(JROSCLIENT_PATH + " " + String.format(fmt, args))
                .run();
        proc.flush(false);
        var code = proc.await();
        var out = proc.stdoutAsString() + "\n" + proc.stderrAsString() + "\n";
        System.out.println("Output:");
        System.out.println(">>>");
        System.out.print(out);
        System.out.println("<<<");
        Assertions.assertEquals(expectedCode, code);
        return out;
    }
    
}
