package id.jrosclient.ros;

import id.jrosclient.ros.responses.ListResponse;
import id.jrosclient.ros.responses.StringResponse;
import id.jrosclient.ros.responses.SystemStateResponse;

public interface MasterApi {

    SystemStateResponse getSystemState(String callerId);
    StringResponse getUri(String callerId);
    StringResponse lookupService(String callerId, String service);
    ListResponse<String> registerPublisher(String callerId, String topic, String topicType);
    ListResponse<String> registerSubscriber(String callerId, String topic, String topicType);
}
