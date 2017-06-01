<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
   每页显示：
 <a href="javascript:void(0);" onclick="abcd('40');"><span  class="STYLE4">20</span></a>
&nbsp;&nbsp;
<a href="javascript:void(0);" onclick="abcd('40');"><span  class="STYLE4">40</span></a>
&nbsp;&nbsp;
<a href="javascript:void(0);" onclick="abcd('60');"><span class="STYLE4">60</span></a>
&nbsp;&nbsp;
<a href="javascript:void(0);" onclick="abcd('100');"> <span class="STYLE4">100</span></a>
<script>	
function abcd(num){
	document.getElementById('pageSize1').value=num;
	document.forms[0].submit();
}
</script>