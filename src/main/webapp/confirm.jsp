<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <p>Are you sure you want to <b>${command}</b> table '${tableName}'?</p>
        <p><a href = "confirm"><button>${command}!</button></a></p>
        <%@include file="footer.jsp" %>
    </body>
</html>