open module jrosclient.tests {
    requires org.junit.jupiter.api;
    requires jrosclient;
    requires id.xfunction;
    requires id.kineticstreamer;
    requires org.junit.jupiter.params;
    
    exports id.jrosclient.tests;
    
    exports id.jrosmessages.tests;
}