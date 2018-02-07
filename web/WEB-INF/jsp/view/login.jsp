<%--
  User: 孙许
  Date: 2018/02/07
  Time: 22:49
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h2>Login</h2>
You must login to access the customer support site.<br/><br/>
<%
    if ((Boolean) request.getAttribute("loginFailed")) {
%>
<b>The username or password you entered are not correct. Please try again.</b><br/><br/>
<%
    }
%>
<form method="post" action="<c:url value="/login"/>">
    Username<br/>
    <input type="text" name="username"/><br/><br/>
    Password<br/>
    <input type="password" name="password"/><br/><br/>
    <input type="submit" value="Login"/>
</form>
</body>
</html>
