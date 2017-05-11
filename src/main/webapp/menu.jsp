<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <c:forEach items="${items}" var="item">
            <a href="select?command=${item}">${item}</a><br>
        </c:forEach>
        <a href="help">help</a><br>
    </body>
</html>