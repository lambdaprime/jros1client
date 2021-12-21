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
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.std_msgs.StringMessage;
import id.xfunction.AssertRunCommand;
import id.xfunction.ResourceUtils;
import id.xfunction.lang.XThread;

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

        for (int i = 0; i < 3; i++) {
            var expected = new StringMessage()
                    .withData("Hello ROS")
                    .toString();
        	new AssertRunCommand(JROSCLIENT_PATH,
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
    	    	.withOutput(expected + "\n\n")
    	    	.withReturnCode(0)
    	    	.run();
        }

        client.unpublish(topicName);
        executor.shutdown();
        client.close();
    }

    @Test
    public void test_echo() {
    	new AssertRunCommand(JROSCLIENT_PATH,
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
	    	.withOutputFromResource("echo")
	    	.withWildcardMatching()
	    	.withReturnCode(0)
	    	.run();

    	new AssertRunCommand(JROSCLIENT_PATH,
    			"rostopic",
    			"echo",
    			"-n",
    			"5",
    			"testTopic",
    			"id.jrosmessages.std_msgs.StringMessage")
	    	.withOutputFromResource("echo")
	    	.withWildcardMatching()
	    	.withReturnCode(0)
	    	.run();
    }

    @Test
    public void test_echo_missing_args() {
    	new AssertRunCommand(JROSCLIENT_PATH,
    			"--masterUrl",
    			URL,
    			"--nodePort",
    			"1234",
    			"rostopic",
    			"echo",
    			"testTopic")
	    	.withOutput(resourceUtils.readResource("jrosclient-README.md") + "\n\n")
	    	.withReturnCode(1)
	    	.run();
    }
    
    @Test
    public void test_wrong_args() throws Exception {
    	new AssertRunCommand(JROSCLIENT_PATH)
    		.withOutput(resourceUtils.readResource("jrosclient-README.md") + "\n\n")
	    	.withReturnCode(1)
	    	.run();
    	new AssertRunCommand(JROSCLIENT_PATH,
    			"rostopic")
    		.withOutput(resourceUtils.readResource("jrosclient-README.md") + "\n\n")
	    	.withReturnCode(1)
	    	.run();
    }

    @Test
    public void test_debug() {
        new AssertRunCommand(JROSCLIENT_PATH,
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
	       	.withOutputFromResource("debug")
	       	.withWildcardMatching()
	       	.withReturnCode(0)
	       	.run();
    }

    @Test
    public void test_list() {
        new AssertRunCommand(JROSCLIENT_PATH,
	       		 "--masterUrl",
	       		 URL,
	       		 "--nodePort",
	       		 "1234",
	       		 "rostopic",
	       		 "list")
	       	.withOutputFromResource("list")
	       	.withWildcardMatching()
	       	.withReturnCode(0)
	       	.run();
    }
    
    @Test
    public void test_truncate() {
        new AssertRunCommand(JROSCLIENT_PATH,
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
        	.withOutputFromResource("truncate")
        	.withWildcardMatching()
        	.withReturnCode(0)
        	.run();
    }
    
    @Test
    public void test_classpath() {
        new AssertRunCommand(JROSCLIENT_PATH,
	       		 "--masterUrl",
	       		 URL,
	       		 "--nodePort",
	       		 "1234",
	       		 "rostopic",
	       		 "list")
	       	.withOutputFromResource("list")
	       	.withWildcardMatching()
	       	.withReturnCode(0)
	       	.withEnvironmentVariables(Map.of("CLASSPATH", "tmp/l.jar"))
	       	.run();
    }
}
