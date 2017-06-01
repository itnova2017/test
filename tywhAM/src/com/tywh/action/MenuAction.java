package com.tywh.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.tywh.orm.TDepartment;
import com.tywh.orm.TDepartmentUser;
import com.tywh.orm.TMenu;
import com.tywh.orm.TRole;
import com.tywh.orm.TUserinfo;
import com.tywh.service.TLogService;
import com.tywh.service.TMenuService;
import com.tywh.service.TRoleService;
import com.tywh.util.Page;
import com.tywh.util.Result;
import com.tywh.util.UtilConfig;

/**
 *	MenuAction  菜单相关方法
 *  author：杜泉
 *  2014-6-24 下午1:53:49
 */
public class MenuAction extends ActionSupport
{
	private static final long	serialVersionUID	= 7953574513910949574L;
	
	private static Log log = LogFactory.getLog(MenuAction.class);
	private TRoleService  troleService;
	private TMenuService menuService;
	private TLogService  logService;
	private  String ipconfig;
	public String getIpconfig()
	{
		return ipconfig;
	}
	public void setIpconfig(String ipconfig)
	{
		this.ipconfig = ipconfig;
	}
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
	
	public String  getMenu(){
		try {
			log.info("查询菜单----------------");
			//通过session获取当前用户的菜单
			HttpServletRequest request= ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			
			Object o = session.getAttribute("navigation");
			Set<Integer> idSet = new HashSet<Integer>();
			if (o == null) {
				//通过session获取当前用户的所有属性
				TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
				if (curUser == null) {
					return INPUT;
				} else {
					//获取当前登陆人的所有角色与部门
					Set<TDepartRoleUser> roles = curUser.getRoles();
					TMenu t = troleService.findByRoleSet(roles, idSet);
					session.setAttribute("navigation", t);
					session.setAttribute("idPermission", idSet);
				}
			}
			ipconfig=UtilConfig.getValue("ipconfig");
			request.setAttribute("ipconfig",ipconfig);
			SaveLog("查询菜单树结构");
			return "tree";
		} catch (Exception e) {
			e.printStackTrace();
			Result r = new Result(false, "异常![" + e.getMessage() + "]", e);
			HttpServletRequest request= ServletActionContext.getRequest();
			request.setAttribute("result", r);
			return INPUT;
		}
	}
	
	/**
	 * 查询菜单集合
	 */
	public  String  queryAllMenuList(){
		try
		{
			log.info("查询菜单集合列表----------------");
			String menuName =getParam("menuName");
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 20 : Integer.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);
			//查询数据
			List<TMenu> list = menuService.queryAllMenuList(menuName,page);//查询菜单的分页
			SaveLog("查询菜单集合");
			//数据放到界面上展示
			request.setAttribute("menuName", menuName);
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			return SUCCESS;
		}
		catch (Exception  e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("MenuAction 中的queryAllMenuList异常![" + e.getMessage() + "]", e);
			return ERROR;
		}
	}
	
	/**
	 * 菜单的删除方法
	 */
	public String deleteMenuById()
	{
		log.info("通过菜单Id删除菜单----------------");
		String menuId =getParam("menuId");
		//删除部门角色菜单表数据
		//删除菜单表数据
		menuService.deleteMenuById(menuId);//通过菜单Id删除角色
		SaveLog("批量删除菜单,id:"+menuId);
		return queryAllMenuList();
	}
	
	/**
	 * 批量删除
	 */
	public String  deleteMenuByIds()
	{
		String menuIds =getParam("menuIds");
		String[] menuId=menuIds.split(",");
		for(String s:menuId){
			if(!s.trim().equals(""))
			{
				menuService.deleteMenuById(s);//通过菜单Id删除角色
			}
		}
		SaveLog("批量删除菜单,ids:"+menuIds);
		return queryAllMenuList();
	}
	
	/**
	 * 跳转到菜单添加页面
	 */
	public String showSaveMenu()
	{
		try
		{
			//查询所有的菜单集合按照Id排序
			List<TMenu>   menuList=menuService.queryMenu();
			SaveLog("跳转到菜单添加页面");
			request.setAttribute("menuList", menuList);
		}
		catch (ParseException e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("MenuAction 中的showSaveMenu异常![" + e.getMessage() + "]", e);
		}
		return "save";
	}

	/**
	 * 保存菜单
	 */
	public String saveMenu()
	{
		try
		{
			//获取所有的增加字段
			String menuName =getParam("menuName");
			String menuContent =getParam("menuContent");
			String parentId=getParam("parentId");
			String isState=getParam("isState");
			String num=getParam("num");
			String order=getParam("order");
			String url=getParam("url");
			String param=getParam("param");
			String remark=getParam("remark");
			TMenu tm=new TMenu();
			tm.setT_name(menuName==null?"":menuName);
			tm.setT_content(menuContent==null?"":menuContent);
			tm.setT_parentId(Integer.parseInt(parentId));
			tm.setT_state(Integer.parseInt(isState));
			tm.setT_num(num==null?"":num);
			tm.setT_order(Integer.parseInt(order));
			tm.setT_url(url==null?"":url);
			tm.setT_parameter(param==null?"":param);
			tm.setT_remark(remark==null?"":remark);
			//获取当前登陆人的ID 
			HttpSession session = request.getSession();
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			tm.setT_user(curUser.getT_id());
			
			menuService.saveMenu(tm);//添加菜单
			SaveLog("添加菜单,菜单名称:"+menuName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("MenuAction 中的saveMenu异常![" + e.getMessage() + "]", e);
		}
		return queryAllMenuList();
	}
	
	/**
	 * 查询当前的菜单名是否唯一
	 */
	public String repeatName()
	{
		String name =getParam("name");
		boolean  has= menuService.repeatName(name.trim());//查询当前的菜单是否唯一
		request.setAttribute("has", has);
		return "repeat";
	}
	
	/**
	 * 跳转到编辑页面
	 */
	public String showEditMenuById()
	{
		try
		{
			log.info("进入showEditMenuById方法");
			String menuId =getParam("menuId");
			TMenu tm=new TMenu();
			tm.setT_id(Integer.parseInt(menuId));
			TMenu tmu=menuService.queryMenuById(tm);
			request.setAttribute("tmu", tmu);
			//查询所有的菜单集合按照Id排序
			List<TMenu>   menuList=menuService.queryMenu();
			SaveLog("跳转到编辑菜单,id:"+menuId);
			request.setAttribute("menuList", menuList);
		}
		catch (ParseException e)
		{
			log.error("MenuAction 中的showEditMenuById跳转到菜单修改异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return "edit";
	}
	
	/**
	 * 编辑保存菜单数据
	 */
	public String editMenu()
	{
		try
		{
			//修改菜单信息  
			//获取所有的增加字段
			String menuId =getParam("menuId");
			String menuName =getParam("menuName");
			String menuContent =getParam("menuContent");
			String parentId=getParam("parentId");
			String num=getParam("num");
			String order=getParam("order");
			String url=getParam("url");
			String param=getParam("param");
			String remark=getParam("remark");
			
			//这部分主要是为了查询原来的部门信息
			TMenu tm=new TMenu();
			tm.setT_id(Integer.parseInt(menuId));
			TMenu tmt=menuService.queryMenuById(tm);
			
			if(null != tmt){
				TMenu tms=new TMenu();
				tms.setT_id(Integer.parseInt(menuId));
				tms.setT_name(menuName==null?"":menuName);
				tms.setT_content(menuContent==null?"":menuContent);
				tms.setT_parentId(Integer.parseInt(parentId));
				tms.setT_num(num==null?"":num);
				tms.setT_order(Integer.parseInt(order));
				tms.setT_url(url==null?"":url);
				tms.setT_parameter(param==null?"":param);
				tms.setT_remark(remark==null?"":remark);
				//获取当前登陆人的ID 
				HttpSession session = request.getSession();
				TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
				tms.setT_user(curUser.getT_id());
				//修改菜单
				menuService.editMenu(tms);
				SaveLog("编辑保存菜单数据,id:"+menuId);
			}
		}
		catch (ParseException e)
		{
			log.error("MenuAction 中的editMenu异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return queryAllMenuList();
	}
	
	/**
	 * 批量添加菜单到权限
	 */
	public String addMenus()
	{
		menuService.addMenus();
		return queryAllMenuList();
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
	public static Log getLog()
	{
		return log;
	}
	public static void setLog(Log log)
	{
		MenuAction.log = log;
	}
	
}
