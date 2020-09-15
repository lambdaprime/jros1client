package id.jrosmessages.primitives;

import java.time.Instant;

import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

public class Time {

    /**
     * Seconds (stamp_secs) since epoch (unsigned)
     */
    @Streamed
    public int sec;
    
    /**
     * Nanoseconds since this.sec (unsigned)
     */
    @Streamed
    public int nsec;

    public Time() {

    }
    
    public Time(int sec, int nsec) {
        this.sec = sec;
        this.nsec = nsec;
    }

    @Override
    public String toString() {
        return XJson.asString("sec", Integer.toUnsignedString(sec),
                "nsec", Integer.toUnsignedString(nsec)).toString();
    }
    
    @Override
    public int hashCode() {
        return sec + nsec;
    }
    
    @Override
    public boolean equals(Object obj) {
        Time other = (Time) obj;
        return sec == other.sec
                && nsec == other.nsec;
    }

    public static Time now() {
        var now = Instant.now();
        return new Time((int) now.getEpochSecond(), now.getNano());
    }
}
