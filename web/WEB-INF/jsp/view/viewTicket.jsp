<%--
  User: 孙许
  Date: 2018/01/16
  Time: 21:18
--%>
<%
    String ticketId = (String) request.getAttribute("ticketId");
    Ticket ticket = (Ticket) request.getAttribute("ticket");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Ticket #<%= ticketId%>: <%= ticket.getSubject()%>
</h2>
<i>Customer Nmae - <%= ticket.getCustomerName()%>
</i><br/><br/>
<%= ticket.getBody()%><br/><br/>
<%
    if (ticket.getNumberOfAttachments() > 0) {
%>Attachments: <%
    int i = 0;
    for (Attachment a : ticket.getAttachments()) {
        if (i++ > 0) {
            out.print(", ");
%><a href="<c:url value="/tickets">
                        <c:param name="action" value="download" />
                        <c:param name="ticketId" value="<%= ticketId%>" />
                        <c:param name="attachment" value="<%= a.getName()%>" />
                    </c:url> "><%= a.getName()%>
</a> <%
        }
    }
%><br/><br/><%
    }
%>
<a href="<c:url value="/tickets" /> ">Return to list tickets</a>
</body>
</html>
