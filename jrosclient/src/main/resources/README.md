**jrosclient** - Java module which allows to access some of the ROS (Robotic Operation System) functionality. This module is focused on ROS for Java and not ROS for Android. That is why it is based on Java 11 API. Its ultimate goal is to implement ROS client libraries requirements: http://wiki.ros.org/Implementing%20Client%20Libraries

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
jrosclient <masterUrl> <nodePort> <command> [args ...]
```

Where:

masterUrl -- ROS master node URL
nodePort -- client node port to use
command -- one of the client commands

Available commands:

rostopic echo <topicName> <topicType> -- display messages published to a topic


# Examples
