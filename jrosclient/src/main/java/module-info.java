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
 * Java module which allows to interact with ROS (Robot Operating System).
 *
 * @see <a href= "https://github.com/lambdaprime/jrosclient/releases">Download</a>
 * @see <a href="https://github.com/lambdaprime/jrosclient">GitHub repository</a>
 * @author lambdaprime intid@protonmail.com
 */
module jrosclient {
    requires xmlrpc.client;
    requires xmlrpc.server;
    requires xmlrpc.common;
    requires id.xfunction;
    requires id.kineticstreamer;
    requires id.ICE;
    requires java.logging;
    requires transitive jrosmessages;

    exports id.jrosclient;
    exports id.jrosclient.ros.api;
    exports id.jrosclient.ros.entities;
    exports id.jrosclient.ros.responses;
    exports id.jrosclient.impl to
            jrosclient.tests;
    exports id.jrosclient.ros to
            jrosclient.tests;
    exports id.jrosclient.ros.transport to
            jrosclient.tests;
    exports id.jrosclient.ros.transport.io to
            jrosclient.tests;
}
