<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="taglib.jsp"%>
<table class="right-font08" cellspacing="0" cellpadding="0" border="0" align="center" width="100%">
	<tbody>
		<tr>
		<td width="12" height="35" ><img src="../html/images/tab_18.gif" width="12" height="35" /></td>
		<td width="50%" class="STYLE5" >
			<div id="buttonBar">
				共<span class="STYLE4">${page.totalRecord}</span>条&nbsp;|&nbsp;
				每页显示<span class="STYLE4">${page.pageSize}</span>条&nbsp;|&nbsp;
				第<span class="STYLE4">${page.curPage}</span>页&nbsp;|&nbsp;
				<jsp:include flush="true" page="pageSizeChange.jsp"></jsp:include>
			</div>
		</td>
		<td align="right"  class="STYLE5" width="49%" >
				<span id="lastSpan" style="float: right"><a href="javascript:lastPage()" ><img src="../html/images/last.gif" width="37" height="15"  style="border: none;"/></a>&nbsp;|&nbsp;</span>
				<span id="nextSpan" style="float: right"><a href="javascript:nextPage()" ><img src="../html/images/next.gif" width="43" height="15"  style="border: none;"/></a>&nbsp;|&nbsp;</span>
				<span id="preSpan" style="float: right"><a href="javascript:previousPage()" ><img src="../html/images/back.gif" width="43" height="15"  style="border: none;"/></a>&nbsp;|&nbsp;</span>
				<span id="firstSpan" style="float: right"><a href="javascript:firstPage()" ><img src="../html/images/first.gif" width="37" height="15" style="border: none;"/></a>&nbsp;|&nbsp;</span>
				<span id="go" style="float: right" class="STYLE1"><input type="text" id="ppp" name="ppp" size="2" value="${page.curPage}" />
				<a href="javascript:goToPage()" ><img src="../html/images/go.gif" width="37" height="15"  style="border: none;"/></a>&nbsp;|&nbsp;</span>
		</td>
		<td width="16"><img src="../html/images/tab_20.gif" width="16" height="35" /></td>
		</tr>
	</tbody>
</table>
<script type="text/javascript">
var totalPage = ${page.totalPage};
var curPage = ${page.curPage};

isShow();

function isShow(){
	if(curPage==1 && curPage == totalPage){
		$("#firstSpan").hide();
		$("#preSpan").hide();
		$("#lastSpan").hide();
		$("#nextSpan").hide();
		$("#go").hide();
	}else if(curPage==1 && totalPage>curPage){
		$("#firstSpan").hide();
		$("#preSpan").hide();
	}else if(curPage == totalPage){
		$("#lastSpan").hide();
		$("#nextSpan").hide();
	}else{
		$("#lastSpan").show();
		$("#nextSpan").show();
		$("#firstSpan").show();
		$("#preSpan").show();
	}
}

function lastPage(){
	if(curPage<totalPage){
		document.getElementById("curPage").value=totalPage;
		document.forms[0].submit();
	}
}

function nextPage(){
	if(curPage<totalPage){
		document.getElementById("curPage").value=curPage+1;
		document.forms[0].submit();
	}
}

function goToPage(){
	var ppp = document.getElementById("ppp").value;
	var reg = /^[0-9]*[1-9][0-9]*$/;
	if(!reg.test(ppp)){
		alert("请输入非0数字!");
	}else{
		if(ppp>totalPage){
			ppp = totalPage;
		}
		if(ppp<1){
			ppp = 1;
		}
		document.getElementById("curPage").value=ppp;
		document.forms[0].submit();
	}
}

function previousPage(){
	if(curPage>1){
		document.getElementById("curPage").value=curPage-1;
		document.forms[0].submit();
	}
}

function firstPage(){
	if(curPage>1){
		document.getElementById("curPage").value=1;
		document.forms[0].submit();
	}
}

$("#ppp").keydown(function(e){
	if(e.keyCode==13){
  	 	goToPage();
	}
});
</script>