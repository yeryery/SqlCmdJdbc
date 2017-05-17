<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <form action="updateValues" method="post">
        <p>Enter new values for updated row:</p>
        <table border="1">
            <tr>
                <c:forEach items="${tableColumns}" var="column">
                    <td>${column}</td>
                </c:forEach>
            </tr>
            <tr>
            <c:forEach items="${tableColumns}" var="column">
                <c:set var="def" value="${definingColumn}"/>
                <c:choose>
                  <c:when test="${column.equals(def)}">
                    <td><b>${definingValue}</b></td>
                  </c:when>
                  <c:otherwise>
                    <td><input type="text" name="${column}"/></td>
                  </c:otherwise>
                </c:choose>
            </c:forEach>
            </tr>
        </table>
        <p><input type="submit" value="update"/></p>
        <%@include file="footer.jsp" %>
    </body>
</html>