<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page language="java" import="com.tywh.util.UtilConfig" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String base = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
String ipconfig=UtilConfig.getValue("ipconfig");
%>

<script language="javascript" src="<%=basePath%>js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" src="<%=basePath%>js/validate.js"></script>
<script type="text/javascript" src="<%=basePath%>js/ddaccordion.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery-1.7.2.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=basePath%>js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="<%=basePath%>js/previewImage.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.bgiframe.min.js" ></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.multiSelect.js" ></script>
<script type="text/javascript" src="<%=basePath%>js/swfobject.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.uploadify-3.1.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/ckplayer/ckplayer.js" charset="utf-8"></script>



<script type="text/javascript">
document.onkeydown = function (e) 
	{ 
		var theEvent = window.event || e; 
		var code = theEvent.keyCode || theEvent.which; 
			if (code == 13) { 
				validate();
			} 
	}
	
</script>


