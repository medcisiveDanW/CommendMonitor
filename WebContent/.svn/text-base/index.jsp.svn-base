<%--
    Document   : index
    Created on : Oct 18, 2011, 3:50:27 PM
    Author     : vhapalchambj
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.medcisive.commend.monitor.*" %>
<%@page import="com.medcisive.utility.sql2.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%!
    public String removeNull(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public String formatString(String str) {
        str = str.replaceAll("\n", "<br>");
        str = str.replaceAll("\"", "'");
        return str;
    }

    public String getId(String str, java.sql.Timestamp ts) {
        return str + ts.getTime();
    }

    public String ifSelected(boolean isSelected) {
        if (isSelected) {
            return "selected=\"selected\"";
        }
        return "";
    }
%>
<%
    CommendMonitorManager manager = new CommendMonitorManager();
    manager.go();
    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MMM dd yyyy");
%>
<html><!-- xmlns="http://www.w3.org/1999/xhtml"-->
    <head>
        <title>Commend Monitor</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link href="css/commend-monitor.css" rel="stylesheet" type="text/css" />
        <link href="css/dark-hive/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" />
        <link type="text/css" href="css/demo_page.css" rel="stylesheet" />
        <link type="text/css" href="css/demo_table.css" rel="stylesheet" />
        <link type="text/css" href="css/demo_table_jui.css" rel="stylesheet" />
        <script language="javascript" type="text/javascript" src="js/jquery-1.6.2.min.js"></script>
        <script language="javascript" type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
        <script language="javascript" type="text/javascript" src="js/jquery.dataTables.min.js"></script>
        <script language="javascript" type="text/javascript" src="js/dataTables.dateSort.js"></script>
        <script language="javascript" type="text/javascript" src="js/commend-monitor.js"></script>
        <script language="javascript" type="text/javascript" src="js/AjaxManager.js"></script>
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
        <script language="javascript" type="text/javascript" src="js/Utility.js"></script>
        <script language="javascript" type="text/javascript" src="js/ManageProviders.js"></script>
        <script language="javascript" type="text/javascript" src="js/AddProvider.js"></script>
        <script language="javascript" type="text/javascript" src="js/AddClinic.js"></script>
        <script language="javascript" type="text/javascript" src="js/AddCosigner.js"></script>
        <script language="javascript" type="text/javascript" src="js/EditProvider.js"></script>
        <script type="text/javascript">
            var manager = new CommendMonitor();
            var ajax = new AjaxManager();
            $(function() {
                new ManageProviders('providerManagement');
                $('#dialog-confirm').hide();

                $('#provider-today').dataTable({
                    "iDisplayLength": 18,
                    "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
                    "aoColumnDefs": [ { "asSorting": [ "asc", "desc" ], "aTargets": [ 0, 1, 2] } ],
                    "bJQueryUI": true,
                    "sPaginationType": "full_numbers"
                });
                $('#provider-usage').dataTable({
                    "iDisplayLength": 22,
                    "aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
                    "aoColumnDefs": [ { "asSorting": [ "asc", "desc" ], "aTargets": [ 0, 1, 2] } ],
                    "bJQueryUI": true,
                    "sPaginationType": "full_numbers"
                });
                var feedbackTable = $('#feedback-table').dataTable({
                    "bLengthChange": false,
                    "bJQueryUI": true,
                    "sScrollY": "455px",
                    "bPaginate": false
                });

            <%
                    for (Integer key : manager.feedback.keySet()) {
                        java.util.LinkedHashMap<String, Object> map = manager.feedback.getRow(key);
                        String duz = (String) map.get("providerDUZ");
                        java.sql.Timestamp time = (java.sql.Timestamp) map.get("logtime");
                        String id = getId(duz, time);
            %>
                        $('#<%=id%>').click(function() {
                            var newText = "<%=formatString((String) map.get("feedbacktext"))%>";
                            $('#feedback_text_area').html(newText);

                        });
                        $('#<%=id%>actionSelectId').change(function() {
                            var value = $('#<%=id%>actionSelectId').val();
                            ajax.updateFeedback('<%=duz%>','<%=time.getTime()%>',value);
                        });
                        $('#<%=id%>delete').click(function() {
                            $('#dialog-confirm').dialog({
                                resizable: false,
                                height:190,
                                modal: true,
                                buttons: {
                                    "Delete": function() {
                                        ajax.deleteFeedback('<%=duz%>','<%=time.getTime()%>');
                                        feedbackTable.fnDeleteRow( feedbackTable.fnGetPosition( document.getElementById('<%=id%>') ));
                                        $( this ).dialog( "close" );
                                    },
                                    Cancel: function() {
                                        $( this ).dialog( "close" );
                                    }
                                }
                            });
                        });
            <%}%>

                        $('#tabs').tabs();
                        $('#tabs_technical').tabs();
            <%
                    String databaseFailur = manager.databaseInfo.get("warning").toString();
                    if (databaseFailur.equalsIgnoreCase("true")) {%>
                            $('.database_baner').css('border-color', '#F00');
            <%}%>

                        var dateSpan,
                        eventGraph,
                        eventHash = { "graphSelectId": { data: <%=manager.eventData%> } };
                        dateSpan = { start: <%=manager.today.getTime() - (manager.oneDay * 30 * 6)%>, end: <%=manager.today.getTime()%> };
                        eventGraph = new GraphAPI("canvasScores", eventHash, dateSpan);
                    });
        </script>
    </head>
    <body class="body">
        <div class="leftmargin"></div>
        <div class="main_canvas">
            <div class="main_baner">
                <div class="main_baner_date">Date: <%=manager.todayStr%></div>
                <div class="main_baner_title">
                    <div class="title_image"></div>
                </div>
            </div>
            <div id="tabs" class="tabs_canvas">
                <ul>
                    <li><a href="#today">Today</a></li>
                    <li><a href="#usage">Provider Usage</a></li>
                    <li><a href="#metrics">Technical Metrics</a></li>
                    <li><a href="#providerManagement">Manage Providers</a></li>
                </ul>
                <div id="today">
                    <div class="database_baner">
                        <div class="pad">
                            <div class="cell">
                                <div class="row">Database Information.</div>
                                <div class="row">Start: <%=format.format(manager.databaseInfo.get("start"))%></div>
                                <div class="row">End: <%=format.format(manager.databaseInfo.get("end"))%></div>
                                <div class="row">Duration: <%=manager.databaseInfo.get("duration")%> sec.</div>
                                <div class="row">Database Failure: <%=manager.databaseInfo.get("warning")%></div>
                                <div class="row">Number of Patients Processed: <%=manager.databaseInfo.get("numberOfPatients")%></div>
                            </div>
                            <div class="cell pad-left">
                                <div class="row">VISN</div>
                                <div class="row">Start: <%=format.format(manager.databaseInfo.get("visnstart"))%></div>
                                <div class="row">End: <%=format.format(manager.databaseInfo.get("visnend"))%></div>
                                <div class="row">Duration: <%=manager.databaseInfo.get("visnduration")%> sec.</div>
                            </div>
                        </div>
                    </div>
                    <div class="provider_table">
                        <table id="provider-today" cellpadding="0" cellspacing="0" border="0" class="display">
                            <thead>
                                <tr>
                                    <th>
                                        Name
                                    </th>
                                    <th>
                                        Last CPRS Login
                                    </th>
                                    <th>
                                        Last Interaction with COMMEND
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for (Integer key : manager.getTodayUsage.keySet()) {
                                        java.util.LinkedHashMap<String, Object> map = manager.getTodayUsage.getRow(key);
                                %>
                                <tr class="gradeC">
                                    <td class="table_font">
                                        <%=map.get("name")%>
                                    </td>
                                    <td class="table_font">
                                        <%=format.format(map.get("lastCPRS"))%>
                                    </td>
                                    <td class="table_font">
                                        <%=format.format(map.get("lastCOMMEND"))%>
                                    </td>
                                </tr>
                                <%}%>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div id="usage">
                    <div class="provider_usage_table">
                        <table id="provider-usage" cellpadding="0" cellspacing="0" border="0" class="display">
                            <thead>
                                <tr>
                                    <th style="width: 170px;">
                                        Name
                                    </th>
                                    <th>
                                        CPRS 2 Weeks
                                    </th>
                                    <th>
                                        CPRS 4 Weeks
                                    </th>
                                    <th>
                                        COMMEND 2 Weeks
                                    </th>
                                    <th>
                                        COMMEND 4 Weeks
                                    </th>
                                    <th style="width: 100px;">
                                        Last Note
                                    </th>
                                    <th>
                                        Note 2 Weeks
                                    </th>
                                    <th>
                                        Note 4 Weeks
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    for (String key : manager.providerUsage.keySet()) {
                                        java.util.LinkedHashMap<String, Object> map = (java.util.LinkedHashMap<String, Object>) manager.providerUsage.get(key);
                                        Object obj = map.get("LastNote");
                                        String lastNote = "";
                                        if (obj != null) {
                                            try {
                                                lastNote = format.format(obj);
                                            } catch (java.lang.Exception e) {
                                                System.out.println(e);
                                            }
                                        }
                                %>
                                <tr class="gradeC">
                                    <td class="table_font">
                                        <%=map.get("name")%>
                                    </td>
                                    <td class="table_font">
                                        <%=removeNull(map.get("CPRS2Weeks"))%>
                                    </td>
                                    <td class="table_font">
                                        <%=removeNull(map.get("CPRS4Weeks"))%>
                                    </td>
                                    <td class="table_font">
                                        <%=removeNull(map.get("COMMEND2Weeks"))%>
                                    </td>
                                    <td class="table_font">
                                        <%=removeNull(map.get("COMMEND4Weeks"))%>
                                    </td>
                                    <td class="table_font">
                                        <%=lastNote%>
                                    </td>
                                    <td class="table_font">
                                        <%=removeNull(map.get("Note2Weeks"))%>
                                    </td>
                                    <td class="table_font">
                                        <%=removeNull(map.get("Note4Weeks"))%>
                                    </td>
                                </tr>
                                <%}%>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div id="metrics">
                    <div id="tabs_technical" class="technical_tabs_canvas" >
                        <ul>
                            <li><a href="#system">System</a></li>
                            <li><a href="#feedback">Feedback</a></li>
                            <li><a href="#graphs">Graphs</a></li>
                        </ul>
                        <div id="system">
                            <div class="provider_usage_table" >
                                <div class="technical_metrics_cavas_top">
                                    <div class="metrics_left">asdf</div>
                                    <div class="metrics_left">asdf</div>
                                </div>
                                <div class="technical_metrics_cavas_bottom">
                                    <div class="metrics_left">asdf</div>
                                    <div class="metrics_left">asdf</div>
                                </div>
                            </div>
                        </div>
                        <div id="feedback">
                            <div class="feedback_table">
                                <table id="feedback-table" cellpadding="0" cellspacing="0" border="0" class="display">
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Date</th>
                                            <th>Type</th>
                                            <th>Action</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            for (Integer key : manager.feedback.keySet()) {
                                                java.util.LinkedHashMap<String, Object> map = manager.feedback.getRow(key);
                                                String id = getId((String) map.get("providerDUZ"), (java.sql.Timestamp) map.get("logtime"));
                                                String action = (String) map.get("action");
                                                if (action == null) {
                                                    action = "1";
                                                }
                                        %>
                                        <tr id="<%=id%>" class="gradeC">
                                            <td class="table_font">
                                                <%=map.get("name")%>
                                            </td>
                                            <td class="table_font">
                                                <%=map.get("logtime")%>
                                            </td>
                                            <td class="table_font">
                                                <%=map.get("type")%>
                                            </td>
                                            <td class="table_font">
                                                <select id="<%=id%>actionSelectId" class="actionSelectClass" >
                                                    <option value="1" <%=ifSelected(action.equalsIgnoreCase("1"))%>>New</option>
                                                    <option value="2" <%=ifSelected(action.equalsIgnoreCase("2"))%>>Viewed</option>
                                                    <option value="3" <%=ifSelected(action.equalsIgnoreCase("3"))%>>Assigned</option>
                                                    <option value="4" <%=ifSelected(action.equalsIgnoreCase("4"))%>>Closed</option>
                                                </select>
                                            </td>
                                            <td>
                                                <img id="<%=id%>delete" class="delete-feedback-class" src="images/X-icon-small.png" alt="Delete Feedback"/>
                                            </td>
                                        </tr>
                                        <%}%>
                                    </tbody>
                                </table>
                            </div>
                            <div class="feedback_div">
                                <div id="feedback_text_area">
                                </div>
                            </div>
                        </div>
                        <div id="graphs">
                            <div id="strLenghtDiv" style="position:absolute;visibility:hidden"></div>
                            <div class="graphBlock">
                                <div id="canvasScores" style="width:100%;height:600px;"></div>
                            </div>
                            <div class="graphSelectBlock">
                                <div id="graphSelectId" class="inner" ></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="providerManagement"></div>
            </div>
        </div>
        <div id="dialog-confirm" title="Delete Feedback?" style="overflow: hidden" >
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>These items will be permanently deleted and cannot be recovered. Are you sure?</p>
        </div>
    </body>
</html>