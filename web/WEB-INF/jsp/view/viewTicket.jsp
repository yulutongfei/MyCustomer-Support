<%--@elvariable id="ticketId" type="java.lang.String"--%>
<%--@elvariable id="ticket" type="com.sunxu.Ticket"--%>
<%--
  User: 孙许
  Date: 2018/01/16
  Time: 21:18
--%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="<c:url value="/login?logout"/>">Logout</a>
<h2>Ticket #${ticketId}: ${ticket.subject}
</h2>
<i>Customer Name - <c:out value="${ticket.customerName}"/>
</i><br/><br/>
<c:out value="${ticket.body}"/> <br/><br/>
<c:if test="${ticket.numberOfAttachments > 0}">
    Attachments:
    <c:forEach items="${ticket.attachments}" var="attachment" varStatus="status">
        <c:if test="${!status.first}">,</c:if>
        <a href="<c:url value="/tickets">
                    <c:param name="action" value="download" />
                    <c:param name="ticketId" value="${ticketId}" />
                    <c:param name="attachment" value="${attachment.name}" />
                </c:url> "><c:out value="${attachment.name}"/>
        </a>
    </c:forEach>
    <br/><br/>
</c:if>
<a href="<c:url value="/tickets" /> ">Return to list tickets</a>
</body>
</html>
