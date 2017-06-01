<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.tywh.orm.TUserinfo"%>
<%
	TUserinfo curUser = (TUserinfo) session
			.getAttribute("curManagUser");
	if (curUser == null) {
		out.println("curUser == null");
		return;
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

.STYLE1 {
	font-size: 12px;
	color: #FFFFFF;
}

.STYLE2 {
	font-size: 9px
}

.STYLE3 {
	color: #FFFFFF;
	font-size: 14px;
}

#clock_a {
	float: right;
	padding: 15px 0 0 0;
}

.jclock {
	color: #33ccff;
	float: right;
	font-weight: bold;
	width: 150px;
}
-->
</style>
<%@include file="../taglib.jsp"%>
<%@include file="../result.jsp"%>
<script src="<%=basePath%>js/jquery.jclock-1.2.0.js.txt"
	type="text/javascript"></script>
<script type="text/javascript">
	$(function($) {
		$('.jclock').jclock();
		window.setInterval("getNoyice()",1000);
	});
	
	function getNoyice(){
		//<a href=\"http://www.baidu.com\" target=\"middle\" >最新公告:"+new Date()+"</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"www.baidu.com\" target=\"middle\" >最新公告2:"+new Date()+"</a>
		document.getElementById("notice").innerHTML="欢迎&nbsp;└(^o^)┘&nbsp;<%=curUser.getT_userName()%>&nbsp;└(^o^)┘&nbsp;登录同源文化考勤辅助平台!!!!!";
	}
</script>
</head>

<body>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="70" background="../../html/images/main_05.gif"><table
					width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td height="24"><table width="100%" border="0"
								cellspacing="0" cellpadding="0">
								<tr>
									<td width="270" height="24"
										background="../../html/images/main_03.gif">&nbsp;</td>
									<td width="505" background="../../html/images/main_04.gif">&nbsp;</td>
									<td>&nbsp;</td>
									<td width="21"><img src="../../html/images/main_07.gif"
										width="21" height="24"></td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td height="38"><table width="100%" border="0"
								cellspacing="0" cellpadding="0">
								<tr>
									<td width="270" height="38"
										background="../../html/images/main_09.gif">&nbsp;</td>
									<td><table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr>
												<td width="77%" height="25" valign="bottom"><table
														width="100%" border="0" cellspacing="0" cellpadding="0">
														<tr><!-- 
															<td width="50" height="19">
																<div align="center">
																	<img
																		src="../../html/images/main_12.gif" width="49"
																		height="19" style="border: none;">
																</div>
															</td>
															
															<td width="100">
																<div align="center">
																	<img
																		src="../../html/images/main_38.gif" width="98"
																		height="19" style="border: none;">
																</div>
															</td> -->
															<td width="100">
																<div align="center">
																	<a href="<%=basePath%>userInfo/showEditUserInfoById.action"
																		target="middel"><img
																		src="../../html/images/main_22.gif" width="98"
																		height="19" style="border: none;"></a>
																</div>
															</td>
															<td width="26">
																<div align="center">
																	<img src="../../html/images/main_21.gif" width="26"
																		height="19">
																</div>
															</td>
															<td width="50">
																<div align="center">
																	<a href="<%=basePath%>userAct/logout.action" target="_parent">
																	<img src="../../html/images/main_20.gif" width="48" height="19" style="border: none;"></a>
																</div>
															</td>
															<td>&nbsp;</td>
														</tr>
													</table></td>
												<td width="23%" valign="bottom" nowrap="nowrap"></div>
													<div class="jclock"></div></td>
											</tr>
										</table></td>
									<td width="21"><img src="../../html/images/main_11.gif"
										width="21" height="38"></td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td height="8" style="line-height: 8px;"><table width="100%"
								border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="270" background="../../html/images/main_29.gif"
										style="line-height: 8px;">&nbsp;</td>
									<td width="505" background="../../html/images/main_30.gif"
										style="line-height: 8px;">&nbsp;</td>
									<td style="line-height: 8px;">&nbsp;</td>
									<td width="21" style="line-height: 8px;"><img
										src="../../html/images/main_31.gif" width="21" height="8"></td>
								</tr>
							</table></td>
					</tr>
				</table></td>
		</tr>
		<tr>
			<td height="28" background="../../html/images/main_36.gif"><table
					width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="177" height="28"
							background="../../html/images/main_32.gif"><table
								width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="15%" height="22">&nbsp;</td>
									<td width="70%" valign="bottom">
									<div align="center" class="STYLE1"> 登陆人:<%=curUser.getT_userName()%></div></td>
									<td width="15%">&nbsp;</td>
								</tr>
							</table>
						</td>
						<td>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<marquee  class="STYLE3"  id="notice"  scrollAmount="3" onMouseOver="this.stop()" onMouseOut="this.start()" direction="right" bgcolor="#66CCFF" behavior="alternate" loop=-1 scrolldelay="100"></marquee>
							</table>
						</td>
						<td width="21"><img src="../../html/images/main_37.gif" width="21" height="28"></td>
					</tr>
				</table></td>
		</tr>
	</table>

</body>
</html>
