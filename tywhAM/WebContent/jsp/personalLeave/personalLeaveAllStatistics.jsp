<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
.STYLE1 {font-size: 12px}
.STYLERed {font-size: 12px;color: red}
.STYLE3 {font-size: 12px; font-weight: bold; }
.STYLE4 {
	color: #03515d;
	font-size: 12px;
}
.STYLE5 {
	color: #03515d;
	font-size: 12px;
	background: url("../html/images/tab_19.gif");
}
.tablestyle_title{
	background:url("../html/images/tab_05.gif");
	padding:5px;
	text-align:left;
	color:#000000;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	height: 14px;
	font-style: normal; font-variant: normal;font-size: 20px;letter-spacing: 2px; padding-left: 10px; 
}
-->
</style>
<script>
var  highlightcolor='#c1ebff';
//此处clickcolor只能用win系统颜色代码才能成功,如果用#xxxxxx的代码就不行,还没搞清楚为什么:(
var  clickcolor='#51b2f6';
function  changeto(){
source=event.srcElement;
if  (source.tagName=="TR"||source.tagName=="TABLE")
return;
while(source.tagName!="TD")
source=source.parentElement;
source=source.parentElement;
cs  =  source.children;
//alert(cs.length);
if  (cs[1].style.backgroundColor!=highlightcolor&&source.id!="nc"&&cs[1].style.backgroundColor!=clickcolor)
for(i=0;i<cs.length;i++){
	cs[i].style.backgroundColor=highlightcolor;
}
}

function  changeback(){
if  (event.fromElement.contains(event.toElement)||source.contains(event.toElement)||source.id=="nc")
return
if  (event.toElement!=source&&cs[1].style.backgroundColor!=clickcolor)
//source.style.backgroundColor=originalcolor
	for(i=0;i<cs.length;i++){
		cs[i].style.backgroundColor="";
	}
}

function  clickto(){
source=event.srcElement;
if  (source.tagName=="TR"||source.tagName=="TABLE")
return;
while(source.tagName!="TD")
source=source.parentElement;
source=source.parentElement;
cs  =  source.children;
//alert(cs.length);
if  (cs[1].style.backgroundColor!=clickcolor&&source.id!="nc")
for(i=0;i<cs.length;i++){
	cs[i].style.backgroundColor=clickcolor;
}
else
for(i=0;i<cs.length;i++){
	cs[i].style.backgroundColor="";
}
}
</script>
<%@include file="../taglib.jsp"%>
<%@include file="../result.jsp"%>
</head>

<body>
<form action="<%=basePath%>personalLeave/showPersonalLeaveAllStatistics.action" method="post" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
   		<td height="30">
			<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tbody>
					<tr>
						<th class="tablestyle_title">
							<span class="STYLE4">你当前的位置：系统管理->请假管理->请假统计</span>
						</th>
					</tr>
						<tr>
							<td>
								<table cellspacing="0" cellpadding="0" border="0" style="width:100%;height:60px;background:'../html/images/bg.gif';">
									<tbody>
										<tr>
											<td width="100%" >
												<fieldset style="height:100%;">
															<input type="hidden" name="pageSize" value="<c:if test="${not empty page}">${page.pageSize}</c:if>" id="pageSize1" /> 
															<input type="hidden" id="curPage" name="curPage" value="1" />
													<table cellspacing="5" cellpadding="5"  style="width:100%">
														<tbody>
															<tr>
																<td align="left"  class="STYLE1" >
																	查询条件:
																</td>
																<td align="center" class="STYLE1" nowrap="nowrap">
																	<div>
																		<span style="font-size:12;">部&nbsp;&nbsp;门：</span>
																		<select id="daptId" name="daptId" style="width: 200px"  onchange="validate();">
																			<option value="">请选择</option>
																			<c:if test="${not empty listDapt}">
																				<c:forEach var="dapt" items="${listDapt}">
																					<option value="${dapt.t_id}"  <c:if test="${dapt.t_id == daptId}">selected="selected"</c:if>>${dapt.t_departName}</option>
																				</c:forEach>
																			</c:if>
																		</select>
																		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<span style="font-size:12;">请&nbsp;假&nbsp;人：</span><input type="text" id="leavaUser"  name="leavaUser" value="${leavaUser}"   onmouseout="validate();" size="16"/>
																	</div>
																</td>
															</tr>
															<tr>
																<td align="left"  class="STYLE1" >
																</td>
																<td align="center"  class="STYLE1" >
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
																		<select id="month" name="month" style="width: 200px"  onchange="validate();">
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
																</td>
																<td  align="center"  class="STYLE1"  >
																	<!-- <input type="button" align="center"  value="查询"   style="background: url(../html/images/tab_05.gif); width:85px;height:30px;no-repeat; border:none;" onclick="validate();"> -->
																</td>
															</tr>
														</tbody>
													</table>
												</fieldset>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
  </tr>
  <tr>
    <td height="30" background="../html/images/tab_05.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="12" height="30"><img src="../html/images/tab_03.gif" width="12" height="30" /></td>
        <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="46%" style="align-text:left;float: left;">
	            <table width="100%" border="0" cellspacing="0" cellpadding="0">
	              <tr>
	                   <td width="500px">
		                   <table width="87%" border="0" cellpadding="0" cellspacing="0">
			                  <tr>
			                    <td class="STYLE1" align="left">
			                    
			                     </td>
			                  </tr>
			                </table>
		                </td>
	              </tr>
	            </table>
            </td>
            <td width="100px" align="right" >
	            <table width="90%" border="0" cellpadding="0" cellspacing="0">
	                  <tr>
	                  </tr>
			    </table>
		    </td>
          </tr>
        </table>
        </td>
        <td width="16"><img src="../html/images/tab_07.gif" width="16" height="30" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="8" background="../html/images/tab_12.gif">&nbsp;</td>
        <td><table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="b5d6e6" onmouseover="changeto()"  onmouseout="changeback()">
          <tr>
            <td width="8%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"  class="STYLE1"><div align="center">操作</div></td>
            <td width="4%" height="22"  background="../html/images/bg.gif"   bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">请假人</span></div></td>
            <td width="4%" height="22"  background="../html/images/bg.gif"   bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">事假统计</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">事假次数</span></div></td>
            <td width="7%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">事假超过8.5小时*2</span></div></td>
            <td width="6%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">事假超过3次*5</span></div></td>
            <td width="9%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">超时和超次均衡最终罚时</span></div></td>
            <td width="6%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">请假总罚时数</span></div></td>
            <td width="6%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">月违规罚款合计</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">一级审核</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">二级审核</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">病假统计</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">婚假统计</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">产假统计</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">哺乳假统计</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">丧假统计</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">驾校假统计</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">论文假统计</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">公出假统计</span></div></td>
            <td width="4%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">串休假统计</span></div></td>
          </tr>
          <c:if test="${not empty list}">
          	   <c:forEach   var="pl"   items="${list}" >
		          <tr>
		          <td height="20" bgcolor="#FFFFFF">
		            	<div align="center">
		            		<span class="STYLE4">
		            			<a href="#" onclick="submitView(${pl.t_userId},${pl.t_leaveYear},${pl.t_leaveMonth});"style="text-decoration:none;"><img src="../html/images/select.jpg" width="16" height="16" style="border: none;"/>查看详细</a>
		            			<a href="#" onclick="submitUpdate(${pl.t_id},${pl.t_userId},${pl.t_leaveYear},${pl.t_leaveMonth});"style="text-decoration:none;"><img src="../html/images/edt.gif" width="16" height="16" style="border: none;"/>修改</a>
		            		</span>
		            	</div>
		            </td>
		            <td height="20" bgcolor="#FFFFFF"><div align="center" ><span class="STYLE1">${pl.userName}</span></div></td>
		            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_leaveStatistics}<c:if test="${empty pl.t_leaveStatistics}">0.0</c:if>小时</span></div></td>
		            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_leaveCountStatistics}<c:if test="${empty pl.t_leaveCountStatistics}">0</c:if>次</span></div></td>
		            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.leaveHourStatisicsTime}<c:if test="${empty pl.leaveHourStatisicsTime}">0.0</c:if>小时</span></div></td>
		            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.leaveCountStatisticsTime}<c:if test="${empty pl.leaveCountStatisticsTime}">0</c:if>小时</span></div></td>
		            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"> ${pl.maxStatisics}<c:if test="${empty pl.maxStatisics}">0.0</c:if>小时</span></div></td>
		            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_leaveHoursStatisics}<c:if test="${empty pl.t_leaveHoursStatisics}">0.0</c:if>小时</span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_monthViolationFineAggregate}<c:if test="${empty pl.t_monthViolationFineAggregate}">0</c:if></span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"><c:if test="${pl.oneApproverState=='0'}">通过</c:if><c:if test="${pl.oneApproverState!='0'}"><a href="#" onclick="submitView(${pl.t_userId},${pl.t_leaveYear},${pl.t_leaveMonth});"style="text-decoration:none;color: red">未通过</a></c:if></span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1"><c:if test="${pl.twoApproverState=='0'}">通过</c:if><c:if test="${pl.twoApproverState!='0'}"><a href="#" onclick="submitView(${pl.t_userId},${pl.t_leaveYear},${pl.t_leaveMonth});"style="text-decoration:none;color: red">未通过</a></c:if></span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_sickLeaveStatistics}<c:if test="${empty pl.t_sickLeaveStatistics}">0.0</c:if>小时</span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_maritalLeaveStatistics}<c:if test="${empty pl.t_maritalLeaveStatistics}">0.0</c:if>小时</span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_maternityLeaveStatistics}<c:if test="${empty pl.t_maternityLeaveStatistics}">0.0</c:if>小时</span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_lactationLeaveStatistics}<c:if test="${empty pl.t_lactationLeaveStatistics}">0.0</c:if>小时</span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_funeralLeaveStatistics}<c:if test="${empty pl.t_funeralLeaveStatistics}">0.0</c:if>小时</span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_drivingLeaveStatistics}<c:if test="${empty pl.t_drivingLeaveStatistics}">0.0</c:if>小时</span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_thesisStatistics}<c:if test="${empty pl.t_thesisStatistics}">0.0</c:if>小时</span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_businessLeaveStatistics}<c:if test="${empty pl.t_businessLeaveStatistics}">0.0</c:if>小时</span></div></td>
					<td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${pl.t_stringBreakLeaveStatistics}<c:if test="${empty pl.t_stringBreakLeaveStatistics}">0.0</c:if>小时</span></div></td>
		          </tr>
	          </c:forEach>
          </c:if>
        </table></td>
        <td width="8" background="../html/images/tab_15.gif">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
        <td height="35" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
        	<jsp:include flush="true" page="../limit.jsp"></jsp:include>
        </td>
  </tr>
</table>
</form>
<script type="text/javascript">
		function validate(){
			document.forms[0].submit();
		}
		
		function submitView(userId,year,month)
		{
			var url ="<%=basePath%>personalLeave/showPersonalLeaveById.action?userId="+userId+"&year="+year+"&month="+month;
			location=url;
		}
		
		function submitUpdate(id,userId,year,month)
		{
			var url ="<%=basePath%>personalLeave/showPersonalLeaveStatisticsById.action?id="+id+"&userId="+userId+"&year="+year+"&month="+month;
			location=url;
		}
</script>
</body>
</html>
