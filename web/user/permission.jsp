<%--
  Created by IntelliJ IDEA.
  User: toanv
  Date: 24/06/2020
  Time: 13:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Manage user permissions</title>
</head>
<body>
<center>
    <h1>Manage user permissions</h1>
    <h2>
        <a href="users?action=users">List All Users</a>
    </h2>
</center>
<div align="center">
    <table border="1" cellpadding="5">
        <caption>
            <h2>
                Edit User
            </h2>
        </caption>
        <tr>
            <th>ID:</th>
            <td>
                <c:if test="${user != null}">
                    <c:out value='${user.id}' />
                </c:if>
            </td>
        </tr>
        <tr>
            <th>User Name:</th>
            <td>
                <c:out value='${user.name}' />
            </td>
        </tr>
        <tr>
            <th>User Email:</th>
            <td>
                <c:out value='${user.email}' />
            </td>
        </tr>
        <tr>
            <th>Country:</th>
            <td>
                <c:out value='${user.country}' />
            </td>
        </tr>

        <tr>
            <th>Permissions:</th>
            <td>
                <c:forEach items='${requestScope["userPermissions"]}' var="userPermission">
                    <c:out value='${userPermission.getName()}' />,
                </c:forEach>
            </td>
        </tr>
    </table>


    <form method="post">
        <table border="1" cellpadding="5">
            <caption>
                <h2>
                    Permission
                </h2>
            </caption>
            <c:forEach items='${requestScope["permissions"]}' var="permisstion">
                <tr>
                    <th><input type="checkbox" name="permission[]" value='${permisstion.getId()}'></th>
                    <td>
                        <c:out value='${permisstion.getName()}' />
                    </td>
                </tr>
            </c:forEach>


            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Save"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
