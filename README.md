**jrosclient** - Java module which allows to interact with ROS (Robotic Operation System). This module is focused on ROS for Java and not ROS for Android. That is why it is based on Java 11 API (which is not available in Android). Its ultimate goal is to implement ROS client libraries requirements: http://wiki.ros.org/Implementing%20Client%20Libraries

Many people run their applications, webservices in Java. The problems they may encounter is when they try to interact from their existing logic with ROS. What sounds as easy thing to do is in fact complicated because there is no many Java based ROS clients. Some of them already outdated other require additional knowledge of catkin, Python or running a separate web service.

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

rostopic echo [-n count] <topicName> <topicType> -- display 'count' messages published to a topic. If 'count' is not set keep displaying messages forever.

rostopic list -- prints information about all publishers, subscribers in the system

# Examples

```bash
% jrosclient --masterUrl "http://ubuntu:11311/" --nodePort 1234 rostopic echo testTopic geometry_msgs/Point
{z=1.0, y=1.0, x=1.0}
```

# Samples

Java samples can be found at <https://github.com/lambdaprime/jrosclient/tree/master/jrosclient.samples>

