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
	<form action="<%=basePath%>department/saveDepartment.action" method="post" >
		<div class="MainDiv">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：系统管理->部门管理->部门Ajax1</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0"
							style="width: 100%">
							<TR>
								<TD width="100%">
									<fieldset style="height: 100%;">
										<legend>数据信息</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
											<tr>
												<td nowrap align="right" width="15%">数据:</td>
												<td width="35%">
													<input class="text"  type="text"  name="shuju"  id="shuju"  onblur="yanZheng(this.value);" style="width: 154px" value=""  size="50" /> 
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
			</TABLE>
		</div>
	</form>
<script type="text/javascript">
function validate(){
		document.forms[0].submit();
}

function yanZheng(shuju)
{
	var flag = verifyRequired("shuju","数据")&&verifyLength("shuju",50,"数据");
	if(flag){
		alert(shuju);
		$.post('<%=basePath%>department/yanZhengMingCheng.action',{shujuxinxi:shuju},
				function(data, textStatus){
						alert(data);
				}
		);
	}
}

</script>
</body>
</html>
