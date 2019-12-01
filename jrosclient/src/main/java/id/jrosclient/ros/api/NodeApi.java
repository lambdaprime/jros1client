package id.jrosclient.ros.api;

import java.util.List;

import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.responses.ProtocolParamsResponse;

public interface NodeApi {

    ProtocolParamsResponse requestTopic(String callerId, String topic, List<Protocol> protocols);
}
