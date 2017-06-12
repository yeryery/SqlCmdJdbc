<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <form action="updatePrepare" method="post">
            <table border="1">
                <%@include file="constructor.jsp"%>
            </table>
            <p>Select the row you want to update</p>
            <p>
                <input type="text" name="column" placeholder="column"/>
                <br>
                <input type="text" name="value" placeholder="value"/>
            </p>
        <p><input type="submit" value="continue"/></p>
        <%@include file="footer.jsp" %>
    </body>
</html>