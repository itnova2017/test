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
	<form action="#" method="post" >
		<div class="MainDiv">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：系统管理->部门管理->角色管理->人员查看</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0"
							style="width: 100%">
							<TR>
								<TD width="100%">
									<fieldset style="height: 100%;">
										<legend>角色信息</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
											<tr>
												<td nowrap align="right" width="15%">角色名称:</td>
												<td width="35%">
													&nbsp;&nbsp;&nbsp;&nbsp;${trs.t_roleName}
												</td>
											</tr>
											<tr>
												<td align="right">当前部门人员:</td>
												<c:if test="${not empty userList}">
												   <td colspan="3">
														<c:forEach var="user"  items="${userList}"  varStatus="status">
														<c:if test="${user.isCheck == '1'}">&nbsp;&nbsp;&nbsp;&nbsp;${user.t_id}.${user.t_userName}</c:if>
														    <c:if test="${status.index %6 == 5}">
																    <br/>
															</c:if>
													   </c:forEach>
													</td>
												</c:if>
											</tr>
											<!-- <tr>
												<td align="right">是否有效:</td>
												<td>
													<input  type="radio" name="isUse"  value="0" />无效
													<input  type="radio" name="isUse"  value="1"  checked="checked"/>有效
												</td> 
											</tr> -->
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
						<input type="button" name="Submit2" value="返回" class="button" onclick="window.history.go(-1);" />
					</TD>
				</TR>
			</TABLE>
		</div>
	</form>
</body>
</html>
