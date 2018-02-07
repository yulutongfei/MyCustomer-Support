<%@ page import="java.util.Map" %><%--
  User: 孙许
  Date: 2018/01/16
  Time: 21:18
--%>
<%
    @SuppressWarnings("unchecked")
    Map<Integer, Ticket> ticketDatabase =
            (Map<Integer, Ticket>) request.getAttribute("ticketDatabase");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Support</title>
</head>
<body>
<a href="<c:url value="/login?logout"/>">Logout</a>
<h2>Tickets</h2>
<a href="<c:url value="/tickets">
            <c:param name="action" value="create" />
        </c:url>">Create Ticket</a><br/><br/>
<%
    if (ticketDatabase.size() == 0) {
%><i>There are no tickets in the system.</i><%
} else {
    for (int id : ticketDatabase.keySet()) {
        String idString = Integer.toString(id);
        Ticket ticket = ticketDatabase.get(id);
%>Ticket #<%= idString %>: <a href="<c:url value="/tickets">
                        <c:param name="action" value="view" />
                        <c:param name="ticketId" value="<%= idString %>" />
                    </c:url>"><%= ticket.getSubject() %>
</a> (customer:
<%= ticket.getCustomerName() %>)<br/><%
        }
    }
%>
</body>
</html>
