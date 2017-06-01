<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" rev="stylesheet" href="../html/style.css" type="text/css" media="all" />
<link rel="stylesheet" rev="stylesheet"   href="../js/uploadify.css"  type="text/css"  media="all" />
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
z-index:99;
width: 300px;
height: 40px;
left:50%;/*FF IE7*/
top: 50%;/*FF IE7*/

margin-left:-150px!important;/*FF IE7 该值为本身宽的一半 */
margin-top:-60px!important;/*FF IE7 该值为本身高的一半*/

margin-top:0px;

position:fixed!important;/*FF IE7*/
position:absolute;/*IE6*/

_top:expression(eval(document.compatMode &&
            document.compatMode=='CSS1Compat') ?
            documentElement.scrollTop + (document.documentElement.clientHeight-this.offsetHeight)/2 :/*IE6*/
            document.body.scrollTop + (document.body.clientHeight - this.clientHeight)/2);/*IE5 IE5.5*/

}

.bg {
background-color: #ccc;
width: 150%;
height: 100%;
left:0;
top:0;/*FF IE7*/
filter:alpha(opacity=50);/*IE*/
opacity:0.5;/*FF*/
z-index:1;

position:fixed!important;/*FF IE7*/
position:absolute;/*IE6*/

_top:       expression(eval(document.compatMode &&
            document.compatMode=='CSS1Compat') ?
            documentElement.scrollTop + (document.documentElement.clientHeight-this.offsetHeight)/2 :/*IE6*/
            document.body.scrollTop + (document.body.clientHeight - this.clientHeight)/2);/*IE5 IE5.5*/
}
</style>
<%@include file="../taglib.jsp"%>
<%@include file="../result.jsp"%>
<script type="text/javascript">
		$(function() {
		    $("#file_upload").uploadify({
		    	'height'        : 27, 
		    	'width'         : 80,  
		    	'buttonText'    : '浏 览',
		        'swf'           : '../js/uploadify.swf',//Flash插件
		        'uploader'      : '<%=basePath%>servlet/Upload',//后台处理的请求
		        'auto'          : false,
		        'method' : "get",
                'queueSizeLimit'  :2,
		        'fileTypeExts'  : '*.xls',
		        'formData'      : {'folder':''},
		        'onUploadStart' : function(file) {
		        	$("#file_upload").uploadify("settings", "formData", {'folder':'excel\\'});
		        }
		    });
		});
        </script> 
</head>

<body class="ContentBody">
	<form action="<%=basePath%>attendance/importAttendance.action" method="post"  enctype="multipart/form-data" >
		<div class="MainDiv" style="overflow: auto;">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" class="CContent">
				<tr>
					<th class="tablestyle_title">你当前的位置：系统管理->考勤管理->导入考勤</th>
				</tr>
				<tr>
					<td class="CPanel">
						<table border="0" cellpadding="0" cellspacing="0"
							style="width: 100%">
							<TR>
								<TD width="100%">
									<fieldset style="height: 100%;">
										<legend>导入考勤</legend>
										<table border="0" cellpadding="2" cellspacing="1" style="width: 100%">
											<tr>
												<td nowrap align="right" width="15%">文件名称:</td>
												<td width="35%">
													<input class="text"  type="text"  name="fileName"  id="fileName" style="width: 154px" value=""  size="50" /> 
													<span style="color: red">填写格式为"文件名带后缀名 例*.xls"</span> 
												</td>
											</tr>
											<tr>
												<td nowrap align="right" width="15%">导入表格:</td>
												<td width="35%">
													<input type="file" name="uploadify" id="file_upload" />
											        <p style="font-size: 16px;font-weight: bold;">
											        <a href="javascript:$('#file_upload').uploadify('upload','*')" style="text-decoration:none;">导入选中文件</a>&nbsp;   
        											<a href="javascript:$('#file_upload').uploadify('cancel', '*')" style="text-decoration:none;">取消文件导入</a> 
											        </p>
												</td>
											</tr>
										</table>
									</fieldset>
								</TD>
							</TR>
						</TABLE>
					</td>
				</tr>
				<TR>
					<TD colspan="2" align="center" height="50px">
						<input type="button" name="Submit" value="确认导入" class="button" onclick="validate();" />
						<input type="button" name="Submit2" value="返回不导入" class="button" onclick="window.history.go(-1);" />
					</TD>
				</TR>
			</TABLE>
		</div>
	</form>
<div id="popDiv" class="mydiv" style="display:none;">请耐心等待,数据较多完成后会自动关闭当前页<br/></div>
<div id="bg" class="bg" style="display:none;"></div>
<script type="text/javascript">
function validate(){
	var flag = verifyRequired("fileName","文件名称")&&verifyLength("fileName",50,"文件名称");
	if(flag){
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
