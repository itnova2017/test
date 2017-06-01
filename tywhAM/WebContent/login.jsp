<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>同源文化办公考勤平台</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #016aa9;
	overflow:hidden;
}
.STYLE1 {
	color: #000000;
	font-size: 12px;
}
-->
</style>
<link rel="stylesheet" type="text/css" href="html/style.css" />
<%@include file="jsp/taglib.jsp"%>
<%@include file="jsp/result.jsp"%>
</head>
<body>
<form action="<%=basePath%>userAct/login.action" method="post">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td>
    <table width="962" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td height="235" background="html/images/login_03.gif">&nbsp;</td>
      </tr>
      <tr>
        <td height="53"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="394" height="53" background="html/images/login_05.gif">&nbsp;</td>
            <td width="206" background="html/images/login_06.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="25%" height="25"><div align="right"><span class="STYLE1">用户名：</span></div></td>
                <td width="45%" height="25"><div align="center">
                  <input type="text"  id="username" name="tuser.t_loginName" style="width:105px; height:17px; background-color:#292929; border:solid 1px #7dbad7; font-size:12px; color:#6cd0ff">
                </div></td>
                <td width="10%" height="25">&nbsp;</td>
              </tr>
              <tr>
                <td width="25%"  height="25"><div align="right"><span class="STYLE1">密码：</span></div></td>
                <td width="45%" height="25">
                		<div align="center">
							<input type="password"  id="password"  name="tuser.t_password"  style="width: 105px; height: 17px; background-color: #292929; border: solid 1px #7dbad7; font-size: 12px; color: #6cd0ff">
						</div>
						</td>
                <td width="10%" height="25"><div align="left"><a href="#" onclick="validate();"><img src="html/images/dl.gif" width="49" height="18" border="0"></a></div></td>
              </tr>
            </table></td>
            <td width="362" background="html/images/login_07.gif">&nbsp;</td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="213" background="html/images/login_08.gif">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
</table>
</form>

<script type="text/javascript">
function validate(){
		var flag = verifyRequired("username","用户名")&&verifyLength("username",30,"用户名")
		 && verifyRequired("password","密码") && verifyLength("password",30,"密码");
		if(flag){document.forms[0].submit();}
}

document.onkeydown = function (e) 
{ 
	var theEvent = window.event || e; 
	var code = theEvent.keyCode || theEvent.which; 
		if (code == 13) { 
			validate();
		} 
}
</script>
</body>
</html>
