<%--@elvariable id="numberOfSessions" type="java.lang.Integer"--%>
<%@ page import="java.util.List" %><%--
  User: 孙许
  Date: 2018/02/08
  Time: 23:53
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%!
    private static String toString(long timeInterval) {
        if (timeInterval < 1000) {
            return "less than one second";
        }
        if (timeInterval < 60000) {
            return (timeInterval / 60000) + " minutes";
        }
        return "about" + (timeInterval / 60000) + " minutes";
    }
%>
<%
    List<HttpSession> sessions = (List<HttpSession>) request.getAttribute("sessionList");
%>
<html>
<head>
    <title>Customer Support</title>
</head>
<body>
<a href="<c:url value="/login?logout"/>">Logout</a>
<h2>Sessions</h2>
There are a total of ${numberOfSessions} active sessions in this application.<br/><br/>
<%
    long timestamp = System.currentTimeMillis();
    for (HttpSession aSession : sessions) {
        out.print(aSession.getId() + " - " + aSession.getAttribute("username"));
        if (aSession.getId().equals(session.getId())) {
            out.print("(you)");
        }
        out.print(" - last active " + toString(timestamp - aSession.getLastAccessedTime()));
        out.print(" ago<br/>");
    }
%>
</body>
</html>
