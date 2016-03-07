package com.medcisive.commend.monitor;

/**
 *
 * @author vhapalchambj
 */
public class CommendEvent {
    public java.sql.Timestamp   date;
    public String               computer;
    public String               SSN;
    public String               DUZ;
    public int                  id;
    public String               description;
    public Type                 type;
    public enum Type {COUNT, DURATION, NUMERIC, ERROR};

    public CommendEvent(java.util.LinkedHashMap<String,Object> map) {
        date = (java.sql.Timestamp)map.get("eventtime");
        computer = (String)map.get("clientComputer");
        SSN = (String)map.get("patientSSN");
        DUZ = (String)map.get("providerDUZ");
        id = (Integer)map.get("eventID");
        description = (String)map.get("event");
        type = getType(description);
    }
    private Type getType(String str) {
        Type result = Type.COUNT;
        if (isDuration(str)) {
            result = Type.DURATION;
        }
        else if (isNumeric(str)) {
            result = Type.NUMERIC;
        }
        return result;
    }
    private boolean isDuration(String str) {
        //0:17:36
        long result = getDuration(str);
        if(result>-1) { return true; }
        return false;
    }
    public long getDuration(String str) {
        long result = -1;
        String hourStr = pullNumber(str);
        long hour = getNumeric(hourStr);
        str = chopRemander(str);
        String minuteStr = pullNumber(str);
        long minute = getNumeric(minuteStr);
        String secondStr = chopRemander(str);
        long second = getNumeric(secondStr);
        hour = hour*60*60;
        minute = minute*60;
        if( (hour>-1) && (minute>-1) && (second>-1) ) { result = hour + minute + second; }
        return result;
    }
    private boolean isNumeric(String str) {
        long result = getNumeric(str);
        if(result>-1) { return true; }
        return false;
    }
    private String pullNumber(String str) {
        String result = null;
        int index = str.indexOf(":");
        try {
            result = str.substring(0,index);
        }
        catch (Exception e) {}
        return result;
    }
    private String chopRemander(String str) {
        String result = null;
        int index = str.indexOf(":");
        try {
            result = str.substring(index+1,str.length());
        }
        catch (Exception e) {}
        return result;
    }
    public long getNumeric(String str) {
        long result = -1;
        if(str!=null) {
            try {
                result = Long.parseLong(str);
            }
            catch (Exception e) {}
        }
        return result;
    }
}
