package com.tywh.action;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tywh.orm.TDepartRolerMenu;
import com.tywh.orm.TDepartment;
import com.tywh.orm.TMenu;
import com.tywh.orm.TRole;
import com.tywh.orm.TUserinfo;
import com.tywh.service.TDepartmentService;
import com.tywh.service.TLogService;
import com.tywh.service.TMenuService;
import com.tywh.service.TRoleService;
import com.tywh.util.Page;

/**
 *	PermissionAction 权限管理
 *  author：杜泉
 *  2014-7-11 上午9:25:00
 */
public class PermissionAction extends ActionSupport
{
	private static final long	serialVersionUID	= -6700391133385384805L;
	private static Log log = LogFactory.getLog(MenuAction.class);
	private TRoleService troleService;
	private TDepartmentService departmentService;
	private TMenuService menuService;
	private TLogService  logService;
	
	HttpServletRequest  request = ServletActionContext.getRequest();
	HttpServletResponse  response= ServletActionContext.getResponse();
	
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
	 * 查询所有的权限数据（来自于角色表中的数据）
	 */
	public  String  queryAllDaptToRoleList(){
		try
		{
			log.info("查询所有的权限数据列表----------------");
			String daptId =getParam("daptId");
			String roleName =getParam("roleName");
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 20 : Integer.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);
			//查询数据
			List<TRole> list = troleService.queryAllDaptToRoleList(daptId,roleName,page);//查询权限数据的分页
			List<TDepartment> listDapt = departmentService.queryAllDepart();//所有部门
			SaveLog("查询所有的权限数据");
			//明天把数据放到界面上展示
			request.setAttribute("daptId", daptId);
			request.setAttribute("roleName", roleName);
			request.setAttribute("list", list);
			request.setAttribute("listDapt", listDapt);
			request.setAttribute("page", page);
			return SUCCESS;
		}
		catch (Exception  e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("PermissionAction 中的queryAllUserList异常![" + e.getMessage() + "]", e);
			return ERROR;
		}
	}
	
	/**
	 * 设置权限（把所有的菜单都查询出来，不包含以有的菜单）
	 */
	public String setPermission()
	{
		try
		{
			String daptId =getParam("daptId");
			String roleId =getParam("roleId");
			String menuName =getParam("menuName");
			//当前已经在关联表中存在的 菜单ID
			TDepartRolerMenu  tdru =new TDepartRolerMenu();
			tdru.setT_daptId(Integer.parseInt(daptId));
			tdru.setT_roleId(Integer.parseInt(roleId));
			//List<TDepartRolerMenu>   tdrus=menuService.queryMenuIdsByDaptAndRole(tdru); 
			//查询菜单表数据 去除已经存在的菜单ID 
			List<TMenu>   tm=menuService.queryMenus(tdru,false,menuName);
			SaveLog("设置权限,部门id:"+daptId+",roleId:"+roleId+",menuName:"+menuName);
			request.setAttribute("tmlist", tm);
			request.setAttribute("roleId", roleId);
			request.setAttribute("daptId", daptId);
			request.setAttribute("menuName", menuName);
		}
		catch (Exception e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("PermissionAction 中的setPermission异常![" + e.getMessage() + "]", e);
		}
		return "setPermission";
	}
	
	/**
	 * 分配权限
	 */
	public String savePermission()
	{
		String daptId =getParam("daptId");
		String roleId =getParam("roleId");
		String menuId =getParam("menuId");
		boolean isUse =true;
		TDepartRolerMenu  tdru =new TDepartRolerMenu();
		tdru.setT_daptId(Integer.parseInt(daptId));
		tdru.setT_roleId(Integer.parseInt(roleId));
		tdru.setT_menuId(Integer.parseInt(menuId));
		menuService.saveMenuIdsByDaptAndRole(tdru); 
		SaveLog("分配权限,部门id:"+daptId+",roleId:"+roleId+",menuId:"+menuId);
		request.setAttribute("roleId", roleId);
		request.setAttribute("daptId", daptId);
		request.setAttribute("isUse", isUse);
		//分配完后进入菜单分配页 重新查询数据
		return setPermission();
	}
	
	
	/**
	 * 批量分配权限
	 */ 
	public String savePermissions()
	{
		String daptId =getParam("daptId");
		String roleId =getParam("roleId");
		String menuIds =getParam("perIds");
		String[] menuId=menuIds.split(",");
		for(String s:menuId){
			if(!s.trim().equals(""))
			{
				TDepartRolerMenu  tdru =new TDepartRolerMenu();
				tdru.setT_daptId(Integer.parseInt(daptId));
				tdru.setT_roleId(Integer.parseInt(roleId));
				tdru.setT_menuId(Integer.parseInt(s));
				menuService.saveMenuIdsByDaptAndRole(tdru); 
			}
		}
		SaveLog("批量分配权限,部门id:"+daptId+",roleId:"+roleId+",menuIds:"+menuIds);
		request.setAttribute("roleId", roleId);
		request.setAttribute("daptId", daptId);
		//分配完后进入菜单分配页 重新查询数据
		return setPermission();
	}
	
	/**
	 * 查看权限（把所有关联表中的菜单查询出来）
	 */
	public String lookPermission()
	{
		try
		{
			String daptId =getParam("daptId");
			String roleId =getParam("roleId");
			String menuName =getParam("menuName");
			//当前已经在关联表中存在的 菜单ID
			TDepartRolerMenu  tdru =new TDepartRolerMenu();
			tdru.setT_daptId(Integer.parseInt(daptId));
			tdru.setT_roleId(Integer.parseInt(roleId));
//			List<TDepartRolerMenu>   tdrus=menuService.queryMenuIdsByDaptAndRole(tdru); 
//			String menuIds="";
//			if(null!=tdrus && tdrus.size()>0)
//			{
//				for(TDepartRolerMenu ts:tdrus){
//					menuIds=menuIds+ts.getT_menuId()+",";
//				}
//				menuIds.substring(0, menuIds.length()-1);
//			}
			//查询菜单表数据 去除已经存在的菜单ID 
			List<TMenu>   tm=menuService.queryMenus(tdru,true,menuName);
			SaveLog("查看权限,部门id:"+daptId+",roleId:"+roleId+",menuName:"+menuName);
			request.setAttribute("tmlist", tm);
			request.setAttribute("roleId", roleId);
			request.setAttribute("daptId", daptId);
		}
		catch (Exception e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("PermissionAction 中的lookPermission异常![" + e.getMessage() + "]", e);
		}
		return "lookPermission";
	}
	
	
	/**
	 * 释放权限
	 */
	public String deletePermission()
	{
		String daptId =getParam("daptId");
		String roleId =getParam("roleId");
		String menuId =getParam("menuId");

		TDepartRolerMenu  tdru =new TDepartRolerMenu();
		tdru.setT_daptId(Integer.parseInt(daptId));
		tdru.setT_roleId(Integer.parseInt(roleId));
		tdru.setT_menuId(Integer.parseInt(menuId));
		menuService.deleteMenuIdsByDaptAndRole(tdru);
		SaveLog("释放权限,部门id:"+daptId+",roleId:"+roleId+",menuId:"+menuId);
		request.setAttribute("roleId", roleId);
		request.setAttribute("daptId", daptId);
		//分配完后进入菜单分配页 重新查询数据
		return lookPermission();
	}
	
	/**
	 * 批量释放权限
	 */
	public String deletePermissions()
	{
		String daptId =getParam("daptId");
		String roleId =getParam("roleId");
		String menuIds =getParam("perIds");
		String[] menuId=menuIds.split(",");
		for(String s:menuId){
			if(!s.trim().equals(""))
			{
				TDepartRolerMenu  tdru =new TDepartRolerMenu();
				tdru.setT_daptId(Integer.parseInt(daptId));
				tdru.setT_roleId(Integer.parseInt(roleId));
				tdru.setT_menuId(Integer.parseInt(s));
				menuService.deleteMenuIdsByDaptAndRole(tdru); 
			}
		}
		SaveLog("批量释放权限,部门id:"+daptId+",roleId:"+roleId+",menuIds:"+menuIds);
		request.setAttribute("roleId", roleId);
		request.setAttribute("daptId", daptId);
		//分配完后进入菜单分配页 重新查询数据
		return lookPermission();
	}
	
	public String deletePermissionById()
	{
		//通过部门Id与角色Id 删除关联菜单表数据
		String daptId =getParam("daptId");
		String roleId =getParam("roleId");
		TDepartRolerMenu  tdru =new TDepartRolerMenu();
		tdru.setT_daptId(Integer.parseInt(daptId));
		tdru.setT_roleId(Integer.parseInt(roleId));
		menuService.deleteMenuIdsByDaptAndRole(tdru); 
		request.setAttribute("roleId", roleId);
		request.setAttribute("daptId", daptId);
		//通过部门Id与角色Id 删除关联业务表数据
		SaveLog("删除权限数据,部门id:"+daptId+",roleId:"+roleId);
		return queryAllDaptToRoleList();
	}
	
	
	/**
	 * 批量删除权限数据
	 */
	public String  deletePermissionByIds()
	{
		String perIds =getParam("perIds");
		//删除部门角色 菜单关联表数据
		if(null!=perIds &&perIds.length()>0)
		{
			//获取到要删除的部门id-角色id 字符串
			String[] pIds=perIds.split(",");
			for(String pid:pIds)
			{
				String[] ids=pid.split("-");
				TDepartRolerMenu  tdru =new TDepartRolerMenu();
				tdru.setT_daptId(Integer.parseInt(ids[0]));
				tdru.setT_roleId(Integer.parseInt(ids[1]));
				menuService.deleteMenuIdsByDaptAndRole(tdru); 
			}
		}
		//删除部门角色 业务关联表数据
		SaveLog("批量删除权限数据,ids:"+perIds);
		return queryAllDaptToRoleList();
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
	public TMenuService getMenuService()
	{
		return menuService;
	}
	public void setMenuService(TMenuService menuService)
	{
		this.menuService = menuService;
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
