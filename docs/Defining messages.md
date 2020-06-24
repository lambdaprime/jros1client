Need to use default ctor and initialize all fields to default non null values otherwise the kineticstreamer will ignore them when writing to byte stream. Due to default ctor that way we can avoid all other ctors and allow user to overwrite only those values default values which needed through the setter methods

ROS keeps message definitions under /opt/ros/melodic/share
Let's see how to add Marker.msg to JRosClient

float64 - double
float32 - float
uint32 - int
time - Time

# Find message definition md5sum
```bash
% rosmsg md5 geometry_msgs/Quaternion
```


geometry_msgs/Pose pose                 # Pose of the object
Point position
float64 x
float64 y
float64 z

Quaternion orientation
float64 x
float64 y
float64 z
float64 w





geometry_msgs/Vector3 scale             # Scale of the object 1,1,1 means default (usually 1 meter square)
std_msgs/ColorRGBA color             # Color [0.0-1.0]
geometry_msgs/Point[] points


rostopic pub testTopic std_msgs/String "hello there"
rostopic pub testTopic std_msgs/Header '{seq: 123, stamp: 1111, frame_id: "aaaa"}'
rostopic pub testTopic std_msgs/ColorRGBA '{r: 0.12, g: 0.13, b: 0.14, a: 0.15}'
rostopic pub testTopic geometry_msgs/Vector3 '{x: 0.12, y: 0.13, z: 0.14}'
rostopic pub testTopic geometry_msgs/Point "{x: 1.0, y: 1.0, z: 1.0}"
rostopic pub testTopic geometry_msgs/Pose "{position: {x: 1.0, y: 1.0, z: 1.0}, orientation: {x: 1.0, y: 1.0, z: 1.0, w: 3.0}}"

rostopic pub testTopic visualization_msgs/Marker "{header: {seq: 123, stamp: 1111, frame_id: "aaaa"}, ns: "test", id: 123, type: 1, action: 0, pose: {position: {x: 1.0, y: 1.0, z: 1.0}, orientation: {x: 1.0, y: 1.0, z: 1.0, w: 3.0}}, scale: {x: 0.12, y: 0.13, z: 0.14}, color: {r: 0.12, g: 0.13, b: 0.14, a: 0.15}, lifetime: 1111, frame_locked: true}"