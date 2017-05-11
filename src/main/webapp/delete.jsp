<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <form action="delete" method="post">
            <table border="1">
                <%@include file="constructor.jsp" %>
            </table>
            <br>
            Select the row you want to delete
            <br>
            <select name = "column">
                <c:forEach items="${columns}" var="column">
                    <option>
                        ${column}
                    </option>
                </c:forEach>
            </select>
            = <input type="text" name="value"/>
            <br>
            <br>
            <input type="submit" value="delete"/>
            <br>
            <br>
            <%@include file="footer.jsp" %>
    </body>
</html>