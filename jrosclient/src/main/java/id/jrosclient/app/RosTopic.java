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
package id.jrosclient.app;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import id.jrosclient.JRosClient;
import id.jrosclient.JRosClientConfiguration;
import id.jrosclient.TopicSubscriber;
import id.jrosclient.impl.ObjectsFactory;
import id.jrosclient.impl.TextUtils;
import id.jrosclient.ros.responses.Response.StatusCode;
import id.jrosmessages.Message;
import id.xfunction.cli.ArgumentParsingException;
import id.xfunction.cli.SmartArgs;
import id.xfunction.function.Unchecked;
import id.xfunction.lang.XRE;

public class RosTopic {

    private static final String CALLER_ID = "jrosclient-rostopic";
    private Optional<String> masterUrl;
    private JRosClientConfiguration config;
    private TextUtils utils;
    
    public RosTopic(Optional<String> masterUrl, JRosClientConfiguration config) {
        this.masterUrl = masterUrl;
        this.config = config;
        utils = new ObjectsFactory().createTextUtils(config);
    }

    public void execute(List<String> positionalArgs) {
        var rest = new LinkedList<>(positionalArgs);
        if (rest.isEmpty()) throw new ArgumentParsingException();
        var cmd = rest.removeFirst();
        switch (cmd) {
        case "echo":
            Unchecked.run(() -> echo(rest));
            break;
        case "list":
            Unchecked.run(() -> list());
            break;
        default: throw new ArgumentParsingException();
        }
    }

    private void list() throws Exception {
        try (JRosClient client = masterUrl.map(JRosClient::new).orElse(new JRosClient())) {
            var systemState = client.getMasterApi().getSystemState(CALLER_ID);
            if (systemState.statusCode != StatusCode.SUCCESS) {
                throw new XRE("Failed to get system status: %s", systemState.statusMessage);
            }
            System.out.println(systemState);
        }
    }

    private void echo(LinkedList<String> rest) throws Exception {
        LinkedList<String> positionalArgs = new LinkedList<>();
        int[] count = new int[1];
        Map<String, Consumer<String>> handlers = Map.of(
                "-n", n -> { count[0] = Integer.parseInt(n); }
        );
        new SmartArgs(handlers, positionalArgs::add).parse(rest.toArray(new String[0]));
        if (positionalArgs.size() < 2) throw new ArgumentParsingException();
        JRosClient client = masterUrl.map(url -> new JRosClient(url, config))
                .orElse(new JRosClient(config));
        var topic = positionalArgs.removeFirst();
        var topicType = positionalArgs.removeFirst();
        Class<Message> clazz = (Class<Message>) getClass().getClassLoader().loadClass(topicType);
        if (clazz == null)
            throw new XRE("Type %s is not found", topicType);
        var subscriber = new TopicSubscriber<Message>(clazz, topic) {
            @Override
            public void onNext(Message message) {
                System.out.println(utils.toString(message));
                count[0]--;
                if (count[0] == 0) { 
                    getSubscription().cancel();
                    Unchecked.run(() -> client.close());
                    return;
                }
            }
        };
        client.subscribe(subscriber
                .withInitialRequest(count[0]));
    }
    
}
