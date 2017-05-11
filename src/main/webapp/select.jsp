<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
    Select the table you require<br>
        <c:forEach items="${tables}" var="table">
            <a href="<c:out value = "${command}"/>?tableName=${table}">${table}</a><br>
        </c:forEach>
        <br>
        <%@include file="footer.jsp" %>
    </body>
</html>