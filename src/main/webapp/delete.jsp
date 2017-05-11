<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <form action="delete" method="post">
            <table border="1">
                <%@include file="constructor.jsp"%>
            </table>
            <br>
            Select the row you want to delete
            <br>
            <input type="text" name="column"/> = <input type="text" name="value"/>
            <br>
            <input type="submit" value="delete"/>
            <br>
            <%@include file="footer.jsp" %>
    </body>
</html>