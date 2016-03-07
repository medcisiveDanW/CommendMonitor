package com.medcisive.commend.monitor;

import com.medcisive.utility.sql2.*;

/**
 *
 * @author vhapalchambj
 */
public class DatabaseManager extends DBCUtil {
    private static java.sql.Timestamp today = CommendMonitorManager.today;
    private static java.util.LinkedHashMap<Integer,java.util.LinkedHashMap<java.sql.Timestamp,CommendEvent>> events = null;
    private static java.util.LinkedHashMap<Integer,GraphEvent> graphEvents = null;

    public DatabaseManager() {}

    public SQLTable getTodayUsage() {
        com.medcisive.utility.Timer t = com.medcisive.utility.Timer.start();
        //modifyed on 22 July 2014 to only look back to thouse who have used Commend in the last year
        String q =
                "SELECT cp.name "
                + "      ,MAX(clt.eventtime) as lastCPRS "
                + "      ,MAX(com.eventtime) as lastCOMMEND "
                + "FROM Commend.dbo.CommendLogTrace clt, "
                + "     Commend.dbo.CommendLogTrace com, "
                + "     Commend.dbo.CommendProviders cp "
                + "WHERE clt.eventID = 1 "
                + "  AND (com.eventID BETWEEN 1003 AND 1004 or com.eventID BETWEEN 1019 AND 1043) "
                + "  AND clt.providerDUZ = cp.DUZ "
                + "  AND com.providerDUZ = cp.DUZ "
                + "  AND clt.eventtime > DATEADD(MONTH,-3,GETDATE()) "
                + "  AND com.eventtime > clt.eventtime "
                + "GROUP BY clt.providerDUZ, cp.name "
                + "ORDER BY cp.name";
        SQLTable result = _dest.getTable(q);
        t.print();
        return result;
    }

    public java.util.HashMap<String, Object> getTodayDatabaseInfo() {
        com.medcisive.utility.Timer t = com.medcisive.utility.Timer.start();
        java.util.HashMap<String, Object> result = new java.util.HashMap();
        String q =
                "SELECT MAX(eventtime) as dbstart "
                + "FROM Commend.dbo.CommendLogTrace "
                + "WHERE eventID = 1000000";
        SQLTable dbstart = _dest.getTable(q);
        java.sql.Timestamp start = (java.sql.Timestamp) dbstart.getRow(0).get("dbstart");
        result.put("start", start);

        q =
                "SELECT MAX(eventtime) as dbend "
                + "FROM Commend.dbo.CommendLogTrace "
                + "WHERE eventID = 1000005";
        SQLTable dbend = _dest.getTable(q);
        java.sql.Timestamp end = (java.sql.Timestamp) dbend.getRow(0).get("dbend");
        result.put("end", end);
        long duration = end.getTime() - start.getTime();
        duration /= 1000;
        result.put("duration", duration);

        java.sql.Timestamp yesterday = new java.sql.Timestamp(today.getTime());
        yesterday.setTime(yesterday.getTime() - (24 * 60 * 60 * 1000));
        boolean warning = false;
        if (end.getTime() < yesterday.getTime()) {
            warning = true;
        }
        result.put("warning", warning);

        q =
                "SELECT TOP 1 event "
                + "FROM Commend.dbo.CommendLogTrace "
                + "WHERE eventID = 1000007 "
                + "ORDER BY eventtime DESC";
        SQLTable patients = _dest.getTable(q);
        String numbOfPatients = (String) patients.getRow(0).get("event");
        result.put("numberOfPatients", numbOfPatients);

        q =
                "SELECT TOP 1 eventtime \n" +
                "FROM Commend.dbo.CommendLogTrace \n" +
                "WHERE eventID = '1000021' \n" +
                "ORDER BY eventtime DESC";
        SQLTable visnstartmap = _dest.getTable(q);
        java.sql.Timestamp visnstart = (java.sql.Timestamp) visnstartmap.getRow(0).get("eventtime");
        result.put("visnstart", visnstart);

        q =
                "SELECT TOP 1 eventtime \n" +
                "FROM Commend.dbo.CommendLogTrace \n" +
                "WHERE eventID = '1000022' \n" +
                "ORDER BY eventtime DESC";
        SQLTable visnendtmap = _dest.getTable(q);
        java.sql.Timestamp visnend = (java.sql.Timestamp) visnendtmap.getRow(0).get("eventtime");
        result.put("visnend", visnend);

        long visnduration = visnend.getTime() - visnstart.getTime();
        visnduration /= 1000;
        result.put("visnduration", visnduration);
        t.print();
        return result;
    }

    public java.util.HashMap<String, Object> getProviderUsages() {
        com.medcisive.utility.Timer t = com.medcisive.utility.Timer.start();
        java.util.HashMap<String, Object> result = new java.util.HashMap();
        String q =
                "SELECT cp.name, "
                + "       COUNT (log.eventID) as COMMEND2Weeks "
                + "FROM Commend.dbo.CommendLogTrace log, "
                + "     Commend.dbo.CommendProviders cp "
                + "WHERE (log.eventID BETWEEN 1003 AND 1004 or log.eventID BETWEEN 1019 AND 1043) "
                + "  AND cp.DUZ = log.providerDUZ "
                + "  AND log.eventtime BETWEEN DATEADD(day,-14,GETDATE()) AND DATEADD(day,0,GETDATE()) "
                + "group by cp.name, log.eventID";
        SQLTable COMMEND2Weeks = _dest.getTable(q);
        for (Integer key : COMMEND2Weeks.keySet()) {
            java.util.LinkedHashMap<String, Object> map = COMMEND2Weeks.getRow(key);
            String name = (String) map.get("name");
            if (name != null) {
                if (result.get(name) == null) {
                    result.put(name, map);
                } else {
                    java.util.LinkedHashMap<String, Object> rMap = (java.util.LinkedHashMap<String, Object>) result.get(name);
                    Integer firstCount = (Integer) map.get("COMMEND2Weeks");
                    if (firstCount != null) {
                        Integer secondCount = (Integer) rMap.get("COMMEND2Weeks");
                        if (secondCount != null) {
                            firstCount += secondCount;
                            rMap.put("COMMEND2Weeks", firstCount);
                        }
                    }
                }
            }
        }
        q =
                "SELECT cp.name, "
                + "     COUNT (log.eventID) as COMMEND4Weeks "
                + "FROM Commend.dbo.CommendLogTrace log, "
                + "     Commend.dbo.CommendProviders cp "
                + "WHERE (log.eventID BETWEEN 1003 AND 1004 or log.eventID BETWEEN 1019 AND 1043) "
                + "  and cp.DUZ = log.providerDUZ "
                + "  and log.eventtime BETWEEN DATEADD(day,-30,GETDATE()) AND DATEADD(day,0,GETDATE()) "
                + "group by cp.name, log.eventID";
        SQLTable COMMEND4Weeks = _dest.getTable(q);
        for (Integer key : COMMEND4Weeks.keySet()) {
            java.util.LinkedHashMap<String, Object> map = COMMEND4Weeks.getRow(key);
            String name = (String) map.get("name");
            if (name != null) {
                if (result.get(name) == null) {
                    result.put(name, map);
                } else {
                    java.util.LinkedHashMap<String, Object> rMap = (java.util.LinkedHashMap<String, Object>) result.get(name);
                    Integer firstCount = (Integer) map.get("COMMEND4Weeks");
                    Integer secondCount = (Integer) rMap.get("COMMEND4Weeks");
                    if (firstCount != null && secondCount == null) {
                        rMap.put("COMMEND4Weeks", firstCount);
                    } else if (firstCount != null && secondCount != null) {
                        firstCount += secondCount;
                        rMap.put("COMMEND4Weeks", firstCount);
                    }
                }
            }
        }
        q =
                "SELECT cp.name, "
                + "       COUNT (eventID) as CPRS2Weeks "
                + "FROM Commend.dbo.CommendLogTrace clt, "
                + "     Commend.dbo.CommendProviders cp "
                + "WHERE eventID = 1 "
                + "  and cp.DUZ = clt.providerDUZ "
                + "  and eventtime BETWEEN DATEADD(day,-14,GETDATE()) AND DATEADD(day,0,GETDATE()) "
                + "group by cp.name, clt.eventID";
        SQLTable CPRS2Weeks = _dest.getTable(q);
        for (Integer key : CPRS2Weeks.keySet()) {
            java.util.LinkedHashMap<String, Object> map = CPRS2Weeks.getRow(key);
            String name = (String) map.get("name");
            if (name != null) {
                if (result.get(name) == null) {
                    result.put(name, map);
                } else {
                    java.util.LinkedHashMap<String, Object> rMap = (java.util.LinkedHashMap<String, Object>) result.get(name);
                    rMap.put("CPRS2Weeks", map.get("CPRS2Weeks"));
                }
            }
        }
        q =
                "SELECT cp.name, "
                + "       COUNT (eventID) as CPRS4Weeks "
                + "FROM Commend.dbo.CommendLogTrace clt, "
                + "     Commend.dbo.CommendProviders cp "
                + "WHERE eventID = 1 "
                + "  and cp.DUZ = clt.providerDUZ "
                + "  and eventtime BETWEEN DATEADD(day,-30,GETDATE()) AND DATEADD(day,0,GETDATE()) "
                + "group by cp.name, clt.eventID";
        SQLTable CPRS4Weeks = _dest.getTable(q);
        for (Integer key : CPRS4Weeks.keySet()) {
            java.util.LinkedHashMap<String, Object> map = CPRS4Weeks.getRow(key);
            String name = (String) map.get("name");
            if (name != null) {
                if (result.get(name) == null) {
                    result.put(name, map);
                } else {
                    java.util.LinkedHashMap<String, Object> rMap = (java.util.LinkedHashMap<String, Object>) result.get(name);
                    rMap.put("CPRS4Weeks", map.get("CPRS4Weeks"));
                }
            }
        }
        q =
                "SELECT cp.name, "
                + "MAX (clt.eventtime) as LastNote "
                + "FROM Commend.dbo.CommendLogTrace clt, "
                + "     Commend.dbo.CommendProviders cp "
                + "WHERE eventID = 1006 "
                + "  and cp.DUZ = clt.providerDUZ "
                + "  and eventtime > DATEADD(MONTH,-12,GETDATE())"
                + "GROUP BY cp.name, clt.eventID";
        SQLTable LastNote = _dest.getTable(q);
        for (Integer key : LastNote.keySet()) {
            java.util.LinkedHashMap<String, Object> map = LastNote.getRow(key);
            String name = (String) map.get("name");
            if (name != null) {
                if (result.get(name) == null) {
                    result.put(name, map);
                } else {
                    java.util.LinkedHashMap<String, Object> rMap = (java.util.LinkedHashMap<String, Object>) result.get(name);
                    rMap.put("LastNote", map.get("LastNote"));
                }
            }
        }
        q =
                "SELECT cp.name, "
                + "COUNT (eventID) as Note2Weeks "
                + "FROM Commend.dbo.CommendLogTrace clta, "
                + "     Commend.dbo.CommendProviders cp "
                + "WHERE eventID = 1006 "
                + "  and cp.DUZ = clta.providerDUZ "
                + "  and eventtime BETWEEN DATEADD(day,-14,GETDATE()) AND DATEADD(day,0,GETDATE()) "
                + "group by cp.name, clta.eventID";
        SQLTable Note2Weeks = _dest.getTable(q);
        for (Integer key : Note2Weeks.keySet()) {
            java.util.LinkedHashMap<String, Object> map = Note2Weeks.getRow(key);
            String name = (String) map.get("name");
            if (name != null) {
                if (result.get(name) == null) {
                    result.put(name, map);
                } else {
                    java.util.LinkedHashMap<String, Object> rMap = (java.util.LinkedHashMap<String, Object>) result.get(name);
                    rMap.put("Note2Weeks", map.get("Note2Weeks"));
                }
            }
        }
        q =
                "SELECT cp.name, "
                + "COUNT (eventID) as Note4Weeks "
                + "FROM Commend.dbo.CommendLogTrace clta, "
                + "     Commend.dbo.CommendProviders cp "
                + "WHERE eventID = 1006 "
                + " and cp.DUZ = clta.providerDUZ "
                + " and eventtime BETWEEN DATEADD(day,-30,GETDATE()) AND DATEADD(day,0,GETDATE()) "
                + "group by cp.name, clta.eventID";
        SQLTable Note4Weeks = _dest.getTable(q);
        for (Integer key : Note4Weeks.keySet()) {
            java.util.LinkedHashMap<String, Object> map = Note4Weeks.getRow(key);
            String name = (String) map.get("name");
            if (name != null) {
                if (result.get(name) == null) {
                    result.put(name, map);
                } else {
                    java.util.LinkedHashMap<String, Object> rMap = (java.util.LinkedHashMap<String, Object>) result.get(name);
                    rMap.put("Note4Weeks", map.get("Note4Weeks"));
                }
            }
        }
        t.print();
        return result;
    }

    public SQLTable getFeedback() {
        com.medcisive.utility.Timer t = com.medcisive.utility.Timer.start();
        String q =
                "SELECT cp.name, "
                + "       clf.logtime, "
                + "       clf.providerDUZ, "
                + "       clf.feedbacktext, "
                + "       clf.type, "
                + "       clf.action "
                + "FROM Commend.dbo.CommendLogFeedback clf, "
                + "     Commend.dbo.CommendProviders cp "
                + "where clf.providerDUZ = cp.DUZ";
        SQLTable result = _dest.getTable(q);
        t.print();
        return result;
    }

    private java.util.LinkedHashMap<Integer, java.util.LinkedHashMap<java.sql.Timestamp, CommendEvent>> getEvents() {
        java.util.LinkedHashMap<Integer, java.util.LinkedHashMap<java.sql.Timestamp, CommendEvent>> result = new java.util.LinkedHashMap();
        String q =
                "SELECT * \n"
                + "FROM Commend.dbo.CommendLogTrace \n"
                + "WHERE logtime > DATEADD(MONTH,-1,GETDATE()) \n"
                + "ORDER BY logtime DESC";
        SQLTable eventsTable = _dest.getTable(q);
        for (Integer key : eventsTable.keySet()) {
            java.util.LinkedHashMap<String, Object> map = eventsTable.getRow(key);
            CommendEvent event = new CommendEvent(map);
            if (result.containsKey(event.id)) {
                java.util.LinkedHashMap eventsWithId = result.get(event.id);
                eventsWithId.put(event.date, event);
            } else {
                java.util.LinkedHashMap eventsWithId = new java.util.LinkedHashMap();
                eventsWithId.put(event.date, event);
                result.put(event.id, eventsWithId);
            }
        }
        return result;
    }

    public boolean deleteFeedback(String providerDUZ, java.sql.Timestamp ts) {
        String q =
                "DELETE FROM Commend.dbo.CommendLogFeedback \n"
                + "WHERE providerDUZ='" + providerDUZ + "' \n"
                + "  and logtime='" + ts + "'";
        int result = _dest.update(q);
        return (result>0) ? true : false;
    }

    public boolean updateFeedback(String providerDUZ, java.sql.Timestamp ts, int action) {
        String q =
                "UPDATE Commend.dbo.CommendLogFeedback \n"
                + "SET action='" + action + "' \n"
                + "WHERE providerDUZ='" + providerDUZ + "' \n"
                + "  and logtime='" + ts + "'";
        int result = _dest.update(q);
        return (result>0) ? true : false;
    }
    /* Commend Monitor Graphing system */
    private void initGraphEvent() {
        for(Integer i : events.keySet()) {
            java.util.LinkedHashMap<java.sql.Timestamp,CommendEvent> map = events.get(i);
            if(map!=null) {
                GraphEvent ge = new GraphEvent(map);
                graphEvents.put(i, ge);
            }
        }
    }

    public String getEventData() {
        com.medcisive.utility.Timer t = com.medcisive.utility.Timer.start();
        events = getEvents();
        graphEvents = new java.util.LinkedHashMap();
        initGraphEvent();
        String result = "";
        for(GraphEvent ge : graphEvents.values()) {
            result += ge.getEventData() + ",";
        }
        if(!result.isEmpty()) {
            result = result.substring(0,result.lastIndexOf(','));
        }
        t.print();
        return result;
    }
}
