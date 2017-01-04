<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>登录</title>
</head>
<body>
<form action="http://127.0.0.1:8080/websocket/Login" method="get">
	    登录人:<input name="userName" type="text" /><br>
	    <input type="submit" value="登录" />
</form>
</body>
</html>