package com.tywh.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tywh.util.ParameterRequestWrapper;
import com.tywh.util.PropertyUtil;
import com.tywh.util.Result;

/**
 *	AuthFilter
 *  author：杜泉
 *  下午2:40:44
 */

public class AuthFilter implements Filter {

	private static Logger log = Logger.getLogger(AuthFilter.class);
	private String encoding = null;

	public void destroy(){

	}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws ServletException,
			IOException{
		System.out.println("进入拦截器");
		//用户表信息
////		ManagUser u = null;
////		try {
////			boolean flag = false;
////			long start=System.currentTimeMillis();
////			
////			if (request.getCharacterEncoding() == null) {
////				String encoding = getEncoding();
////				if (encoding != null)
////					request.setCharacterEncoding(encoding);
////			}
////			
////			ServletRequest wrapRequest = trim(request);
////			
////			
////			String target = "";
////			HttpServletRequest r = (HttpServletRequest) request;
////			//System.out.println(r.getRequestURL()+r.getQueryString());
////			String method = r.getParameter("method");
////			
////			if ("login".equals(method) || "menu".equals(method) || "logout".equals(method)) {
////				flag = true;
////			} else {
////				
////				HttpSession session = r.getSession(true);
////				//从Session中获取 对象数据 信息 
////				u = (ManagUser) session.getAttribute("curManagUser");
////				
////				if (u == null) {
////					Result result = new Result(false, "没有session或session过期!");
////					log.error(result.getMsg());
////					request.setAttribute("result", result);
////					request.getRequestDispatcher("/html/jsp/managUser/reLogin.jsp").forward(request, response);
////					response.flushBuffer();
////					return;
////				}
////				//ManagRoleServiceImpl.getBusinessObjectByMngUid();
////				
////				
////				String query=r.getQueryString();
////				if(query==null||(!query.startsWith("method=changeMypwd")&&!query.startsWith("method=modifyMyPassword"))){
////					boolean pass=false;
////					String str=r.getRequestURI();
////					List<String> buzObjs=(List<String>)session.getAttribute("buzObjs");
////					if(buzObjs==null){
////						//用户的对象与角色名称
////						buzObjs=ManagRoleServiceImpl.getBusinessObjectByMngUid(u.getId());
////						session.setAttribute("buzObjs", buzObjs);
////					}
////					for(String s:buzObjs){
////						if(str.endsWith("/"+s+".do")){
////							pass=true;
////							break;
////						}
////					}
////					
////					if(!pass){
////						request.getRequestDispatcher("/html/jsp/managUser/noRight.jsp").forward(request, response);
////						return;
////					}
//				}
//				//用户的角色获取导航菜单的表
//				ManagNavigation n = (ManagNavigation) session.getAttribute("navigation");
//				if (n == null) {
//					Result result = new Result(false, "没有菜单!");
//					log.error("managUserName=" + u.getCnName() + "   " + result.getMsg());
//					request.setAttribute("result", result);
//					//如果没有找到这个人在session的菜单 就重新登录 回到首页
//					request.getRequestDispatcher("/html/jsp/managUser/reLogin.jsp").forward(request, response);
//					return;
//				}

				
				//是否允许
//				Set idPermission = (Set) session.getAttribute("idPermission");
//				if (idPermission == null || idPermission.size() == 0) {
//					Result result = new Result(false, "没有授权列表!");
//					log.error("managUserName=" + u.getCnName() + "   " + result.getMsg());
//					request.setAttribute("result", result);
//					request.getRequestDispatcher("/html/jsp/managUser/reLogin.jsp").forward(request, response);
//					return;
//				}
//
//				String itemIdStr = r.getParameter("itemId");
//				if (itemIdStr != null && !"".equals(itemIdStr)) {
//					session.setAttribute("itemId", itemIdStr);
//				} else {
//					itemIdStr = (String) session.getAttribute("itemId");
//				}
//				long itemId = Long.parseLong(itemIdStr);
//				Iterator it = idPermission.iterator();
//				while (it.hasNext()) {
//					long id = (Long) it.next();
//
//					if (itemId == id) {
//						flag = true;
//						break;
//					}
//				}
//			}
			
			
					//res.sendRedirect("/birt/frameset?__report="+query+".rptdesign");
			
//				if (flag) {
//					chain.doFilter(wrapRequest, response);
//					log.info("time:"+(System.currentTimeMillis()-start));
//				} else {
//					Result result = new Result(false, "没有权限!");
//					log.error("managUserName=" + u.getCnName() + " target=" + target + " " + result.getMsg());
//					request.setAttribute("result", result);
//					request.getRequestDispatcher("/html/jsp/managUser/reLogin.jsp").forward(request, response);
//					return;
//				}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Result result = new Result(false, "异常!exception:" + e.getMessage());
//			log.error("managUserName=" + u.getCnName() + " " + result.getMsg());
//			request.setAttribute("result", result);
//			request.getRequestDispatcher("/html/jsp/managUser/reLogin.jsp").forward(request, response);
//			return;
//		}
	}

	private ParameterRequestWrapper trim(ServletRequest request){
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		Map map = new HashMap(request.getParameterMap());
		Set keys = map.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String[] value = (String[]) map.get(key);
			if("content".equals(key) || "cont".equals(key)){
				String p = filteScript(value[0]);
				map.put(key, p);
				continue;
			}
			if(pass(key, req)){
				String[] _v=new String[value.length];
				for(int i=0;i<_v.length;++i){
					if(value[i]!=null){
						_v[i]=value[i].trim();
					}
				}
				map.put(key, _v);
			}else{
				String p = doIt(value[0],key,req);
				map.put(key, p);
			}
			
		}

		ParameterRequestWrapper wrapRequest=new ParameterRequestWrapper(req,map);		
        return wrapRequest;
	}

	private String doIt(String p, String key, HttpServletRequest req){
		
		if (p != null) {
			p = p.trim();
			
			int position = p.indexOf("%");
			if (position != -1) {
				p = p.replaceAll("%", "百分之");
			}
			position = p.indexOf("％");
			if (position != -1) {
				p = p.replaceAll("％", "百分之");
			}
			position = p.indexOf("?");
			if (position != -1) {
				p = p.replaceAll("\\?", "？");
			}
			position = p.indexOf("\"");
			if (position != -1) {
				p = p.replaceAll("\"", "");
			}
			position = p.indexOf("'");
			if (position != -1) {
				p = p.replaceAll("'", "");
			}
			
			position = p.indexOf("=");
			if (position != -1) {
				p = p.replaceAll("=", "");
			}
			position = p.indexOf("like");
			if (position != -1) {
				p = p.replaceAll("like", "");
			}
		}
		return p;
	}
	
	//允许通过的几个访问方法不限制参数。。。。
	private boolean pass(String key, HttpServletRequest req) {
		String uri=req.getRequestURI();
		String query=req.getQueryString();
		if(uri!=null&&uri.endsWith("/managUser.do")&&query!=null&&query.endsWith("method=modify")&&"pwd".equals(key)){
			return true;
		}
		if(uri!=null&&uri.endsWith("/managMenu.do")&&query!=null&&(query.endsWith("method=saveNew")||query.endsWith("method=save"))&&"url".equals(key)){
			return true;
		}
		if(uri!=null&&uri.endsWith("/adminManagWeiXin.do")&&query!=null&&(query.endsWith("method=showSave")||query.endsWith("method=saveManagWeiXin"))&&"weiXinName".equals(key)){
			return true;
		}
		if(uri!=null&&uri.endsWith("/agent.do")&&query!=null&&query.endsWith("method=save")&&"office".equals(key)){
			return true;
		}
		if(uri!=null&&uri.endsWith("/zUserReg.do")&&query!=null&&query.startsWith("method=toQueryDetail")&&"nickName".equals(key)){
			return true;
		}
		return false;
	}

	private String filteScript(String p){
		if (p != null) {
			p = p.trim();
			//p = p.toLowerCase();
			String reg = "<script[\\s\\S]*</script>";
			p = p.replaceAll(reg, "");
			String reg2 ="&lt;script[\\s\\S]*&lt;/script&gt;";
			p = p.replaceAll(reg2, "");
			String reg3 = "<SCRIPT[\\s\\S]*</SCRIPT>";
			p = p.replaceAll(reg3, "");
			String reg4 ="&lt;SCRIPT[\\s\\S]*&lt;/SCRIPT&gt;";
			p = p.replaceAll(reg4, "");
		}
		return p;
	}

	public void init(FilterConfig filterConfig) throws ServletException{
		this.encoding = filterConfig.getInitParameter("encoding");

		
		
		InputStream in2 = filterConfig.getServletContext().getResourceAsStream(
				"/WEB-INF/classes/mskymanager.properties");
		
		
		Properties p = new Properties();
		try {
			p.load(in2);
			//这里不知道是设置什么的
			PropertyUtil.setCms(p);
			in2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getEncoding(){
		return (this.encoding);
	}

}