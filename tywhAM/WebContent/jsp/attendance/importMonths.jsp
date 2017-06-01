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
	<form action="<%=basePath%>attendance/importMonthAllAttendances.action" method="post"  enctype="multipart/form-data" >
		<div class="MainDiv" style="overflow: auto;">
			<table width="99%" border="0" cellpadding="0" cellspacing="0"
				class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：系统管理->考勤管理->录入导出考勤相关数据(按天)</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0" style="width: 100%">
							<tr>
								<td width="100%">
									<fieldset style="height: 100%;">
										<legend>录入导出考勤相关数据(按天)</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
												<tr>
												<td align="center" class="STYLE1"  colspan="2" nowrap="nowrap">
													<div>
														<span style="font-size:12;">导&nbsp;出&nbsp;日&nbsp;期&nbsp;范&nbsp;围：</span><input type="text" id="startDate"  name="startDate" value="${startDate}"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"  onchange="validate();"  class="Wdate" size="16"/>
														<span style="font-size:12;">&nbsp;至&nbsp;</span><input type="text" id="endDate"  name="endDate" value="${endDate}"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"   onchange="validate();" class="Wdate" size="16"/>
													    <span style="color: red">默认查询上月1号/上月最后一天</span>
													</div>
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
			var flag = verifyRequired("startDate", "开始日期")
					  && verifyRequired("endDate", "结束日期");
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
