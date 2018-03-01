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
<c:choose>
    <%--@elvariable id="ticketDatabase" type="java.util.Map<Integer, com."--%>
    <c:when test="${fn:length(ticketDatabase) == 0}">
        <i>There are no tickets in the system.</i>
    </c:when>
    <c:otherwise>
        <c:forEach items="${ticketDatabase}" var="entry">
            Ticket ${entry.key}: <a href="<c:url value="/tickets">
                                                <c:param name="action" value="view"/>
                                                <c:param name="ticketId" value="${entry.key}"/>
                                          </c:url> "><c:out value="${entry.value.subject}"/></a>
            (customer: <c:out value="${entry.value.customerName}"/>)<br/>
        </c:forEach>
    </c:otherwise>
</c:choose>
</body>
</html>
