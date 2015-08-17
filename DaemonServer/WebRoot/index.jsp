<%@ page language="java" import="java.util.*" pageEncoding="utf8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>ALL IN</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript" src="jquery-ui.min.js"></script>
<link rel="stylesheet" href="jquery-ui.min.css" type="text/css"></link>
<script type="text/javascript">
	$(function() {
		$("#tags").autocomplete({
			source : "suggest"
		});
	});
</script></head>
<body>
<br>
<br>
<br>
<br>
	<h1 align="center">ALL  IN</h1>
		<br>
		<br>
		<form action="search" method="post" >
			<p align="center">
				<input  id="tags" name="wd" style="font-size:20px;width:400"> 
				<input type="submit" value="ALL IN"  style="font-size:20px">
			</p>
		</form>
	
</body>
</html>
