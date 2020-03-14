package id.jrosclient.ros.transport;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Optional;

import static id.jrosclient.ros.transport.ConnectionHeader.*;

public class ConnectionHeaderWriter {

    private DataOutput out;

    public ConnectionHeaderWriter(DataOutput out) {
        this.out = out;
    }

    public void write(ConnectionHeader header) throws IOException {
        int len = 0;
        len += len(CALLER_ID, header.callerId) + 4;
        len += len(TOPIC, header.topic) + 4;
        len += len(TYPE, header.type) + 4;
        len += len(MESSAGE_DEFINITION, header.messageDefinition) + 4;
        len += len(MD5_SUM, header.md5sum) + 4;
        writeLen(out, len);
        writeField(CALLER_ID, header.callerId, out);
        writeField(TOPIC, header.topic, out);
        writeField(TYPE, header.type, out);
        writeField(MESSAGE_DEFINITION, header.messageDefinition, out);
        writeField(MD5_SUM, header.md5sum, out);
    }

    private void writeLen(DataOutput out, int len) throws IOException {
        out.writeInt(Integer.reverseBytes(len));
    }

    private void writeField(String field, Optional<String> value, DataOutput out) throws IOException {
        if (value.isEmpty()) return;
        writeLen(out, len(field, value));
        out.write(field.getBytes());
        out.write('=');
        out.write(value.get().getBytes());
    }

    private int len(String field, Optional<String> value) {
        int len = lenField(value, field.length());
        len += 1; // '='
        return len;
    }

    private int lenField(Optional<String> field, int len) {
        return field.map(String::length).orElse(0) +
                (field.isPresent()? len: 0);
    }

}