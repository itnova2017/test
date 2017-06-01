<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tywh.orm.TMenu" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>

<html>
<head>

<title></title>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
%>
<script type="text/javascript">
	var rootPath = "<%=request.getContextPath()%>";
</script>
<script src="<%=basePath%>/html/resource/xtree/stree.js"></script>
<link type="text/css" rel="stylesheet" href="<%=basePath%>/html/resource/xtree/stree.css">

<style>
	body { background: white; color: black; }
	input { width: 120px; }
</style>

</head>
<body style="margin: 0px">

<div  style="position: absolute; width: 100%; top: 0px; left: 0px; height:95%; overflow: auto;">


<%
	TMenu navigation = (TMenu)session.getAttribute("navigation");
	if(navigation!=null){
%>
<script>
if (document.getElementById) {
	var tree = new WebFXTree('<%=navigation.getT_name()%>',"javascript:nodeClick('<%=basePath+navigation.getT_url()+"?itemId="+navigation.getT_id()+"&param="+navigation.getT_parameter()%>')");
	tree.setBehavior('classic');
	<%
		List children = navigation.getChildren();
		Iterator it = children.iterator();
		while(it.hasNext()){
			TMenu child = (TMenu)it.next();
	%>
		var child = new WebFXTreeItem('<%=child.getT_name()%>',"javascript:nodeClick('<%=basePath+child.getT_url()+"?itemId="+child.getT_id()+"&param="+child.getT_parameter()%>')");
		tree.add(child);
		<%
			List sunzis = child.getChildren();
			Iterator itt = sunzis.iterator();
			while(itt.hasNext()){
				TMenu sunzi = (TMenu)itt.next();
				%>
					var sunzi = new WebFXTreeItem('<%=sunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+sunzi.getT_url()+"?itemId="+sunzi.getT_id()+"&param="+sunzi.getT_parameter()%>')");
					child.add(sunzi);
				<%
				List chongsunzis = sunzi.getChildren();
				Iterator ittt = chongsunzis.iterator();
				while(ittt.hasNext()){
					TMenu chongsunzi = (TMenu)ittt.next();
					%>
						var chongsunzi = new WebFXTreeItem('<%=chongsunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+chongsunzi.getT_url()+"?itemId="+chongsunzi.getT_id()+"&param="+chongsunzi.getT_parameter()%>')");
						sunzi.add(chongsunzi);
					<%
					List foursunzis = chongsunzi.getChildren();
					Iterator itttt = foursunzis.iterator();
					while(itttt.hasNext()){
						TMenu foursunzi = (TMenu)itttt.next();
						%>
							var foursunzi = new WebFXTreeItem('<%=foursunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+foursunzi.getT_url()+"?itemId="+foursunzi.getT_id()+"&param="+foursunzi.getT_parameter()%>')");
							chongsunzi.add(foursunzi);
						<%
						List fivesunzis = foursunzi.getChildren();
						Iterator it5 = fivesunzis.iterator();
						while(it5.hasNext()){
							TMenu fivesunzi = (TMenu)it5.next();
							%>
								var fivesunzi = new WebFXTreeItem('<%=fivesunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+fivesunzi.getT_url()+"?itemId="+fivesunzi.getT_id()+"&param="+fivesunzi.getT_parameter()%>')");
								foursunzi.add(fivesunzi);
							<%
							List sixsunzis = fivesunzi.getChildren();
							Iterator it6 = sixsunzis.iterator();
							while(it6.hasNext()){
								TMenu sixsunzi = (TMenu)it6.next();
								%>
									var sixsunzi = new WebFXTreeItem('<%=sixsunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+sixsunzi.getT_url()+"?itemId="+sixsunzi.getT_id()+"&param="+sixsunzi.getT_parameter()%>')");
									fivesunzi.add(sixsunzi);
								<%
								List sevensunzis = sixsunzi.getChildren();
								Iterator it7 = sevensunzis.iterator();
								while(it7.hasNext()){
									TMenu sevensunzi = (TMenu)it7.next();
									%>
										var sevensunzi = new WebFXTreeItem('<%=sevensunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+sevensunzi.getT_url()+"?itemId="+sevensunzi.getT_id()+"&param="+sevensunzi.getT_parameter()%>')");
										sixsunzi.add(sevensunzi);
									<%
									List eightsunzis = sevensunzi.getChildren();
									Iterator it8 = eightsunzis.iterator();
									while(it8.hasNext()){
										TMenu eightsunzi = (TMenu)it8.next();
										%>
											var eightsunzi = new WebFXTreeItem('<%=eightsunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+eightsunzi.getT_url()+"?itemId="+eightsunzi.getT_id()+"&param="+eightsunzi.getT_parameter()%>')");
											sevensunzi.add(eightsunzi);
										<%
										List ninesunzis = eightsunzi.getChildren();
										Iterator it9 = ninesunzis.iterator();
										while(it9.hasNext()){
											TMenu ninesunzi = (TMenu)it9.next();
											%>
												var ninesunzi = new WebFXTreeItem('<%=ninesunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+ninesunzi.getT_url()+"?itemId="+ninesunzi.getT_id()+"&param="+ninesunzi.getT_parameter()%>')");
												eightsunzi.add(ninesunzi);
											<%
											List tensunzis = ninesunzi.getChildren();
											Iterator it10 = tensunzis.iterator();
											while(it10.hasNext()){
												TMenu tensunzi = (TMenu)it10.next();
												%>
													var tensunzi = new WebFXTreeItem('<%=tensunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+tensunzi.getT_url()+"?itemId="+tensunzi.getT_id()+"&param="+tensunzi.getT_parameter()%>')");
													ninesunzi.add(tensunzi);
												<%
												List elevensunzis = tensunzi.getChildren();
												Iterator it11 = elevensunzis.iterator();
												while(it11.hasNext()){
													TMenu elevensunzi = (TMenu)it11.next();
													%>
														var elevensunzi = new WebFXTreeItem('<%=elevensunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+elevensunzi.getT_url()+"?itemId="+elevensunzi.getT_id()+"&param="+elevensunzi.getT_parameter()%>')");
														tensunzi.add(elevensunzi);
													<%
													List twelvesunzis = elevensunzi.getChildren();
													Iterator it12 = twelvesunzis.iterator();
													while(it12.hasNext()){
														TMenu twelvesunzi = (TMenu)it12.next();
														%>
															var twelvesunzi = new WebFXTreeItem('<%=twelvesunzi.getT_name()%>',"javascript:nodeClick('<%=basePath+twelvesunzi.getT_url()+"?itemId="+twelvesunzi.getT_id()+"&param="+twelvesunzi.getT_parameter()%>')");
															elevensunzi.add(twelvesunzi);
														<%
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		%>
	<%
		}
	%>
	document.write(tree);
}

</script>
<%		
	}
%>

</div>


</body>


<script>
function isChild(url){
    if(url.indexOf(".action") != -1){ 
          return true;
    }else{
          return false;
     }
}

function nodeClick(url){
	if(url!="" && isChild(url)){
		var frame= window.parent.document.getElementById("middle");
  		frame.src=url;
	}
}

function addNode() {
	if (tree.getSelected()) {
		tree.getSelected().add(new WebFXTreeItem('New'));
	}
}

function addNodes() {
	if (tree.getSelected()) {
		var foo = tree.getSelected().add(new WebFXTreeItem('New'));
		var bar = foo.add(new WebFXTreeItem('Sub 1'));
		var fbr = foo.add(new WebFXTreeItem('Sub 2'));
	}
}

function delNode() {
	if (tree.getSelected()) {
		tree.getSelected().remove();
	}
}


</script>
</html>
