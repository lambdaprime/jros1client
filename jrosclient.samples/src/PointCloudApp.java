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
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import id.jrosclient.JRos1Client;
import id.jrosclient.JRosClientConfiguration;
import id.jrosclient.core.TopicSubmissionPublisher;
import id.jrosmessages.primitives.Time;
import id.jrosmessages.sensor_msgs.PointCloud2Message;
import id.jrosmessages.sensor_msgs.PointFieldMessage;
import id.jrosmessages.sensor_msgs.PointFieldMessage.DataType;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.xfunction.cli.CommandLineInterface;

/**
 * Reads point cloud from obj file and creates a message which
 * later publishes to the topic.
 * To see published point cloud use RViz.
 */
public class PointCloudApp {

    /**
     * Populates PointCloud2Message from obj file
     * @param srcObjFile path to obj file
     * @param dst message to populate
     */
    private static void populateFromObj(Path srcObjFile, PointCloud2Message dst) {
        var pointStep = 12;
        dst.withHeight(1)
            .withIsDense(true)
            .withPointStep(pointStep)
            .withFields(
                new PointFieldMessage().withName("x")
                    .withOffset(0)
                    .withCount(1)
                    .withDataType(DataType.FLOAT64),
                new PointFieldMessage().withName("y")
                    .withOffset(4)
                    .withCount(1)
                    .withDataType(DataType.FLOAT64),
                new PointFieldMessage().withName("z")
                    .withOffset(8)
                    .withCount(1)
                    .withDataType(DataType.FLOAT64));
        var buf = new ByteArrayOutputStream();
        try (var reader = Files.newBufferedReader(srcObjFile)) {
            var line = "";
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("v ")) continue;
                var a = line.split(" ");
                var b = ByteBuffer.allocate(pointStep)
                        .order(ByteOrder.nativeOrder());
                b.putFloat(Float.parseFloat(a[1]));
                b.putFloat(Float.parseFloat(a[2]));
                b.putFloat(Float.parseFloat(a[3]));
                buf.write(b.array());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        var data = buf.toByteArray();
        dst.withData(data);
        dst.withRowStep(data.length);
        dst.withWidth(data.length / pointStep);
    }
    
    public static void main(String[] args) throws Exception {
        var cli = new CommandLineInterface();
        var config = new JRosClientConfiguration();
        
        // printing pointcloud messages to standard output may cause
        // too much noise so we truncate any long lines in the output (optional)
        config.setMaxMessageLoggingLength(1200);
        
        // defining topic name
        String topic = "/PointCloud";
        
        try (var client = new JRos1Client("http://localhost:11311/", config)) {
            var publisher = new TopicSubmissionPublisher<>(PointCloud2Message.class, topic);
            client.publish(publisher);
            PointCloud2Message pointCloud = new PointCloud2Message()
                    .withHeader(new HeaderMessage()
                            .withFrameId("map")
                            .withStamp(Time.now()));
            var path = Paths.get(PointCloudApp.class.getResource("sample.obj").getFile());
            populateFromObj(path, pointCloud);
            cli.print("Press any key to stop publishing...");
            while (!cli.wasKeyPressed()) {
                publisher.submit(pointCloud);
                cli.print("Published");
                Thread.sleep(1000);
            }
        }
    }
    
}