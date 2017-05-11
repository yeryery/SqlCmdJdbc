<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <form action="insert" method="post">
            <table border="1">
                <%@include file="constructor.jsp" %>
                <tr>
                    <c:forEach items="${columns}" var="column">
                        <td><input type="text" name="${column}"/></td>
                    </c:forEach>`
                </tr>
            </table>
            <br>
            <input type="submit" value="insert"/>
            <br>
            <%@include file="footer.jsp" %>
    </body>
</html>