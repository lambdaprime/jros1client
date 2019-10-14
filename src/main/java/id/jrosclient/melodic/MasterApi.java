package id.jrosclient.melodic;

import id.jrosclient.melodic.responses.ListResponse;
import id.jrosclient.melodic.responses.StringResponse;
import id.jrosclient.melodic.responses.SystemStateResponse;

public interface MasterApi {

    SystemStateResponse getSystemState(String callerId);
    StringResponse getUri(String callerId);
    StringResponse lookupService(String callerId, String service);
    ListResponse<String> registerPublisher(String callerId, String topic, String topicType, String callerApi);
}
