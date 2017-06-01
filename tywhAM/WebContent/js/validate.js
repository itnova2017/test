function preFunction(id){
	var v = document.getElementById(id).value;
	var r = $.trim(v);
	document.getElementById(id).value=r;
}
function compDate(ida,idb,labela,labelb){
	preFunction(ida);
	preFunction(idb);
	var a = document.getElementById(ida).value;
	var b = document.getElementById(idb).value;
	if(a=="" || b==""){
		return true;
	}
	var arr=a.split("-");
	var starttime=new Date(arr[0],arr[1],arr[2]);
	var starttimes=starttime.getTime();
	var arrs=b.split("-");
	var lktime=new Date(arrs[0],arrs[1],arrs[2]);
	var lktimes=lktime.getTime();
	if(starttimes<=lktimes){
		return true;
	}else{
		fail(labela+" 不能大于 "+labelb);
		document.getElementById(ida).focus();
		return false;
	}
}

function compTime2(ida,idb,labela,labelb){
	preFunction(ida);
	preFunction(idb);
	var a = document.getElementById(ida).value;
	var b = document.getElementById(idb).value;
	if(a=="" || b==""){
		return true;
	}
	var arr1=a.split(":");
	var arr2=b.split(":");
	if(arr1[0]<=arr2[0]){
		return true;
	}else{
		fail(labela+" 不能大于 "+labelb);
		document.getElementById(ida).focus();
		return false;
	}
}

function compTime(ida,idb,labela,labelb){
	preFunction(ida);
	preFunction(idb);
	var beginTime = document.getElementById(ida).value;
	var endTime = document.getElementById(idb).value;
	if(beginTime=="" || endTime==""){
		return true;
	}
	var beginTimes=beginTime.substring(0,10).split('-');
	var endTimes=endTime.substring(0,10).split('-');
	beginTime=beginTimes[1]+'-'+beginTimes[2]+'-'+beginTimes[0]+' '+beginTime.substring(10,19);
	endTime=endTimes[1]+'-'+endTimes[2]+'-'+endTimes[0]+' '+endTime.substring(10,19);
	var a =(Date.parse(endTime)-Date.parse(beginTime))/3600/1000;
	if(a<=0){
		fail(labela+" 必须小于 "+labelb);
		document.getElementById(ida).focus();
		return false;
	}else{
		return true;
	}
}


function fail(msg) {
	$("#trueDiv").hide();
	$("#falseDiv").hide();
	var str='<div id="result_msg_div_alert" class="portlet-msg-alert"><font color="red" class="result_msg_div_msg">'+msg+'</font></div>';
	document.getElementById("result_msg_div").innerHTML = str;
}

function verifyLength(id,long,label){
	preFunction(id);
	var v = document.getElementById(id).value;
	v = $.trim(v);
	if(v.length<=long){
		return true;
	}else{
		fail(label+" 的长度必须不能超过 "+long);
		document.getElementById(id).focus();
		return false;
	}
}
function verifyPassLength(id,label){
	preFunction(id);
	var v = document.getElementById(id).value;
	v = $.trim(v);
	var reg = /^[A-Za-z0-9]{6,20}$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(label+" 的长度必须在6-20之间,必须是英文字母或者数字!");
		document.getElementById(id).focus();
		return false;
	}
}

function verifyLengthEQ(id,long,label){
	preFunction(id);
	var v = document.getElementById(id).value;
	v = $.trim(v);
	if(v.length==long){
		return true;
	}else{
		fail(label+" 的长度必须等于 "+long);
		document.getElementById(id).focus();
		return false;
	}
}

function regex(id, lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	v = $.trim(v);
	if (v == null || v == "") {
		fail(lable + " 不能为空!");
		document.getElementById(id).focus();
		return false;
	} else {
		if(verifyMatch(id, lable, "number") &&  v>0 && v<=31) {
			return true;
		}
		else
		{
			fail(lable + "不是数字或数字不再0与31之间");
			document.getElementById(id).focus();
			return false;
		}
	}
}

function verifyRequired(id, lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	v = $.trim(v);
	if (v == null || v == "") {
		fail(lable + " 不能为空!");
		document.getElementById(id).focus();
		return false;
	} else {
		return true;
	}
}
function verifyMatch(id, lable, type) {
	if (verifyRequired(id, lable)) {
		if (type == "number") {
			return matchNumber(id, lable);
		} else if(type == "channel"){
			return matchChannel(id,lable);	
		}else if (type == "en") {
			//return matchEn(id, lable);
			return matchEnName(id,lable);
		}else if (type == "enupper") {
			return matchEn(id, lable);
		} else if (type == "tel") {
			return metchTel(id, lable);
		} else if (type == "certid") {
			return metchCertid(id, lable);
		} else if(type == "cn"){
			//return matchCn(id,lable);
			return matchCnName(id,lable);
		}else if(type=="ip"){
			return matchIp(id,lable);
		}else if(type=="mail"){
			return matchMail(id,lable);
		}else if(type=="userNameEn"){
			return matchEnName(id,lable);
		}else if(type=="userNameCn"){
			return matchCnName(id,lable);
		}else if(type=="oem"){
			return matchOem(id,lable);
		}else if(type=="pubName"){
			return matchPubName(id,lable);
		}else if(type="NumPo"){
			return matchNumPo(id,lable);
		}else if(type="mob"){
			return metchMob(id,lable);
		}else{
			fail("不知道author想校验啥!");
			return false;
		}
	} else {
		return false;
	}
}


function matchPubName(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^[A-Z]{3}_[a-z]{1}[0-9]{2}_[0-9]{2}[.]{1}[0-9]{2}[.]{1}[0-9]{3}$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 的格式必须为:三个大写字母+一个下划线+一个小写字母+两个数字+一个下划线+两个数字+一个点+两个数字+一个点+三个数字");
		document.getElementById(id).focus();
		return false;
	}
}

function matchNumPo(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^\+?\d+(\.\d+)?$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 不是正实数!");
		document.getElementById(id).focus();
		return false;
	}
}
function matchNumDouble(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^(\+|\-)?\d+(\.\d+)?$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 不是实数!");
		document.getElementById(id).focus();
		return false;
	}
}
function matchInt(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^(\+|\-)?\d+$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 不是整数!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchEnName(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^[a-zA-Z]{1}[0-9a-zA-Z_]{1,}$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 必须是字母和数字,并以字母开头!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchEnNameWithSpace(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^[a-zA-Z ]{1}[0-9a-zA-Z_ ]{1,}$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 必须是字母和数字,并以字母开头!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchCnName(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]){1,}[0-9a-zA-Z_]*$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 必须是中文+字母或数字,并以中文开头!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchNumber(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^[0-9]*[1-9][0-9]*$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 不是数字!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchNumber2(id,lable) {
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^[0-9]*[0-9][0-9]*[.0-9]*$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 不是数字或小数!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchNumberOrZero(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /(^[0-9]*[1-9][0-9]*$)|(^[0]{1}$)/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 不是正整数!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchChannel(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^[0-9]{4}$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 不是4位正整数!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchEn(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	//var reg = /^[A-Za-z]+$/;
	var reg = /^[A-Z]{3}$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 必须是3位大写英文字母!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchOem(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	//var reg = /^[A-Za-z]+$/;
	var reg = /^[A-Z]{3}$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 必须是3位大写英文字母!");
		document.getElementById(id).focus();
		return false;
	}
}

function metchTel(id,lable) {

	preFunction(id);
	
	var v = document.getElementById(id).value;
	
	if (v == null || v == ""){return true;}
	
	var reg = /(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{3,4}\-[0-9]{5}$)|(^[0-9]{7,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)|(^9[0-9]{4}$)|(^400\-[0-9]{4}\-[0-9]{3}$)|(^400-[0-9]{3}\-[0-9]{4}$)|(^400[0-9]{1}\-[0-9]{3}\-[0-9]{3}$)|(^400[0-9]{7}$)|(^1[3,4,8,5]{1}[0-9]{9}$)/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 请输入正确电话号码!");
		document.getElementById(id).focus();
		return false;
	}
}

function metchMob(id,lable) {

	preFunction(id);
	
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	
	var reg = /^1[3,4,8,5]{1}[0-9]{9}$/;
	
	if(reg.test(v)){
	
		return true;
	}else{
	
		fail(lable+" 请输入正确的手机号码!");
		
		document.getElementById(id).focus();
		return false;
	}
}

function metchCertid(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg1 = /^\d{17}[X|x|0-9]$/;
	var reg2 = /^\d{15}$/;
	if(reg2.test(v) || reg1.test(v)){
		return true;
	}else{
		fail(lable+" 请输入正确的身份证号码!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchCn(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	//var reg =/^([\u4E00-\uFA29]|[\uE7C7-\uE7F3])*$/;
	var reg = /^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]){1,}[0-9a-zA-Z_]*$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 必须是中文+字母或数字,并以中文开头!");
		document.getElementById(id).focus();
		return false;
	}
}


function matchIp(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg =/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 请输入正确的IP地址!");
		document.getElementById(id).focus();
		return false;
	}
}

function matchMail(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 请输入正确的邮箱地址!");
		document.getElementById(id).focus();
		return false;
	}
}


function matchTime(id,lable) {
	preFunction(id);
	var v = document.getElementById(id).value;
	if (v == null || v == ""){return true;}
	var reg = /^\[0-2]{1}\[0-6]{1}:\[0-5]{1}\[0-9]{1}:\[0-5]{1}\[0-9]{1}$/;
	if(reg.test(v)){
		return true;
	}else{
		fail(lable+" 请输入正确的时间! 格式必须同12:12:12");
		document.getElementById(id).focus();
		return false;
	}
}
