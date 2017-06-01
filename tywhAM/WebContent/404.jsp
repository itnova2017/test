<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"   isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>404</title>
<style type="text/css">
body{margin:0;padding:0;font:14px/1.6 Arial,Sans-serif;background:#fff url(img/body.png) repeat-x;}
a:link,a:visited{color:#007ab7;text-decoration:none;}
h1{
	position:relative;
	z-index:2;
	width:540px;
	height:0;
	margin:110px auto 15px;
	padding:230px 0 0;
	overflow:hidden;
	xxxxborder:1px solid;
	background-image: url(html/images/Main.jpg);
	background-repeat: no-repeat;
}
h2{
	position:absolute;
	top:55px;
	left:233px;
	margin:0;
	font-size:0;
	text-indent:-999px;
	-moz-user-select:none;
	-webkit-user-select:none;
	user-select:none;
	cursor:default;
	width: 404px;
	height: 90px;
}
h2 em{display:block;font:italic bold 200px/120px "Times New Roman",Times,Serif;text-indent:0;letter-spacing:-5px;color:rgba(216,226,244,0.3);}
.link a{margin-right:1em;}
.link,.texts{width:540px;margin:0 auto 15px;color:#505050;}
.texts{line-height:2;}
.texts dd{margin:0;padding:0 0 0 15px;}
.texts ul{margin:0;padding:0;}
.portal{color:#505050;text-align:center;white-space:nowrap;word-spacing:0.45em;}
.portal a:link,.portal a:visited{color:#505050;word-spacing:0;}
.portal a:hover,.portal a:active{color:#007ab7;}
.portal span{display:inline-block;height:38px;line-height:35px;background:url(img/portal.png) repeat-x;}
.portal span span{padding:0 0 0 20px;background:url(img/portal.png) no-repeat 0 -40px;}
.portal span span span{padding:0 20px 0 0;background-position:100% -80px;}
.STYLE1 {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 65px;
}
</style>
<%@include file="jsp/taglib.jsp"%>
<script type="text/javascript">
    		setTimeout(reDo, 2000);
    		function reDo(){
    			window.top.location.href = "http://<%=ipconfig%>:8080/tywhAM/";
    		}
</script>
</head>
<body>
    <h1></h1>
    
    <dl class="texts">
        <dt>我们正在联系火星总部查找404您所需要的页面.请返回等待信息..</dt>
<dd>
            <ul>
           		<li>2秒后自动转到登录页面....</li>
                <li>不要返回吗?</li>
                <li>确定不要返回吗?</li>
                <li>真的真的确定不要返回吗?</li>
                <li>好吧.还是随便你要不要真的确定返回吧</li>
               	 <li>信息： <%=request.getAttribute("javax.servlet.error.message")%></li>
				 <li>异常： <%=request.getAttribute("javax.servlet.error.exception_type")%></li>
            </ul>
        </dd>
    </dl>
    <p class="link">
        <a href="javascript:history.go(-1);">&#9666;返回上一页</a>
    </p>
        
</body>
</html>