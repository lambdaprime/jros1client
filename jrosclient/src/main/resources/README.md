**jrosclient** - Java module which allows to interact with ROS (Robotic Operation System). This module is focused on ROS for Java and not ROS for Android. That is why it is based on Java 11 API (which is not available in Android). Its ultimate goal is to implement ROS client libraries requirements: http://wiki.ros.org/Implementing%20Client%20Libraries

This module still under development so any help is welcome.

lambdaprime <id.blackmesa@gmail.com>

# Requirements

- Java 11
- ROS (tested with Melodic)

# Download

You can download **jrosclient** from <https://github.com/lambdaprime/jrosclient/tree/master/jrosclient/release>

# Documentation

# Usage

```bash
jrosclient <--masterUrl MASTER_URL> <--nodePort NODE_PORT> [--debug] <COMMAND> [args ...]
```

Where:

MASTER\_URL -- ROS master node URL
NODE\_PORT -- client node port to use
COMMAND -- one of the client commands

Options:

--debug - turns on debug mode

Available commands:

rostopic echo <topicName> <topicType> -- display messages published to a topic

# Examples

```bash
% jrosclient --masterUrl "http://ubuntu:11311/" --nodePort 1234 rostopic echo testTopic geometry_msgs/Point
{z=1.0, y=1.0, x=1.0}
```
