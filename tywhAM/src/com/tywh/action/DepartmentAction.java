package com.tywh.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tywh.orm.TDepartment;
import com.tywh.orm.TUserinfo;
import com.tywh.service.TDepartmentService;
import com.tywh.service.TLogService;
import com.tywh.service.TUserInfoService;
import com.tywh.util.Page;
/**
 *	DepartmentAction
 *  author：杜泉
 *  2014-7-3 下午4:20:05
 */
public class DepartmentAction extends ActionSupport
{	
	private static final long	serialVersionUID	= 2383475840145818729L;
	private static Log log = LogFactory.getLog(UserInfoAction.class);
	private TDepartmentService departmentService;
	private TUserInfoService userInfoService;
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
	 * 查询所有的部门
	 */
	public  String  queryAllDaptList(){
		try
		{
			log.info("查询所有部门列表----------------");
			String daptName =getParam("daptName");
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 20 : Integer.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);
			//查询数据
			List<TDepartment> list = departmentService.queryAllDaptList(daptName,page);//查询用户的分页
			SaveLog("查询所有的部门");
			//明天把数据放到界面上展示
			request.setAttribute("daptName", daptName);
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			return SUCCESS;
		}
		catch (Exception  e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("DepartmentAction 中的queryAllDaptList异常![" + e.getMessage() + "]", e);
			return ERROR;
		}
	}
	
	/**
	 * 部门的删除方法
	 */
	public String deleteDepartmentById()
	{
		log.info("通过部门Id删除部门----------------");
		try
		{
			String daptId =getParam("daptId");
			//删除级联数据
			//删除用户表中的部门Id数据
			//先查询当前用户中有多少是这个部门的
			List<TUserinfo> listUser=userInfoService.queryUserByDepart(daptId);
			if(null!=listUser && listUser.size()>0)
			{
				for(TUserinfo tui:listUser)
				{
					String dapt=tui.getT_daptId();
					String[] dapts=dapt.split(","+daptId+",");
					if(null!=dapts ){
						if(dapts.length==0)
						{
							tui.setT_daptId("");
						}
						else if(dapt.length()==1)
						{
							tui.setT_daptId(dapts[0]+",");
						}
						else if(dapt.length()>1)
						{
							tui.setT_daptId(dapts[0]+","+dapts[1]);
						}
						tui.setT_password(tui.getT_password());
						userInfoService.editUserInfo(tui);
				   }
				}
			}
			//删除部门与用户表数据
			//删除部门用户角色表数据
			//删除部门用户菜单表数据
			//删除部门与角色表数据
			departmentService.deleteDepartmentById(daptId);//通过部门Id删除部门	
			SaveLog("删除部门,id:"+daptId);
		}
		catch (ParseException e)
		{
			log.error("DepartmentAction 中的deleteDepartmentById异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return queryAllDaptList();
	}
	
	/**
	 * 批量删除
	 */
	public String  deleteDepartmentByIds()
	{
		String daptIds =getParam("daptIds");
		String[] daptId=daptIds.split(",");
		for(String s:daptId){
			if(!s.trim().equals(""))
			{
				departmentService.deleteDepartmentById(s);//通过部门Id删除部门
			}
		}
		SaveLog("批量删除部门,ids:"+daptIds);
		return queryAllDaptList();
	}
	
	/**
	 * 跳转到部门添加页面
	 */
	public String showSaveDepartment()
	{
		return "save";
	}

	/**
	 * 保存部门
	 */
	public String saveDepartment()
	{
		//获取所有的增加字段
		String daptName =getParam("daptName");
		String isUse =getParam("isUse");
		TDepartment td=new TDepartment();
		td.setT_departName(daptName==null?"":daptName);
		td.setT_isUse(isUse==null || "".equals(isUse) ? null : Integer.parseInt(isUse));
		departmentService.saveDepartment(td);//添加部门
		SaveLog(" 保存部门,部门名称:"+daptName);
		return queryAllDaptList();
	}
	
	/**
	 * 查询当前的部门是否唯一
	 */
	public String repeatName()
	{
		String name =getParam("name");
		boolean  has= departmentService.repeatName(name.trim());//查询当前的部门是否唯一
		request.setAttribute("has", has);
		return "repeat";
	}
	
	/**
	 * 跳转到编辑页面
	 */
	public String showEditDepartmentById()
	{
		try
		{
			log.info("进入showEditDepartmentById方法");
			String daptId =getParam("daptId");
			TDepartment td=new TDepartment();
			td.setT_id(Integer.parseInt(daptId));
			TDepartment tdt=departmentService.queryDaptById(td);
			SaveLog(" 跳转修改角色信息 ,部门id:"+daptId);
			request.setAttribute("tdt", tdt);
		}
		catch (ParseException e)
		{
			log.error("DepartmentAction 中的showEditDepartmentById跳转到部门修改异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		
		return "edit";
	}
	
	/**
	 * 编辑保存角色数据
	 */
	public String editDepartmentInfo()
	{
		try
		{
			//修改角色信息  
			//获取所有的增加字段
			String daptId =getParam("daptId");
			String daptName =getParam("daptName");
			//String isUse =getParam("isUse");
			
			//这部分主要是为了查询原来的部门信息
			TDepartment td=new TDepartment();
			td.setT_id(Integer.parseInt(daptId));
			TDepartment tdt=departmentService.queryDaptById(td);
			
			
			if(null != tdt){
				TDepartment tds=new TDepartment();
				tds.setT_id(tdt.getT_id());
				tds.setT_departName(daptName==null?"":daptName);
				//tds.setT_isUse(isUse==null || "".equals(isUse) ? null : Integer.parseInt(isUse));
				//修改人员
				departmentService.editDepartmentInfo(tds);
			}
			
			SaveLog("修改角色信息 ,部门id:"+daptId+"部门名称:"+daptName);
		}
		catch (ParseException e)
		{
			log.error("DepartmentAction 中的editDepartmentInfo异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return queryAllDaptList();
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
	
	public  String ajax1()
	{
		return "ajax1";
	}
	
	
	public void yanZhengMingCheng() throws IOException
	{
		
		String shuju=(String) request.getParameter("shujuxinxi");
		//调用数据库信息  进行 sql查询  返回结果集 进行 返回 
		System.out.println(shuju);
		
		PrintWriter  pw=response.getWriter();
		if(shuju.equals("chenggong"))
		{
			pw.print("成功数据");
		}
		else
		{
			pw.print("失败");
		}
		pw.close();
	}
	
	public TDepartmentService getDepartmentService()
	{
		return departmentService;
	}
	public void setDepartmentService(TDepartmentService departmentService)
	{
		this.departmentService = departmentService;
	}
	public TUserInfoService getUserInfoService()
	{
		return userInfoService;
	}
	public void setUserInfoService(TUserInfoService userInfoService)
	{
		this.userInfoService = userInfoService;
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
