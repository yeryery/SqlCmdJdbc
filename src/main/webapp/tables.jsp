<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <c:forEach items="${tables}" var="table">
            <a href="display?table=${table}">${table}</a><br>
        </c:forEach>
        <br>
        <%@include file="footer.jsp" %>
    </body>
</html>