/*
 * Copyright 2021 jrosclient project
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
import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.std_msgs.StringMessage;

/**
 * Creates a new topic and publishes messages to it.
 */
public class PublisherApp {

    public static void main(String[] args) throws Exception {
        // specify URL of the master node
        var client = new JRosClient("http://localhost:11311/");
        String topicName = "/helloRos";
        var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topicName);
        // register a new publisher for a new topic with ROS
        client.publish(publisher);
        while (true) {
            publisher.submit(new StringMessage().withData("Hello ROS"));
            System.out.println("Published");
            Thread.sleep(1000);
        }
    }
}

