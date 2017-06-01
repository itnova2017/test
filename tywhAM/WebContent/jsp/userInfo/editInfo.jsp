<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" rev="stylesheet" href="../html/style.css" type="text/css" media="all" />
<title></title>
<style type="text/css">
.atten {
	font-size: 12px;
	font-weight: normal;
	color: #F00;
}
</style>
<%@include file="../taglib.jsp"%>
<%@include file="../result.jsp"%>
</head>

<body class="ContentBody">
	<form action="<%=basePath%>userInfo/editUserInfos.action" method="post" >
		<div class="MainDiv">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：我的设置->我的信息</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0"
							style="width: 100%">
							<TR>
								<TD width="100%">
									<fieldset style="height: 100%;">
										<legend>人员信息</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
											<tr>
												<td nowrap align="right" width="15%">登录帐号:</td>
												<td width="35%">
													<input class="text"  type="text"  name="loginName"  id="loginName" style="width: 154px" value="${tui.t_loginName}"  readonly="readonly"  size="50" /> 
													<input class="text"  type="hidden"  name="oldLoginName"  id="oldLoginName" style="width: 154px" value="${tui.t_loginName}"  size="50" /> 
													<input class="text"  type="hidden"   name="uid"  id="uid" style="width: 154px" value="${tui.t_id}"  size="50" /> 
													<span class="red">*不可修改</span>
												</td>
												<td width="16%" align="right" nowrap="nowrap">真实姓名:</td>
												<td width="34%">
													<input class="text"  type="text"  name="userName" id="userName" style="width: 154px" value="${tui.t_userName}"  readonly="readonly" size="50">
													<span class="red">*不可修改</span>
												</td>
											</tr>
											<tr>
												<td nowrap="nowrap" align="right">登录密码:</td>
												<td>
													<input class="text" name= "password" id="password" style="width: 154px" value="${tui.t_password}" />
													<span class="red">*不填默认为123456</span>
												</td>
												<td align="right">确认密码:</td>
												<td>
													<input class="text" name="password2" id="password2" style="width: 154px" value="${tui.t_password}" />
												</td>
											</tr>
											<tr>
												<td align="right">所在部门:</td>
												<c:if test="${not empty listDapt}">
												   <td colspan="3">
														<c:forEach var="dapt"  items="${listDapt}"  varStatus="status">
															<c:if test="${dapt.isCheck == '1'}">${dapt.t_departName}</c:if>&nbsp;&nbsp;&nbsp;&nbsp;
														    <c:if test="${status.index %4 == 3}">
														    <br/>
															</c:if>
													   </c:forEach>
													   <span class="red">*</span>
													</td>
												</c:if>
											</tr>
											<tr>
												<td align="right">电话:</td>
												<td>
													<input class="text" name="telphone"  id="telphone" style="width: 154px" value="${tui.t_telphone}" />
												</td>
												<td align="right">QQ:</td>
												<td>
													<input class="text" name="qq"  id="qq" style="width: 154px" value="${tui.t_qq}" />
												</td>
											</tr>
											<tr>
												<td align="right">电子邮箱:</td>
												<td>
												<input class="text" name="email" id="email" style="width: 154px" value="${tui.t_email}" />
												</td>
												<%-- <td align="right">是否有效:</td>
												<td>
													<input  type="radio" name="isUse"  value="0" <c:if test="${tui.t_isUse ==0}"> checked="checked"</c:if>/>无效
													<input  type="radio" name="isUse"  value="1" <c:if test="${tui.t_isUse ==1}"> checked="checked"</c:if>/>有效
												</td> --%>
											</tr>
										</table>
										<br />
									</fieldset>
								</TD>
							</TR>
						</TABLE>
					</td>
				</tr>
				<TR>
					<TD colspan="2" align="center" height="50px">
						<input type="button" name="Submit" value="保存" class="button" onclick="validate();" />
						<input type="button" name="Submit2" value="返回" class="button" onclick="window.history.go(-1);" />
					</TD>
				</TR>
			</TABLE>
		</div>
	</form>
<script type="text/javascript">
function validate(){

	var flag = verifyRequired("loginName","登录帐号")&&verifyLength("loginName",50,"登录帐号")
	 && verifyRequired("userName","真实姓名") && verifyLength("userName",50,"真实姓名")&& verifyPassWord();
		if(flag){

				var loginName=document.getElementById("loginName").value;
				var oldLoginName=document.getElementById("oldLoginName").value;
				if(loginName!=oldLoginName)
				{
					$.post('<%=basePath%>userInfo/repeatName.action',{name:loginName},
						function(data, textStatus){
							if(data&&$.trim(data)=='error'){
								alert("该登录名已有，请重新输入");
							}else{
								document.forms[0].submit();
							}
						}
					);
				}
				else
				{
					document.forms[0].submit();
				}
		}
}

function verifyPassWord()
{
	var p1 = document.getElementById("password").value;
	var p2 = document.getElementById("password2").value;
	if(p1!="" && p2==""){
		fail("如果填写了密码，也请填写确认密码");
		return false;
	}
	else if(p1!="" && p2!="" )
	{
		if(p1==p2)
		{
			return true;
		}
		else
		{
			fail("密码与确认密码不相同请重新填写");
			return false;
		}
	}
	return true;
}
</script>
</body>
</html>
