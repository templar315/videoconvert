<jsp:useBean id="user" scope="request" type="com.satrumroom.dto.UserDTO"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="files" scope="request" type="java.util.List"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User panel</title>
    <link rel="icon" href="http://vladmaxi.net/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="http://vladmaxi.net/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="../../css/style.css" media="screen" type="text/css" />
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>

<body>
<div id="upper" >
    <h1><a href="${pageContext.request.contextPath}/${user.id}">${user.login}</a></h1>

    <button onclick="location.href='${pageContext.request.contextPath}/logout'">Logout</button>
</div>
<div id="registration">
    <form action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data" method="post">
        <span>
			<input type="submit" value="upload">
		</span>
        <span class="upload_form">
			<label>
				<input name="file" type="file" class="main_input_file">
				<div>choose</div>
			</label>
		</span>
        <span>
            <label>
			    <input class="f_name" type="text" id="f_name" value="File not selected" disabled style="width: 60%;">
            </label>
		</span>
    </form>
</div>

<c:forEach var="file" items="${files}">
<div id="file">
    <span id="file_name">${file.name}</span>
    <br><br>
    <span id="file_last_mod">Video/audio:
        <c:choose>
            <c:when test="${file.videoFormat != null}">${file.videoFormat}</c:when>
            <c:otherwise>--</c:otherwise>
        </c:choose>
        /
        <c:choose>
            <c:when test="${file.audioFormat != null}">${file.audioFormat}</c:when>
            <c:otherwise>--</c:otherwise>
        </c:choose>
    </span><br>
    <span id="file_last_mod2">Last modified: ${file.lastChange}</span><br>
    <div>
        <form action="">
            <button class="footer" style="border-radius: 0 0 0 5px; ">convert</button>
        </form>
        <form action="${pageContext.request.contextPath}/download/${file.id}">
            <button class="footer">download</button>
        </form>
        <form action="">
            <button class="footer">Edit</button>
        </form>
        <form action="${pageContext.request.contextPath}/delete/${file.id}">
            <button class="footer" style="background-color: #c90000;  border-radius: 0 0 5px 0;">Delete</button>
        </form>
    </div>
</div>
</c:forEach>
<script>
    $(document).ready(function() {
        $(".main_input_file").change(function(){
            var f_name = [];
            for (var i = 0; i < $(this).get(0).files.length; ++i) {
                f_name.push(' ' + $(this).get(0).files[i].name);
            }
            $("#f_name").val(f_name.join(', '));
        });
    });
</script>
</body>
</html>