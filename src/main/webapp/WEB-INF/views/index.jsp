<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="icon" href="http://vladmaxi.net/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="http://vladmaxi.net/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="../../css/style.css" media="screen" type="text/css" />
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
</head>

<body>

<div id="login-form">
    <h1>AUTHORIZATION</h1>
    <fieldset>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="text" name="login" placeholder="Login">
            <input type="password" name="password" placeholder="Password">
            <input type="submit" value="SIGN IN">
        </form>
    </fieldset>
</div>
</body>
</html>