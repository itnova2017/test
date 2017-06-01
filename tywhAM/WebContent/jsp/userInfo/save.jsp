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
	<form action="<%=basePath%>userInfo/saveUserInfo.action" method="post" >
		<div class="MainDiv">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：系统管理->人员管理->人员新增</th>
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
													<input class="text"  type="text"  name="loginName"  id="loginName" style="width: 154px" value=""  size="50" /> 
													<span class="red">*建议 部门简拼-人名+员工号  例wl-xiaoming123</span>
												</td>
												<td width="16%" align="right" nowrap="nowrap">真实姓名:</td>
												<td width="34%">
													<input class="text"  type="text"  name="userName" id="userName" style="width: 154px" value="" size="50">
													<span class="red">*</span>
												</td>
											</tr>
											<tr>
												<td nowrap="nowrap" align="right">登录密码:</td>
												<td>
													<input class="text" name= "password" id="password" style="width: 154px" value="" />
													<span class="red">*不填默认为123456</span>
												</td>
												<td align="right">确认密码:</td>
												<td>
													<input class="text" name="password2" id="password2" style="width: 154px" value="" />
												</td>
											</tr>
											<tr>
												<td align="right">部门选择:<span class="red">*</span></td>
												<c:if test="${not empty listDapt}">
												   <td colspan="3">
														<c:forEach var="dapt"  items="${listDapt}"  varStatus="status">
															<input type="checkbox"  name="dapts"  id="dapts"  value="${dapt.t_id}">${dapt.t_departName}&nbsp;&nbsp;&nbsp;&nbsp;
													  		<c:if test="${status.index %6 == 5}">
																   <br/>
															</c:if>
													   </c:forEach>
													</td>
												</c:if>
											</tr>
											<tr>
												<td align="right">电话:</td>
												<td>
													<input class="text" name="telphone"  id="telphone" style="width: 154px" value="" />
												</td>
												<td align="right">QQ:</td>
												<td>
													<input class="text" name="qq"  id="qq" style="width: 154px" value="" />
												</td>
											</tr>
											<tr>
												<td align="right">电子邮箱:</td>
												<td>
												<input class="text" name="email" id="email" style="width: 154px" value="" />
												</td>
												<td align="right">是否有效:</td>
												<td>
													<input  type="radio" name="isUse"  value="0" />无效
													<input  type="radio" name="isUse"  value="1"  checked="checked"/>有效
												</td> 
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
	 && verifyRequired("userName","真实姓名") && verifyLength("userName",50,"真实姓名")
	 && verifyDapts() && verifyPassWord();
		if(flag){
				var loginName=document.getElementById("loginName").value;
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

function verifyDapts() 
{
	var obj=document.getElementsByName("dapts");
	var bl=0;
	for(var i=0;i<obj.length;i++){
         if(obj[i].checked)
         {
        	 bl=bl+1;
         }
    } 
	
	if(bl>0)
	{
		return true;
	}
	else
	{
		 alert("请先选择部门");
   		return false;
	}
}
</script>
</body>
</html>
