<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <form action="createName" method="post">
            <table>
                <tr>
                    <td>Name of table</td>
                    <td><input type="text" name="tableName"/></td>
                </tr>
                <tr>
                    <td>Number of columns</td>
                    <td><input type="number" name="size" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="continue"/></td>
                </tr>
            </table>
        </form>
        <%@include file="footer.jsp" %>
    </body>
</html>
