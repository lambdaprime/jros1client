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
 * - lambdaprime <id.blackmesa@gmail.com>
 */
/**
 * Copyright 2020 lambdaprime
 * 
 * Email: id.blackmesa@gmail.com 
 * Website: https://github.com/lambdaprime
 * 
 */
package id.jrosclient.tests.integration;

import static id.xfunction.XUtils.readResource;

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
import id.xfunction.TemplateMatcher;
import id.xfunction.XExec;
import id.xfunction.XUtils;

public class JRosClientAppTests {

    private static final String JROSCLIENT_PATH = Paths.get("")
            .toAbsolutePath()
            .resolve("build/jrosclient/jrosclient")
            .toString();

    @BeforeEach
    void setup() throws IOException {
    }
    
    @AfterEach
    void cleanup() throws IOException {
    }
    
    @Test
    public void test() throws Exception {
        test_no_args();
        test_echo();
        test_echo_missing_args();
        test_debug();
        test_list();
    }

    /**
     * Test that client can reconnect successfully in case their connection
     * was abruptly closed.
     */
    @Test
    public void test_client_reconnect() throws Exception {
        String topicName = "/testTopic2";
        var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topicName);
        var client = new JRosClient("http://ubuntu:11311/");
        client.publish(publisher);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (!executor.isShutdown()) {
                publisher.submit(new StringMessage().withData("Hello ROS"));
                System.out.println("Published");
                XUtils.sleep(1000);
            }
        });

        String actual, expected;

        for (int i = 0; i < 3; i++) {
            actual = runOk("--masterUrl http://ubuntu:11311/ --nodePort 1234 rostopic echo -n 1 testTopic2 std_msgs/String")
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

    private void test_echo() {
        var out = runOk("--masterUrl http://ubuntu:11311/ --nodePort 1234 rostopic echo -n 5 testTopic std_msgs/String");
        Assertions.assertTrue(new TemplateMatcher(readResource("echo")).matches(out));
    }

    private void test_echo_missing_args() {
        var out = runFail("--masterUrl http://ubuntu:11311/ --nodePort 1234 rostopic echo testTopic");
        Assertions.assertEquals(readResource("README.md") + "\n\n", out);
    }
    
    private void test_no_args() throws Exception {
        var out = runFail("");
        Assertions.assertEquals(readResource("README.md") + "\n\n", out);
    }

    private void test_debug() {
        var out = runOk("--masterUrl http://ubuntu:11311/ --nodePort 1234 --debug rostopic echo -n 1 testTopic std_msgs/String");
        Assertions.assertTrue(new TemplateMatcher(readResource("debug")).matches(out));
    }

    private void test_list() {
        var out = runOk("--masterUrl http://ubuntu:11311/ --nodePort 1234 rostopic list");
        Assertions.assertTrue(new TemplateMatcher(readResource("list")).matches(out));
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
        System.out.print(out);
        Assertions.assertEquals(expectedCode, code);
        return out;
    }
    
}
