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
	<form action="<%=basePath%>attendance/saveAttendance.action" method="post" >
		<div class="MainDiv">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：系统管理->考勤管理->考勤补录</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0"
							style="width: 100%">
							<TR>
								<TD width="100%">
									<fieldset style="height: 100%;">
										<legend>考勤补录</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
											<tr>
												<td nowrap align="right" width="15%">补录人员名称:</td>
												<td width="35%">
														<select id="numIds" name="numIds" style="width: 200px" >
																<option value="">请选择</option>
															<c:if test="${not empty tapList}">
																<c:forEach var="tap" items="${tapList}">
																	<option value="${tap.num}-${tap.name}">${tap.name}</option>
																</c:forEach>
															</c:if>
														</select>
														<span style="color: red">*可以选择已有的人员</span>
												</td>
												<td align="right" width="15%">考勤日期:</td>
												<td width="35%">
													<input type="text"  id="aDate"  name="aDate"  value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"  class="Wdate"  style="width: 154px" size="16"/>
													<span style="color: red">补录某天的日期</span>
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">新增人员编号:</td>
												<td width="35%">
													<input type="text"  id="newNum"  name="newNum"    value=""    style="width: 154px" size="16"/>
													<span style="color: red">*</span>
												</td>
												<td nowrap align="right" width="15%">新增人员名称:</td>
												<td width="35%">
													<input type="text"  id="newName"  name="newName"  value=""   style="width: 154px"  size="16"/>
													<span style="color: red">*可以新增人员</span>
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">上班时间:</td>
												<td width="35%">
													<input type="text" id="startWork"  name="startWork" value=""  onfocus="WdatePicker({dateFmt:'HH:mm'})"  class="Wdate"    style="width: 154px"  size="16"/>
												</td>
												<td align="right" width="15%">下班时间:</td>
												<td width="35%">
													<input type="text" id="endWork"  name="endWork"  value=""    onfocus="WdatePicker({dateFmt:'HH:mm'})"  class="Wdate"    style="width: 154px"   size="16"/>
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">中午下班时间:</td>
												<td width="35%">
													<input type="text" id="startWork2"  name="startWork2" value=""  onfocus="WdatePicker({dateFmt:'HH:mm'})"  class="Wdate"    style="width: 154px"  size="16"/>
												</td>
												<td align="right" width="15%">中午上班时间:</td>
												<td width="35%">
													<input type="text" id="endWork2"  name="endWork2"  value=""    onfocus="WdatePicker({dateFmt:'HH:mm'})"  class="Wdate"    style="width: 154px"   size="16"/>
												</td>
											</tr>
											<tr>
												<td align="right" width="15%">平时加班（时）:</td>
												<td width="35%">
													<input type="text" id="plusWork"  name="plusWork"  value=""       style="width: 154px"   size="16"/>
												</td>
												<td nowrap align="right" width="15%">公休加班（时）:</td>
												<td width="35%">
													<input type="text" id="holidayWork"  name="holidayWork" value=""  style="width: 154px"  size="16"/>
												</td>
											</tr>
											<tr>
												<td align="right" width="15%">缺勤（时）:</td>
												<td width="35%">
													<input type="text" id="absenceWork"  name="absenceWork"  value=""   style="width: 154px"   size="16"/>
												</td>
												<td nowrap align="right" width="15%">缺勤次数:</td>
												<td width="35%">
													<input type="text" id="absenceCount"  name="absenceCount" value=""  style="width: 154px"  size="16"/>
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
	var flag = ((verifyRequired("newName","新增人员名称") && verifyRequired("newNum","新增人员编号")) || verifyRequired("numIds","补录人员名称"))
	&&verifyRequired("aDate","考勤日期")&&verifyRequired("startWork","上班时间")&&verifyRequired("endWork","下班时间")
	&&matchNumber2("plusWork","平时加班（时）")&&matchNumber2("holidayWork","公休加班（时）")
	&&matchNumber2("absenceWork","缺勤（时）")&&matchNumber2("absenceCount","缺勤次数");
	
	if(flag){
			var newName=document.getElementById("newName").value;
			$.post('<%=basePath%>attendance/repeatName.action',{name:newName},
				function(data, textStatus){
					if(data&&$.trim(data)=='error'){
						alert("该人员名称已有，请重新输入其它名称或加入关键字用已区别");
					}else{
						document.forms[0].submit();
					}
				}
		   );
		
	}
}

</script>
</body>
</html>
