Tests for jrosclient library.

For integration tests to succeed make sure to run following:

```bash
% roscore
```

Create testTopic:

```bash
% rostopic pub -r 10 testTopic std_msgs/String "hello there"
```

# Useful commands

```bash
rostopic pub testTopic std_msgs/String "hello there"
rostopic pub testTopic std_msgs/Header '{seq: 123, stamp: 1111, frame_id: "aaaa"}'
rostopic pub testTopic std_msgs/ColorRGBA '{r: 0.12, g: 0.13, b: 0.14, a: 0.15}'
rostopic pub testTopic geometry_msgs/Vector3 '{x: 0.12, y: 0.13, z: 0.14}'
rostopic pub testTopic geometry_msgs/Point "{x: 1.0, y: 1.0, z: 1.0}"
rostopic pub testTopic geometry_msgs/Pose "{position: {x: 1.0, y: 1.0, z: 1.0}, orientation: {x: 1.0, y: 1.0, z: 1.0, w: 3.0}}"

rostopic pub testTopic visualization_msgs/Marker "{header: {seq: 123, stamp: 1111, frame_id: "aaaa"}, ns: "test", id: 123, type: 1, action: 0, pose: {position: {x: 1.0, y: 1.0, z: 1.0}, orientation: {x: 1.0, y: 1.0, z: 1.0, w: 3.0}}, scale: {x: 0.12, y: 0.13, z: 0.14}, color: {r: 0.12, g: 0.13, b: 0.14, a: 0.15}, lifetime: 1111, frame_locked: true}"
```