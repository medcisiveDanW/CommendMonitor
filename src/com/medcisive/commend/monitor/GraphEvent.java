package com.medcisive.commend.monitor;

import java.sql.Timestamp;

/**
 *
 * @author vhapalchambj
 */
public class GraphEvent {
    private java.util.LinkedHashMap<java.sql.Timestamp,CommendEvent> commendEvents;
    private CommendEvent.Type           type = CommendEvent.Type.ERROR;
    private java.util.LinkedHashMap<java.sql.Timestamp,Integer> graphEvents;
    private java.text.SimpleDateFormat  formater    = new java.text.SimpleDateFormat("yyyy-MM-dd");
    private int                         id;
    private boolean                     isBoolean = false;
    private static java.util.HashMap<Integer,String> eventMap = new java.util.HashMap();
    static {
        // Client Events
        eventMap.put(0, "Commend client listener started");
        eventMap.put(1, "Provider logged in");
        eventMap.put(2, "Patient selected");
        eventMap.put(3, "Commend main window displayed");
        eventMap.put(4, "Commend Browser closed");
        eventMap.put(5, "Download error");
        eventMap.put(6, "Window close event");
        // Server Events
        eventMap.put(1000, "Database controller setup");
        eventMap.put(1001, "MDWSAdaptor created");
        eventMap.put(1002, "Commend Dashboard Selected");
        eventMap.put(1003, "Patient Panel Selected");
        eventMap.put(1004, "Patient Details Selected");
        eventMap.put(1005, "MDWS note writing failed");
        eventMap.put(1006, "MDWS note saved");
        eventMap.put(1007, "Patient selection Error");
        eventMap.put(1008, "CommendWebappManager is not valid");
        eventMap.put(1009, "MDWS connection failure");
        eventMap.put(1010, "Custom Outcome save failure.");
        eventMap.put(1011, "Measures.");
        eventMap.put(1013, "TherapyMode");
        eventMap.put(1014, "SymptomFunctioning");
        eventMap.put(1015, "GoalTracking");



        // Database builder Events
        eventMap.put(1000000, "Database Builder Started");
        eventMap.put(1000001, "Database Builder Finished");
        eventMap.put(1000002, "Build Duration");
        eventMap.put(1000003, "Note threads have started");
        eventMap.put(1000004, "Note threads have ended");
        eventMap.put(1000005, "Note threads Duration");
        eventMap.put(1000006, "Number of MDWS errors");
        eventMap.put(1000007, "Number of patients processed");
        eventMap.put(1000008, "Commend Access Deletes");
        eventMap.put(1000009, "MDWS Threads Timeout");
        eventMap.put(1000010, "SQL Encounters");
        eventMap.put(1000011, "SQL Demographics");
        eventMap.put(1000012, "SQL Studies");
        eventMap.put(1000013, "SQL PrescriptionsRaw");
        eventMap.put(1000014, "SQL Prescriptions");
        eventMap.put(1000015, "SQL Conditions");
        eventMap.put(1000016, "SQL MedicationManagementVAonly");
        eventMap.put(1000017, "SQL EncounterSummaries");
        eventMap.put(1000018, "Main proccess timeout exception");
    }
    public GraphEvent(java.util.LinkedHashMap<java.sql.Timestamp,CommendEvent> commendEvents) {
        this.commendEvents = commendEvents;
        graphEvents = new java.util.LinkedHashMap();
        java.util.LinkedHashMap<java.sql.Timestamp,CommendEvent> removeList = new java.util.LinkedHashMap();
        boolean first = true;
        for(java.sql.Timestamp t : commendEvents.keySet()) { // clear non-types and set event type
            if(first) {
                type = commendEvents.get(t).type;
                id = commendEvents.get(t).id;
                isBoolean = hasBoolean(commendEvents.get(t).description);
                first = false;
            }
            if(type != commendEvents.get(t).type) {
                removeList.put(t,commendEvents.get(t));
            }
        }
        for(java.sql.Timestamp t : removeList.keySet()) { commendEvents.remove(t); }

        if(type==CommendEvent.Type.COUNT) {
            processCountType();
        }
        else if(type==CommendEvent.Type.DURATION) {
            processDurationType();
        }
        else if(type==CommendEvent.Type.NUMERIC) {
            processNumericType();
        }
    }
    private void processCountType() {
        for(java.sql.Timestamp t : commendEvents.keySet()) {
            java.sql.Timestamp day = getDayTimestamp(t);
            boolean isTrue = getBoolean(commendEvents.get(t).description);
            if(day!=null) {
                if(graphEvents.containsKey(day)) {
                    if(isBoolean) {
                        if(isTrue) {
                            Integer i = graphEvents.get(day);
                            i++;
                            graphEvents.put(day, i);
                        }
                    }
                    else {
                        Integer i = graphEvents.get(day);
                        i++;
                        graphEvents.put(day, i);
                    }
                }
                else {
                    graphEvents.put(day, 1);
                }
            }
        }
    }
    private void processDurationType() {
        for(java.sql.Timestamp t : commendEvents.keySet()) {
            CommendEvent ce = commendEvents.get(t);
            int duration = (int)ce.getDuration(ce.description);
            graphEvents.put(t,duration);
        }
    }
    private void processNumericType() {
        for(java.sql.Timestamp t : commendEvents.keySet()) {
            CommendEvent ce = commendEvents.get(t);
            int duration = (int)ce.getNumeric(ce.description);
            graphEvents.put(t,duration);
        }
    }
    private java.sql.Timestamp getDayTimestamp(java.sql.Timestamp day) {
        java.util.Date dayStr = null;
        java.sql.Timestamp result = null;
        try {
            dayStr = formater.parse(day.toString());
        }
        catch (Exception e) {}
        if(dayStr!=null) {
            result = new java.sql.Timestamp(dayStr.getTime());
        }
        return result;
    }
    private boolean hasBoolean(String str) {
        boolean result = false;
        if(str.contains("true") || str.contains("false")) {
            result = true;
        }
        return result;
    }
    private boolean getBoolean(String str) {
        boolean result = false;
        if(str.contains("true")) {
            result = true;
        }
        return result;
    }
    public java.util.LinkedHashMap<java.sql.Timestamp,Integer> getEventHash() {
        return graphEvents;
    }
    public String getEventData() {
        String name = eventMap.get(id);
        String result = "\"" + name + "\": { label: \"" + name + "\", data:[";
        for(java.sql.Timestamp date: graphEvents.keySet()) {
            result += "[" + date.getTime() + "," + graphEvents.get(date) + "],";
        }
        result = result.substring(0,result.lastIndexOf(','));
        result += "] }";
        return result;
    }
}
