/*
 * Copyright 2020 jrosclient project
 *
 * Website: https://github.com/lambdaprime/jrosclient
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Java module which allows to interact with ROS1 (Robot Operating System).
 *
 * <p>For usage examples see <a href="http://portal2.atwebpages.com/jrosclient">Documentation</a>
 *
 * @see <a href= "https://github.com/lambdaprime/jrosclient/releases">Download</a>
 * @see <a href="https://github.com/lambdaprime/jrosclient">GitHub repository</a>
 * @author lambdaprime intid@protonmail.com
 */
module jros1client {
    requires xmlrpc.client;
    requires xmlrpc.server;
    requires xmlrpc.common;
    requires id.xfunction;
    requires id.ICE;
    requires transitive jrosclient;
    requires transitive jros1messages;
    requires java.logging;
    requires jrosmessages;

    exports id.jros1client;
    exports id.jros1client.ros.api;
    exports id.jros1client.ros.entities;
    exports id.jros1client.ros.responses;
    exports id.jros1client.impl to
            jros1client.tests,
            jros1services;
    exports id.jros1client.ros to
            jros1client.tests,
            id.xfunction;
    exports id.jros1client.ros.transport to
            jros1client.tests,
            jros1services;
    exports id.jros1client.ros.transport.io to
            jros1client.tests,
            jros1services;
}
