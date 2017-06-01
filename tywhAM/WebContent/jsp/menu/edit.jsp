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
	<form action="<%=basePath%>menu/editMenu.action" method="post" >
		<div class="MainDiv">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：系统管理->菜单管理->菜单修改</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0"
							style="width: 100%">
							<TR>
								<TD width="100%">
									<fieldset style="height: 100%;">
										<legend>菜单信息</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
											<tr>
												<td nowrap align="right" width="15%">菜单名称:</td>
												<td width="35%">
													<input class="text"  type="text"  name="menuName"  id="menuName" style="width: 154px" value="${tmu.t_name}"  size="50" /> 
													<input class="text"  type="hidden"  name="oldMenuName"  id="oldMenuName" style="width: 154px" value="${tmu.t_name}"  size="50" /> 
													<input class="text"  type="hidden"  name="menuId"  id="menuId" style="width: 154px" value="${tmu.t_id}"  size="50" /> 
												</td>
												<td align="right">菜单描述:</td>
												<td width="35%">
													<input class="text"  type="text"  name="menuContent"  id="menuContent" style="width: 154px" value="${tmu.t_content}"  size="50" /> 
												</td>
											</tr>
											<tr>
												<td align="right">上级菜单:<span class="red">*</span></td>
											    <td>
													<select id="parentId" name="parentId" style="width: 200px">
															<c:if test="${not empty menuList}">
																<c:forEach var="menu" items="${menuList}">
																	<option value="${menu.t_id}"  <c:if test="${menu.t_id == tmu.t_parentId}">selected="selected"</c:if>>${menu.t_name}</option>
																</c:forEach>
															</c:if>
													</select>
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">编号:</td>
												<td width="35%">
													<input class="text"  type="text"  name="num"  id="num" style="width: 154px" value="${tmu.t_num}"  size="50" /> 
													<span class="red">*</span>主要用在资源目录上
												</td>
												<td nowrap align="right" width="15%">排序:</td>
												<td width="35%">
													<input class="text"  type="text"  name="order"  id="order" style="width: 154px" value="${tmu.t_order}"  size="50" /> 
												</td>
												
											</tr>
											<tr>
												<td align="right">url:</td>
												<td width="35%">
													<input class="text"  type="text"  name="url"  id="url" style="width: 350px" value="${tmu.t_url}"  size="50" /> 
													<span class="red">例如：/XXX/XXX.action</span>
												</td>
												<td align="right">参数:</td>
												<td width="35%">
													<input class="text"  type="text"  name="param"  id="param" style="width: 154px" value="${tmu.t_parameter}"  size="50" /> 
												</td>
											</tr>
											<tr>
												<td align="right">备注:</td>
												<td width="35%">
													<input class="text"  type="text"  name="remark"  id="remark" style="width: 154px" value="${tmu.t_remark}"  size="50" /> 
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
	var flag = verifyRequired("menuName","菜单名称")&&verifyLength("menuName",50,"菜单名称")&&verifyMatch("order","排序","number")&&verifyLength("order",20,"排序");
		if(flag){
				var menuName=document.getElementById("menuName").value;
				var oldMenuName=document.getElementById("oldMenuName").value;
				if(menuName!=oldMenuName)
				{
					$.post('<%=basePath%>menu/repeatName.action',{name:menuName},
						function(data, textStatus){
							if(data&&$.trim(data)=='error'){
								alert("该菜单名称已有，请重新输入");
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
