<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <p>${tableName}</p>
        <form action="drop" method="post">
            <table border="1">
                <%@include file="constructor.jsp"%>
            </table>
            <p>You are about to drop table '${tableName}'</p>
            <p><input type="submit" value="drop"/></p>
            <%@include file="footer.jsp" %>
    </body>
</html>