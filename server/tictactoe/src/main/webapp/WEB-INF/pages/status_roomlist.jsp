<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>roomlist</title>
</head>
<body>
status/roomlist
<table>
<tr>
<th>roomId:</th>
<th>roomPassword</th>
<th>isFull</th>
<th>waitPawn</th>
<th>lastActivityTime</th>
<th>userIdA</th>
<th>userIdB</th>
<th>isUserAReady</th>
<th>isUserBReady</th>
<th>whoCanPlay</th>
</tr>
<c:forEach items="${roomList}" var="room">
	<tr>
		<th>${room.roomId}</th>
		<th>${room.roomPassword}</th>
		<th>${room.isFull}</th>
		<th>${room.waitPawn}</th>
		<th>${room.lastActivityTime}</th>
		<th>${room.userA.userId}</th>
		<th>${room.userB.userId}</th>
		<th>${room.isUserAReady}</th>
		<th>${room.isUserBReady}</th>
		<th>${room.whoCanPlay}</th>
	</tr>
</c:forEach>
</table>
</body>
</html>