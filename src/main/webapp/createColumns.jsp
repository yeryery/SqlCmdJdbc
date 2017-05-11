<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <form action="createColumns" method="post">
            <table>
                <tr>
                    <td>Name of column</td>
                    <c:forEach var = "i" begin = "1" end = "${size}">
                        <td><input type="text" name="columnName" /></td>
                    </c:forEach>
                </tr>
                <tr>
                    <td>Type of column</td>
                    <c:forEach var = "i" begin = "1" end = "${size}">
                        <td><input type="text" name="columnType" /></td>
                    </c:forEach>
                </tr>
                <tr>
                    <c:forEach begin = "1" end = "${size}">
                        <td></td>
                    </c:forEach>
                    <td><input type="submit" value="create"/></td>
                </tr>
            </table>
        </form>
        <%@include file="footer.jsp" %>
    </body>
</html>
