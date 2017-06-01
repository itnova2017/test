package com.tywh.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tywh.orm.TDepartment;
import com.tywh.orm.TDepartmentUser;
import com.tywh.orm.TUserinfo;
import com.tywh.service.TDepartmentService;
import com.tywh.service.TLogService;
import com.tywh.service.TUserInfoService;
import com.tywh.util.Page;

/**
 *	UserInfoAction 用户相关操作
 *  author：杜泉
 *  2014-6-27 上午10:03:32
 */
public class UserInfoAction extends ActionSupport
{	
	private static final long	serialVersionUID	= 3085095061250074085L;
	
	private static Log log = LogFactory.getLog(UserInfoAction.class);
	private TUserInfoService userInfoService;
	private TDepartmentService departmentService;
	private TLogService  logService;
	HttpServletRequest  request = ServletActionContext.getRequest();
	HttpServletResponse  response= ServletActionContext.getResponse();
	HttpSession session = request.getSession();
	
	protected String getParam(String key) {
		if(ServletActionContext.getRequest().getParameter(key)!=null)
		{
			return ServletActionContext.getRequest().getParameter(key).trim();
		}
		else
		{
			return ServletActionContext.getRequest().getParameter(key);
		}
	}
	
	protected String[] getParams(String key) {
		return ServletActionContext.getRequest().getParameterValues(key);
	}
	
	/**
	 * 查询所有人员列表
	 */
	public  String  queryAllUserList(){
				try
				{
					log.info("查询所有人员列表----------------");
					String daptId =getParam("daptId");
					String userName =getParam("userName");
					String tempPageSize = getParam("pageSize");
					String tempCurPage = getParam("curPage");
					int pageSize = tempPageSize == null ? 20 : Integer.parseInt(tempPageSize);
					int curPage = tempCurPage == null ? 1 : Integer.parseInt(tempCurPage);
					Page page = new Page(pageSize, curPage);
					//查询数据
					List<TUserinfo> list = userInfoService.queryAllUserList(daptId,userName,page);//查询用户的分页
					List<TDepartment> listDapt = departmentService.queryAllDepart();//所有部门
					
					
					Map<Integer,String> ids=new HashMap<Integer,String>();
					//判断是否为多个部门的人员拼接用的部门名称
					if(null!=listDapt && listDapt.size()>0){
						for(TDepartment da:listDapt)
						{
							ids.put(da.getT_id(),da.getT_departName());
						}
					}
					//拼装用户的部门名称
					if(null!=list && list.size()>0){
						for(TUserinfo tu:list)
						{
							StringBuffer daptName=new StringBuffer();//部门名称
							String daptIds=tu.getT_daptId();//查询人员所有的部门Id
							if(null!=daptIds && !daptIds.equals(""))
							{
								String[] id=daptIds.split(",");
								for(String ss:id)
								{
									if(!ss.trim().equals(""))
									{
										daptName.append(ids.get(Integer.parseInt(ss.trim()))+",");//保存通过部门id 查询到的Map中的部门名称
									}
								}
								tu.setDapts(daptName.toString().substring(0, daptName.length()-1));//该人的所有的部门名称
							}
							else
							{
								tu.setDapts("");//该人的所有的部门名称
							}
						}
					}
					
					SaveLog("查询所有人员列表");
					request.setAttribute("daptId", daptId);
					request.setAttribute("userName", userName);
					request.setAttribute("list", list);
					request.setAttribute("listDapt", listDapt);
					request.setAttribute("page", page);
					return SUCCESS;
				}
				catch (Exception  e)
				{
					//在此捕获“异常”
					e.printStackTrace();
					log.error("UserInfoAction 中的queryAllUserList异常![" + e.getMessage() + "]", e);
					return ERROR;
				}
	  }
	
	/**
	 * 用户的删除方法
	 */
	public String deleteUserById()
	{
		log.info("通过用户Id删除用户----------------");
		String userId =getParam("userId");
		//删除级联数据
		//删除部门与用户表数据
		//删除部门用户角色表数据
		userInfoService.deleteUserById(userId);//通过用户Id删除用户
		SaveLog("用户的删除方法,userId:"+userId);
		return queryAllUserList();
	}
	
	/**
	 * 批量删除
	 */
	public String  deleteUserByIds()
	{
		String userIds =getParam("userIds");
		String[] userId=userIds.split(",");
		for(String s:userId){
			if(!s.trim().equals(""))
			{
				userInfoService.deleteUserById(s);//通过用户Id删除用户
			}
		}
		SaveLog("批量的用户删除方法,userIds:"+userIds);
		return queryAllUserList();
	}
	
	/**
	 * 跳转到人员添加页面
	 */
	public String showSaveUser()
	{
		log.info("进入showSaveUser方法");
		List<TDepartment> listDapt;
		try
		{
			listDapt = departmentService.queryAllDepart();
			SaveLog("人员添加页面");
			request.setAttribute("listDapt", listDapt);
		}
		catch (ParseException e)
		{
			log.error("UserInfoAction 中的showSaveUser查询部门集合异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}//所有部门
		return "save";
	}

	/**
	 * 保存人员
	 */
	public String saveUserInfo()
	{
		try
		{
			//获取所有的增加字段
			String loginName =getParam("loginName");
			String userName =getParam("userName");
			String password =getParam("password");
			String[] dapts =getParams("dapts");
			String telphone =getParam("telphone");
			String qq =getParam("qq");
			String email =getParam("email");
			String isUse =getParam("isUse");
			
			//增加部门
			StringBuffer daptss=new StringBuffer();
			for(int i=0;i<dapts.length;i++){  
				if(!dapts[i].trim().equals(""))
				{
					daptss.append(","+dapts[i]);
				}
			 }
			daptss.append(",");
			TUserinfo tui=new TUserinfo();
			tui.setT_loginName(loginName==null?"":loginName);
			tui.setT_userName(userName==null?"":userName);
			tui.setT_password(password.equals("")||password==null?"123456":password);
			tui.setT_daptId(daptss.toString()==null?"":daptss.toString());
			tui.setT_telphone(telphone==null?"":telphone);
			tui.setT_qq(qq==null?"":qq);
			tui.setT_email(email==null?"":email);
			tui.setT_isUse(isUse==null || "".equals(isUse) ? null : Integer.parseInt(isUse));

			userInfoService.saveUserInfo(tui);//添加人员
			//插入用户的同时需要把用户的部门插入到用户关联表中
			TUserinfo tu=userInfoService.queryUserInfo(tui);
			if(null!=tu)
			{
				for(String da:dapts)
				{
					TDepartmentUser du=new TDepartmentUser();
					du.setT_departId(Integer.parseInt(da));
					du.setT_userId(tu.getT_id());
					departmentService.saveDaptAndUser(du);
				}
			}
			SaveLog("保存人员,登录名:"+loginName+",用户组:"+dapts);
		}
		catch (ParseException e)
		{
			log.error("UserInfoAction 中的saveUserInfo保存用户异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return queryAllUserList();
	}
	
	/**
	 * 查询当前的登录名是否唯一
	 */
	public String repeatName()
	{
		String name =getParam("name");
		boolean  has= userInfoService.repeatName(name.trim());//查询当前的登录名是否唯一
		request.setAttribute("has", has);
		return "repeat";
	}
	
	/**
	 * 跳转到编辑页面
	 */
	public String showEditUserById()
	{
		try
		{
			log.info("进入showEditUserById方法");
			String userId =getParam("userId");
			TUserinfo tu=new TUserinfo();
			//判断是从菜单目录上选择的个人信息设置  就查询当前session中的ID
			if(null==userId)
			{
				TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
				tu.setT_id(curUser.getT_id());
			}
			else
			{
				tu.setT_id(Integer.parseInt(userId));
			}
			TUserinfo tui=userInfoService.queryUserInfo(tu);
			String dapts=tui.getT_daptId();
			List<TDepartment> listDapt =departmentService.queryAllDepart();
			if(dapts!=null && !dapts.equals("")){
				for(TDepartment td:listDapt)
				{
					boolean  i=dapts.contains(","+td.getT_id()+",");
					if(i)
					{
						td.setIsCheck("1");
					}
				}
			}
			SaveLog("编辑页面,userId:"+userId+",用户组:"+dapts);
			request.setAttribute("listDapt", listDapt);
			request.setAttribute("tui", tui);
		}
		catch (ParseException e)
		{
			log.error("UserInfoAction 中的showEditUserById跳转到用户修改异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		
		return "edit";
	}
	
	/**
	 * 编辑保存人员数据
	 */
	public String editUserInfo()
	{
		try
		{
			//修改用户信息  
			//获取所有的增加字段
			String uid =getParam("uid");
			String loginName =getParam("loginName");
			String userName =getParam("userName");
			String password =getParam("password");
			String[] dapts =getParams("dapts");
			String telphone =getParam("telphone");
			String qq =getParam("qq");
			String email =getParam("email");
			//String isUse =getParam("isUse");
			
			//增加部门
			StringBuffer daptss=new StringBuffer();
			for(int i=0;i<dapts.length;i++){  
				if(!dapts[i].trim().equals(""))
				{
					daptss.append(","+dapts[i]);
				}
			 }
			daptss.append(",");
			
			//这部分主要是为了查询原来的部门Ids
			TUserinfo tui=new TUserinfo();
			tui.setT_id(Integer.parseInt(uid));
			TUserinfo tu =userInfoService.queryUserInfo(tui);
			//获取老部门
			String oldDapts=tu.getT_daptId();
			//获得新部门
			String newDapts=daptss.toString();
			if(null != tu){
				TUserinfo t=new TUserinfo();
				t.setT_id(tu.getT_id());
				t.setT_loginName(loginName==null?"":loginName);
				t.setT_userName(userName==null?"":userName);
				t.setT_password(password.equals("")||password==null?"123456":password);
				t.setT_daptId(daptss.toString()==null?"":daptss.toString());
				t.setT_telphone(telphone==null?"":telphone);
				t.setT_qq(qq==null?"":qq);
				t.setT_email(email==null?"":email);
				//t.setT_isUse(isUse==null || "".equals(isUse) ? null : Integer.parseInt(isUse));
				//修改人员
				userInfoService.editUserInfo(t);
			}
			
			//修改部门与用户表之间的关系 (先删除 后添加)；
			//如果部门没有修改 就不进行操作关联表
			if(null!=oldDapts && !oldDapts.equals(newDapts))
			{
				String[] ss=oldDapts.split(",");
				for(String s:ss)
				{
					if(!s.equals(""))
					{
						TDepartmentUser du=new TDepartmentUser();
						du.setT_departId(Integer.parseInt(s));
						du.setT_userId(tu.getT_id());
						departmentService.deleteDaptAndUser(du);
					}
				}
				
				String[] sss=newDapts.split(",");
				for(String ssss:sss)
				{
					if(!ssss.equals(""))
					{
						TDepartmentUser du2=new TDepartmentUser();
						du2.setT_departId(Integer.parseInt(ssss));
						du2.setT_userId(tu.getT_id());
						departmentService.saveDaptAndUser(du2);
					}
				}
			}
			else
			{
				String[] sss=newDapts.split(",");
				for(String ssss:sss)
				{
					if(!ssss.equals(""))
					{
						TDepartmentUser du2=new TDepartmentUser();
						du2.setT_departId(Integer.parseInt(ssss));
						du2.setT_userId(tu.getT_id());
						departmentService.saveDaptAndUser(du2);
					}
				}
			}
			SaveLog("编辑保存人员,uid:"+uid+",用户组:"+dapts);
		}
		catch (ParseException e)
		{
			//在此捕获“异常”
			e.printStackTrace();
		}
		return queryAllUserList();
	}
	
	/**
	 * 用户个人修改
	 */
	public  String showEditUserInfoById()
	{
		try
		{
			log.info("进入showEditUserInfoById方法");
			//判断是从菜单目录上选择的个人信息设置  就查询当前session中的ID
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			TUserinfo  tu=new TUserinfo();
			tu.setT_id(curUser.getT_id());
			TUserinfo tui=userInfoService.queryUserInfo(tu);
			String dapts=tui.getT_daptId();
			List<TDepartment> listDapt = departmentService.queryAllDepart();
			for(TDepartment td:listDapt)
			{
				boolean  i=dapts.contains(","+td.getT_id()+",");
				if(i)
				{
					td.setIsCheck("1");
				}
			}
			SaveLog("用户个人修改");
			request.setAttribute("listDapt", listDapt);
			request.setAttribute("tui", tui);
		}
		catch (ParseException e)
		{
			log.error("UserInfoAction 中的showEditUserInfoById跳转到用户个人修改异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return "editInfo";
	}
	
	/**
	 * 编辑保存人员数据
	 */
	public String editUserInfos()
	{
		try
		{
			//修改用户信息  
			//获取所有的增加字段
			String uid =getParam("uid");
			String loginName =getParam("loginName");
			String userName =getParam("userName");
			String password =getParam("password");
			String telphone =getParam("telphone");
			String qq =getParam("qq");
			String email =getParam("email");
			
			//这部分主要是为了查询原来的部门Ids
			TUserinfo tui=new TUserinfo();
			tui.setT_id(Integer.parseInt(uid));
			TUserinfo tu =userInfoService.queryUserInfo(tui);
			if(null != tu){
				TUserinfo t=new TUserinfo();
				t.setT_id(tu.getT_id());
				t.setT_loginName(loginName==null?"":loginName);
				t.setT_userName(userName==null?"":userName);
				t.setT_password(password==""||password==null?"123456":password);
				t.setT_daptId(tu.getT_daptId());
				t.setT_telphone(telphone==null?"":telphone);
				t.setT_qq(qq==null?"":qq);
				t.setT_email(email==null?"":email);
				//t.setT_isUse(isUse==null || "".equals(isUse) ? null : Integer.parseInt(isUse));
				//修改人员
				userInfoService.editUserInfo(t);
				SaveLog("用户个人修改保存,用户id:"+uid);
			}
			
		}
		catch (ParseException e)
		{
			//在此捕获“异常”
			e.printStackTrace();
		}
		return showEditUserInfoById();
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
	
	public TUserInfoService getUserInfoService()
	{
		return userInfoService;
	}

	public void setUserInfoService(TUserInfoService userInfoService)
	{
		this.userInfoService = userInfoService;
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
}
