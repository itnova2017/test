<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" rev="stylesheet" href="../html/style.css"
	type="text/css" media="all" />
<link rel="stylesheet" rev="stylesheet" href="../js/uploadify.css"
	type="text/css" media="all" />
<title></title>
<style type="text/css">
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
.mydiv {
	background-color: #FFFFFF;
	border: 1px solid #66ffff;
	text-align: center;
	line-height: 40px;
	font-size: 13px;
	font-weight: bold;
	z-index: 99;
	width: 300px;
	height: 40px;
	left: 50%; /*FF IE7*/
	top: 50%; /*FF IE7*/
	margin-left: -150px !important; /*FF IE7 该值为本身宽的一半 */
	margin-top: -60px !important; /*FF IE7 该值为本身高的一半*/
	margin-top: 0px;
	position: fixed !important; /*FF IE7*/
	position: absolute; /*IE6*/
	_top: expression(eval(document.compatMode &&  
              document.compatMode ==   'CSS1Compat')?  
            documentElement.scrollTop+   (document.documentElement.clientHeight-this.offsetHeight
		)/2:   /*IE6*/  
            document.body.scrollTop+   (document.body.clientHeight-
		this.clientHeight )/2 ); /*IE5 IE5.5*/
}

.bg {
	background-color: #ccc;
	width: 150%;
	height: 100%;
	left: 0;
	top: 0; /*FF IE7*/
	filter: alpha(opacity =   50); /*IE*/
	opacity: 0.5; /*FF*/
	z-index: 1;
	position: fixed !important; /*FF IE7*/
	position: absolute; /*IE6*/
	_top: expression(eval(document.compatMode &&  
              document.compatMode ==   'CSS1Compat')?  
            documentElement.scrollTop+   (document.documentElement.clientHeight-this.offsetHeight
		)/2:   /*IE6*/  
            document.body.scrollTop+   (document.body.clientHeight-
		this.clientHeight )/2 ); /*IE5 IE5.5*/
}
</style>
<%@include file="../taglib.jsp"%>
<%@include file="../result.jsp"%>
</head>

<body class="ContentBody">
	<form action="<%=basePath%>attendance/importAttendances.action"
		method="post"  enctype="multipart/form-data" >
		<div class="MainDiv" style="overflow: auto;">
			<table width="99%" border="0" cellpadding="0" cellspacing="0"
				class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：系统管理->考勤管理->录入导出考勤相关数据(按月汇总)</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0" style="width: 100%">
							<tr>
								<td width="100%">
									<fieldset style="height: 100%;">
										<legend>录入导出考勤相关数据(按月汇总)</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
												<tr>
												<td align="center" class="STYLE1" style="width:100%">
												<span style="font-size: 12;">查&nbsp;看&nbsp;年&nbsp;月&nbsp;范&nbsp;围：</span>
													<select id="year" name="year" style="width: 200px" >
														<c:if test="${not empty listYears}">
														 <option value="">请选择</option>
															<c:forEach var="years" items="${listYears}">
																<option value="${years}"
																	<c:if test="${years==year }">selected="selected"</c:if>>${years}</option>
															</c:forEach>
														</c:if>
												</select> 年 
												<select id="month" name="month" style="width: 200px" >
														<option value="">请选择</option>
														<option value="01"
															<c:if test="${month==1 }">selected="selected"</c:if>>1</option>
														<option value="02"
															<c:if test="${month==2}">selected="selected"</c:if>>2</option>
														<option value="03"
															<c:if test="${month==3 }">selected="selected"</c:if>>3</option>
														<option value="04"
															<c:if test="${month==4 }">selected="selected"</c:if>>4</option>
														<option value="05"
															<c:if test="${month==5 }">selected="selected"</c:if>>5</option>
														<option value="06"
															<c:if test="${month==6 }">selected="selected"</c:if>>6</option>
														<option value="07"
															<c:if test="${month==7 }">selected="selected"</c:if>>7</option>
														<option value="08"
															<c:if test="${month==8}">selected="selected"</c:if>>8</option>
														<option value="09"
															<c:if test="${month==9}">selected="selected"</c:if>>9</option>
														<option value="10"
															<c:if test="${month==10 }">selected="selected"</c:if>>10</option>
														<option value="11"
															<c:if test="${month==11 }">selected="selected"</c:if>>11</option>
														<option value="12"
															<c:if test="${month==12}">selected="selected"</c:if>>12</option>
												</select> 月</td>
											</tr>
											<tr>
												<td nowrap align="center" width="100%">导出月应出勤天数:<input class="text" type="text"
													name="realityDate" id="realityDate" style="width: 50px"
													value="26" size="50" />天 <span style="color: red">例如：26</span></td>
											</tr>
											<tr>
												<td nowrap align="center" width="100%">应完成工时:<input class="text" type="text"
													name="realityTime" id="realityTime" style="width: 50px"
													value="220" size="50" />小时 <span style="color: red">例如：220</span></td>
											</tr>
										</table>
										<br />
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR>
					<TD colspan="2" align="center" height="50px"><input
						type="button" name="Submit" value="确认" class="button"
						onclick="validate();" /> <input type="button" name="Submit2"
						value="返回" class="button" onclick="window.history.go(-1);" /></TD>
				</TR>
			</TABLE>
		</div>
	</form>
	<div id="popDiv" class="mydiv" style="display:none;">请耐心等待,正在准备数据完成后会自动关闭当前页<br/></div>
<div id="bg" class="bg" style="display:none;"></div>
	<script type="text/javascript">
		function validate() {
			var flag = verifyRequired("year", "年")
					  && verifyRequired("month", "月")
					  && verifyRequired("realityDate", "导出月应出勤天数")
					  && regex("realityDate", "导出月应出勤天数");
			if (flag) {
				showDiv();
				document.forms[0].submit();
			}
		}
		
		
		function showDiv(){
			document.getElementById('popDiv').style.display='block';
			document.getElementById('bg').style.display='block';
		}
	</script>
</body>
</html>
