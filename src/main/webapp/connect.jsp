<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <form action="connect" method="post">
            <table>
                <tr>
                    <td>Database name</td>
                    <td><input type="text" name="dbname"/></td>
                </tr>
                <tr>
                    <td>Username</td>
                    <td><input type="text" name="username"/></td>
                </tr>
                <tr>
                    <td>Password</td>
                    <td><input type="password" name="password"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="connect"/></td>
                </tr>
            </table>
        </form>
        <%@include file="footer.jsp" %>
    </body>
</html>