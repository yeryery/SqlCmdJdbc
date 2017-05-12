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
                    <td><input type="text" name="keyName" placeholder="primary key"/></td>
                    <c:forEach var = "i" begin = "2" end = "${size}">
                        <td><input type="text" name="columnName" placeholder="column"/></td>
                    </c:forEach>
                </tr>
                <tr>
                    <td><input type="text" name="keyType" placeholder="data type"/></td>
                    <c:forEach var = "i" begin = "2" end = "${size}">
                        <td><input type="text" name="columnType" placeholder="data type"/></td>
                    </c:forEach>
                </tr>
                <tr>
                    <td><input type="submit" value="create"/></td>
                </tr>
            </table>
        </form>
        <%@include file="footer.jsp" %>
    </body>
</html>
