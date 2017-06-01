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
	<form action="<%=basePath%>personalLeave/savePersonalLeave.action" method="post" >
		<div class="MainDiv">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：我的设置->我的请假->请假新增</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0"
							style="width: 100%">
							<TR>
								<TD width="100%">
									<fieldset style="height: 100%;">
										<legend>请假信息</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
											<tr>
												<td nowrap align="right" width="15%">请假类型:</td>
												<td width="35%">
														<select id="plstate" name="plstate" style="width: 200px" onchange="editLeaveType(this.value)">
															<option value="">请选择</option>
															<option value="1">事假</option>
															<option value="2">病假</option>
															<option value="3">公出</option>
															<option value="4">串休</option>
															<option value="5">丧假</option>
															<option value="6">驾校假</option>
															<option value="7">产假</option>
															<option value="8">哺乳假</option>
															<option value="9">婚假</option>
															<option value="10">论文假</option>
														</select>
														<span style="color: red">*</span>
												</td>
												<td nowrap align="right" width="15%">请假日期:</td>
												<td width="35%">
													<input type="text" id="leaveDay"  name="leaveDay" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"  style="width: 154px"  class="Wdate" size="16"/>
													<span style="color: red">*</span>
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">请假时间段:</td>
												<td width="35%">
													<input type="text" id="leaveTimes"  name="leaveTimes"  style="width: 154px" onBlur="editLeaveDuration(this.value)"  size="16"/>
													<span style="color: red">*请复制修改时间,不要修改格式 
													</br>例:上午 08:25-11:55  下午 12:55-17:00  全天 08:25-17:00
													</span>
												</td>
												<td align="right" width="15%">请假时长:</td>
												<td width="35%">
													<input type="text" id="leaveDuration"  name="leaveDuration"  readonly="readonly"  style="width: 154px" size="16"/>
													<span style="color: red">自动计算时长单位为小时</span>
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">请假原因:</td>
												<td width="35%">
													<input type="text" id="leaveType"  name="leaveType" style="width: 154px"  size="16"  onBlur="editLeaveType2()"/>
												</td>
												<td align="right" width="15%">该请假是否需要多级审批:</td>
												<td width="35%">
													<input type="checkbox" id="isTwoApprover"  name="isTwoApprover"   style="width: 154px" size="16"/>
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
	var flag = verifyRequired("plstate","请假类型")&&verifyRequired("leaveDay","请假日期")&&verifyRequired("leaveTimes","请假时间段")&&verifyRequired("leaveDuration","请正确填写请假时间段格式/");
	if(flag){document.forms[0].submit();}
}


function editLeaveType(value)
{
	//两个分钟数相减得到时间部分的差值，以分钟为单位
	if(value=="4")
	{
		document.getElementById("leaveType").value="调休;"+document.getElementById("leaveType").value;
	}
	else
	{
		document.getElementById("leaveType").value=document.getElementById("leaveType").value;
	}
}

function editLeaveType2()
{
	var ss=document.getElementById("plstate").value;
	//两个分钟数相减得到时间部分的差值，以分钟为单位
	if(ss=="4")
	{
		document.getElementById("leaveType").value="调休;";
	}
	else
	{
		document.getElementById("leaveType").value=document.getElementById("leaveType").value;
	}
}

function editLeaveDuration(value)
{
	var arr = value.split("-");
	var time = value.replace("：",":");
	var hour1=parseInt(arr[0].substr(0,2));//第一小时
	var min1=parseInt(arr[0].substr(3,5));//第一分钟
	var hour2=parseInt(arr[1].substr(0,2));//第二小时
	var min2=parseInt(arr[1].substr(3,5));//第二分钟
	var sum1=hour1*60+min1;//第一时间和
	var sum2=hour2*60+min2;//第二时间和
	
	//两个分钟数相减得到时间部分的差值，以分钟为单位
	var n=(sum2-sum1)/60;
	n=parseFloat(n).toFixed(1);//这里保留一位小数   四舍五入
	//分别判断 改时间是否都在上午  或者 跨越中午  这样的话 减去中午的一个小时
	//第一种 上午请假  
	if(hour1<=11 && hour2<=11){
		if(n>3.5)
		{
			n=3.5;
		}else
		{
			n=n;
		}
		//alert("上午："+n);
	}
	//第二种  跨越中午请假 全天请假
	else  if(hour1<=11 && hour2>=12){
		if(hour1<=11 && hour2>=12)
		{
			n=n-1;
		}else
		{
			n=n;
		}
		
		if(n>7.5)
		{
			n=7.5;
		}
		//alert("跨越中午请假："+parseFloat(n).toFixed(1));
	}
	//第三种 下午请假
	else if(hour1>=11 && hour2>=12){
		if(hour1==12 && hour2>=17)
		{
			n=4.0;
		}
		else
		{
			n=n;
		}
		//alert("下午请假："+n);
	}
	//替换：字符串
	document.getElementById("leaveTimes").value=time;
	if(n>0)
	{
		document.getElementById("leaveDuration").value=parseFloat(n).toFixed(1);
	}
	else
	{
		alert("请输入正确的时段");
		document.getElementById("leaveDuration").value="";
		document.getElementById("leaveTimes").value="";
		document.getElementById("leaveTimes").focus();
	} 
}

</script>
</body>
</html>
