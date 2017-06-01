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
	<form action="<%=basePath%>department/editDepartmentInfo.action" method="post" >
		<div class="MainDiv">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：系统管理->部门管理->部门修改</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0"
							style="width: 100%">
							<TR>
								<TD width="100%">
									<fieldset style="height: 100%;">
										<legend>部门信息</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
											<tr>
												<td nowrap align="right" width="15%">原部门名称:</td>
												<td width="35%">
													${tdt.t_departName}
												</td>
												<td nowrap align="right" width="15%">部门名称:</td>
												<td width="35%">
													<input class="text"  type="text"  name="daptName"  id="daptName" style="width: 154px" value="${tdt.t_departName}"  size="50" /> 
													<input class="text"  type="hidden"  name="oldDaptName"  id="oldDaptName" style="width: 154px" value="${tdt.t_departName}"  size="50" /> 
													<input class="text"  type="hidden"  name="daptId"  id="daptId" style="width: 154px" value="${tdt.t_id}"  size="50" /> 
												</td><!-- 
												<td align="right">是否有效:</td>
												<td>
													<input  type="radio" name="isUse"  value="0" />无效
													<input  type="radio" name="isUse"  value="1"  checked="checked"/>有效
												</td> -->
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
	var flag = verifyRequired("daptName","部门名称")&&verifyLength("daptName",50,"部门名称");
		if(flag){
				var daptName=document.getElementById("daptName").value;
				var oldDaptName=document.getElementById("oldDaptName").value;
				if(daptName!=oldDaptName)
				{
						$.post('<%=basePath%>department/repeatName.action',{name:daptName},
							function(data, textStatus){
								if(data&&$.trim(data)=='error'){
									alert("该部门名称已有，请重新输入");
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

</script>
</body>
</html>
