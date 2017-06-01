package com.tywh.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tywh.orm.TOrderTheMeals;
import com.tywh.orm.TUserinfo;
import com.tywh.service.TLogService;
import com.tywh.service.TOrderTheMealsService;
import com.tywh.util.DateFun;
import com.tywh.util.Page;


/**
 *	OrderTheMealsAction  订饭Action
 *  author：杜泉
 *  2014-12-16 上午10:55:26
 */
public class OrderTheMealsAction extends ActionSupport
{
	private static final long	serialVersionUID	= 1900157219726658616L;
	private static Log log = LogFactory.getLog(AttendanceAction.class);
	private TOrderTheMealsService orderTheMealsService;
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

	public TOrderTheMealsService getOrderTheMealsService()
	{
		return orderTheMealsService;
	}
	public void setOrderTheMealsService(TOrderTheMealsService orderTheMealsService)
	{
		this.orderTheMealsService = orderTheMealsService;
	}
	public TLogService getLogService()
	{
		return logService;
	}
	public void setLogService(TLogService logService)
	{
		this.logService = logService;
	}
	public HttpServletRequest getRequest()
	{
		return request;
	}
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}
	public HttpServletResponse getResponse()
	{
		return response;
	}
	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}
	/**
	 * 记录日志
	 */
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
			log.error("OrderTheMealsAction 中的SaveLog异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询某时间段的本人订餐记录
	 */
	public  String  queryUserList(){
		try
		{
			log.info(" 查询某时间段的本人订餐记录----------------");
			String startDate =getParam("startDate");//开始时间
			String endDate =getParam("endDate");//结束时间
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int onOM=0;//是否可以定晚饭;
			int pageSize = tempPageSize == null ? 20 : Integer.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(null==startDate || "".equals(startDate) )
			{
				Calendar   cal=Calendar.getInstance();//获取当前日期 
			    cal.add(Calendar.MONTH, 0);
			    cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
				startDate=sdf.format(cal.getTime());
			}
			
			if(null==endDate || "".equals(endDate) )
			{
			    Calendar ca2 = Calendar.getInstance();    
		        ca2.set(Calendar.DAY_OF_MONTH, ca2.getActualMaximum(Calendar.DAY_OF_MONTH));  
			    endDate=sdf.format(ca2.getTime());
			}
			
			HttpSession session = request.getSession();//获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			//查询数据
			List<TOrderTheMeals> list = orderTheMealsService.queryUserList(startDate,endDate,curUser.getT_id(),page);//查询用户请假的分页
			SaveLog("查询某时间段的本人订餐记录,"+curUser.getT_id()+"-"+curUser.getT_userName());
			
			//判断当前时间是否为12:40之前 显示添加按钮
			String  onDay=sdf2.format(new Date());//今天
			String  dates=onDay.substring(0,10)+" 12:40:00";//判断每天12:40时间
			long    now = sdf2.parse(sdf2.format(new Date())).getTime();//获取当前时间的long值
			long    now2=sdf2.parse(dates).getTime();//获取每天12:40的long时间
			if(now<=now2)
			{
				onOM=1;
			}
			//明天把数据放到界面上展示
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			request.setAttribute("onOM", onOM);
			return SUCCESS;
		}
		catch (Exception  e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("OrderTheMealsAction 中的queryUserList异常![" + e.getMessage() + "]", e);
			return ERROR;
		}
	}
	
	/**
	 * 查询今天是否订过饭
	 */
	public String repeatOM()
	{
		HttpSession session = request.getSession();//获取session中当前登陆的人
		TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
		int  userId=curUser.getT_id();
		boolean  has= orderTheMealsService.repeatOM(userId);//查询今天是否订过饭
		request.setAttribute("has", has);
		return "repeat";
	}
	
	/**
	 * 插入订餐信息
	 */
	public String saveOrderMeals()
	{
		HttpSession session = request.getSession();//获取session中当前登陆的人
		TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
		 
		//获取当前登陆人数据进行添加
		TOrderTheMeals tom=new TOrderTheMeals();
		tom.setT_userId(curUser.getT_id());
		tom.setT_week(DateFun.getWeek(new Date()));//星期
		orderTheMealsService.saveOrderMeals(tom);//添加部门
		SaveLog("插入订餐,插入人信息:"+curUser.getT_id()+"-"+curUser.getT_userName());
		return queryUserList();
	}
	
	/**
	 * 订餐的删除方法
	 */
	public String deleteOrderMeals()
	{
		log.info("通过订餐Id删除订餐----------------");
		String omId =getParam("omId");
		HttpSession session = request.getSession();//获取session中当前登陆的人
		TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
		orderTheMealsService.deleteOrderMeals(omId);//通过订餐Id删除订餐
		SaveLog("通过订餐Id删除订餐,删除人ID:"+curUser.getT_id()+"-"+curUser.getT_userName());
		return queryUserList();
	}
	
	/**
	 * 查询某时间段的全部人员的订餐记录
	 */
	public  String  queryAllUserList(){
		try
		{
			log.info(" 查询某时间段的全部订餐记录----------------");
			String startDate =getParam("startDate");//开始时间
			String endDate =getParam("endDate");//结束时间
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 20 : Integer.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(null==startDate || "".equals(startDate) )
			{
				startDate=sdf.format(new Date());
			}
			
			if(null==endDate || "".equals(endDate) )
			{
			    endDate=sdf.format(new Date());
			}
			
			HttpSession session = request.getSession();//获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			//查询数据
			List<TOrderTheMeals> list = orderTheMealsService.queryUserList(startDate,endDate,0,page);//查询订餐的分页
			SaveLog("查询某时间段的全部订餐记录,"+curUser.getT_id()+"-"+curUser.getT_userName());
			
			
			//明天把数据放到界面上展示
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			return "view";
		}
		catch (Exception  e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("OrderTheMealsAction 中的queryAllUserList异常![" + e.getMessage() + "]", e);
			return ERROR;
		}
	}
	
	
	
}
