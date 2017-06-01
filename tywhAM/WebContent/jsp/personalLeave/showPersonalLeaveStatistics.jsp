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
	<form action="<%=basePath%>personalLeave/editPersonalLeaveStatistics.action" method="post" >
		<div class="MainDiv">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：系统设置->请假统计->修改月违规罚款合计</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0"
							style="width: 100%">
							<TR>
								<TD width="100%">
									<fieldset style="height: 100%;">
										<legend>修改月违规罚款合计</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
											<tr>
												<td nowrap align="right" width="15%">人员：</td>
												<td width="35%">
														<input type="hidden" id="userId"  name="userId" value="${userId}"  />
														<input type="hidden" id="year"  name="year" value="${year}"  />
														<input type="hidden" id="month"  name="month" value="${month}"  />
														<input type="hidden" id="id"  name="id" value="${id}"  />
														${tplss.userName}
												</td>
												<td nowrap align="right" width="15%">月违规罚款合计：</td>
												<td width="35%">
													<input type="text" id="monthViolationFineAggregate"  name="monthViolationFineAggregate" value="${tplss.t_monthViolationFineAggregate}"   style="width: 154px"  size="16" />
													<span style="color: red">合计只能为数字</span>
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">月违规罚款原因：</td>
												<td width="35%">
													<input type="text" id="remarks"  name="remarks" value="${tplss.t_remarks}"    style="width: 154px"  size="16"/>
												</td>
												<td align="right" width="15%"></td>
												<td width="35%">
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
	var flag = matchNumber2("monthViolationFineAggregate","月违规罚款合计");
	if(flag){document.forms[0].submit();}
}
</script>
</body>
</html>
