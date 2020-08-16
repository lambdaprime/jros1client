package id.jrosclient.ros.transport.io;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Optional;

import id.jrosclient.ros.transport.ConnectionHeader;

import static id.jrosclient.ros.transport.ConnectionHeader.*;

public class ConnectionHeaderWriter {

    private DataOutput out;
    private Utils utils = new Utils();

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
        utils.writeLen(out, len);
        writeField(CALLER_ID, header.callerId);
        writeField(TOPIC, header.topic);
        writeField(TYPE, header.type);
        writeField(MESSAGE_DEFINITION, header.messageDefinition);
        writeField(MD5_SUM, header.md5sum);
    }
    
    private void writeField(String field, Optional<String> value) throws IOException {
        if (value.isEmpty()) return;
        utils.writeLen(out, len(field, value));
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
