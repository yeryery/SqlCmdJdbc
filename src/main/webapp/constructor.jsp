<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:out value = "${tableName}"/>
<c:forEach items="${tableRows}" var="row">
    <tr>
        <c:forEach items="${row}" var="element">
            <td>
                ${element}
            </td>
        </c:forEach>
    </tr>
</c:forEach>