<%-- 
    Document   : updateFeedback
    Created on : Nov 7, 2011, 5:35:39 PM
    Author     : vhapalchambj
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.medcisive.commend.monitor.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    CommendMonitorManager manager = (CommendMonitorManager) session.getAttribute("Manager");
    if(manager!=null) {
        String DUZ = (String)request.getParameter("DUZ");
        String timestampStr = (String)request.getParameter("TIMESTAMP");
        long timestampLong = Long.parseLong(timestampStr);
        java.sql.Timestamp timestamp = new java.sql.Timestamp(timestampLong);
        String isDeleteStr = (String)request.getParameter("DELETE");
        String actionStr = (String)request.getParameter("ACTION");
        boolean isDelete = false;
        if(isDeleteStr.equalsIgnoreCase("true")) { isDelete = true; }
        if(isDelete) {
            System.out.println("Delete this record!");
            System.out.println("DUZ: " + DUZ);
            System.out.println("TIMESTAMP " + timestampStr);
            boolean test = manager.dbc.deleteFeedback(DUZ, timestamp);
            if(test) {
                System.out.println("Delete Sucsesfull!");
            }
            else {
                System.out.println("Delete Unsucsesfull!");
            }
        }
        else {
            int action = -1;
            try {
                action = Integer.parseInt(actionStr);
            }
            catch (Exception e) { action = -1; }
            if(action>0) {
                manager.dbc.updateFeedback(DUZ, timestamp, action);
            }
        }
    }
%>