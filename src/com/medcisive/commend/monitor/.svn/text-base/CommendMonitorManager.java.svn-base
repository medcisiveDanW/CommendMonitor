package com.medcisive.commend.monitor;

import com.medcisive.utility.sql2.SQLTable;

/**
 *
 * @author vhapalchambj
 */
public class CommendMonitorManager {
    private static final java.util.Date             todayDate = new java.util.Date();
    public static final java.sql.Timestamp          today = new java.sql.Timestamp(todayDate.getTime());
    public static final java.text.SimpleDateFormat  formater = new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy");
    public static String                            todayStr = formater.format(today).toString();
    public static long                              oneDay = 1 * 24 * 60 * 60 * 1000;
    public java.util.HashMap<String,Object>         databaseInfo = null;
    public java.util.HashMap<String,Object>         providerUsage = null;
    public SQLTable                                 feedback = null;
    public String                                   eventData = null;
    public SQLTable                                 getTodayUsage = null;
    public DatabaseManager 							dbc = new DatabaseManager();
    private enum Func {INFO, USAGE, FEEDBACK, EVENTDATA, TODAYUSAGE};

    public CommendMonitorManager() {
    	
    }

    public void go() {
        System.out.println("Start go.");
        class ForkManager extends Thread {
            public Func func;
            public ForkManager(Func f) { func = f; }
            @Override
            public void run() {
                if(func==Func.INFO) {
                    databaseInfo = dbc.getTodayDatabaseInfo();
                }
                if(func==Func.USAGE) {
                    providerUsage = dbc.getProviderUsages();
                }
                if(func==Func.FEEDBACK) {
                    feedback = dbc.getFeedback();
                }
                if(func==Func.EVENTDATA) {
                    eventData = "{" + dbc.getEventData() + "}";
                }
                if(func==Func.TODAYUSAGE) {
                    getTodayUsage = dbc.getTodayUsage();
                }
            }
        }
        java.util.List<Thread> threads = new java.util.ArrayList();
        threads.add(new ForkManager(Func.INFO));
        threads.add(new ForkManager(Func.USAGE));
        threads.add(new ForkManager(Func.FEEDBACK));
        threads.add(new ForkManager(Func.EVENTDATA));
        threads.add(new ForkManager(Func.TODAYUSAGE));
        for(Thread t : threads) { t.start(); }
        for(Thread t :threads) {
            try { t.join(); } catch(java.lang.InterruptedException e) { System.err.println(e); }
        }
    }
}