<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="users" class="java.util.ArrayList" scope="session"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin panel</title>
    <link rel="icon" href="http://vladmaxi.net/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="http://vladmaxi.net/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="../../css/style.css" media="screen" type="text/css" />
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
</head>

<body>
<div id="upper" >
    <h1><a href="${pageContext.request.contextPath}/admin">Admin panel</a></h1>
    <form action="${pageContext.request.contextPath}/admin" method="post">
        <input type="text" name="login" placeholder="Enter login">
        <input type="submit" value="search">
    </form>
    <button onclick="location.href='${pageContext.request.contextPath}/logout'">Logout</button>
</div>
<div id="registration">
    <form action="${pageContext.request.contextPath}/admin/edit/${user.id}" method="post">
            <input type="text"
                   name="login"
                      minlength="8"
                      maxlength="16"
                   placeholder="Enter new login">
            <input type="password"
                   name="password"
               minlength="8"
               maxlength="16"
                   placeholder="Enter new password">
            <input type="submit" value="CONFIRM" style="font-size: 12px;">
    </form>
</div>
    <div id="user">
        <span style="width: 18%;">${user.id}</span>
        <span style="width: 10%;">${user.role}</span>
        <span style="width: 30%;">${user.login}</span>
        <form action="${pageContext.request.contextPath}/admin/delete/${user.id}">
            <button id="delete">Delete</button>
        </form>
    </div>

</body>
</html>