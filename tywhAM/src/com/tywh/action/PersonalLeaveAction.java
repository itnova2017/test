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
import com.tywh.orm.TAttendancePeople;
import com.tywh.orm.TDepartment;
import com.tywh.orm.TPersonalLeave;
import com.tywh.orm.TPersonalLeaveStatistics;
import com.tywh.orm.TUserinfo;
import com.tywh.service.TAttendanceService;
import com.tywh.service.TDepartmentService;
import com.tywh.service.TLogService;
import com.tywh.service.TPersonalLeaveService;
import com.tywh.util.DateFun;
import com.tywh.util.Page;

/**
 *	PersonalLeaveAction 请假Action
 *  author：杜泉
 *  2014-11-3 上午10:28:38
 */
public class PersonalLeaveAction extends ActionSupport
{
	private static final long	serialVersionUID	= 4220659400871030186L;
	private static Log log = LogFactory.getLog(AttendanceAction.class);
	private TAttendanceService		attendanceService;
	private TPersonalLeaveService personalLeaveService;
	private TDepartmentService departmentService;
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
			log.error("PersonalLeaveAction 中的SaveLog异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询某时间段的本人请假记录
	 */
	public  String  queryAllPersonalLeaveList(){
		try
		{
			log.info("查询某时间段的本人请假记录----------------");
			String plstate =getParam("plstate");//请假类型
			String startDate =getParam("startDate");//开始时间
			String endDate =getParam("endDate");//结束时间/
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 20 : Integer.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
			List<TPersonalLeave> list = personalLeaveService.queryAllPersonalLeaveList(plstate,startDate,endDate,curUser.getT_id()+"",null,page);//查询用户请假的分页
			SaveLog("查询某时间段的本人请假记录,"+curUser.getT_id()+"-"+curUser.getT_userName());
			//明天把数据放到界面上展示
			request.setAttribute("plstate", plstate);
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
			log.error("PersonalLeaveAction 中的queryAllPersonalLeaveList异常![" + e.getMessage() + "]", e);
			return ERROR;
		}
	}
	
	/**
	 * 请假的删除方法
	 */
	public String deletePersonalLeaveById()
	{
		log.info("通过请假Id删除请假信息----------------");
		try
		{
			String plId =getParam("plId");
			//删除请假统计表中该人当月该类型的请假数据集合
			//然后删除该请假数据
			
			//通过请假Id查询请假信息
			TPersonalLeave tpl=new TPersonalLeave();
			tpl.setId(Integer.parseInt(plId));
			TPersonalLeave tpls=personalLeaveService.queryPersonalLeave(tpl);
			if(null!=tpls)
			{
				//获取当前的请假时长然后如果是事假修改事假时长减去与事假次数-1
				int state=tpls.getState();//请假类型
				float times=tpls.getLeaveDuration();//请假时长
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String year=sdf.format(tpls.getLeaveDay()).substring(0,4);//获取到当前月份
				String month=sdf.format(tpls.getLeaveDay()).substring(5,7);//获取到当前月份
				//通过当前登录人ID 与 月份 修改 请假统计
				HttpSession session = request.getSession();//获取session中当前登陆的人
				TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
				int userId=curUser.getT_id();
				TPersonalLeaveStatistics tplss=new TPersonalLeaveStatistics();
				tplss.setT_userId(userId);//用户ID
				tplss.setT_leaveYear(year);//请假集合年份
				tplss.setT_leaveMonth(month);//请假集合月份
				TPersonalLeaveStatistics tplsss=personalLeaveService.queryTPersonalLeaveStatistics(tplss);
				if(null!=tplsss)
				{
					//根据类型减去请假时间 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假 9.婚假
					if(state==1)
					{
						tplsss.setT_leaveStatistics(tplsss.getT_leaveStatistics()-times);
						tplsss.setT_leaveCountStatistics(tplsss.getT_leaveCountStatistics()-1);
					}
					else if(state==2)
					{
						tplsss.setT_sickLeaveStatistics(tplsss.getT_sickLeaveStatistics()-times);
					}
					else if(state==3)
					{
						tplsss.setT_businessLeaveStatistics(tplsss.getT_businessLeaveStatistics()-times);
					}
					else if(state==4)
					{
						tplsss.setT_stringBreakLeaveStatistics(tplsss.getT_stringBreakLeaveStatistics()-times);
					}
					else if(state==5)
					{
						tplsss.setT_funeralLeaveStatistics(tplsss.getT_funeralLeaveStatistics()-times);
					}
					else if(state==6)
					{
						tplsss.setT_drivingLeaveStatistics(tplsss.getT_drivingLeaveStatistics()-times);
					}
					else if(state==7)
					{
						tplsss.setT_maternityLeaveStatistics(tplsss.getT_maternityLeaveStatistics()-times);
					}
					else if(state==8)
					{
						tplsss.setT_lactationLeaveStatistics(tplsss.getT_lactationLeaveStatistics()-times);
					}
					else if(state==9)
					{
						tplsss.setT_maritalLeaveStatistics(tplsss.getT_maritalLeaveStatistics()-times);
					}
					else if(state==10)
					{
						tplsss.setT_thesisStatistics(tplsss.getT_thesisStatistics()-times);
					}
					
					tplsss.setT_operatingPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
					//修改统计表数据
					personalLeaveService.updateTPersonalLeaveStatistics(tplsss);
				}
			}
			personalLeaveService.deletePersonalLeaveById(plId);//通过请假Id删除请假内容	
			SaveLog("删除请假信息,id:"+plId);
		}
		catch (ParseException e)
		{
			log.error("PersonalLeaveAction 中的deletePersonalLeaveById异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return queryAllPersonalLeaveList();
	}
	
	/**
	 * 跳转到请假添加页面
	 */
	public String showSavepersonalLeave()
	{
		return "save";
	}
	
	/**
	 * 保存请假信息
	 */
	public String  savePersonalLeave()
	{
		try
		{
			//获取所有的增加字段
			String plstate =getParam("plstate");//请假类型
			String leaveDay = getParam("leaveDay");
			String leaveTimes =getParam("leaveTimes");//请假时间段
			String leaveDuration = getParam("leaveDuration");
			float times=Float.parseFloat(leaveDuration);//请假时长
			String leaveType =getParam("leaveType");//请假原因
			String isTwoApprover =getParam("isTwoApprover");//是否二级审批

			HttpSession session = request.getSession();//获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			int userId=curUser.getT_id();//用户ID 
			String deptId=curUser.getT_daptId();//用户部门ID
			TPersonalLeave tpl=new TPersonalLeave();
			tpl.setDaptId(deptId);
			tpl.setUserId(userId);
			tpl.setState(Integer.parseInt(plstate));
			tpl.setLeaveTimes(leaveTimes);
			tpl.setLeaveDuration(times);
			tpl.setLeaveDay(DateFun.toDate(leaveDay));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(leaveDay);
			tpl.setLeaveWeekDay(DateFun.getWeek(date));
			tpl.setLeaveType(leaveType);
			tpl.setIsTwoApprover(isTwoApprover==null || "".equals(isTwoApprover) ? 0 : 1);
			tpl.setCreatePersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
			personalLeaveService.savePersonalLeave(tpl);//添加请假
			String year=leaveDay.substring(0,4);//获取到当前月份
			String month=leaveDay.substring(5,7);//获取到当前月份
			//修改请假统计数据
			TPersonalLeaveStatistics tplss=new TPersonalLeaveStatistics();
			tplss.setT_userId(userId);//用户ID
			tplss.setT_leaveYear(year);//请假集合年份
			tplss.setT_leaveMonth(month);//请假集合月份
			TPersonalLeaveStatistics tplsss=personalLeaveService.queryTPersonalLeaveStatistics(tplss);
			//这是有本月统计的情况 进行修改 判断是否有当月数据 没有添加 有就修改数据
			if(null!=tplsss)
			{
				//根据类型减去请假时间 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假 9.婚假 10.论文假
				if("1".equals(plstate))
				{
					tplsss.setT_leaveStatistics(tplsss.getT_leaveStatistics()+times);
					tplsss.setT_leaveCountStatistics(tplsss.getT_leaveCountStatistics()+1);
				}
				else if("2".equals(plstate))
				{
					tplsss.setT_sickLeaveStatistics(tplsss.getT_sickLeaveStatistics()+times);
				}
				else if("3".equals(plstate))
				{
					tplsss.setT_businessLeaveStatistics(tplsss.getT_businessLeaveStatistics()+times);
				}
				else if("4".equals(plstate))
				{
					tplsss.setT_stringBreakLeaveStatistics(tplsss.getT_stringBreakLeaveStatistics()+times);
				}
				else if("5".equals(plstate))
				{
					tplsss.setT_funeralLeaveStatistics(tplsss.getT_funeralLeaveStatistics()+times);
				}
				else if("6".equals(plstate))
				{
					tplsss.setT_drivingLeaveStatistics(tplsss.getT_drivingLeaveStatistics()+times);
				}
				else if("7".equals(plstate))
				{
					tplsss.setT_maternityLeaveStatistics(tplsss.getT_maternityLeaveStatistics()+times);
				}
				else if("8".equals(plstate))
				{
					tplsss.setT_lactationLeaveStatistics(tplsss.getT_lactationLeaveStatistics()+times);
				}
				else if("9".equals(plstate))
				{
					tplsss.setT_maritalLeaveStatistics(tplsss.getT_maritalLeaveStatistics()+times);
				}
				else if("10".equals(plstate))
				{
					tplsss.setT_thesisStatistics(tplsss.getT_thesisStatistics()+times);
				}
				tplsss.setT_operatingPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
				//修改统计表数据
				personalLeaveService.updateTPersonalLeaveStatistics(tplsss);
			}
			else //没有添加统计表数据
			{
				TPersonalLeaveStatistics tpls=new TPersonalLeaveStatistics();
				tpls.setT_daptId(deptId);//部门ID
				tpls.setT_userId(userId);//用户ID
				//根据类型减去请假时间 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假 9.婚假 10.论文假
				if("1".equals(plstate))
				{
					tpls.setT_leaveStatistics(times);
					tpls.setT_leaveCountStatistics(1);
				}
				else
				{
					tpls.setT_leaveStatistics(0);
					tpls.setT_leaveCountStatistics(0);
				}
				if("2".equals(plstate))
				{
					tpls.setT_sickLeaveStatistics(times);
				}
				else
				{
					tpls.setT_sickLeaveStatistics(0);
				}
				if("3".equals(plstate))
				{
					tpls.setT_businessLeaveStatistics(times);
				}
				else
				{
					tpls.setT_businessLeaveStatistics(0);
				}
				if("4".equals(plstate))
				{
					tpls.setT_stringBreakLeaveStatistics(times);
				}
				else
				{
					tpls.setT_stringBreakLeaveStatistics(0);
				}
			    if("5".equals(plstate))
				{
					tpls.setT_funeralLeaveStatistics(times);
				}
			    else
			    {
			    	tpls.setT_funeralLeaveStatistics(0);
			    }
				if("6".equals(plstate))
				{
					tpls.setT_drivingLeaveStatistics(times);
				}
				else
				{
					tpls.setT_drivingLeaveStatistics(0);
				}
				if("7".equals(plstate))
				{
					tpls.setT_maternityLeaveStatistics(times);
				}
				else
				{
					tpls.setT_maternityLeaveStatistics(0);
				}
				if("8".equals(plstate))
				{
					tpls.setT_lactationLeaveStatistics(times);
				}
				else
				{
					tpls.setT_lactationLeaveStatistics(0);
				}
				if("9".equals(plstate))
				{
					tpls.setT_maritalLeaveStatistics(times);
				}
				else
				{
					tpls.setT_maritalLeaveStatistics(0);
				}
				if("10".equals(plstate))
				{
					tpls.setT_thesisStatistics(times);
				}
				else
				{
					tpls.setT_thesisStatistics(0);
				}
				tpls.setT_leaveYear(year);//请假集合月份
				tpls.setT_leaveMonth(month);//请假集合月份
				tpls.setT_createPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
				//增加统计表数据
				personalLeaveService.saveTPersonalLeaveStatistics(tpls);
			}
			SaveLog(" 保存请假信息,请假人:"+curUser.getT_id()+"-"+curUser.getT_userName()+",请假日期:"+leaveDay+",请假时长:"+leaveDuration);
		}
		catch (NumberFormatException e)
		{
			log.error("PersonalLeaveAction 中的savePersonalLeave异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			log.error("PersonalLeaveAction 中的savePersonalLeave异常![" + e.getMessage() + "]", e);
			//在此捕获“异常”
			e.printStackTrace();
		}
		return queryAllPersonalLeaveList();
	}
	
	/**
	 * 跳转到请假修改页面
	 */
	public String showEditPersonalLeaveById()
	{
		try
		{
			String plId =getParam("plId");
			//通过请假Id查询请假信息
			TPersonalLeave tpl=new TPersonalLeave();
			tpl.setId(Integer.parseInt(plId));
			TPersonalLeave tpls=personalLeaveService.queryPersonalLeave(tpl);
			request.setAttribute("tpls", tpls);
		}
		catch (NumberFormatException e)
		{
			//在此捕获“异常”
			log.error("PersonalLeaveAction 中的showEditPersonalLeaveById异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			//在此捕获“异常”
			log.error("PersonalLeaveAction 中的showEditPersonalLeaveById异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return "edit";
	}
	
	/**
	 * 编辑保存请假数据
	 */
	public String editPersonalLeave()
	{
		try
		{
			//获取所有的增加字段
			String plId =getParam("plId");
			String plstate =getParam("plstate");//请假类型
			String oldplstate =getParam("oldplstate");//以前请假类型
			String leaveDay = getParam("leaveDay");//请假日期
			String oldleaveDay = getParam("oldleaveDay");//以前请假日期
			String leaveTimes =getParam("leaveTimes");//请假时间段
			String oldleaveTimes =getParam("oldleaveTimes");//以前请假时间段
			String leaveDuration = getParam("leaveDuration");
			String oldleaveDuration = getParam("oldleaveDuration");//以前的请假时长
			float times=Float.parseFloat(leaveDuration);//请假时长
			String leaveType =getParam("leaveType");//请假原因
			String isTwoApprover =getParam("isTwoApprover");//是否二级审批

			//通过请假Id查询请假信息
			TPersonalLeave tpl=new TPersonalLeave();
			tpl.setId(Integer.parseInt(plId));
			TPersonalLeave tpls=personalLeaveService.queryPersonalLeave(tpl);
			
			HttpSession session = request.getSession();//获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			int userId=curUser.getT_id();//用户ID 
			String deptId=curUser.getT_daptId();//用户部门ID
			if(null != tpls){
				tpls.setId(Integer.parseInt(plId));
				tpls.setDaptId(deptId);
				tpls.setUserId(userId);
				tpls.setState(Integer.parseInt(plstate));
				tpls.setLeaveTimes(leaveTimes);
				tpls.setLeaveDuration(times);
				tpls.setLeaveDay(DateFun.toDate(leaveDay));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sdf.parse(leaveDay);
				tpl.setLeaveWeekDay(DateFun.getWeek(date));
				tpls.setLeaveType(leaveType);
				tpls.setIsTwoApprover(isTwoApprover==null || "".equals(isTwoApprover) ? 0 : 1);
				tpls.setOperatingPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
				//修改请假
				personalLeaveService.editPersonalLeave(tpls);
			}
			if(!plstate.equals(oldplstate) || !leaveDay.equals(oldleaveDay) || !leaveTimes.equals(oldleaveTimes)  || !leaveDuration.equals(oldleaveDuration)){
				//先减去以前的请假时间
				//根据类型减去请假时间 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假 9.婚假
				TPersonalLeaveStatistics tplss=new TPersonalLeaveStatistics();
				String year=oldleaveDay.substring(0,4);//获取到当前月份
				String month=oldleaveDay.substring(5,7);//获取到当前月份
				tplss.setT_userId(userId);//用户ID
				tplss.setT_leaveYear(year);//请假集合年份
				tplss.setT_leaveMonth(month);//请假集合月份
				TPersonalLeaveStatistics tplsss=personalLeaveService.queryTPersonalLeaveStatistics(tplss);
				//这是有本月统计的情况 进行修改 判断是否有当月数据 没有添加 有就修改数据
				//根据类型减去请假时间 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假 9.婚假
				float oldtimes=Float.parseFloat(oldleaveDuration);//请假时长
				if("1".equals(oldplstate))
				{
					tplsss.setT_leaveStatistics(tplsss.getT_leaveStatistics()-oldtimes);
					tplsss.setT_leaveCountStatistics(tplsss.getT_leaveCountStatistics()-1);
				}
				else if("2".equals(oldplstate))
				{
					tplsss.setT_sickLeaveStatistics(tplsss.getT_sickLeaveStatistics()-oldtimes);
				}
				else if("3".equals(oldplstate))
				{
					tplsss.setT_businessLeaveStatistics(tplsss.getT_businessLeaveStatistics()-oldtimes);
				}
				else if("4".equals(oldplstate))
				{
					tplsss.setT_stringBreakLeaveStatistics(tplsss.getT_stringBreakLeaveStatistics()-oldtimes);
				}
				else if("5".equals(oldplstate))
				{
					tplsss.setT_funeralLeaveStatistics(tplsss.getT_funeralLeaveStatistics()-oldtimes);
				}
				else if("6".equals(oldplstate))
				{
					tplsss.setT_drivingLeaveStatistics(tplsss.getT_drivingLeaveStatistics()-oldtimes);
				}
				else if("7".equals(oldplstate))
				{
					tplsss.setT_maternityLeaveStatistics(tplsss.getT_maternityLeaveStatistics()-oldtimes);
				}
				else if("8".equals(oldplstate))
				{
					tplsss.setT_lactationLeaveStatistics(tplsss.getT_lactationLeaveStatistics()-oldtimes);
				}
				else if("9".equals(oldplstate))
				{
					tplsss.setT_maritalLeaveStatistics(tplsss.getT_maritalLeaveStatistics()-oldtimes);
				}
				else if("10".equals(oldplstate))
				{
					tplsss.setT_thesisStatistics(tplsss.getT_thesisStatistics()-oldtimes);
				}
				
				tplss.setT_operatingPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
				//修改统计表数据
				personalLeaveService.updateTPersonalLeaveStatistics(tplsss);
				
				//再加上修改后的新增时间
				String years=leaveDay.substring(0,4);//获取到当前月份
				String months=leaveDay.substring(5,7);//获取到当前月份
				//修改请假统计数据
				TPersonalLeaveStatistics tplssss=new TPersonalLeaveStatistics();
				tplssss.setT_userId(userId);//用户ID
				tplssss.setT_leaveYear(years);//请假集合年份
				tplssss.setT_leaveMonth(months);//请假集合月份
				TPersonalLeaveStatistics tplsssss=personalLeaveService.queryTPersonalLeaveStatistics(tplssss);
				//这是有本月统计的情况 进行修改 判断是否有当月数据 没有添加 有就修改数据
				if(null!=tplsssss)
				{
					//根据类型减去请假时间 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假 9.婚假
					if("1".equals(plstate))
					{
						tplsssss.setT_leaveStatistics(tplsssss.getT_leaveStatistics()+times);
						tplsssss.setT_leaveCountStatistics(tplsssss.getT_leaveCountStatistics()+1);
					}
					else if("2".equals(plstate))
					{
						tplsssss.setT_sickLeaveStatistics(tplsssss.getT_sickLeaveStatistics()+times);
					}
					else if("3".equals(plstate))
					{
						tplsssss.setT_businessLeaveStatistics(tplsssss.getT_businessLeaveStatistics()+times);
					}
					else if("4".equals(plstate))
					{
						tplsssss.setT_stringBreakLeaveStatistics(tplsssss.getT_stringBreakLeaveStatistics()+times);
					}
					else if("5".equals(plstate))
					{
						tplsssss.setT_funeralLeaveStatistics(tplsssss.getT_funeralLeaveStatistics()+times);
					}
					else if("6".equals(plstate))
					{
						tplsssss.setT_drivingLeaveStatistics(tplsssss.getT_drivingLeaveStatistics()+times);
					}
					else if("7".equals(plstate))
					{
						tplsssss.setT_maternityLeaveStatistics(tplsssss.getT_maternityLeaveStatistics()+times);
					}
					else if("8".equals(plstate))
					{
						tplsssss.setT_lactationLeaveStatistics(tplsssss.getT_lactationLeaveStatistics()+times);
					}
					else if("9".equals(plstate))
					{
						tplsssss.setT_maritalLeaveStatistics(tplsssss.getT_maritalLeaveStatistics()+times);
					}
					else if("10".equals(plstate))
					{
						tplsssss.setT_thesisStatistics(tplsssss.getT_thesisStatistics()+times);
					}
					
					tplsssss.setT_operatingPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
					//修改统计表数据
					personalLeaveService.updateTPersonalLeaveStatistics(tplsssss);
				}
				else //没有添加统计表数据
				{
					TPersonalLeaveStatistics tplssssss=new TPersonalLeaveStatistics();
					tplssssss.setT_daptId(deptId);//部门ID
					tplssssss.setT_userId(userId);//用户ID
					//根据类型减去请假时间 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假 9.婚假 10.论文假
					if("1".equals(plstate))
					{
						tplssssss.setT_leaveStatistics(times);
						tplssssss.setT_leaveCountStatistics(1);
					}
					else
					{
						tplssssss.setT_leaveStatistics(0);
						tplssssss.setT_leaveCountStatistics(0);
					}
					if("2".equals(plstate))
					{
						tplssssss.setT_sickLeaveStatistics(times);
					}
					else
					{
						tplssssss.setT_sickLeaveStatistics(0);
					}
					if("3".equals(plstate))
					{
						tplssssss.setT_businessLeaveStatistics(times);
					}
					else
					{
						tplssssss.setT_businessLeaveStatistics(0);
					}
					if("4".equals(plstate))
					{
						tplssssss.setT_stringBreakLeaveStatistics(times);
					}
					else
					{
						tplssssss.setT_stringBreakLeaveStatistics(0);
					}
				    if("5".equals(plstate))
					{
				    	tplssssss.setT_funeralLeaveStatistics(times);
					}
				    else
				    {
				    	tplssssss.setT_funeralLeaveStatistics(0);
				    }
					if("6".equals(plstate))
					{
						tplssssss.setT_drivingLeaveStatistics(times);
					}
					else
					{
						tplssssss.setT_drivingLeaveStatistics(0);
					}
					if("7".equals(plstate))
					{
						tplssssss.setT_maternityLeaveStatistics(times);
					}
					else
					{
						tplssssss.setT_maternityLeaveStatistics(0);
					}
					if("8".equals(plstate))
					{
						tplssssss.setT_lactationLeaveStatistics(times);
					}
					else
					{
						tplssssss.setT_lactationLeaveStatistics(0);
					}
					if("9".equals(plstate))
					{
						tplssssss.setT_maritalLeaveStatistics(times);
					}
					else
					{
						tplssssss.setT_maritalLeaveStatistics(0);
					}
					if("10".equals(plstate))
					{
						tplssssss.setT_thesisStatistics(times);
					}
					else
					{
						tplssssss.setT_thesisStatistics(0);
					}
					tplssssss.setT_leaveYear(years);//请假集合月份
					tplssssss.setT_leaveMonth(months);//请假集合月份
					tplssssss.setT_createPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
					//增加统计表数据
					personalLeaveService.saveTPersonalLeaveStatistics(tplssssss);
				}
			}
			SaveLog("修改请假信息,请假人:"+curUser.getT_id()+"-"+curUser.getT_userName()+",请假日期:"+leaveDay+",请假时长:"+leaveDuration);
		}
		catch (ParseException e)
		{
			log.error("PersonalLeaveAction 中的editPersonalLeave异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return queryAllPersonalLeaveList();
	}

	//查看请假统计
	public String showPersonalLeaveStatistics()
	{
		try
		{
			//获取所有的增加字段
			String year =getParam("year");
			String month =getParam("month");//请假类型
			//通过与用户信息查询该用户本月的请假统计情况
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(year==null)
			{
				 year=sdf.format(new Date()).substring(0,4);//获取到当前年份
			}
			
			if(month==null)
			{
				month=sdf.format(new Date()).substring(5,7);//获取到当前月份
			}
			
			HttpSession session = request.getSession();//获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			
			TPersonalLeaveStatistics tpls=new TPersonalLeaveStatistics();
			tpls.setT_userId(curUser.getT_id());//用户ID
			tpls.setT_leaveYear(year);//请假集合月份
			tpls.setT_leaveMonth(month);//请假集合月份
			TPersonalLeaveStatistics tplss=personalLeaveService.queryTPersonalLeaveStatistics(tpls);
				
			float leaveCountStatisticsTime=0;//请假次数查过三次 乘以5
			float leaveHourStatisicsTime=0;//请假小时数超过8.5部分乘以2
			float maxStatisics=0;//事假最大罚时
			float funeralLeaveStatisticsTime=0; //丧假统计
			float maritalLeaveStatisticsTime=0; //婚假统计
			float drivingLeaveStatisticsTime=0;//驾校假统计
			float drivingLeaveStatisticsAllTime=0;//全部驾校假统计=(已累计的驾校假统计+本月的驾校假统计)
			float thesisStatisticsTime=0;//论文假统计
			float thesisStatisticsAllTime=0;//全部论文假统计=(已累计的驾校假统计+本月的驾校假统计)

			if(tplss==null) tplss=new TPersonalLeaveStatistics();
			//统计请假合计数据  统计请假罚时事假的小时数  是否翻倍   在翻倍统计完成后 加上其他请假的时间 
			if(tplss.getT_leaveCountStatistics()>3)
			{
				leaveCountStatisticsTime=tplss.getT_leaveStatistics()*5;
			}
			if(tplss.getT_leaveStatistics()>8.5)
			{
				leaveHourStatisicsTime=(float) ((tplss.getT_leaveStatistics()-8.5)*2+8.5);
			}
			maxStatisics=leaveCountStatisticsTime>leaveHourStatisicsTime?leaveCountStatisticsTime:leaveHourStatisicsTime;//统计事假总共罚时
			maxStatisics=tplss.getT_leaveStatistics()>maxStatisics?tplss.getT_leaveStatistics():maxStatisics;//统计事假总共罚时
			//统计合计扣时    丧假加上X-8.5*3  婚假 Y-8.5*10  +产假 +哺乳假的小时 +驾校假
			funeralLeaveStatisticsTime=(float) (tplss.getT_funeralLeaveStatistics()-8.5*3>0?(tplss.getT_funeralLeaveStatistics()-8.5*3):0);
			maritalLeaveStatisticsTime=(float) (tplss.getT_maritalLeaveStatistics()-8.5*10>0?(tplss.getT_maritalLeaveStatistics()-8.5*10):0);

			//查询本人某年某月之前所有的驾校假小时数 部分时间段限制 
			drivingLeaveStatisticsTime=personalLeaveService.queryAllDrivingLeaveStatisticsTimes(curUser.getT_id(),year,month);

			//如果历史驾校假大于等于85,那么本月的请假乘2加入到罚时中
			//如果历史的小于85但是加上本月的小时数超过了85 那么超过的部分 乘2罚时  在一个月中请了85个小时
			//如果历史的加上当月的 都没超过85  那么 只加上本月的小时数 不罚时
			if(drivingLeaveStatisticsTime>85)
			{
				drivingLeaveStatisticsAllTime=(tplss.getT_drivingLeaveStatistics()*2);
			}
			else
			{
				//判断一下  因为有可能默认一次性请假到85小时  那么累计查询的  和本月请的就是一样的 那么就会按照双被扣罚了
				
				if(drivingLeaveStatisticsTime+tplss.getT_drivingLeaveStatistics()>85)
				{
					float he1=(drivingLeaveStatisticsTime+tplss.getT_drivingLeaveStatistics()-85);//合计减去85小时 超出翻倍的部分
					drivingLeaveStatisticsAllTime=he1*2+tplss.getT_drivingLeaveStatistics()-he1;//超过85小时的翻倍  然后减去
				}
				else
				{
					drivingLeaveStatisticsAllTime=tplss.getT_drivingLeaveStatistics();
				}
			}
			
			//查询本人所有的论文假小时数 部分时间段限制 
			thesisStatisticsTime=personalLeaveService.queryAllThesisStatisticsTimes(curUser.getT_id(), year, month);
			
			//如果历史论文假大于等于85,那么本月的请假乘2加入到罚时中
			//如果历史的小于85但是加上本月的小时数超过了85 那么超过的部分 乘2罚时
			//如果历史的加上当月的 都没超过85  那么 只加上本月的小时数 不罚时
			if(thesisStatisticsTime>85)
			{
				thesisStatisticsAllTime=(tplss.getT_thesisStatistics()*2);
			}
			else
			{
				if(thesisStatisticsTime+tplss.getT_thesisStatistics()>85)
				{
					float he2=(thesisStatisticsTime+tplss.getT_thesisStatistics()-85);//合计减去85小时 超出翻倍的部分
					thesisStatisticsAllTime=he2*2+tplss.getT_thesisStatistics()-he2;//超过85小时的翻倍  然后减去
				}
				else
				{
					thesisStatisticsAllTime=tplss.getT_thesisStatistics();
				}
			}
			
			//?需要最终进行罚时的论文假与事假进行最终的翻倍罚时
			//tplss.getT_drivingLeaveStatistics()+tplss.getT_thesisStatistics();
			//本月所有请假小时数 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假 9.婚假  10.论文假   (包含 驾校假与论文假 最终罚时）
			tplss.setT_leaveHoursStatisics(maxStatisics+tplss.getT_sickLeaveStatistics()+funeralLeaveStatisticsTime+tplss.getT_maternityLeaveStatistics()+tplss.getT_lactationLeaveStatistics()+ maritalLeaveStatisticsTime+drivingLeaveStatisticsAllTime+thesisStatisticsAllTime);
		
			tplss.setLeaveCountStatisticsTime(leaveCountStatisticsTime);//请假次数查过三次 乘以5
			tplss.setLeaveHourStatisicsTime(leaveHourStatisicsTime);//请假小时数超过8.5部分乘以2
			tplss.setMaxStatisics(maxStatisics);//事假最大罚时
			
			//查询数据所有存在的年份 
			List<String> listYears=personalLeaveService.queryTPersonalLeaveStatisticsYear();
			if(listYears.size()==0)
			{
				listYears.add(sdf.format(new Date()).substring(0,4));//获取到当前年份
			}
			request.setAttribute("listYears", listYears);//默认查询本月的
			request.setAttribute("year", year);//默认查询本月的
			request.setAttribute("month", month);//默认查询本月的
			request.setAttribute("tplss", tplss);//请假统计获得 没有请假就没有请假统计数据 显示本月暂无请假
			request.setAttribute("drivingLeaveStatisticsTime", drivingLeaveStatisticsTime);//查询本人所有的驾校假小时数 部分时间段限制
			request.setAttribute("thesisStatisticsTime", thesisStatisticsTime);//查询本人所有的论文假小时数 部分时间段限制
		 }
		catch (ParseException e)
		{
			log.error("PersonalLeaveAction 中的showPersonalLeaveStatistics异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return "view";
	}
	
	//跳转到一级审核请假页面
	public String oneStageAuditOfLeave()
	{
		try
		{
			String leavaUser =getParam("leavaUser");//请假人
			String daptId =getParam("daptId");//部门
			//根据部门查询该部门中请假的未通过审核的人数 ，查询一级审核状态为0 或 一级审核人姓名为空的所有人 
			HttpSession session = request.getSession();//获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			String dapts=curUser.getT_daptId();
			//或取到登陆人的部门
			//循环部门中的所有信息查询,*,来检索所有的部门信息
			String[] dapt=dapts.split(",");
			List<TPersonalLeave> listTp=personalLeaveService.queryPersonalLeavesForAudit(dapt,1,leavaUser,daptId);//审核人的部门集合id  审核级别 1为一级审核 2为二级审核
			request.setAttribute("listTp", listTp);//请假待审集合
			request.setAttribute("leavaUser", leavaUser);//请假待审集合
			request.setAttribute("daptId", daptId);//请假待审集合
		}
		catch (ParseException e)
		{
			log.error("PersonalLeaveAction 中的oneStageAuditOfLeave异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return "audit";
	}

	//跳转到一级审核请假成功
	public String personalLeaveAuditSuccess()
	{
		try
		{
			String plId =getParam("plId");
			//通过请假Id 修改请假的审核人信息与状态
			TPersonalLeave tpl=new TPersonalLeave();
			tpl.setId(Integer.parseInt(plId));
			TPersonalLeave tpls=personalLeaveService.queryPersonalLeave(tpl);
			
			HttpSession session = request.getSession();//获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			
			if(null != tpls){
				SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
				TPersonalLeave tplss=new TPersonalLeave();
				tplss.setId(Integer.parseInt(plId));
				tplss.setOneApprover(curUser.getT_userName()+"/"+sdf.format(new Date()));
				tplss.setOperatingPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
				//修改请假一级审核状态
				personalLeaveService.editPersonalLeaveOfOneAudit(tplss);
			}
		}
		catch (NumberFormatException e)
		{
			log.error("PersonalLeaveAction 中的personalLeaveAuditSuccess异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			log.error("PersonalLeaveAction 中的personalLeaveAuditSuccess异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return oneStageAuditOfLeave();
	}

	//跳转到二级审核请假页面
	public String twoStageAuditOfLeave()
		{
			try
			{
				String leavaUser =getParam("leavaUser");//请假人
				String daptId =getParam("daptId");//部门
				List<TDepartment> listDapt = departmentService.queryAllDepart();//所有部门
				//根据部门查询该部门中请假的未通过审核的人数 ，查询一级审核状态为0 或 一级审核人姓名为空的所有人 
				//或取到登陆人的部门
				//循环部门中的所有信息查询,*,来检索所有的部门信息
				List<TPersonalLeave> listTp=personalLeaveService.queryPersonalLeavesForAudit(null,2,leavaUser,daptId);//审核人的部门集合id  审核级别 1为一级审核 2为二级审核
				request.setAttribute("listDapt", listDapt);//部门集合
				request.setAttribute("listTp", listTp);//请假待审集合
				request.setAttribute("leavaUser", leavaUser);//请假待审集合
				request.setAttribute("daptId", daptId);//请假待审集合
			}
			catch (ParseException e)
			{
				log.error("PersonalLeaveAction 中的twoStageAuditOfLeave异常![" + e.getMessage() + "]", e);
				e.printStackTrace();
			}
			return "audit2";
		}
		
    //跳转到二级审核请假成功
	public String personalLeaveTwoAuditSuccess()
		{
			try
			{
				String plId =getParam("plId");
				//通过请假Id 修改请假的审核人信息与状态
				TPersonalLeave tpl=new TPersonalLeave();
				tpl.setId(Integer.parseInt(plId));
				TPersonalLeave tpls=personalLeaveService.queryPersonalLeave(tpl);
				HttpSession session = request.getSession();//获取session中当前登陆的人
				TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
				
				if(null != tpls){
					SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
					TPersonalLeave tplss=new TPersonalLeave();
					tplss.setId(Integer.parseInt(plId));
					tplss.setTwoApprover(curUser.getT_userName()+"/"+sdf.format(new Date()));
					tplss.setOperatingPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
					//修改请假二级审核状态
					personalLeaveService.editPersonalLeaveOfTwoAudit(tplss);
				}
			}
			catch (NumberFormatException e)
			{
				log.error("PersonalLeaveAction 中的personalLeaveTwoAuditSuccess异常![" + e.getMessage() + "]", e);
				e.printStackTrace();
			}
			catch (ParseException e)
			{
				log.error("PersonalLeaveAction 中的personalLeaveTwoAuditSuccess异常![" + e.getMessage() + "]", e);
				e.printStackTrace();
			}
			return twoStageAuditOfLeave();
		}
	
		/**
		 * 查询全部人员的请假合计记录
		 */
	public  String  showPersonalLeaveAllStatistics(){
		try
		{
			log.info("查询全部人员的请假合计记录----------------");
			String leavaUser =getParam("leavaUser");//请假人
			String daptId =getParam("daptId");//部门
			String year =getParam("year");
			String month =getParam("month");//请假类型
			//通过与用户信息查询该用户本月的请假统计情况
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(year==null)
			{
				 year=sdf.format(new Date()).substring(0,4);//获取到当前年份
			}
			
			if(month==null)
			{
				month=sdf.format(new Date()).substring(5,7);//获取到当前月份
			}
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 20 : Integer.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);
			//查询数据
			List<TPersonalLeaveStatistics> list = personalLeaveService.showPersonalLeaveAllStatistics(leavaUser,daptId,year,month,page);//查询用户请假合计的分页

			if(null!=list && list.size()>0)
			{
				for(TPersonalLeaveStatistics tpls:list)
				{
					//统计请假罚时
					float leaveCountStatisticsTime=0;//请假次数查过三次 乘以5
					float leaveHourStatisicsTime=0;//请假小时数超过8.5部分乘以2
					float maxStatisics=0;//事假最大罚时
					float funeralLeaveStatisticsTime=0; //丧假统计
					float maritalLeaveStatisticsTime=0; //婚假统计
					float drivingLeaveStatisticsTime=0;//驾校假统计
					float drivingLeaveStatisticsAllTime=0;//全部驾校假统计=(已累计的驾校假统计+本月的驾校假统计)
					float thesisStatisticsTime=0;//论文假统计
					float thesisStatisticsAllTime=0;//全部论文假统计=(已累计的驾校假统计+本月的驾校假统计)
					if(tpls!=null)
					{
						//统计请假合计数据  统计请假罚时事假的小时数  是否翻倍   在翻倍统计完成后 加上其他请假的时间 
						if(tpls.getT_leaveCountStatistics()>3)
						{
							leaveCountStatisticsTime=tpls.getT_leaveStatistics()*5;
						}
						if(tpls.getT_leaveStatistics()>8.5)
						{
							leaveHourStatisicsTime=(float) ((tpls.getT_leaveStatistics()-8.5)*2+8.5);
						}
						maxStatisics=leaveCountStatisticsTime>leaveHourStatisicsTime?leaveCountStatisticsTime:leaveHourStatisicsTime;//统计事假总共罚时
						maxStatisics=tpls.getT_leaveStatistics()>maxStatisics?tpls.getT_leaveStatistics():maxStatisics;//统计事假总共罚时
						//统计合计扣时    丧假加上X-8.5*3  婚假 Y-8.5*10  +产假 +哺乳假的小时 +驾校假 +论文假
						funeralLeaveStatisticsTime=(float) (tpls.getT_funeralLeaveStatistics()-8.5*3>0?(tpls.getT_funeralLeaveStatistics()-8.5*3):0);
						maritalLeaveStatisticsTime=(float) (tpls.getT_maritalLeaveStatistics()-8.5*10>0?(tpls.getT_maritalLeaveStatistics()-8.5*10):0);
						
						//查询本人本月以前的驾校假小时数 部分时间段限制 
						drivingLeaveStatisticsTime=personalLeaveService.queryAllDrivingLeaveStatisticsTimes(tpls.getT_userId(),year,month);

						//如果历史驾校假大于等于85,那么本月的请假乘2加入到罚时中
						//如果历史的小于85但是加上本月的小时数超过了85 那么超过的部分 乘2罚时
						//如果历史的加上当月的 都没超过85  那么 只加上本月的小时数 不罚时
						if(drivingLeaveStatisticsTime>85)
						{
							drivingLeaveStatisticsAllTime=(tpls.getT_drivingLeaveStatistics()*2);
						}
						else
						{
							//判断一下  因为有可能默认一次性请假到85小时  那么累计查询的  和本月请的就是一样的 那么就会按照双被扣罚了
							
							if(drivingLeaveStatisticsTime+tpls.getT_drivingLeaveStatistics()>85)
							{
								float he1=(drivingLeaveStatisticsTime+tpls.getT_drivingLeaveStatistics()-85);//合计减去85小时 超出翻倍的部分
								drivingLeaveStatisticsAllTime=he1*2+tpls.getT_drivingLeaveStatistics()-he1;//超过85小时的翻倍  然后减去
							}
							else
							{
								drivingLeaveStatisticsAllTime=tpls.getT_drivingLeaveStatistics();
							}
						}
						
						//查询本人所有的论文假小时数 部分时间段限制 
						thesisStatisticsTime=personalLeaveService.queryAllThesisStatisticsTimes(tpls.getT_userId(), year, month);
						
						//如果历史论文假大于等于85,那么本月的请假乘2加入到罚时中
						//如果历史的小于85但是加上本月的小时数超过了85 那么超过的部分 乘2罚时
						//如果历史的加上当月的 都没超过85  那么 只加上本月的小时数 不罚时
						if(thesisStatisticsTime>85)
						{
							thesisStatisticsAllTime=(tpls.getT_thesisStatistics()*2);
						}
						else
						{
							if(thesisStatisticsTime+tpls.getT_thesisStatistics()>85)
							{
								float he2=(thesisStatisticsTime+tpls.getT_thesisStatistics()-85);//合计减去85小时 超出翻倍的部分
								thesisStatisticsAllTime=he2*2+tpls.getT_thesisStatistics()-he2;//超过85小时的翻倍  然后减去
							}
							else
							{
								thesisStatisticsAllTime=tpls.getT_thesisStatistics();
							}
						}
						
						//本月所有请假小时数 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假 9.婚假  10.论文假
						tpls.setT_leaveHoursStatisics(maxStatisics+tpls.getT_sickLeaveStatistics()+funeralLeaveStatisticsTime+tpls.getT_maternityLeaveStatistics()+tpls.getT_lactationLeaveStatistics()+ maritalLeaveStatisticsTime+drivingLeaveStatisticsAllTime+thesisStatisticsAllTime);
						tpls.setLeaveCountStatisticsTime(leaveCountStatisticsTime);//请假次数查过三次 乘以5
						tpls.setLeaveHourStatisicsTime(leaveHourStatisicsTime);//请假小时数超过8.5部分乘以2
						tpls.setMaxStatisics(maxStatisics);//事假最大罚时
						
						//统计请假审核情况
						//一级请假情况  二级请假情况 用户id  请假年份  月份  级别
						int oneApproverState=personalLeaveService.queryApproverState(tpls.getT_userId(),tpls.getT_leaveYear(),tpls.getT_leaveMonth(),1);
						int twoApproverState=personalLeaveService.queryApproverState(tpls.getT_userId(),tpls.getT_leaveYear(),tpls.getT_leaveMonth(),2);	
						tpls.setOneApproverState(oneApproverState);
						tpls.setTwoApproverState(twoApproverState);
					}
				}
				
			}
			//查询数据所有存在的年份 
			List<String> listYears=personalLeaveService.queryTPersonalLeaveStatisticsYear();
			if(listYears.size()==0)
			{
				listYears.add(sdf.format(new Date()).substring(0,4));//获取到当前年份
			}
			List<TDepartment> listDapt = departmentService.queryAllDepart();//所有部门
			request.setAttribute("listDapt", listDapt);//部门集合
			SaveLog("查询全部人员的请假合计记录");
			request.setAttribute("listYears", listYears);
			request.setAttribute("leavaUser", leavaUser);
			request.setAttribute("daptId", daptId);
			request.setAttribute("year", year);
			request.setAttribute("month", month);
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			return "viewAll";
		}
		catch (Exception  e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("PersonalLeaveAction 中的showPersonalLeaveAllStatistics异常![" + e.getMessage() + "]", e);
			return ERROR;
		}
	}

	/**
	 * 通过id与年/月查询时间段内本人请假记录
	 */
	public  String  showPersonalLeaveById(){
		try
		{
			log.info("通过id与年/月查询时间段内本人请假记录----------------");
			String plstate =getParam("plstate");//请假类型
			String userId =getParam("userId");//用户ID
			String year =getParam("year");//年
			String month =getParam("month");//月
			//查询数据
			List<TPersonalLeave> list = personalLeaveService.showPersonalLeaveById(plstate,userId,year,month);//查询用户请假的分页
			SaveLog("通过id与年/月查询时间段内本人请假记录,"+userId+","+year+"-"+month);
			request.setAttribute("plstate", plstate);
			request.setAttribute("userId", userId);
			request.setAttribute("year", year);
			request.setAttribute("month", month);
			request.setAttribute("list", list);
			return "show";
		}
		catch (Exception  e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("PersonalLeaveAction 中的showPersonalLeaveById异常![" + e.getMessage() + "]", e);
			return ERROR;
		}
	}
	
	/**
	 * 跳转到请假统计修改页面
	 */
	public String showPersonalLeaveStatisticsById()
	{
		try
		{
			log.info("跳转到请假统计修改页面----------------");
			String userId =getParam("userId");//用户ID
			String id =getParam("id");//ID
			String year =getParam("year");//年
			String month =getParam("month");//月
//			HttpSession session = request.getSession();//获取session中当前登陆的人
//			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			//通过请假Id查询请假信息
			TPersonalLeaveStatistics tpls=new TPersonalLeaveStatistics();
			tpls.setT_id(Integer.parseInt(id));
			tpls.setT_userId(Integer.parseInt(userId));
			tpls.setT_leaveYear(year);
			tpls.setT_leaveMonth(month);
//			tpls.setT_operatingPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
			TPersonalLeaveStatistics tplss=personalLeaveService.queryTPersonalLeaveStatistics(tpls);
			request.setAttribute("tplss", tplss);
			request.setAttribute("userId", userId);
			request.setAttribute("year", year);
			request.setAttribute("month", month);
			request.setAttribute("id", id);
		}
		catch (NumberFormatException e)
		{
			//在此捕获“异常”
			log.error("PersonalLeaveAction 中的showPersonalLeaveStatisticsById异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			//在此捕获“异常”
			e.printStackTrace();
		}
		return "edit";
	}

	/**
	 * 编辑修改月违规罚款合计数据
	 */
	public String editPersonalLeaveStatistics()
	{
		try
		{
			//获取所有的增加字段
			String userId =getParam("userId");//用户ID
			String id =getParam("id");//ID
			String year =getParam("year");//年
			String month =getParam("month");//月
			String monthViolationFineAggregate =getParam("monthViolationFineAggregate");//罚款合计
			String remarks =getParam("remarks");//罚款备注

			//通过请假Id查询请假信息
			TPersonalLeaveStatistics tpls=new TPersonalLeaveStatistics();
			tpls.setT_id(Integer.parseInt(id));
			tpls.setT_userId(Integer.parseInt(userId));
			tpls.setT_leaveYear(year);
			tpls.setT_leaveMonth(month);
			TPersonalLeaveStatistics tplss=personalLeaveService.queryTPersonalLeaveStatistics(tpls);
			
			HttpSession session = request.getSession();//获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			
			if(null != tplss){
				if("".equals(monthViolationFineAggregate) || null==monthViolationFineAggregate)
				{
					tplss.setT_monthViolationFineAggregate(0);
				}
				else
				{
					tplss.setT_monthViolationFineAggregate(Float.parseFloat(monthViolationFineAggregate));
				}
				tplss.setT_remarks(remarks);
				tplss.setT_operatingPersonnel(curUser.getT_id()+"-"+curUser.getT_userName());
				//修改请假
				personalLeaveService.editPersonalLeaveStatistics(tplss);
			}
			
			SaveLog("修改月违规罚款合计数据,请假人:"+curUser.getT_id()+"-"+curUser.getT_userName());
		}
		catch (ParseException e)
		{
			log.error("PersonalLeaveAction 中的editPersonalLeaveStatistics异常![" + e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return showPersonalLeaveAllStatistics();
	}
	
	
	/**
	 * 查询某时间段的本人请假记录
	 */
	public  String  queryAllUserPersonalLeaveList(){
		try
		{
			log.info("查询某时间段的本人请假记录----------------");
			String userId =getParam("numId");//请假用户num
			String leavaUser =getParam("leavaUser");//请假用户名
			String plstate =getParam("plstate");//请假类型
			String startDate =getParam("startDate");//开始时间
			String endDate =getParam("endDate");//结束时间/
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 40 : Integer.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
			List<TPersonalLeave> list = personalLeaveService.queryAllPersonalLeaveList(plstate,startDate,endDate,userId,leavaUser,page);//查询用户请假的分页
			SaveLog("查询某时间段的请假记录,"+curUser.getT_id()+"-"+curUser.getT_userName());
			// 查询所有的考勤人员名称
			List<TAttendancePeople> tapList = attendanceService.queryAllAttendancePeople();
			//明天把数据放到界面上展示
			request.setAttribute("numId", userId);
			request.setAttribute("leavaUser", leavaUser);
			request.setAttribute("plstate", plstate);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("list", list);
			request.setAttribute("tapList", tapList);
			request.setAttribute("page", page);
			return "viewAllUser";
		}
		catch (Exception  e)
		{
			//在此捕获“异常”
			e.printStackTrace();
			log.error("PersonalLeaveAction 中的queryAllUserPersonalLeaveList异常![" + e.getMessage() + "]", e);
			return ERROR;
		}
	}
	
	public TPersonalLeaveService getPersonalLeaveService()
	{
		return personalLeaveService;
	}
	public void setPersonalLeaveService(TPersonalLeaveService personalLeaveService)
	{
		this.personalLeaveService = personalLeaveService;
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
	public TDepartmentService getDepartmentService()
	{
		return departmentService;
	}
	public void setDepartmentService(TDepartmentService departmentService)
	{
		this.departmentService = departmentService;
	}
	public TAttendanceService getAttendanceService()
	{
		return attendanceService;
	}
	public void setAttendanceService(TAttendanceService attendanceService)
	{
		this.attendanceService = attendanceService;
	}
	
	
}
