**jrosclient** - Java module which allows to interact with ROS (Robotic Operation System).

lambdaprime <id.blackmesa@gmail.com>

# Download

You can download **jrosclient** from <https://github.com/lambdaprime/jrosclient/releases>

# Documentation

Documentation is available here <http://portal2.atwebpages.com/jrosclient>

# Usage

```bash
jrosclient <--masterUrl MASTER_URL> [--nodePort NODE_PORT] [--truncate MAX_LENGTH] [--debug] <COMMAND> [args ...]
```

Where:

MASTER_URL -- ROS master node URL

NODE_PORT -- client node port to use

COMMAND -- one of the client commands

Options:

--truncate - truncate objects logging

--debug - turns on debug mode

Available commands:

rostopic echo [-n count] <topicName> <topicType> -- display 'count' messages published to a topic. If 'count' is not set keep displaying messages forever.

rostopic list -- prints information about all publishers, subscribers in the system

# Examples

```bash
% jrosclient --masterUrl "http://localhost:11311/" --nodePort 1234 rostopic echo testTopic geometry_msgs/Point
{z=1.0, y=1.0, x=1.0}
% jrosclient --masterUrl "http://localhost:11311/" rostopic echo testTopic std_msgs/String
```
