<div id="trueDiv" class="portlet-msg-success" style="display:none">
	<font color="green"><c:out value="${result.msg}"/></font>
</div>
<div id="falseDiv"  class="portlet-msg-alert" style="display:none">
	<font color="red"><c:out value="${result.msg}"/><c:out value="${result.stackTrace}"/></font>
</div>
<div id="result_msg_div"></div>
<%
	com.tywh.util.Result r =(com.tywh.util.Result)request.getAttribute("result");
	if(r==null){
		%>
		<script type="text/javascript">
			$("#trueDiv").hide();
			$("#falseDiv").hide();
		</script>
		<%
	}else{
	%>
	
	<script type="text/javascript">
	if(${result.flag}){
		$("#trueDiv").show();
	}else if(${result.flag}==false){
		$("#falseDiv").show();
	}else{
		$("#trueDiv").hide();
		$("#falseDiv").hide();
	}
</script>
	
<%
	}
 %>


