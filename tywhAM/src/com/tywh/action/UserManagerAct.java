package com.tywh.action;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tywh.orm.TDepartRoleUser;
import com.tywh.orm.TDepartmentUser;
import com.tywh.orm.TUserinfo;
import com.tywh.service.TDepartmentService;
import com.tywh.service.TLogService;
import com.tywh.service.TRoleService;
import com.tywh.service.TUserManagerService;
import com.tywh.util.DateFun;
import com.tywh.util.Result;
import com.tywh.util.UtilConfig;

public class UserManagerAct extends ActionSupport {

	private static final long serialVersionUID = -281275682819237996L;

	private static Log log = LogFactory.getLog(UserManagerAct.class);

	private TUserinfo tuser = new TUserinfo();

	private TUserManagerService<TUserinfo>  tuserService;
	private TRoleService  troleService;
	private TDepartmentService departmentService;
	private  String ipconfig;
	private List<TUserinfo> users;

	private String searchText;
	
	private TLogService  logService;
	HttpServletRequest  request = ServletActionContext.getRequest();
	HttpServletResponse  response= ServletActionContext.getResponse();
	
	public String doIndex() {
		return SUCCESS;
	}
	
	public String doLogin() {
		log.info("进入登录doLogin方法");
		//获取登录名与登录密码啊
		String loginName=this.tuser.getT_loginName();
		String password=this.tuser.getT_password();
		
		//前台页面已经判断是否为空？
		if (loginName == null || password == null)
			return ERROR;
		try {
			//查询是否有用户
			TUserinfo user = tuserService.doLogin(loginName,password);
			if (user != null) {
				//获取request中的值  创建session 把登录的对象放到session中
				HttpServletRequest request= ServletActionContext.getRequest();
				HttpSession session = request.getSession();
				session.invalidate();
				session = request.getSession(true);
				//先查询该用户属于那些部门
				TDepartmentUser tdu=new TDepartmentUser();
				tdu.setT_userId(user.getT_id());
				List<TDepartmentUser>   duList=departmentService.queryDaptUserById(tdu);
				//获取部门集合
				StringBuilder sql = new StringBuilder();
				if(duList.size()==1){
					int dapt =duList.get(0).getT_departId();
					sql.append(" and dru.t_daptId=" +dapt);
				}else if(duList.size()>1){
					sql.append(" and dru.t_daptId in(");
					for(TDepartmentUser du:duList){
						int dapt = du.getT_departId();
						sql.append(dapt+",");
					}
					sql.delete(sql.length()-1, sql.length());
					sql.append(")");
				}
				//根据查询出来的用户查询出当前用户的角色并保存到该用户的set集合中
				 List<TDepartRoleUser>  userRoles=troleService.queryRoleByUserId(user.getT_id(),sql);
				Set<TDepartRoleUser> roles=new HashSet<TDepartRoleUser>();
				if(userRoles == null || userRoles.size()==0)
				{
					request.setAttribute("result","该用户的角色未分配");
					return "errorNullRole";
				}
				roles.addAll(userRoles);
				user.setRoles(roles);
				//把获得角色对象放到用户对象中保存起来
				session.setAttribute("curManagUser", user);
				log.info("登入 成功!用户名=" + user.getT_userName()+","+DateFun.getDateLongString(new Date()));
				SaveLog("登入成功!用户名=" + user.getT_userName());
				return SUCCESS;
			} 
			else
				log.info("登入 失败!"+DateFun.getDateLongString(new Date()));
				return ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("异常![" + e.getMessage() + "]", e);
		
			return ERROR;
		}
	}
	public String doLogout() {
		try {
			log.info("进入登出doLogout方法");
			HttpServletRequest request= ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			if (session == null) {
				log.info("登出 成功! session is null");
				return SUCCESS;
			} else {
				TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
				session.removeAttribute("curManagUser");
				session.removeAttribute("navigation");
				session.invalidate();
				if(curUser==null){
					log.info("登出 成功!");
				}else{
					log.info("登出 成功!用户名=" + curUser.getT_userName()+DateFun.getDateLongString(new Date()));
				}
				ipconfig=UtilConfig.getValue("ipconfig");
				request.setAttribute("ipconfig",ipconfig);
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Result result = new Result(false, "登出 失败!["+e.getMessage()+"]",e);
			HttpServletRequest request= ServletActionContext.getRequest();
			request.setAttribute("result", result);
			log.info("登出 失败!["+e.getMessage()+"]"+DateFun.getDateLongString(new Date()),e);
			return ERROR;
		}
	}

	public void SaveLog(String content)
	{
		try
		{
			HttpSession session = request.getSession();//获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			logService.saveLog(content, curUser.getT_id()+"-"+curUser.getT_userName());
		}
		catch (ParseException e)
		{
			log.error("AuditFolderAction 中的SaveLog异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
	}
	
//	public String doQuery() {
//		try
//		{
//			searchText = getParam("queryText");
//			users = tuserService.queryUsers(searchText, TUserinfo.class);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			log.error("异常![" + e.getMessage() + "]", e);
//		}
//		return null;
//	}
//
//	public String doAdd() {
//		String result = "";
//		try {
//			String param = getParam("param");
//			if (Integer.parseInt(param) > 0) {
//				//tuser.setT_id(0);
//				tuserService.addUser(tuser);
//				result = doQuery();
//			} else
//				result = "addUser";
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("异常![" + e.getMessage() + "]", e);
//		}
//		return result;
//	}
//
//	public String doEdit() {
//		try {
//			Integer param = Integer.parseInt(getParam("param"));
//			if (param == 0) {
//				Integer id = Integer.parseInt(getParam("id"));
//				tuser = tuserService.getUser(TUserinfo.class, id);
//				return "editUser";
//			} else if (param == 1) {
//				tuserService.modifyUser(tuser);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("异常![" + e.getMessage() + "]", e);
//		}
//		return doQuery();
//	}
//
//	public String doDelete() {
//		try {
//			Integer param = Integer.parseInt(getParam("id"));
//			tuserService.deleteUser(param, TUserinfo.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("异常![" + e.getMessage() + "]", e);
//		}
//		return doQuery();
//	}

	protected String getParam(String key) {
		return ServletActionContext.getRequest().getParameter(key);
	}

	public String getSearchText() {
		return searchText;
	}

	public TUserinfo getTuser()
	{
		return tuser;
	}

	public void setTuser(TUserinfo tuser)
	{
		this.tuser = tuser;
	}

	public TUserManagerService<TUserinfo> getTuserService()
	{
		return tuserService;
	}

	public void setTuserService(TUserManagerService<TUserinfo> tuserService)
	{
		this.tuserService = tuserService;
	}

	public List<TUserinfo> getUsers()
	{
		return users;
	}

	public void setUsers(List<TUserinfo> users)
	{
		this.users = users;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public TRoleService getTroleService()
	{
		return troleService;
	}

	public void setTroleService(TRoleService troleService)
	{
		this.troleService = troleService;
	}

	public TDepartmentService getDepartmentService()
	{
		return departmentService;
	}

	public void setDepartmentService(TDepartmentService departmentService)
	{
		this.departmentService = departmentService;
	}

	public TLogService getLogService()
	{
		return logService;
	}

	public void setLogService(TLogService logService)
	{
		this.logService = logService;
	}

	public String getIpconfig()
	{
		return ipconfig;
	}

	public void setIpconfig(String ipconfig)
	{
		this.ipconfig = ipconfig;
	}
	
}
