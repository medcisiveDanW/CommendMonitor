package com.medcisive.commend.monitor;

/**
 *
 * @author vhapalchambj
 */
public class GraphEventNormalized {
private java.util.LinkedHashMap<java.sql.Timestamp,Float> normalizedGraphEvents;
    private CommendEvent.Type           type = CommendEvent.Type.ERROR;
    private java.util.LinkedHashMap<java.sql.Timestamp,Integer> graphEvents;
    private java.text.SimpleDateFormat  formater    = new java.text.SimpleDateFormat("yyyy-MM-dd");

    public GraphEventNormalized(java.util.LinkedHashMap<java.sql.Timestamp,Integer> graphEvents) {
        this.graphEvents = graphEvents;
        graphEvents = new java.util.LinkedHashMap();
        java.util.LinkedHashMap<java.sql.Timestamp,CommendEvent> removeList = new java.util.LinkedHashMap();
        boolean first = true;
        for(java.sql.Timestamp t : graphEvents.keySet()) {
            int value = graphEvents.get(t);

        }
    }
    public java.util.LinkedHashMap<java.sql.Timestamp,Integer> getEventHash() {
        return graphEvents;
    }
}
