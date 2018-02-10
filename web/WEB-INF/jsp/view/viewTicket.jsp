<%--@elvariable id="ticketId" type="java.lang.String"--%>
<%--@elvariable id="ticket" type="com.sunxu.Ticket"--%>
<%--
  User: 孙许
  Date: 2018/01/16
  Time: 21:18
--%>
<%
    Ticket ticket = (Ticket) request.getAttribute("ticket");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="<c:url value="/login?logout"/>">Logout</a>
<h2>Ticket #${ticketId}: ${ticket.subject}
</h2>
<i>Customer Name - ${ticket.customerName}
</i><br/><br/>
${ticket.body}<br/><br/>
<%
    if (ticket.getNumberOfAttachments() > 0) {
%>Attachments: <%
    for (Attachment a : ticket.getAttachments()) {
%><a href="<c:url value="/tickets">
                        <c:param name="action" value="download" />
                        <c:param name="ticketId" value="${ticketId}" />
                        <c:param name="attachment" value="<%= a.getName()%>" />
                    </c:url> "><%= a.getName()%>
</a> <%
    }
%><br/><br/><%
    }
%>
<a href="<c:url value="/tickets" /> ">Return to list tickets</a>
</body>
</html>
