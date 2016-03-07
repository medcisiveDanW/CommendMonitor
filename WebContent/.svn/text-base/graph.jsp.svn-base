<%-- 
    Document   : graph
    Created on : Nov 8, 2011, 5:15:13 PM
    Author     : vhapalchambj
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.medcisive.commend.monitor.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    CommendMonitorManager manager = (CommendMonitorManager) session.getAttribute("Manager");
    if(manager==null) {
        manager = new CommendMonitorManager();
        session.setAttribute("Manager", manager);
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="expires" content="-1">
        <meta http-equiv="cache-control" content="no-cache">
        <link href="css/commend-monitor.css" rel="stylesheet" type="text/css" />
        <link href="css/dark-hive/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
        <script language="javascript" type="text/javascript" src="js/jquery-1.6.2.min.js"></script>
        <script language="javascript" type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
        <script language="javascript" type="text/javascript" src="js/flot/excanvas.js"></script>
        <script language="javascript" type="text/javascript" src="js/flot/jquery.flot.js"></script>
        <script language="javascript" type="text/javascript" src="js/flot/jquery.flot.navigate_GraphAPI.js"></script>
        <script language="javascript" type="text/javascript" src="js/flot/jquery.flot.symbol.js"></script>
        <script language="javascript" type="text/javascript" src="js/flot/jquery.flot.resize.js"></script>
        <script language="javascript" type="text/javascript" src="js/jquery.wresize.js"></script>
        <script language="javascript" type="text/javascript" src="js/GeneralUtil.js"></script>
        <script language="javascript" type="text/javascript" src="js/GFXUtil.js"></script>
        <script language="javascript" type="text/javascript" src="js/GraphAPI.js"></script>
        <script language="javascript" type="text/javascript" src="js/date.format.js"></script>
        <script type="text/javascript">
            $(function() {
                var dateSpan,
                    eventGraph,
                    eventHash = 
                    {
                    "graphSelectId": { data: {
                        <%=manager.dbc.getEventData() %>
                    
                        }
                    };
                dateSpan = { start: <%=manager.today.getTime()-(manager.oneDay*30*6) %>, end: <%=manager.today.getTime()%> };
                eventGraph = new GraphAPI("canvasScores", eventHash, dateSpan);
            });
        </script>
    </head>
    <body class="ui-widget-content" style="padding: 0px; margin: 0px; width: 100%; height: 450px; border: 0px;">
        <div id="strLenghtDiv" style="position:absolute;visibility:hidden"></div>
        <div class="graphBlock">
            <div id="canvasScores" style="width:100%;height:600px;"></div>
        </div>
        <div class="graphSelectBlock">
            <div id="graphSelectId" class="inner" ></div>
        </div>
    </body>
</html>
