<%--
  User: 孙许
  Date: 2018/01/16
  Time: 21:18
--%>
<html>
<head>
    <title>MyCustomer Support</title>
</head>
<body>
<a href="<c:url value="/login?logout"/>">Logout</a>
<h2>Create a Ticket</h2>
<form method="post" action="tickets" enctype="multipart/form-data">
    <input type="hidden" name="action" value="create"/>
    <%=session.getAttribute("username")%><br/><br/>
    Subject<br/>
    <input type="text" name="subject"><br/><br/>
    Body<br/>
    <textarea name="body" rows="5" cols="30"></textarea><br/><br/>
    <b>Attachments</b>
    <input type="file" name="file1"/><br/><br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>
