package id.jrosclient.melodic;

public interface MasterApi {

    SystemStateResponse getSystemState(String callerId);
    StringResponse getUri(String callerId);
    StringResponse lookupService(String callerId, String service);
}
