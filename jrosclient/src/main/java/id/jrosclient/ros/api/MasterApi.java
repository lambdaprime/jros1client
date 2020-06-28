package id.jrosclient.ros.api;

import id.jrosclient.ros.responses.ListResponse;
import id.jrosclient.ros.responses.StringResponse;
import id.jrosclient.ros.responses.SystemStateResponse;

/**
 * @see <a href="http://wiki.ros.org/ROS/Master_API">ROS Maste API</a>
 */
public interface MasterApi {

    /**
     * Retrieve list representation of system state (i.e. publishers, subscribers,
     * and services)
     * 
     * @param callerId ROS caller ID
     */
    SystemStateResponse getSystemState(String callerId);
    
    /**
     * Get the URI of the master
     * 
     * @param callerId ROS caller ID
     * @return masterURI
     */
    StringResponse getUri(String callerId);
        
    /**
     * Register the caller as a publisher the topic
     * 
     * @param callerId ROS caller ID 
     * @param topic Fully-qualified name of topic
     * @param topicType Message name
     * @return List of current subscribers of topic
     */
    ListResponse<String> registerPublisher(String callerId, String topic, String topicType);
    
    /**
     * Subscribe the caller to the specified topic
     * 
     * @param callerId ROS caller ID 
     * @param topic Fully-qualified name of topic.
     * @param topicType Message name
     * @return list of current publishers
     */
    ListResponse<String> registerSubscriber(String callerId, String topic, String topicType);
    
    StringResponse lookupService(String callerId, String service);
}
