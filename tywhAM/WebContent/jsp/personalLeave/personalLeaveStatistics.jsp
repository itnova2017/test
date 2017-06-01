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
	<form action="<%=basePath%>personalLeave/showPersonalLeaveStatistics.action" method="post" >
		<div class="MainDiv">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：我的管理->我的请假->请假统计</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0"
							style="width: 100%">
							<tr>
							<td align="center" class="STYLE1" nowrap="nowrap">
								<div>
									<span style="font-size:12;">查&nbsp;看&nbsp;年&nbsp;月&nbsp;范&nbsp;围：</span>
									<select id="year" name="year" style="width: 200px"  onchange="validate();">
											<option value="">请选择</option>
											<c:if test="${not empty listYears}">
												<c:forEach var="years" items="${listYears}">
													<option value="${years}"  <c:if test="${years==year }">selected="selected"</c:if>>${years}</option>
												</c:forEach>
											</c:if>
									</select>		
									年		
									<select id="month" name="month" style="width: 200px" onchange="validate();">
											<option value="">请选择</option>
											<option value="1"  <c:if test="${month==1}">selected="selected"</c:if>>1</option>
											<option value="2"  <c:if test="${month==2}">selected="selected"</c:if>>2</option>
											<option value="3"  <c:if test="${month==3}">selected="selected"</c:if>>3</option>
											<option value="4"  <c:if test="${month==4}">selected="selected"</c:if>>4</option>
											<option value="5"  <c:if test="${month==5}">selected="selected"</c:if>>5</option>
											<option value="6"  <c:if test="${month==6}">selected="selected"</c:if>>6</option>
											<option value="7"  <c:if test="${month==7}">selected="selected"</c:if>>7</option>
											<option value="8"  <c:if test="${month==8}">selected="selected"</c:if>>8</option>
											<option value="9"  <c:if test="${month==9}">selected="selected"</c:if>>9</option>
											<option value="10" <c:if test="${month==10}">selected="selected"</c:if>>10</option>
											<option value="11" <c:if test="${month==11}">selected="selected"</c:if>>11</option>
											<option value="12" <c:if test="${month==12}">selected="selected"</c:if>>12</option>
									</select>		
									月			
									<!-- <input type="button" align="center"  value="查询"   style="background: url(../html/images/tab_05.gif); width:85px;height:30px;no-repeat; border:none;" onclick="validate();">										 -->
							</div>
							</td>
						</tr>
						<tr>
								<td width="100%">
									<fieldset style="height: 100%;">
										<legend>请假信息</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
											<tr>
												<td nowrap align="right" width="15%">事假统计:</td>
												<td width="35%">
														${tplss.t_leaveStatistics}<c:if test="${empty tplss.t_leaveStatistics}">0.0</c:if>小时
												</td>
												<td nowrap align="right" width="15%">病假统计:</td>
												<td width="35%">
													${tplss.t_sickLeaveStatistics}<c:if test="${empty tplss.t_sickLeaveStatistics}">0.0</c:if>小时
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">公休统计:</td>
												<td width="35%">
														${tplss.t_businessLeaveStatistics}<c:if test="${empty tplss.t_businessLeaveStatistics}">0.0</c:if>小时
												</td>
												<td nowrap align="right" width="15%">串休统计:</td>
												<td width="35%">
													${tplss.t_stringBreakLeaveStatistics}<c:if test="${empty tplss.t_stringBreakLeaveStatistics}">0.0</c:if>小时
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">丧假统计:</td>
												<td width="35%">
														${tplss.t_funeralLeaveStatistics}<c:if test="${empty tplss.t_funeralLeaveStatistics}">0.0</c:if>小时
												</td>
												<td nowrap align="right" width="15%">婚假统计:</td>
												<td width="35%">
														${tplss.t_maritalLeaveStatistics}<c:if test="${empty tplss.t_maritalLeaveStatistics}">0.0</c:if>小时
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">产假统计:</td>
												<td width="35%">
														${tplss.t_maternityLeaveStatistics}<c:if test="${empty tplss.t_maternityLeaveStatistics}">0.0</c:if>小时
												</td>
												<td nowrap align="right" width="15%">哺乳假统计:</td>
												<td width="35%">
													    ${tplss.t_lactationLeaveStatistics}<c:if test="${empty tplss.t_lactationLeaveStatistics}">0.0</c:if>小时
												</td>
											</tr>
											<tr>
												
												<td nowrap align="right" width="15%">驾校假统计:</td>
												<td width="35%">
													  ${tplss.t_drivingLeaveStatistics}<c:if test="${empty tplss.t_drivingLeaveStatistics}">0.0</c:if>小时
												</td>
												<td nowrap align="right" width="15%">本月之前驾校假小时数:</td>
												<td width="35%">
												    ${drivingLeaveStatisticsTime}<c:if test="${empty drivingLeaveStatisticsTime}">0.0</c:if>小时
												    <span style="color: red">*自己一共请过的驾校假,如果该时间超多10天(8.5小时*10)就会按照双倍扣钱</span>
												</td>
											</tr>
											<tr>
												
												<td nowrap align="right" width="15%">论文假统计:</td>
												<td width="35%">
													  ${tplss.t_thesisStatistics}<c:if test="${empty tplss.t_thesisStatistics}">0.0</c:if>小时
												</td>
												<td nowrap align="right" width="15%">本月之前论文假小时数:</td>
												<td width="35%">
												    ${thesisStatisticsTime}<c:if test="${empty thesisStatisticsTime}">0.0</c:if>小时
												    <span style="color: red">*自己一共总共请过的驾校假,如果该时间超多10天(8.5小时*10)就会按照双倍扣钱</span>
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">事假次数统计:</td>
												<td width="35%">
													${tplss.t_leaveCountStatistics}<c:if test="${empty tplss.t_leaveCountStatistics}">0</c:if>次
												</td>
												<td nowrap align="right" width="15%">事假超过8.5小时*2统计:</td>
												<td width="35%">
												    ${tplss.leaveHourStatisicsTime}<c:if test="${empty tplss.leaveHourStatisicsTime}">0.0</c:if>小时
												    <span style="color: red">*请事假时长超过8.5小时(事假-8.5)*2+8.5</span>
												</td>
												
											</tr>
											<tr>
												<td nowrap align="right" width="15%">事假超过3次乘以5倍小时数:</td>
												<td width="35%">
													${tplss.leaveCountStatisticsTime}<c:if test="${empty tplss.leaveCountStatisticsTime}">0</c:if>小时
													<span style="color: red">*请假次数大于3(事假*5)</span>
												</td>
												<td nowrap align="right" width="15%">超时和超次均衡最终罚时:</td>
												<td width="35%">
												    ${tplss.maxStatisics}<c:if test="${empty tplss.maxStatisics}">0.0</c:if>小时
												    <span style="color: red">*这里时长取事假罚时中大的数作为基准</span>
												</td>
												
											</tr>
											<tr>
												<td nowrap align="right" width="15%">月违规罚款合计:</td>
												<td width="35%">
												    ${tplss.t_monthViolationFineAggregate}<c:if test="${empty tplss.t_monthViolationFineAggregate}">0</c:if>
												    <span style="color: red">*这里是审计人员通过查询全部人员的统计时进行修改</span>
												</td>
												<td nowrap align="right" width="15%">请假总罚时数(各项请假总罚时数):</td>
												<td width="35%">
													${tplss.t_leaveHoursStatisics}<c:if test="${empty tplss.t_leaveHoursStatisics}">0.0</c:if>小时
													<span style="color: red">*这里时长取事假罚时中大的数作为基准,病假的小时数/丧假大于8.5*3的小时数/(驾校假/论文假)大于8.5*10双倍的小时数/婚假大于8.5*10双倍的小时数/哺乳假小时数/产假之和</span>
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">月违规罚款备注(其他扣款注释):</td>
												<td width="35%">
													${tplss.t_remarks}
												</td>
											</tr>
										</table>
										<br />
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</form>
<script type="text/javascript">
function validate(){
	var flag = verifyRequired("year","年")&&verifyRequired("month","月");
	if(flag)
	{document.forms[0].submit();}
}
</script>
</body>
</html>

