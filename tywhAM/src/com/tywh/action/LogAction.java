package com.tywh.action;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tywh.orm.TDepartment;
import com.tywh.orm.TLog;
import com.tywh.service.TDepartmentService;
import com.tywh.service.TLogService;
import com.tywh.util.Page;

/**
 *	LogAction
 *  author：杜泉
 *  2014-7-14 下午3:30:56
 */
public class LogAction extends ActionSupport
{
	private static final long	serialVersionUID	= -370188332837548290L;
	private static Log log = LogFactory.getLog(UserInfoAction.class);
	private TLogService logService;
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
	 * 查询所有的日志
	 */
	public  String  queryAllLogList(){
		try
		{
			log.info("查询所有日志列表----------------");
			String keyword =getParam("keyword");//关键字
			String startDate =getParam("startDate");//起始日期
			String endDate =getParam("endDate");//截止日期
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 40 : Integer.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);
			//查询数据
			List<TLog> list = logService.queryAllLogList(keyword,startDate,endDate,page);//查询用户的分页
			//明天把数据放到界面上展示
			request.setAttribute("keyword", keyword);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			return SUCCESS;
		}
		catch (Exception  e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("LogAction 中的queryAllLogList异常![" + e.getMessage() + "]", e);
			return ERROR;
		}
	}
	
	/**
	 * 查看日志
	 */
	public  String viewLog()
	{
		try
		{
		log.info("通过日志ID查看日志信息----------------");
		String logId =getParam("logId");//日志ID
		TLog tl = logService.queryLogById(logId);
			request.setAttribute("tl", tl);
		}
		catch (ParseException e)
		{
			//在此捕获“异常”
			log.error("LogAction 中的viewLog异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return "viewLog";
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
