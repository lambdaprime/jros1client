Tests for jrosclient library.

For integration tests to succeed make sure to run following:

```bash
% source /opt/ros/melodic/setup.zsh
% roscore
```

Create testTopic:

```bash
% source /opt/ros/melodic/setup.zsh
% rostopic pub -r 10 testTopic std_msgs/String "hello there"
```
