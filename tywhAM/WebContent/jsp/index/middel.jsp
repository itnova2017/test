<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
<style> 
.navPoint { 
COLOR: white; CURSOR: hand; FONT-FAMILY: Webdings; FONT-SIZE: 9pt 
} 
</style> 
<script>
function switchSysBar(){
	var ssrc=document.getElementById("img1").src.indexOf("main_55.gif");
	if (ssrc>0)
	{
		document.all("img1").src="../../html/images/main_55_1.gif";
		document.all("frmTitle").style.display="none";
	} 
	else
	{ 
		document.all("img1").src="../../html/images/main_55.gif";
		document.all("frmTitle").style.display="";
	} 
} 
</script>
</head>

<body style="overflow:hidden" oncontextmenu="event.returnValue=false">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
  <tr>
    <td width="340px"  id="frmTitle" nowrap="nowrap"  name="fmTitle" align="center" valign="top">
    	<table  style="table-layout:fixed; width:100%;height:100%;border:0 ;cellpadding:0 ;cellspacing:0;" >
	      <tr>
	        <td  bgcolor="#1873aa" style="width:6px;">&nbsp;</td>
	        <td width="100%">
	        <iframe name="left" id="left" height="100%" width="100%" src="left.jsp"> 浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。</iframe>
	        </td>
	      </tr>
    </table>		
    </td>
    <td width="6"  style="width:6px;"valign="middle" bgcolor="1873aa" onclick="switchSysBar()">
	    <SPAN class=navPoint   id="switchPoint"    title="关闭/打开左栏">
	    <img src="../../html/images/main_55.gif"  name="img1" width="6"  height="40"  id="img1"></SPAN>
    </td>
    <td width="100%" align="center" valign="top">
    	<iframe name="middel"  id="middle"  height="100%" width="100%" border="0" frameborder="0" src="tab.jsp"> 浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。</iframe></td>
  </tr>
</table>
</body>
</html>
