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
.STYLE1 {font-size: 12px;width:150px;}
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
.STYLE6 {font-size: 12px;}
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
<form action="<%=basePath%>attendance/queryAllListForMonth.action" method="post" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
   		<td height="30">
			<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tbody>
					<tr>
						<th class="tablestyle_title">
							<span class="STYLE4">你当前的位置：系统管理->考勤管理->考勤列表(按天展示)</span>
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
																<td align="left" class="STYLE6"  colspan="3" nowrap="nowrap">
																	<div>
																		<span style="font-size:12;">查&nbsp;看&nbsp;时&nbsp;间&nbsp;范&nbsp;围：</span><input type="text" id="startDate"  name="startDate" value="${startDate}"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"  onchange="validate();"  class="Wdate" size="16"/>
																		<span style="font-size:12;">&nbsp;至&nbsp;</span><input type="text" id="endDate"  name="endDate" value="${endDate}"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"   onchange="validate();" class="Wdate" size="16"/>
																		<span style="color: red">默认查询本月1号/本月最后一天</span>
																		&nbsp;&nbsp;&nbsp;&nbsp;
																		<span style="font-size:12;">人&nbsp;员&nbsp;名&nbsp;称：</span>
																		<select id="numId" name="numId" style="width: 200px"  onchange="validate();">
																			<option value="">请选择</option>
																			<c:if test="${not empty tapLists}">
																				<c:forEach var="tap" items="${tapLists}">
																					<option value="${tap.num}"  <c:if test="${tap.num == numId}">selected="selected"</c:if> >${tap.name}</option>
																				</c:forEach>
																			</c:if>
																		</select>
																	</div>
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
            <td width="200px" align="right" >
	            <table width="90%" border="0" cellpadding="0" cellspacing="0">
	                  <tr>
	                     <!-- <td class="STYLE1"><div style="float:right"><a href="#" onclick="submitSaveAttendance();"style="text-decoration:none;"><img src="../html/images/22.gif" width="14" height="14" style="border: none;"/>补录考勤</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="submitImportAttendance();"style="text-decoration:none;"><img src="../html/images/22.gif" width="14" height="14" style="border: none;"/>导入考勤</a></div></td> -->
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
    <td><table border="0" cellspacing="0" cellpadding="0" >
      <tr>
        <td width="8" background="../html/images/tab_12.gif">&nbsp;</td>
        <td>
        <table border="0" cellpadding="0" cellspacing="1" bgcolor="b5d6e6" onmouseover="changeto()"  onmouseout="changeback()" >
          <tr style="overflow: auto;">
          		  <td width="4%" height="22"  background="../html/images/bg.gif"   bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">人员编号</span></div></td>
        		  <td width="3%" height="22"  background="../html/images/bg.gif"   bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">姓名</span></div></td>
          	 <c:if test="${not empty listDate}">
          	   	<c:forEach   var="da"   items="${listDate}"  varStatus="">
            		<td height="22"  background="../html/images/bg.gif"   bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${da}</span></div></td>
	          </c:forEach>
            </c:if>
          </tr>
       	 <c:if test="${not empty listAttendance}">
    	   	<c:forEach   items="${listAttendance}" var="at" >
     	   			<tr>
	      	   				<c:forEach   items="${at}" var="ats" >
	          					<td height="20" bgcolor="#FFFFFF"><div align="center" class="STYLE1"><div align="center">${ats}</div></div></td>
	          				</c:forEach>
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
		var flag = compDate('startDate','endDate','开始日期','结束日期') ;
		if(flag)
		{document.forms[0].submit();}
	}
</script>
</body>
</html>
