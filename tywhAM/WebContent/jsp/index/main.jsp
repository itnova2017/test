<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"" />
<title>同源文化办公考勤平台</title>
<%@include file="../taglib.jsp"%>
<%@include file="../result.jsp"%>
</head>
<frameset rows="98,*,8"  frameborder="no" name="frame"  border="0" framespacing="0">
  <frame src="<%=basePath%>jsp/index/top.jsp" name="topFrame"  scrolling="no" noresize="noresize" id="topFrame" />
  <frame src="<%=basePath%>jsp/index/middel.jsp" name="mainFrame" id="mainFrame" />
  <frame src="<%=basePath%>jsp/index/down.jsp" name="bottomFrame"  scrolling="no"  noresize="noresize" id="bottomFrame" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>