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
</head>

<body>
<form action="<%=basePath%>permission/setPermission.action" method="post" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
   		<td height="30">
			<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tbody>
					<tr>
						<th class="tablestyle_title">
							<span class="STYLE4">你当前的位置：系统管理->权限管理->权限设置->添加权限菜单</span>
						</th>
					</tr>
						<tr>
							<td>
								<table cellspacing="0" cellpadding="0" border="0" style="width:100%;height:60px;background:'../html/images/bg.gif';">
									<tbody>
										<tr>
											<td width="100%" >
												<fieldset style="height:100%;">
															<input type="hidden" name="daptId" id="daptId" value="<c:if test="${not empty daptId}">${daptId}</c:if>" />
															<input type="hidden" name="roleId" id="roleId"  value="<c:if test="${not empty roleId}">${roleId}</c:if>" />
													<table cellspacing="5" cellpadding="5"  style="width:100%">
														<tbody>
															<tr>
																<td align="left"  class="STYLE1" >
																	查询条件:
																</td>
																<td align="center" class="STYLE1" nowrap="nowrap">
																	<div>
																		<span style="font-size:12;">菜&nbsp;单&nbsp;名&nbsp;称：</span><input type="text" id="menuName"  name="menuName" value="${menuName}" size="60">
																	</div>
																</td>
																<td  align="center"  class="STYLE1"  >
																	<input type="button" align="center"  value="查询"   style="background: url(../html/images/tab_05.gif); width:85px;height:30px;no-repeat; border:none;" onclick="validate();">
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
			                     		 <input type="checkbox" name="checkbox62" value="checkbox"   onclick="if(this.checked==true) { checkAll('menulist'); } else { clearAll('menulist'); }"/>全选 &nbsp;&nbsp;&nbsp;
			                     		 <a href="#" onclick="submitCheckedSave();"style="text-decoration:none;"><img src="../html/images/22.gif" width="14" height="14"  style="border: none;"/>&nbsp;&nbsp;&nbsp;批量添加所选菜单</a>
			                    </td>
			                  </tr>
			                </table>
		                </td>
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
            <td width="3%" height="22" background="../html/images/bg.gif" bgcolor="#FFFFFF"></td>
            <td width="7%" height="22"  background="../html/images/bg.gif"   bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">菜单ID</span></div></td>
            <td width="10%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">菜单名称</span></div></td>
            <td width="15%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">菜单描述</span></div></td>
            <td width="10%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">上级菜单</span></div></td>
            <td width="20%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">URL</span></div></td>
            <td width="30%" height="22" background="../html/images/bg.gif"  bgcolor="#FFFFFF"  class="STYLE1"><div align="center">基本操作</div></td>
          </tr>
          <c:if test="${not empty tmlist}">
          	   <c:forEach   var="menu"   items="${tmlist}" >
		          <tr>
		            <td height="20" bgcolor="#FFFFFF">
			            <div align="center">
			              <input type="checkbox" name="menulist" value="${menu.t_id}" />
			            </div>
		            </td>
		            <td height="20" bgcolor="#FFFFFF"><div align="center" class="STYLE1"><div align="center">${menu.t_id}</div></div></td>
		            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${menu.t_name}</span></div></td>
		            <td height="20" bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${menu.t_content} </span></div></td>
		            <td bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${menu.t_parentName}</span></div></td>
		            <td bgcolor="#FFFFFF"><div align="center"><span class="STYLE1">${menu.t_url}</span></div></td>
		            <td height="20" bgcolor="#FFFFFF">
		           		 <div align="center">
			           		 <span class="STYLE4">
			            		<a href="#" onclick="submitSavePermission(${menu.t_id});" style="text-decoration:none;"><img src="../html/images/22.gif" width="16" height="16"  style="border: none;"/>分配菜单</a>
			           		 </span>
		           		 </div>
		            </td>
		          </tr>
	          </c:forEach>
          </c:if>
        </table></td>
        <td width="8" background="../html/images/tab_15.gif">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
</table>
</form>
<script type="text/javascript">
	var daptId = document.getElementById("daptId").value;
	var roleId = document.getElementById("roleId").value;
	function checkAll(name)
	{
	    var el = document.getElementsByTagName('input');
	    var len = el.length;
	    for(var i=0; i<len; i++)
	    {
	        if((el[i].type=="checkbox") && (el[i].name==name))
	        {
	            el[i].checked = true;
	        }
	    }
	}
	function clearAll(name)
	{
	    var el = document.getElementsByTagName('input');
	    var len = el.length;
	    for(var i=0; i<len; i++)
	    {
	        if((el[i].type=="checkbox") && (el[i].name==name))
	        {
	            el[i].checked = false;
	        }
	    }
	}
	
	function validate(){
		var flag = true ;
		if(flag){document.forms[0].submit();}
	}
	
	function submitSavePermission(menuId){

		var url = "<%=basePath%>permission/savePermission.action?daptId="+daptId+"&roleId="+roleId+"&menuId="+menuId;
		location=url;
	}
	
	function submitCheckedSave(){	
			var obj=document.getElementsByName("menulist");
			var obs="";
			for(var i=0;i<obj.length;i++){
		         if(obj[i].checked)
		         {
		           obs=obs+obj[i].value+",";
		         }  
		    } 
			if(obs.length>0)
			{
			    var url = "<%=basePath%>permission/savePermissions.action?perIds="+obs+"&daptId="+daptId+"&roleId="+roleId;
				location=url;
			}
			else
			{
				alert("请先选择要添加的数据");	
			}
	}
</script>
</body>
</html>
