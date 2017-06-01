package com.tywh.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.tywh.orm.TAttendance;
import com.tywh.orm.TAttendancePeople;
import com.tywh.orm.TDepartment;
import com.tywh.orm.TPersonalLeave;
import com.tywh.orm.TPersonalLeaveStatistics;
import com.tywh.orm.TSuperModel;
import com.tywh.orm.TUserinfo;
import com.tywh.service.TAttendanceService;
import com.tywh.service.TDepartmentService;
import com.tywh.service.TLogService;
import com.tywh.service.TPersonalLeaveService;
import com.tywh.service.TUserInfoService;
import com.tywh.util.DateFun;
import com.tywh.util.Page;
import com.tywh.util.PinYin;

/**
 * AttendanceAction 考勤管理 author：杜泉 2014-11-3 上午10:18:11
 */
public class AttendanceAction extends ActionSupport
{
	private static final long		serialVersionUID	= -8685225614457144036L;
	private static Log				log					= LogFactory
																.getLog(AttendanceAction.class);
	private TAttendanceService		attendanceService;
	private TPersonalLeaveService	personalLeaveService;
	private TDepartmentService departmentService;
	private TUserInfoService		userInfoService;
	private TLogService				logService;
	HttpServletRequest				request				= ServletActionContext
																.getRequest();
	HttpServletResponse				response			= ServletActionContext
																.getResponse();

	protected String getParam(String key)
	{
		if (ServletActionContext.getRequest().getParameter(key) != null)
		{
			return ServletActionContext.getRequest().getParameter(key).trim();
		}
		else
		{
			return ServletActionContext.getRequest().getParameter(key);
		}
	}

	protected String[] getParams(String key)
	{
		return ServletActionContext.getRequest().getParameterValues(key);
	}

	/**
	 * 记录日志
	 */
	public void SaveLog(String content)
	{
		try
		{
			HttpSession session = request.getSession();// 获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session
					.getAttribute("curManagUser");
			logService.saveLog(content,
					curUser.getT_id() + "-" + curUser.getT_userName());
		}
		catch (ParseException e)
		{
			log.error("AttendanceAction 中的SaveLog异常![" + e.getMessage() + "]",
					e);
			e.printStackTrace();
		}
	}

	/**
	 * 查询我的考勤记录
	 */
	public String queryAttendanceList()
	{
		try
		{
			log.info("查询我的考勤记录----------------");
			String startDate = getParam("startDate");// 开始时间
			String endDate = getParam("endDate");// 结束时间
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 60 : Integer
					.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer
					.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (null == startDate || "".equals(startDate))
			{
				Calendar cal = Calendar.getInstance();// 获取当前日期
				cal.add(Calendar.MONTH, 0);
				cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				startDate = sdf.format(cal.getTime());
			}

			if (null == endDate || "".equals(endDate))
			{
				Calendar ca2 = Calendar.getInstance();
				ca2.set(Calendar.DAY_OF_MONTH,
						ca2.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = sdf.format(ca2.getTime());
			}

			HttpSession session = request.getSession();// 获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session
					.getAttribute("curManagUser");
			// 查询数据
			List<TAttendance> list = attendanceService.queryAttendanceList(startDate, endDate, curUser.getT_userName(), null, null,page);// 查询用户考勤的分页
			SaveLog("查询某时间段的考勤记录," + curUser.getT_id() + "-"
					+ curUser.getT_userName());

			//
			if (null != list && list.size() > 0)
			{
				for (TAttendance ta : list)
				{
					// 考勤日期 根据考勤日期查询当日的请假情况
					Date date = ta.getaDate();
					TPersonalLeave tpls = new TPersonalLeave();
					tpls.setLeaveDay(date);// 考勤日期
					tpls.setUserId(curUser.getT_id());// 当前登陆人的Id
					List<TPersonalLeave> ltpl = personalLeaveService
							.queryPersonalLeaves(tpls);
					StringBuffer sb = new StringBuffer();
					if (null != ltpl && ltpl.size() > 0)
					{
						// 循环请假事项 保存到当前考勤数据中
						for (TPersonalLeave tpl : ltpl)
						{
							// 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假
							// 9.婚假
							if (1 == tpl.getState())
								sb.append("事假:");
							if (2 == tpl.getState())
								sb.append("病假:");
							if (3 == tpl.getState())
								sb.append("公出:");
							if (4 == tpl.getState())
								sb.append("串休:");
							if (5 == tpl.getState())
								sb.append("丧假:");
							if (6 == tpl.getState())
								sb.append("驾校假:");
							if (7 == tpl.getState())
								sb.append("产假:");
							if (8 == tpl.getState())
								sb.append("哺乳假:");
							if (9 == tpl.getState())
								sb.append("婚假:");
							if (10 == tpl.getState())
								sb.append("论文假:");
							sb.append(tpl.getLeaveDuration() + "小时  ");
						}
					}
					ta.setPersonalLeaveDetailed(sb.toString());
				}
			}
			// 明天把数据放到界面上展示
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("list", list);
			request.setAttribute("page", page);
			return SUCCESS;
		}
		catch (Exception e)
		{
			// 在此捕获“异常”
			e.printStackTrace();
			log.error(
					"AttendanceAction 中的queryAttendanceList异常!["
							+ e.getMessage() + "]", e);
			return ERROR;
		}
	}

	/**
	 * 查询全部考勤记录
	 */
	public String queryAllAttendanceList()
	{
		try
		{
			log.info("查询全部考勤记录----------------");
			String numId = getParam("numId");// 人员编号
			String daptId =getParam("daptId");//部门
			String numName = getParam("numName");// 人员名称
			String startDate = getParam("startDate");// 开始时间
			String endDate = getParam("endDate");// 结束时间
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 60 : Integer
					.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer
					.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (null == startDate || "".equals(startDate))
			{
				Calendar cal = Calendar.getInstance();// 获取当前日期
				cal.add(Calendar.MONTH, 0);
				cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				startDate = sdf.format(cal.getTime());
			}

			if (null == endDate || "".equals(endDate))
			{
				Calendar ca2 = Calendar.getInstance();
				ca2.set(Calendar.DAY_OF_MONTH,
						ca2.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = sdf.format(ca2.getTime());
			}

			// 查询数据 如果name是一个人的名字就是单独查询一个人的考勤情况 如果为空就是不限制 全部
			List<TAttendance> list = attendanceService.queryAttendanceList(startDate, endDate, numName, numId,daptId, page);// 查询用户考勤的分页
			SaveLog("查询某时间段的考勤记录," + startDate + "-" + endDate + ",考勤人编号："
					+ numId + "-" + numName);

			//
			if (null != list && list.size() > 0)
			{
				for (TAttendance ta : list)
				{
					// 考勤日期 根据考勤日期查询当日的请假情况
					Date date = ta.getaDate();
					TPersonalLeave tpls = new TPersonalLeave();
					tpls.setLeaveDay(date);// 考勤日期
					tpls.setUserName(ta.getName());
					List<TPersonalLeave> ltpl = personalLeaveService
							.queryPersonalLeaves(tpls);
					StringBuffer sb = new StringBuffer();
					if (null != ltpl && ltpl.size() > 0)
					{
						// 循环请假事项 保存到当前考勤数据中
						for (TPersonalLeave tpl : ltpl)
						{
							// 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假
							// 9.婚假
							if (1 == tpl.getState())
								sb.append("事假:");
							if (2 == tpl.getState())
								sb.append("病假:");
							if (3 == tpl.getState())
								sb.append("公出:");
							if (4 == tpl.getState())
								sb.append("串休:");
							if (5 == tpl.getState())
								sb.append("丧假:");
							if (6 == tpl.getState())
								sb.append("驾校假:");
							if (7 == tpl.getState())
								sb.append("产假:");
							if (8 == tpl.getState())
								sb.append("哺乳假:");
							if (9 == tpl.getState())
								sb.append("婚假:");
							if (10 == tpl.getState())
								sb.append("论文假:");
							sb.append(tpl.getLeaveDuration() + "小时  ");
						}
					}
					ta.setPersonalLeaveDetailed(sb.toString());
				}
			}
			// 查询所有的考勤人员名称
			List<TAttendancePeople> tapList = attendanceService
					.queryAllAttendancePeople();
			List<TDepartment> listDapt = departmentService.queryAllDepart();//所有部门
			request.setAttribute("listDapt", listDapt);//部门集合
			// 明天把数据放到界面上展示
			request.setAttribute("daptId", daptId);
			request.setAttribute("numId", numId);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("list", list);
			request.setAttribute("tapList", tapList);
			request.setAttribute("page", page);
			return "allView";
		}
		catch (Exception e)
		{
			// 在此捕获“异常”
			e.printStackTrace();
			log.error(
					"AttendanceAction 中的queryAllAttendanceList异常!["
							+ e.getMessage() + "]", e);
			return ERROR;
		}
	}

	/**
	 * 跳转到导入考勤页面
	 */
	public String showImportAttendance()
	{
		return "import";
	}

	/**
	 * 导入excel到数据库中
	 */
	public String importAttendance()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 计算导入时间
		long start = new Date().getTime();
		String fileName = getParam("fileName");// 文件名称.
		if (fileName != null && !fileName.equals(""))
		{
			fileName = fileName.trim();
		}
		HttpSession session = request.getSession();// 获取session中当前登陆的人
		TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
		// 根据文件名称获取文件 解析文件
		List<TAttendance> list = new ArrayList<TAttendance>();
		Set<String> ns = new LinkedHashSet<String>();// 用于存储所有考勤人员名称
		try
		{
			Workbook rwb = Workbook.getWorkbook(new File(request
					.getRealPath("//upload") + "//excel//" + fileName));
			Sheet rs = rwb.getSheet(0);// 或者rwb.getSheet(0)
			int clos = rs.getColumns();// 得到所有的列
			int rows = rs.getRows();// 得到所有的行
			// 循环行中的数字
			for (int i = 1; i < rows - 1; i++)
			{
				for (int j = 0; j < clos; j++)
				{
					// 第一个是列数，第二个是行数
					TAttendance ta = new TAttendance();
					String num = rs.getCell(j++, i).getContents().trim();// 人员编号
					String name = rs.getCell(j++, i).getContents().trim();// 姓名
					String da = rs.getCell(j++, i).getContents();// 考勤日期
					String week = rs.getCell(j++, i).getContents();// 星期
					String workTime = rs.getCell(j++, i).getContents();// 班次
					String startWork = rs.getCell(j++, i).getContents();// 上班1
					String endWork = rs.getCell(j++, i).getContents();// 下班2
					String startWork2 = rs.getCell(j++, i).getContents();// 上班3
					String endWork2 = rs.getCell(j++, i).getContents();// 下班4
					j++;// 白班(时)
					j++;// 晚班(时)
					j++;// 应出勤(天)
					j++;// 实出勤(天)
					String plusWork = rs.getCell(j++, i).getContents();// 平时加班(时)
					String holidayWork = rs.getCell(j++, i).getContents();// 公休加班(时)
					j++;// 节日加班
					j++;// 事假(时)
					j++;// 病假(时)
					j++;// 出差(时)
					j++;// 外出(时)
					j++;// 外出次(次)
					j++;// 迟到(分)
					j++;// 早退(分)
					String absenceWork = rs.getCell(j++, i).getContents();// 缺勤(时)
					String c = rs.getCell(j++, i).getContents();// 缺勤次数(次)
					j++;// 应签到(次)
					j++;// 未签到(次)

					/**
					 * 以下为Excel中的数据转化到对象中
					 */
					ta.setNumId(num);// 人员编号
					ta.setName(name);// 姓名
					ns.add(num + "-" + name);// 把用户的编号-姓名保存到Set 集合 然后保存到数据库中
					Date date = null;// 考勤日期
					if (!da.equals(""))
					{
						date = sdf.parse(da);
						ta.setaDate(date);
					}
					else
					{
						ta.setaDate(null);// 考勤日期
					}
					ta.setWeekDay(week);// 星期
					ta.setWorkTime(workTime);// 班次
					ta.setStartWork(startWork);// 上班1
					ta.setEndWork(endWork);// 下班2
					ta.setStartWork2(startWork2);// 上班3
					ta.setEndWork2(endWork2);// 下班4
					// 判断是否有加班 加班时长
					// 1.统计每天的加班时长（周六日单独计算） 大于17：30 加班达到0.3小时的 记录 否则 为空
					// 2.判断星期 然后看一下是不是 周六周日 然后 分别计算周六周日的 加班时间 中间知否有串休0
					if (!week.equals("星期六") && !week.equals("星期日"))
					{
						Double zhi = DateFun.getTimeDifferenceCompare("17:30",
								endWork);
						if (zhi == 0.0)
							ta.setPlusWork("0");// 平时加班(时)
						else if (plusWork.equals("") && zhi < 0.3)
						{
							ta.setPlusWork("0");// 平时加班(时)
						}
						else if (plusWork.equals("") && zhi >= 0.3 && zhi < 0.8)
						{
							ta.setPlusWork(DateFun.getTimeDifferenceCompare(
									"17:30", endWork) + "");// 平时加班(时)
						}
						else
						{
							ta.setPlusWork(plusWork);// 平时加班(时)
						}
					}
					else if (week.equals("星期六") || week.equals("星期日"))
					{
						// 先判断是endWork 是否有晚加班情况 没有 在计算是否有少于4小时的情况
						Double zc = DateFun.getTimeDifferenceCompare("13:00",
								endWork);// 计算下午的加班时间 //计算晚5点半以后的加班时间
						Double dj = DateFun.getTimeDifferenceCompare("17:30",
								endWork);// 计算下午的加班时间 //计算晚5点半以后的加班时间
						if (zc == 0.0)
						{
							ta.setPlusWork("0");// 平时加班(时)
						}
						else if (zc <= 4)
						{
							ta.setPlusWork(zc + "");// 平时加班(时)
						}
						else if (zc > 4 && zc < 4.8) // zc大于4.8 自然dj>=0.3以上了
														// 所以只要 dj有值那么
														// 就是晚间已经在加班了
						{
							ta.setPlusWork("4");// 平时加班(时)
						}
						else
						{
							ta.setPlusWork((4 + dj) + "");// 平时加班(时)
						}
					}
					ta.setHolidayWork(holidayWork);// 公休加班(时)
					ta.setAbsenceWork(absenceWork);// 缺勤(时)

					int count = 0;
					if (!c.equals(""))
					{
						count = Integer.parseInt(c);
						ta.setAbsenceCount(count);// 缺勤次数(次)
					}
					else
					{
						ta.setAbsenceCount(0);// 缺勤次数(次)
					}

					// 判断早晚班是否正常出勤
					// 判断早上是否未打卡
					if (!week.equals("星期日"))
					{
						if (startWork.equals(""))
						{
							ta.setNoEarlyPunch(1);// noEarlyPunch;//无早打卡 0为否 1为是
						}
						else
						{
							ta.setNoEarlyPunch(0);// noEarlyPunch;//无早打卡 0为否 1为是
						}
						// 判断晚上是否未打卡
						if (endWork.equals("")
								&& (endWork.equals("") && endWork2.equals("")))
						{
							ta.setNoLatePunch(1);// noLatePunch;//无晚打卡 0为否 1为是
						}
						else
						{
							ta.setNoLatePunch(0);// noLatePunch;//无晚打卡 0为否 1为是
						}
					}
					else
					{
						ta.setNoLatePunch(0);// noEarlyPunch;//无早打卡 0为否 1为是
						ta.setNoEarlyPunch(0);// noEarlyPunch;//无早打卡 0为否 1为是
					}
					// 判断是否有满勤与夜宵补助 然后设置ta考勤数据
					// 判断夜宵补助 下班时间 是否大于22:00 如果大于设置1
					if (DateFun.getHourCompare(22, endWork))
					{
						ta.setSupper(1);// Supper夜宵补助 0为否 1为是
					}
					else
					{
						ta.setSupper(0);// Supper夜宵补助 0为否 1为是
					}
					// 是否当前满勤符合 这里需要判断天是否请假？ 还是后期计算满勤奖的时候进行计算
					// 因为这里进行判断 会很慢 100个人就需要 100*30天次数据库访问
					if ((DateFun.getTimeCompare("8:25", startWork) && DateFun.getTimeCompare(endWork, "17:00")) )
					{
						ta.setWorkFullHours(1);// 早晚正常打卡,无计算中间是否请假 统计时在查询 0为否 1为是
					}
					else
					{
						ta.setWorkFullHours(0);// 早晚正常打卡 0为否 1为是 
					}

					// 通过早打卡时间 判断是否有迟到可能性
					// 1.没有打卡 就写成1 或是打卡时间 晚于9点 那么就可以记为迟到
					// 是否真的迟到 等到 统计 和请假进行判断一下
					if (startWork.equals("")
							|| (DateFun.getTimeDifferenceCompare2(startWork,
									"09:00") < 0.5))// 大于8点半的开始统计出来
													// 有当日的缺勤时间但是不知道什么时间缺的勤
					{
						ta.setIsBeLate(1);// 是否有迟到可能性 0为无 1为有 为正常迟到
					}
					else
					{
						ta.setIsBeLate(0);// 是否有迟到可能性 0为无 1为有 为正常迟到
					}

					// 计算当日的出勤时间
					// 当天出勤时间 晚打卡时间 - 早出勤时间 -（中午回来打卡时间-中午走的打卡时间 ）- 下班之后的时间
					// 每天应出勤时间 =( 当天出勤时间+缺勤时间) =8.5
					// 每天的实际出勤时间=每天应出勤时间 +加班时间

					// 早于8：30的时间 需要减去 大于0 说明早于8：30
					Double meiyongTime = DateFun.getTimeDifferenceCompare(
							startWork, "08:30");
					// 晚于17：00的时间 需要减去 大于0 说明晚于17：00
					Double meiyongTime2 = DateFun.getTimeDifferenceCompare(
							"17:00", endWork);
					// 中午请假打卡时间 特殊情况
					Double noonTime = DateFun.getTimeDifferenceCompare(
							startWork2, endWork2);

					// 加班时间 ta.getPlusWork();
					// 计算当前的 实际出勤时间 没计算 早来 晚走
					Double realityTime = DateFun.getTimeDifferenceCompare(
							startWork, endWork);
					// 这里不算加班 加上加班时间就是当天的正常出勤范围内的全部出勤时间 没有减去当日请假的时间
					// 计算当日是否有请假 减去请假时间
					// 根据Num name查询用户信息
					TUserinfo tu = new TUserinfo();
					tu.setT_num(num);
					tu.setT_userName(name);
					List<TUserinfo> tui = userInfoService.queryUDRs(tu);
					List<TPersonalLeave> tpls = new ArrayList<TPersonalLeave>();
					if (null != tui && tui.size() > 0)
					{
						TPersonalLeave tpl = new TPersonalLeave();
						tpl.setUserId(tui.get(0).getT_id());
						tpl.setUserName(tui.get(0).getT_userName());
						tpl.setLeaveDay(date);
						tpl.setIsNoState(1);
						tpls = personalLeaveService.queryPersonalLeaves(tpl);
					}

					double tplTime = 0.0;
					if (tpls.size() > 0)
					{
						// 获取请假时长 然后 进行减去
						for (TPersonalLeave tps : tpls)
						{
							tplTime = tps.getLeaveDuration() + tplTime;
						}
					}
					// 当日实际出勤时间 减去请假时间了
					Double realTimeAttendance = realityTime - meiyongTime
							- meiyongTime2 - noonTime - tplTime;
					if (realTimeAttendance < 0)
						realTimeAttendance = 0d;

					ta.setRealTimeAttendance(DateFun
							.rounding(realTimeAttendance));

					// 所有出勤时间 这里是加班时间加上 当日出勤时间 包括
					String plusWorks = ta.getPlusWork();
					if (plusWorks.equals(""))
						plusWorks = "0";
					Double realTime = (realTimeAttendance + Double
							.parseDouble(plusWorks));
					ta.setUpWork(DateFun.rounding(realTime));
					ta.setCreatePersonnel(curUser.getT_id() + "-"
							+ curUser.getT_userName());
					list.add(ta);
				}
			}
			// 查询所有的考勤人员名称
			List<String> nameList = attendanceService
					.queryAllAttendancePeopleName();
			List<TAttendancePeople> tapList = new ArrayList<TAttendancePeople>();
			for (String values : ns)
			{
				String[] value = values.split("-");
				String num = value[0];
				String name = value[1];
				boolean s = nameList.contains(name);
				if (!s)
				{
					TAttendancePeople tap = new TAttendancePeople();
					tap.setNum(num);
					tap.setName(name);
					tapList.add(tap);
				}
			}
			// 保存所有用户的名称到 用户名称表中
			for (TAttendancePeople tap : tapList)
			{
				attendanceService.addAttendancePeople(tap);
				// 第一次的时候会把所有的导入人员不重名的人员 导入到用户列表中
				// 导入到用户列表数据 用于同步人名 与编号
				TUserinfo tui = new TUserinfo();
				tui.setT_userName(tap.getName());// 用户名
				tui.setT_loginName(tap.getNum() + "-"
						+ PinYin.getPinYin(tap.getName()));// 用拼音转换成登录名
				tui.setT_isUse(1);// 是否可用
				tui.setT_num(tap.getNum());// 用户编号
				// 考勤插入用户数据
				attendanceService.saveTUserinfo(tui);
			}
			// 然后循环list中的数据 保存到数据库中
			for (TAttendance ta : list)
			{
				attendanceService.addAttendance(ta);
			}
			// 计算导入时间
			long end = new Date().getTime();
			System.out.println("数据导入成功,耗时:" + (end - start) / 1000 + "秒");
		}
		catch (Exception e)
		{
			log.error(
					"AttendanceAction 中的importAttendance异常![" + e.getMessage()
							+ "]", e);
			e.printStackTrace();
		}
		return queryAllAttendanceList();
	}

	/**
	 * 跳转到补录当前考勤人员的名单
	 */
	public String showSaveAttendance()
	{
		// 查询所有的考勤人员名称
		List<TAttendancePeople> tapList;
		try
		{
			tapList = attendanceService.queryAllAttendancePeople();
			request.setAttribute("tapList", tapList);
		}
		catch (ParseException e)
		{
			log.error(
					"AttendanceAction 中的showSaveAttendance异常!["
							+ e.getMessage() + "]", e);
			e.printStackTrace();
		}
		return "save";
	}

	/**
	 * 补录当前考勤人员的名单
	 */
	public String saveAttendance()
	{
		try
		{
			String numIds = getParam("numIds");// 补录人员Num
			String newName = getParam("newName");// 新增人员名称
			String newNum = getParam("newNum");// 新增人员编号
			String aDate = getParam("aDate");// 考勤日期
			String startWork = getParam("startWork");// 上班时间
			String endWork = getParam("endWork");// 下班时间
			String startWork2 = getParam("startWork2");// 中午下班时间
			String endWork2 = getParam("endWork2");// 中午上班时间
			String plusWork = getParam("plusWork");// 平时加班（时）
			String holidayWork = getParam("holidayWork");// 公休加班（时）
			String absenceWork = getParam("absenceWork");// 缺勤（时）
			String absenceCount = getParam("absenceCount");// 缺勤次数

			TAttendance ta = new TAttendance();

			// 判断是否为新增人员 没有默认为原有人员
			if (!numIds.equals(""))
			{
				String[] ss = numIds.split("-");
				ta.setNumId(ss[0]);// 人员编号
				ta.setName(ss[1]);// 姓名
			}

			if (!newName.equals("") && !newNum.equals(""))
			{
				ta.setNumId(newNum);// 人员编号
				ta.setName(newName);// 姓名

				// 插入新增人员信息
				TAttendancePeople tap = new TAttendancePeople();
				tap.setNum(newNum);
				tap.setName(newName);
				attendanceService.addAttendancePeople(tap);
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(aDate);
			ta.setaDate(date);// 考勤日期
			String week = DateFun.getWeek(date);
			ta.setWeekDay(week);// 星期
			ta.setWorkTime("白班");// 班次
			ta.setStartWork(startWork);// 上班1
			ta.setEndWork(endWork);// //下班1
			ta.setStartWork2(startWork2);// 上班3
			ta.setEndWork2(endWork2);// 下班4
			// 判断是否有加班 加班时长
			// 1.统计每天的加班时长（周六日单独计算） 大于17：30 加班达到0.3小时的 记录 否则 为空
			// 2.判断星期 然后看一下是不是 周六周日 然后 分别计算周六周日的 加班时间 中间知否有串休0
			if (!week.equals("星期六") && !week.equals("星期日"))
			{
				Double zhi = DateFun
						.getTimeDifferenceCompare("17:30", plusWork);
				if (zhi == 0.0)
					ta.setPlusWork("0");// 平时加班(时)
				else if (plusWork.equals("") && zhi < 0.3)
				{
					ta.setPlusWork("0");// 平时加班(时)
				}
				else if (plusWork.equals("") && zhi >= 0.3 && zhi < 0.8)
				{
					ta.setPlusWork(DateFun.getTimeDifferenceCompare("17:30",
							plusWork) + "");// 平时加班(时)
				}
				else
				{
					ta.setPlusWork(plusWork);// 平时加班(时)
				}
			}
			else if (week.equals("星期六") || week.equals("星期日"))
			{
				// 先判断是endWork 是否有晚加班情况 没有 在计算是否有少于4小时的情况
				Double zc = DateFun.getTimeDifferenceCompare("13:00", endWork);// 计算下午的加班时间
																				// //计算晚5点半以后的加班时间
				Double dj = DateFun.getTimeDifferenceCompare("17:30", endWork);// 计算下午的加班时间
																				// //计算晚5点半以后的加班时间
				if (zc == 0.0)
				{
					ta.setPlusWork("0");// 平时加班(时)
				}
				else if (zc <= 4 && zc > 0.0)
				{
					ta.setPlusWork(zc + "");// 平时加班(时)
				}
				else if (zc > 4 && zc < 4.8) // zc大于4.8 自然dj>=0.3以上了 所以只要 dj有值那么
												// 就是晚间已经在加班了
				{
					ta.setPlusWork("4");// 平时加班(时)
				}
				else
				{
					ta.setPlusWork((4 + dj) + "");// 平时加班(时)
				}
			}
			ta.setHolidayWork(holidayWork);// 公休加班(时)
			ta.setAbsenceWork(absenceWork);// 缺勤(时)
			int count = 0;
			String c = absenceCount;
			if (!c.equals(""))
			{
				count = Integer.parseInt(c);
				ta.setAbsenceCount(count);// 缺勤次数(次)
			}
			else
			{
				ta.setAbsenceCount(0);// 缺勤次数(次)
			}

			if (!ta.getWeekDay().equals("星期日"))
			{
				if (ta.getStartWork().equals(""))
				{
					ta.setNoEarlyPunch(1);// noEarlyPunch;//无早打卡 0为否 1为是
				}
				else
				{
					ta.setNoEarlyPunch(0);// noEarlyPunch;//无早打卡 0为否 1为是
				}
				// 判断晚上是否未打卡
				if (ta.getEndWork().equals("")
						&& (ta.getEndWork().equals("") && ta.getEndWork2()
								.equals("")))
				{
					ta.setNoLatePunch(1);// noLatePunch;//无晚打卡 0为否 1为是
				}
				else
				{
					ta.setNoLatePunch(0);// noLatePunch;//无晚打卡 0为否 1为是
				}
			}
			else
			{
				ta.setNoLatePunch(0);// noEarlyPunch;//无早打卡 0为否 1为是
				ta.setNoEarlyPunch(0);// noEarlyPunch;//无早打卡 0为否 1为是
			}
			HttpSession session = request.getSession();// 获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session
					.getAttribute("curManagUser");
			ta.setCreatePersonnel(curUser.getT_id() + "-"
					+ curUser.getT_userName());

			// 判断是否有满勤与夜宵补助 然后设置ta考勤数据
			// 判断夜宵补助 下班时间 是否大于22:00 如果大于设置1
			if (DateFun.getHourCompare(22, endWork))
			{
				ta.setSupper(1);// Supper夜宵补助 0为否 1为是
			}
			else
			{
				ta.setSupper(0);// Supper夜宵补助 0为否 1为是
			}
			// 是否当前满勤符合 这里需要判断天是否请假？ 还是后期计算满勤奖的时候进行计算
			// 因为这里进行判断 会很慢 100个人就需要 100*30天次数据库访问
			if (DateFun.getTimeCompare("8:25", startWork)
					&& DateFun.getTimeCompare(endWork, "17:00"))
			{
				ta.setWorkFullHours(1);// 早晚正常打卡,无计算中间是否请假 统计时在查询 0为否 1为是 2为周日
			}
			else if (week.equals("星期日"))
			{
				ta.setWorkFullHours(2);// 满勤符合,无计算中间是否请假 统计时在查询 0为不符合 1为符合 2为周日
			}
			else
			{
				ta.setWorkFullHours(0);// 早晚正常打卡 0为否 1为是 2为周日
			}

			// 通过早打卡时间 判断是否有迟到可能性
			// 1.没有打卡 就写成1 或是打卡时间 晚于9点 那么就可以记为迟到
			// 是否真的迟到 等到 统计 和请假进行判断一下
			if (startWork.equals("")
					|| (DateFun.getTimeDifferenceCompare2(startWork, "09:00") < 0.5))// 大于8点半的开始统计出来
																						// 有当日的缺勤时间但是不知道什么时间缺的勤
			{
				ta.setIsBeLate(1);// 是否有迟到可能性 0为否 1为是为正常迟到
			}
			else
			{
				ta.setIsBeLate(0);// 是否有迟到可能性 0为否 1为是为正常迟到
			}

			// 计算当日的出勤时间
			// 当天出勤时间 晚打卡时间 - 早出勤时间 -（中午回来打卡时间-中午走的打卡时间 ）- 下班之后的时间
			// 每天应出勤时间 =( 当天出勤时间+缺勤时间) =8.5
			// 每天的实际出勤时间=每天应出勤时间 +加班时间

			// 早于8：30的时间 需要减去 大于0 说明早于8：30
			Double meiyongTime = DateFun.getTimeDifferenceCompare(startWork,
					"08:30");
			// 晚于17：00的时间 需要减去 大于0 说明晚于17：00
			Double meiyongTime2 = DateFun.getTimeDifferenceCompare("17:00",
					endWork);
			// 中午请假打卡时间 特殊情况
			Double noonTime = DateFun.getTimeDifferenceCompare(startWork2,
					endWork2);

			// 加班时间 ta.getPlusWork();
			// 计算当前的 实际出勤时间 没计算 早来 晚走
			Double realityTime = DateFun.getTimeDifferenceCompare(startWork,
					endWork);
			// 这里不算加班 加上加班时间就是当天的正常出勤范围内的全部出勤时间
			Double realTimeAttendance = realityTime - meiyongTime
					- meiyongTime2 - noonTime;
			if (realTimeAttendance < 0)
				realTimeAttendance = 0d;

			ta.setRealTimeAttendance(DateFun.rounding(realTimeAttendance));
			String plusWorks = ta.getPlusWork();
			if (plusWorks.equals(""))
				plusWorks = "0";
			Double realTime = (realTimeAttendance + Double
					.parseDouble(plusWorks));
			ta.setUpWork(DateFun.rounding(realTime));
			attendanceService.addAttendance(ta);
		}
		catch (NumberFormatException e)
		{
			// 在此捕获“异常”
			log.error("AttendanceAction 中的saveAttendance异常![" + e.getMessage()
					+ "]", e);
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// 在此捕获“异常”
			log.error("AttendanceAction 中的saveAttendance异常![" + e.getMessage()
					+ "]", e);
			e.printStackTrace();
		}

		return queryAllAttendanceList();
	}

	/**
	 * 查询当前的人员名称是否唯一
	 */
	public String repeatName()
	{
		String name = getParam("name");
		boolean has = attendanceService.repeatName(name.trim());// 查询当前的人员名称是否唯一
		request.setAttribute("has", has);
		return "repeat";
	}

	/**
	 * 展示某个月的所有人员考勤情况
	 */
	public String queryAllListForMonthList()
	{
		try
		{
			// 获取前天参数
			String numId = getParam("numId");// 补录人员Num
			String startDate = getParam("startDate");// 开始时间
			String endDate = getParam("endDate");// 结束时间
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 10 : Integer
					.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer
					.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (null == startDate || "".equals(startDate))
			{
				Calendar cal = Calendar.getInstance();// 获取当前日期
				cal.add(Calendar.MONTH, 0);
				cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				startDate = sdf.format(cal.getTime());
			}

			if (null == endDate || "".equals(endDate))
			{
				Calendar ca2 = Calendar.getInstance();
				ca2.set(Calendar.DAY_OF_MONTH,
						ca2.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = sdf.format(ca2.getTime());
			}

			// 先把数据组装好
			// 1.根据日期集合把所有日期都查询出来
			ArrayList<String[]> listAttendance = new ArrayList<String[]>();
			ArrayList<String> listDate = DateFun
					.TimeQuantum(startDate, endDate);
			// 2.查询所有人员信息 然后按照日期 分别查询当日的请假数据情况
			// 2-1.先获取所有考勤人员
			List<TAttendancePeople> tapList = new ArrayList<TAttendancePeople>();
			List<TAttendancePeople> tapLists = attendanceService
					.queryAllAttendancePeople();
			if (numId != null && !numId.equals(""))
			{
				// 查询考勤用户的名单
				TAttendancePeople tp = new TAttendancePeople();
				tp.setNum(numId);
				TAttendancePeople taps = attendanceService
						.queryAttendancePeople(tp);
				tapList.add(taps);
				page.setTotalRecord(1);
			}
			else
			{
				tapList = attendanceService.queryAllAttendancePeople(page);
			}

			// 2-2.根据人员 然后循环 获取 当日请假统计情况 现循环日期 再循环人员
			for (TAttendancePeople tap : tapList)
			{
				// 考勤日期 根据考勤日期查询当日的请假情况
				int len = listDate.size() + 2;// 展示数组的长度 +2 是 编号与名称 剩下的是日期的循环
				String[] s = new String[len];// 创建的数据集合
				s[0] = tap.getNum() + "";// 编号ID
				s[1] = tap.getName() + "";// 人员名称
				int i = 2;// 从第二个数组位数开始循环位数
				for (String date : listDate)
				{
					// 2-3.把每日的请假情况统计到数据集合中
					// 根据循环日期获取当日考勤情况
					TPersonalLeave tpls = new TPersonalLeave();
					tpls.setLeaveDay(sdf.parse(date));// 考勤日期
					tpls.setUserName(tap.getName());
					List<TPersonalLeave> ltpl = personalLeaveService
							.queryPersonalLeaves(tpls);
					StringBuffer sb = new StringBuffer();
					if (null != ltpl && ltpl.size() > 0)
					{
						// 循环请假事项 保存到当前考勤数据中
						for (TPersonalLeave tpl : ltpl)
						{
							// 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假
							// 9.婚假
							if (1 == tpl.getState())
								sb.append("事假:");
							if (2 == tpl.getState())
								sb.append("病假:");
							if (3 == tpl.getState())
								sb.append("公出:");
							if (4 == tpl.getState())
								sb.append("串休:");
							if (5 == tpl.getState())
								sb.append("丧假:");
							if (6 == tpl.getState())
								sb.append("驾校假:");
							if (7 == tpl.getState())
								sb.append("产假:");
							if (8 == tpl.getState())
								sb.append("哺乳假:");
							if (9 == tpl.getState())
								sb.append("婚假:");
							if (10 == tpl.getState())
								sb.append("论文假:");
							sb.append(tpl.getLeaveDuration() + "小时  ");
						}
					}
					// 通过日期与人员信息获取当天的考勤状况
					// 3 如果传入了个别人的Id 那么通过ID至查询一个人的数据
					TAttendance ta = attendanceService.queryAttendance(date,
							tap.getNum(), tap.getName());
					if (null != ta)
					{
						int c = ta.getAbsenceCount();
						if (c > 0)
						{
							sb.append(" 缺勤 ");
						}

						if (!ta.getWeekDay().equals("星期日"))
						{
							if (ta.getStartWork().equals(""))
							{
								sb.append(" 无早打卡 ");// noEarlyPunch;//无早打卡 0为否
													// 1为是
							}
							// 判断晚上是否未打卡
							if (ta.getEndWork().equals("")
									&& (ta.getEndWork().equals("") && ta
											.getEndWork2().equals("")))
							{
								sb.append(" 无晚打卡 ");// noLatePunch;//无晚打卡 0为否
													// 1为是
							}
						}

						// 判断是否有加班 加班时长
						// 1.统计每天的加班时长（周六日单独计算） 大于17：30 加班达到0.3小时的 记录 否则 为空
						// 2.判断星期 然后看一下是不是 周六周日 然后 分别计算周六周日的 加班时间 中间知否有串休0
						if (!ta.getWeekDay().equals("星期六")
								&& !ta.getWeekDay().equals("星期日"))
						{
							Double zhi = DateFun.getTimeDifferenceCompare(
									"17:30", ta.getEndWork());
							if (ta.getPlusWork().equals("") && zhi >= 0.3
									&& zhi < 0.8)
							{
								sb.append(" 平时加班(时):" + ta.getPlusWork() + "小时");
							}
							else
							{
								sb.append(" 平时加班(时):" + ta.getPlusWork() + "小时");
							}
						}
						else if (ta.getWeekDay().equals("星期六")
								|| ta.getWeekDay().equals("星期日"))
						{
							// 先判断是endWork 是否有晚加班情况 没有 在计算是否有少于4小时的情况
							Double zc = DateFun.getTimeDifferenceCompare(
									"13:00", ta.getEndWork());// 计算下午的加班时间
																// //计算晚5点半以后的加班时间
							Double dj = DateFun.getTimeDifferenceCompare(
									"17:30", ta.getEndWork());// 计算下午的加班时间
																// //计算晚5点半以后的加班时间
							if (zc == 0.0)
							{
								sb.append(" 平时加班(时):" + 0);
							}
							else if (zc <= 4)
							{
								sb.append(" 平时加班(时):" + zc + "小时 ");
							}
							else if (zc > 4 && zc < 4.8) // zc大于4.8 自然dj>=0.3以上了
															// 所以只要 dj有值那么
															// 就是晚间已经在加班了
							{
								sb.append(" 平时加班(时):4小时");
							}
							else
							{
								sb.append(" 平时加班(时):" + 4 + dj + "小时 ");
							}
						}

						// 判断是否有满勤与夜宵补助 然后设置ta考勤数据
						// 判断夜宵补助 下班时间 是否大于22:00 如果大于设置1
						if (DateFun.getHourCompare(22, ta.getEndWork()))
						{
							sb.append(" 夜宵补助: 有 ");
						}
						// 是否当前满勤符合 这里需要判断天是否请假？ 还是后期计算满勤奖的时候进行计算
						// 因为这里进行判断 会很慢 100个人就需要 100*30天次数据库访问
						if (DateFun.getTimeCompare("8:25", ta.getStartWork())
								&& DateFun.getTimeCompare(ta.getEndWork(),
										"17:00"))
						{
						}
						else if (ta.getWeekDay().equals("星期日"))
						{
						}
						else
						{
							sb.append(" 满勤符合: 不符合 ");
						}

						// 通过早打卡时间 判断是否有迟到可能性
						// 1.没有打卡 就写成1 或是打卡时间 晚于9点 那么就可以记为迟到
						// 是否真的迟到 等到 统计 和请假进行判断一下
						if (ta.getStartWork().equals("")
								|| (DateFun.getTimeDifferenceCompare2(
										ta.getStartWork(), "09:00") < 0.5))// 大于8点半的开始统计出来
																			// 有当日的缺勤时间但是不知道什么时间缺的勤
						{
							sb.append(" 迟到可能性: 有 ");
						}

						// 计算当日的出勤时间
						// 当天出勤时间 晚打卡时间 - 早出勤时间 -（中午回来打卡时间-中午走的打卡时间 ）- 下班之后的时间
						// 每天应出勤时间 =( 当天出勤时间+缺勤时间) =8.5
						// 每天的实际出勤时间=每天应出勤时间 +加班时间

						// 早于8：30的时间 需要减去 大于0 说明早于8：30
						Double meiyongTime = DateFun.getTimeDifferenceCompare(
								ta.getStartWork(), "08:30");
						// 晚于17：00的时间 需要减去 大于0 说明晚于17：00
						Double meiyongTime2 = DateFun.getTimeDifferenceCompare(
								"17:00", ta.getEndWork());
						// 中午请假打卡时间 特殊情况
						Double noonTime = DateFun.getTimeDifferenceCompare(
								ta.getStartWork2(), ta.getEndWork2());

						// 加班时间 ta.getPlusWork();
						// 计算当前的 实际出勤时间 没计算 早来 晚走
						Double realityTime = DateFun.getTimeDifferenceCompare(
								ta.getStartWork(), ta.getEndWork());
						// 这里不算加班 加上加班时间就是当天的正常出勤范围内的全部出勤时间
						Double realTimeAttendance = realityTime - meiyongTime
								- meiyongTime2 - noonTime;
						if (realTimeAttendance < 0)
							realTimeAttendance = 0d;
						sb.append(" 实际出勤时间(不算加班):"
								+ DateFun.rounding(realTimeAttendance));
						// 统计完每天的信息
					}
					s[i] = sb.toString();
					i++;
				}
				listAttendance.add(s);
			}
			request.setAttribute("listAttendance", listAttendance);// 按天查询的考勤数据
			request.setAttribute("listDate", listDate);// 所有查询的日期集合
			request.setAttribute("tapLists", tapLists);// 所有考勤人员的数据
			request.setAttribute("tapList", tapList);// 当前查询的考勤人员的数据
			request.setAttribute("page", page);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("numId", numId);
		}
		catch (ParseException e)
		{
			// 在此捕获“异常”
			log.error("AttendanceAction 中的saveAttendance异常![" + e.getMessage()
					+ "]", e);
			e.printStackTrace();
		}
		return "allView2";
	}

	/**
	 * 跳转到导出考勤数据界面
	 */
	public String showImportAttendances()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 查询数据所有存在的年份
		List<String> listYears = personalLeaveService.queryTPersonalLeaveStatisticsYear();
		if (listYears.size() == 0)
		{
			for(String ss:listYears)
			{
				listYears.add(ss.substring(0, 4));// 获取到当前年份
			}
		}

		request.setAttribute("listYears", listYears);
		request.setAttribute("year", sdf.format(new Date()).substring(0,4));//获取到当前年份
		request.setAttribute("month", sdf.format(new Date()).substring(5,7));//获取到当前月份
		return "imports";
	}

	/**
	 * 通过导入的考勤
	 */
	public void importAttendances()
	{
		try
		{
			// 计算导入时间
			long start = new Date().getTime();
			String realityDate = getParam("realityDate");// 导出月应出勤天数根据天数算出本月的基本工时
			String realityTime = getParam("realityTime");// 导出月应完成工时
															// 
			String year = getParam("year");// 需要导出的考勤年
			String month = getParam("month");// 需要导出的考勤月

			// 根据年月 把需要的考勤数据先查询出来
			// 然后根据不同的条件进行判断筛选
			// 按照需要的数据进行导出
			// 大部门 小部门 姓名 加班小时数 周六（或周日）加班 加班合计 事假合计 病假合计 其他请假合计（不算病假、事假）
			// 事假超8.5小时*2倍扣罚 事假超3次*5倍扣罚 迟到超过2次后每次按照50元扣 合计扣发工时 请假次数 迟到次数 夜宵补助
			// 完成工时 满勤奖 备注
			// 首先根据年月查询考勤人 集合 以这个数据 去最小查询数据 作为基础
			List<TAttendancePeople> listUsers = attendanceService
					.queryAttendanceUser(year, month);
			List<TSuperModel> tsmList = new ArrayList<TSuperModel>();
			for (TAttendancePeople tap : listUsers)
			{
				String num = tap.getNum();// 人员编号
				String name = tap.getName();// 人员名称

				// 现根据 人员名称 人员编号 查询部门 角色等相关信息 可以得到 部门名称 角色名称 姓名
				TUserinfo tu = new TUserinfo();
				TSuperModel tsm = new TSuperModel();
				tu.setT_num(num);
				tu.setT_userName(name);
				List<TUserinfo> tui = userInfoService.queryUDRs(tu);
				// -------------------------------------这里可以判断一下 tui 长度 循环出两条数据
				// 暂时没必要所以没写
				if (null != tui && tui.size() > 0)
				{
					tsm.setDaptName(tui.get(0).getT_daptName());// 部门名称
					tsm.setRoleName(tui.get(0).getT_roleName());// 角色名称
					tsm.setUserName(tui.get(0).getT_userName());// 姓名
				}
				else
				{
					tsm.setDaptName("");// 部门名称
					tsm.setRoleName("");// 角色名称
					tsm.setUserName(name);// 姓名
				}

				// 周六（或周日）加班 加班合计(两部分整合) 夜宵补助（sum统计 以计算字段） 完成工时 满勤奖(需要判断是否有请假 )
				// 查询考勤表 进行sum计算 每个人数据进行算数 先排除 周六 于周日的
				TAttendance ta = attendanceService.queryAttendanceInfo(year,
						month, num, 0); // 0:不包含周六周日 1:只查周六的加班 2:只查周日的加班
				// 3:只查周六和周日的加班 4:全部
				String plusWorks = "";
				if (null != ta)
				{
					plusWorks = DateFun.rounding(Double.parseDouble(ta
							.getPlusWork()));// 加班小时数 不包含周六日
				}
				else
				{
					plusWorks = "0";// 加班小时数 不包含周六日
				}
				
				tsm.setPlusWorks(plusWorks);//单独的加班合计
				TAttendance ta1 = attendanceService.queryAttendanceInfo(year,
						month, num, 1);// 1:只查周六的加班
				TAttendance ta2 = attendanceService.queryAttendanceInfo(year,
						month, num, 2);// 2:只查周日的加班

				String satPlusWeekSum = "";
				if (null != ta1)
				{
					satPlusWeekSum = ta1.getPlusWork();// 周六的加班合计
				}
				else
				{
					satPlusWeekSum = "0";// 加班小时数 不包含周日
				}
				String sunPlusWeekSum = "";// 周日的加班合计
				if (null != ta2)
				{
					sunPlusWeekSum = ta2.getPlusWork();// 周日的加班合计
				}
				else
				{
					sunPlusWeekSum = "0";// 周日的加班合计
				}
				List<TPersonalLeave> plss = personalLeaveService.queryPersonalLeaves(year, month, num, name, null, null);// 查询全部请假
				TAttendance tat = attendanceService.queryAttendanceInfo(year,month, num, 5);// 查询本月全部考勤
				// 先判断周日的的加班合计应该为0 因为只要不为零 那么就说明 这个周日的前一个周六会有串休
				// 或是其他周六 然后看一下本月应该的周六合计是多少 根据月份查询当前月应该有几个周六 这样可以算出
				// 周六个数 *4=周六一共的加班小时数 小于这个的可能是周六请假或是没来
				// 然后判断一下这个人的请假记录 通过年月查询 当月是否又周六请假 或是公休的情况 这样可以判断是否为调休 或是
				// 周日全天算为加班的情况
				if (sunPlusWeekSum.equals("0"))
				{
					tsm.setWeekPlusWorks(DateFun.rounding(Double.parseDouble(satPlusWeekSum)));// 周日无加班的情况下 只计算周六的加班
				}
				else
				{
					// 通过月份查询本月应该有几个周六
					int satCount = DateFun.getWeekQuantiy(
							Integer.parseInt(year), Integer.parseInt(month));
					int satPlusSum = satCount * 4;// 周六正常应该的加班时间

					if (Double.parseDouble(satPlusWeekSum) < satPlusSum) //如果本月的周六加班小于  本月应有的周六加班小时数查询请假情况
					{
						// 查询一下本人本月请假中是否有周六请假 是否挑换了公休 这样可以把周日的上班当作周六来算
						String week = "星期六";
						String weeks = "星期日";
						String state = "4";// 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假
											// 6.驾校假 7.产假 8.哺乳假 9.婚假 10.论文假
						List<TPersonalLeave> pls = personalLeaveService
								.queryPersonalLeaves(year, month, num, name,
										week, state);
						List<TAttendance> tas = attendanceService
								.queryAttendances(year, month, num, name, weeks);
						// 计算周日的出勤情况
						double sunPlusSum = 0.0;
						if (null != pls && pls.size() > 0)
						{
							// 查询周日的考勤 是否有出勤 可以判定当月是否有调休可能 调休的定义 是只能调本月的
							// 算出调休的小时 然后如果是下午的话 还需要判断是否加班情况
							// 还需要看下一下周日的出勤情况 根据出勤那天进行查询可能会有请假
							// 统计一下 周六请假的情况
							double noPlusSum = 0.0;
							double plusSum = 0.0;
							for (TPersonalLeave pl : pls)
							{
								// 获取请假时段
								String plt = pl.getLeaveTimes();
								// 获取请假时长
								float pld = pl.getLeaveDuration();
								// 根据plt算出请假时间中有多少在加班当中 08:30-17:00
								String[] ss = plt.split("-");
								String startT = ss[0];// 起始请假时间
								String endT = ss[1];// 结束请假时间
								// 看请假时间有多少是在13点以后的四个小时里面 然后
								// 如果其实时间 大于12：55那么请假就是在下午 加班时间 如果大于0说明晚于12：55
								double amTime = DateFun
										.getTimeDifferenceCompare("12:55",
												startT);
								// 如果结束时间 小于11：55 那么就是在上去请假 非加班时间 如果大于0说明晚于11：55
								double pmTime = DateFun
										.getTimeDifferenceCompare(endT, "11:55");
								if (amTime > 0)
								{
									plusSum = pld + plusSum;
								}

								if (pmTime < 0)
								{
									noPlusSum = pld + noPlusSum;
								}
							}
							// 然后算出最后的和 用于统计周日是否出勤可以满足当日的请假
							// 如果大于了就是满足了 如果小于了 那么就统计到请假罚时中 单独计算一列
							// 实际的周六加班 减去请假
							double realSatPlusSum = Double.parseDouble(satPlusWeekSum) - plusSum;// 周六的实际加班
							// 计算周日的出勤情况 sunPlusSum
							if (tas.size() > 0)
							{
								// 算调休 顶多久 如果多了算在周日加班里
								for (TAttendance tass : tas)
								{
									sunPlusSum = Double.parseDouble(tass
											.getPlusWork()) + sunPlusSum;
								}
							}


							tsm.setWeekPlusWorks((DateFun.rounding(realSatPlusSum) + DateFun.rounding(sunPlusSum))+ "");// 周日无加班的情况下 只计算周六的加班
						}
						else
						{
							for (TAttendance tass : tas)
							{
								sunPlusSum = Double.parseDouble(tass.getPlusWork()) + sunPlusSum;
							}
							String spw = DateFun.rounding(Double.parseDouble(satPlusWeekSum));
							String sps = DateFun.rounding(sunPlusSum);
							double ss = Double.parseDouble(spw)+ Double.parseDouble(sps);
							tsm.setWeekPlusWorks(DateFun.rounding(ss) + "");// 周日无加班的情况下 只计算周六的加班
						}
					}
					else{
						//没计算如果周六的加班大于了本月的周六加班那么就直接累计相加

						String spws = DateFun.rounding(Double.parseDouble(satPlusWeekSum));
						String spss = DateFun.rounding(Double.parseDouble(sunPlusWeekSum));
						double ssss = Double.parseDouble(spws)+ Double.parseDouble(spss);
						tsm.setWeekPlusWorks(DateFun.rounding(ssss)+ "");// 周日无加班的情况下 只计算周六的加班
					}
				}
				
				double d1 = 0d;
				double d2 = 0d;
				if (null != plusWorks)
				{
					d1 = Double.parseDouble(plusWorks);
				}
				if (null != tsm && null != tsm.getWeekPlusWorks())
				{
					d2 = Double.parseDouble(tsm.getWeekPlusWorks());
				}

				double ss2 = d1 + d2;
				String fullPlusWorks = DateFun.rounding(ss2) + ""; // 加班合计
				tsm.setPlusWorkSum(fullPlusWorks);
				int supper = 0;// 夜宵补助


				// ----------------------------计算请假
				if (null != plss && plss.size() > 0)
				{
					TPersonalLeaveStatistics tpls = new TPersonalLeaveStatistics();
					tpls.setT_userId(tui.get(0).getT_id());// 用户ID
					tpls.setT_leaveYear(year);// 请假集合月份
					tpls.setT_leaveMonth(month);// 请假集合月份
					TPersonalLeaveStatistics tplss = personalLeaveService
							.queryTPersonalLeaveStatistics(tpls);

					float leaveCountStatisticsTime = 0;// 请假次数查过三次 乘以5
					float leaveHourStatisicsTime = 0;// 请假小时数超过8.5部分乘以2
					float maxStatisics = 0;// 事假最大罚时
					float funeralLeaveStatisticsTime = 0; // 丧假统计
					float maritalLeaveStatisticsTime = 0; // 婚假统计
					float drivingLeaveStatisticsTime = 0;// 驾校假统计
					float drivingLeaveStatisticsAllTime = 0;// 全部驾校假统计=(已累计的驾校假统计+本月的驾校假统计)
					float thesisStatisticsTime = 0;// 论文假统计
					float thesisStatisticsAllTime = 0;// 全部论文假统计=(已累计的驾校假统计+本月的驾校假统计)

					if (tplss == null)
						tplss = new TPersonalLeaveStatistics();
					// 统计请假合计数据 统计请假罚时事假的小时数 是否翻倍 在翻倍统计完成后 加上其他请假的时间
					if (tplss.getT_leaveCountStatistics() > 3)
					{
						leaveCountStatisticsTime = tplss.getT_leaveStatistics() * 5;
					}
					if (tplss.getT_leaveStatistics() > 8.5)
					{
						leaveHourStatisicsTime = (float) ((tplss
								.getT_leaveStatistics() - 8.5) * 2 + 8.5);
					}
					maxStatisics = leaveCountStatisticsTime > leaveHourStatisicsTime ? leaveCountStatisticsTime
							: leaveHourStatisicsTime;// 统计事假总共罚时
					maxStatisics = tplss.getT_leaveStatistics() > maxStatisics ? tplss
							.getT_leaveStatistics() : maxStatisics;// 统计事假总共罚时
					// 统计合计扣时 丧假加上X-8.5*3 婚假 Y-8.5*10 +产假 +哺乳假的小时 +驾校假
					funeralLeaveStatisticsTime = (float) (tplss
							.getT_funeralLeaveStatistics() - 8.5 * 3 > 0 ? (tplss
							.getT_funeralLeaveStatistics() - 8.5 * 3) : 0);
					maritalLeaveStatisticsTime = (float) (tplss
							.getT_maritalLeaveStatistics() - 8.5 * 10 > 0 ? (tplss
							.getT_maritalLeaveStatistics() - 8.5 * 10) : 0);

					// 查询本人某年某月之前所有的驾校假小时数 部分时间段限制
					drivingLeaveStatisticsTime = personalLeaveService
							.queryAllDrivingLeaveStatisticsTimes(tui.get(0)
									.getT_id(), year, month);

					// 如果历史驾校假大于等于85,那么本月的请假乘2加入到罚时中
					// 如果历史的小于85但是加上本月的小时数超过了85 那么超过的部分 乘2罚时 在一个月中请了85个小时
					// 如果历史的加上当月的 都没超过85 那么 只加上本月的小时数 不罚时
					if (drivingLeaveStatisticsTime > 85)
					{
						drivingLeaveStatisticsAllTime = (tplss
								.getT_drivingLeaveStatistics() * 2);
					}
					else
					{
						// 判断一下 因为有可能默认一次性请假到85小时 那么累计查询的 和本月请的就是一样的 那么就会按照双被扣罚了

						if (drivingLeaveStatisticsTime
								+ tplss.getT_drivingLeaveStatistics() > 85)
						{
							float he1 = (drivingLeaveStatisticsTime
									+ tplss.getT_drivingLeaveStatistics() - 85);// 合计减去85小时
																				// 超出翻倍的部分
							drivingLeaveStatisticsAllTime = he1 * 2
									+ tplss.getT_drivingLeaveStatistics() - he1;// 超过85小时的翻倍
																				// 然后减去
						}
						else
						{
							drivingLeaveStatisticsAllTime = tplss
									.getT_drivingLeaveStatistics();
						}
					}

					// 查询本人所有的论文假小时数 部分时间段限制
					thesisStatisticsTime = personalLeaveService
							.queryAllThesisStatisticsTimes(
									tui.get(0).getT_id(), year, month);

					// 如果历史论文假大于等于85,那么本月的请假乘2加入到罚时中
					// 如果历史的小于85但是加上本月的小时数超过了85 那么超过的部分 乘2罚时
					// 如果历史的加上当月的 都没超过85 那么 只加上本月的小时数 不罚时
					if (thesisStatisticsTime > 85)
					{
						thesisStatisticsAllTime = (tplss
								.getT_thesisStatistics() * 2);
					}
					else
					{
						if (thesisStatisticsTime
								+ tplss.getT_thesisStatistics() > 85)
						{
							float he2 = (thesisStatisticsTime
									+ tplss.getT_thesisStatistics() - 85);// 合计减去85小时
																			// 超出翻倍的部分
							thesisStatisticsAllTime = he2 * 2
									+ tplss.getT_thesisStatistics() - he2;// 超过85小时的翻倍
																			// 然后减去
						}
						else
						{
							thesisStatisticsAllTime = tplss
									.getT_thesisStatistics();
						}
					}

					// 事假合计 病假合计 其他请假合计（不算病假、事假） 事假超8.5小时*2倍扣罚 事假超3次*5倍扣罚 合计扣发工时
					// 请假次数
					// 这里通过请假表 和 请假统计表能够进行查询 方法可参考 请假统计时的统计
					tsm.setLeaveStatistics(tplss.getT_leaveStatistics() + "");
					tsm.setSickLeaveStatistics(tplss.getT_sickLeaveStatistics()
							+ "");
					tsm.setOtherLeaveStatistics((funeralLeaveStatisticsTime
							+ tplss.getT_maternityLeaveStatistics()
							+ tplss.getT_lactationLeaveStatistics()
							+ maritalLeaveStatisticsTime
							+ drivingLeaveStatisticsAllTime + thesisStatisticsAllTime)
							+ "");
					tsm.setLeaveHourStatisicsTime(leaveHourStatisicsTime + "");
					tsm.setLeaveCountStatisticsTime(leaveCountStatisticsTime
							+ "");
					tsm.setLeaveHoursStatisics((maxStatisics
							+ tplss.getT_sickLeaveStatistics()
							+ funeralLeaveStatisticsTime
							+ tplss.getT_maternityLeaveStatistics()
							+ tplss.getT_lactationLeaveStatistics()
							+ maritalLeaveStatisticsTime
							+ drivingLeaveStatisticsAllTime + thesisStatisticsAllTime)
							+ "");
					tsm.setLeaveCountStatistics(plss.size() + "");
				}
				else
				{
					tsm.setLeaveStatistics("0");
					tsm.setSickLeaveStatistics("0");
					tsm.setOtherLeaveStatistics("0");
					tsm.setLeaveHourStatisicsTime("0");
					tsm.setLeaveCountStatisticsTime("0");
					tsm.setLeaveHoursStatisics("0");
					tsm.setLeaveCountStatistics("0");
				}
				// 迟到次数 迟到超过2次后每次按照50元扣
				if (null != tat)
				{
					tsm.setBeLateCount(tat.getBeLateCount());
				}
				else
				{
					tsm.setBeLateCount("--");
				}
				
				
				Double rTime= 7.5*Integer.parseInt(realityDate)+Double.parseDouble(tsm.getPlusWorks());
				Double sTime=Double.parseDouble(tsm.getLeaveStatistics())+Double.parseDouble(tsm.getSickLeaveStatistics())+Double.parseDouble(tsm.getOtherLeaveStatistics());
				String realTime = DateFun.rounding(rTime-sTime);
				TAttendance ta4 = attendanceService.queryAttendanceInfo(year,month, num, 4); // 0:不包含周六周日 1:只查周六的加班 2:只查周日的加班// 3:只查周六和周日的加班 4:全部
				if (null != ta4)
				{
					// 判断满勤 而且出勤数》=查询的满勤数
					if (ta4.getWorkFullHours() > 0 && ta4.getWorkFullHours() >= Integer.parseInt(realityDate) && Double.parseDouble(realTime) >=Double.parseDouble(realityTime) && null== plss)
					{
						tsm.setIsWorkFullHours("有");
					}
					else
					{
						tsm.setIsWorkFullHours("无");
					}
					supper = ta4.getSupper();
				}
				else
				{
					tsm.setRealTime("0");
					tsm.setIsWorkFullHours("无");
				}
				tsm.setRealTime(realTime);
				tsm.setSupperSum((supper * 10) + "");

				// 备注 里面 一般会记录这个人的出勤几天的状况 可以 count 一下本月的条数 应该就OK 或许想的简单了
				// 最后整合到一个 NB的对象中 准备整理到 Excel中
				tsmList.add(tsm);
			}
			// 计算导入时间
			long end = new Date().getTime();
			System.out.println("数据统计成功,耗时:" + (end - start) / 1000 + "秒");
			// 通过Excel 导出方法 把NB的对象导出到文件中
			ExcelDemo(tsmList, year, month);
		}
		catch (Exception e)
		{
			log.error(
					"AttendanceAction 中的importAttendances异常![" + e.getMessage()
							+ "]", e);
			e.printStackTrace();
		}
	}

	/**
	 * 导出Excel 对象
	 */
	public void ExcelDemo(List<TSuperModel> ve, String year,String month)
	{
		try
		{
			// 计算导入时间
			long start = new Date().getTime();
			File outFile = new File(request.getServletContext().getRealPath("upload/"+year + "-" + month+ "KaoQin.xls")); 
			OutputStream os = new FileOutputStream(outFile);
			response.setContentType("application/force-download");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ year + "-" + month + "KaoQin.xls\"");
			response.setContentType("text/html;charset=UTF-8");
			WritableWorkbook wwb;
			os = response.getOutputStream();
			wwb = Workbook.createWorkbook(os);
			WritableSheet ws = wwb.createSheet(
					"" + year + "-" + month + "考勤统计", 0);
			// 大部门 小部门 姓名 加班小时数 周六（或周日）加班 加班合计 事假合计 病假合计 其他请假合计（不算病假、事假）
			// 事假超8.5小时*2倍扣罚 事假超3次*5倍扣罚 迟到超过2次后每次按照50元扣 合计扣发工时 请假次数 迟到次数 夜宵补助
			// 完成工时 满勤奖 备注
			ws.addCell(new jxl.write.Label(0, 1, "部门"));
			ws.addCell(new jxl.write.Label(1, 1, "角色"));
			ws.addCell(new jxl.write.Label(2, 1, "姓名"));
			ws.addCell(new jxl.write.Label(3, 1, "加班小时数"));
			ws.addCell(new jxl.write.Label(4, 1, "周六（或周日）加班"));
			ws.addCell(new jxl.write.Label(5, 1, "加班合计"));
			ws.addCell(new jxl.write.Label(6, 1, "事假合计"));
			ws.addCell(new jxl.write.Label(7, 1, "病假合计"));
			ws.addCell(new jxl.write.Label(8, 1, "其他请假合计(不算病假、事假)"));
			ws.addCell(new jxl.write.Label(9, 1, "事假超8.5小时*2倍扣罚"));
			ws.addCell(new jxl.write.Label(10, 1, "事假超3次*5倍扣罚"));
			ws.addCell(new jxl.write.Label(11, 1, "合计扣发工时"));
			ws.addCell(new jxl.write.Label(12, 1, "请假次数"));
			ws.addCell(new jxl.write.Label(13, 1, "迟到次数"));
			ws.addCell(new jxl.write.Label(14, 1, "迟到罚钱合计"));
			ws.addCell(new jxl.write.Label(15, 1, "夜宵补助"));
			ws.addCell(new jxl.write.Label(16, 1, "完成工时"));
			ws.addCell(new jxl.write.Label(17, 1, "满勤奖"));
			ws.addCell(new jxl.write.Label(18, 1, "备注"));
			int demoSize = ve.size();
			TSuperModel demo = new TSuperModel();
			for (int i = 0; i < demoSize; i++)
			{
				demo = (TSuperModel) ve.get(i);
				ws.addCell(new jxl.write.Label(0, i + 2, demo.getDaptName()));
				ws.addCell(new jxl.write.Label(1, i + 2, demo.getRoleName()));
				ws.addCell(new jxl.write.Label(2, i + 2, demo.getUserName()));
				ws.addCell(new jxl.write.Label(3, i + 2, demo.getPlusWorks()));
				ws.addCell(new jxl.write.Label(4, i + 2, demo.getWeekPlusWorks()));
				ws.addCell(new jxl.write.Label(5, i + 2, demo.getPlusWorkSum()));
				ws.addCell(new jxl.write.Label(6, i + 2, demo.getLeaveStatistics()));
				ws.addCell(new jxl.write.Label(7, i + 2, demo.getSickLeaveStatistics()));
				ws.addCell(new jxl.write.Label(8, i + 2, demo.getOtherLeaveStatistics()));
				ws.addCell(new jxl.write.Label(9, i + 2, demo.getLeaveHourStatisicsTime()));
				ws.addCell(new jxl.write.Label(10, i + 2, demo.getLeaveCountStatisticsTime()));
				ws.addCell(new jxl.write.Label(11, i + 2, demo.getLeaveHoursStatisics()));
				ws.addCell(new jxl.write.Label(12, i + 2, demo.getLeaveCountStatistics()));
				ws.addCell(new jxl.write.Label(13, i + 2, demo.getBeLateCount()));
				ws.addCell(new jxl.write.Label(14, i + 2, demo.getLatePenaltyMoney()));
				ws.addCell(new jxl.write.Label(15, i + 2, demo.getSupperSum()));
				ws.addCell(new jxl.write.Label(16, i + 2, demo.getRealTime()));
				ws.addCell(new jxl.write.Label(17, i + 2, demo.getIsWorkFullHours()));
				ws.addCell(new jxl.write.Label(18, i + 2, demo.getRemark()));
			}
			ws.addCell(new jxl.write.Label(0, 0, year+"-"+ month+"考勤统计"));
			long end = new Date().getTime();
			System.out.println("数据导出成功,耗时:" + (end - start) / 1000 + "秒");
			wwb.write();
			// 关闭Excel工作薄对象
			wwb.close();
			os.flush();
			os.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询本人部门全部考勤记录
	 */
	public String queryAllAttendanceListByDapt()
	{
		try
		{
			log.info("查询全部考勤记录----------------");
			String numId = getParam("numId");// 人员编号
			String numName = getParam("numName");// 人员名称
			String startDate = getParam("startDate");// 开始时间
			String endDate = getParam("endDate");// 结束时间
			String tempPageSize = getParam("pageSize");
			String tempCurPage = getParam("curPage");
			int pageSize = tempPageSize == null ? 60 : Integer
					.parseInt(tempPageSize);
			int curPage = tempCurPage == null ? 1 : Integer
					.parseInt(tempCurPage);
			Page page = new Page(pageSize, curPage);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (null == startDate || "".equals(startDate))
			{
				Calendar cal = Calendar.getInstance();// 获取当前日期
				cal.add(Calendar.MONTH, 0);
				cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				startDate = sdf.format(cal.getTime());
			}

			if (null == endDate || "".equals(endDate))
			{
				Calendar ca2 = Calendar.getInstance();
				ca2.set(Calendar.DAY_OF_MONTH,
						ca2.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = sdf.format(ca2.getTime());
			}
			HttpSession session = request.getSession();// 获取session中当前登陆的人
			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
			String daptId =curUser.getT_daptId();//部门
			// 查询数据 如果name是一个人的名字就是单独查询一个人的考勤情况 如果为空就是不限制 全部
			List<TAttendance> list = attendanceService.queryAttendanceList(startDate, endDate, numName, numId,daptId, page);// 查询用户考勤的分页
			SaveLog("查询某时间段的考勤记录," + startDate + "-" + endDate + ",考勤人编号："
					+ numId + "-" + numName);
			if (null != list && list.size() > 0)
			{
				for (TAttendance ta : list)
				{
					// 考勤日期 根据考勤日期查询当日的请假情况
					Date date = ta.getaDate();
					TPersonalLeave tpls = new TPersonalLeave();
					tpls.setLeaveDay(date);// 考勤日期
					tpls.setUserName(ta.getName());
					List<TPersonalLeave> ltpl = personalLeaveService
							.queryPersonalLeaves(tpls);
					StringBuffer sb = new StringBuffer();
					if (null != ltpl && ltpl.size() > 0)
					{
						// 循环请假事项 保存到当前考勤数据中
						for (TPersonalLeave tpl : ltpl)
						{
							// 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假
							// 9.婚假
							if (1 == tpl.getState())
								sb.append("事假:");
							if (2 == tpl.getState())
								sb.append("病假:");
							if (3 == tpl.getState())
								sb.append("公出:");
							if (4 == tpl.getState())
								sb.append("串休:");
							if (5 == tpl.getState())
								sb.append("丧假:");
							if (6 == tpl.getState())
								sb.append("驾校假:");
							if (7 == tpl.getState())
								sb.append("产假:");
							if (8 == tpl.getState())
								sb.append("哺乳假:");
							if (9 == tpl.getState())
								sb.append("婚假:");
							if (10 == tpl.getState())
								sb.append("论文假:");
							sb.append(tpl.getLeaveDuration() + "小时  ");
						}
					}
					ta.setPersonalLeaveDetailed(sb.toString());
				}
			}
			// 查询当前部门的考勤人员名称
			List<TAttendancePeople> tapList = attendanceService.queryAttendanceListForDapt(startDate, endDate, daptId);
			// 明天把数据放到界面上展示
			request.setAttribute("numId", numId);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("list", list);
			request.setAttribute("tapList", tapList);
			request.setAttribute("page", page);
			return "allViewDapt";
		}
		catch (Exception e)
		{
			// 在此捕获“异常”
			e.printStackTrace();
			log.error("AttendanceAction 中的queryAllAttendanceListByDapt异常!["+ e.getMessage() + "]", e);
			return ERROR;
		}
	}

	/**
	 * 跳转到导出当月全部考勤数据界面
	 */
	public String showImportMonthAllAttendance()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String startDate = sdf.format(cal.getTime());

		Calendar ca2 = Calendar.getInstance();
		ca2.add(Calendar.MONTH, -1);
		ca2.set(Calendar.DAY_OF_MONTH,ca2.getActualMaximum(Calendar.DAY_OF_MONTH));
		String endDate = sdf.format(ca2.getTime());
			
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		return "importMonths";
	}
	 
	/**
	 * 准备导出某月全部考勤记录
	 */
	public void importMonthAllAttendances()
	{
		try
		{
			// 计算导入时间
			long start = new Date().getTime();
			log.info("查询某月全部考勤记录----------------");
			String startDate = getParam("startDate");// 开始时间
			String endDate = getParam("endDate");// 结束时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (null == startDate || "".equals(startDate))
			{
				Calendar cal = Calendar.getInstance();// 获取当前日期
				cal.add(Calendar.MONTH, 0);
				cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				startDate = sdf.format(cal.getTime());
			}

			if (null == endDate || "".equals(endDate))
			{
				Calendar ca2 = Calendar.getInstance();
				ca2.set(Calendar.DAY_OF_MONTH,
						ca2.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = sdf.format(ca2.getTime());
			}

			// 查询数据 如果name是一个人的名字就是单独查询一个人的考勤情况 如果为空就是不限制 全部
			List<TAttendance> list = attendanceService.queryAttendanceList(startDate, endDate);// 查询用户考勤的分页
			SaveLog("查询导出某时间段的考勤记录," + startDate + "-" + endDate );

			if (null != list && list.size() > 0)
			{
				for (TAttendance ta : list)
				{
					// 考勤日期 根据考勤日期查询当日的请假情况
					Date date = ta.getaDate();
					TPersonalLeave tpls = new TPersonalLeave();
					tpls.setLeaveDay(date);// 考勤日期
					tpls.setUserName(ta.getName());
					List<TPersonalLeave> ltpl = personalLeaveService
							.queryPersonalLeaves(tpls);
					StringBuffer sb = new StringBuffer();
					if (null != ltpl && ltpl.size() > 0)
					{
						// 循环请假事项 保存到当前考勤数据中
						for (TPersonalLeave tpl : ltpl)
						{
							// 请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假
							// 9.婚假
							if (1 == tpl.getState())
								sb.append("事假:");
							if (2 == tpl.getState())
								sb.append("病假:");
							if (3 == tpl.getState())
								sb.append("公出:");
							if (4 == tpl.getState())
								sb.append("串休:");
							if (5 == tpl.getState())
								sb.append("丧假:");
							if (6 == tpl.getState())
								sb.append("驾校假:");
							if (7 == tpl.getState())
								sb.append("产假:");
							if (8 == tpl.getState())
								sb.append("哺乳假:");
							if (9 == tpl.getState())
								sb.append("婚假:");
							if (10 == tpl.getState())
								sb.append("论文假:");
							sb.append(tpl.getLeaveDuration() + "小时  ");
						}
					}
					ta.setPersonalLeaveDetailed(sb.toString());
				}
			}
			// 计算导入时间
			long end = new Date().getTime();
			System.out.println(startDate + "-" + endDate+"全部考勤记录统计成功,耗时:" + (end - start) / 1000 + "秒");
			// 通过Excel 导出方法 把Attendance的对象导出到文件中
			ExcelMonthAllAttendance(list, startDate, endDate);
		}
		catch (Exception e)
		{
			// 在此捕获“异常”
			e.printStackTrace();
			log.error("AttendanceAction 中的importMonthAllAttendances异常!["+ e.getMessage() + "]", e);
		}
	}

	/**
	 * 导出ExcelMonthAllAttendance 对象
	 */
	public void ExcelMonthAllAttendance(List<TAttendance> ve, String startDate,String endDate)
	{
		try
		{
			// 计算导入时间
			long start = new Date().getTime();
			File outFile = new File(request.getServletContext().getRealPath("upload/"+startDate + "-" + endDate+ "KaoQin.xls")); 
			OutputStream os = new FileOutputStream(outFile);
			response.setContentType("application/force-download");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=\""+ startDate + "-" + endDate + "KaoQin.xls\"");
			response.setContentType("text/html;charset=UTF-8");
			WritableWorkbook wwb;
			os = response.getOutputStream();
			wwb = Workbook.createWorkbook(os);
			WritableSheet ws = wwb.createSheet("" + startDate + "-" + endDate + "考勤统计", 0);
			ws.addCell(new jxl.write.Label(0, 1, "姓名"));
			ws.addCell(new jxl.write.Label(1, 1, "考勤日期"));
			ws.addCell(new jxl.write.Label(2, 1, "星期"));
			ws.addCell(new jxl.write.Label(3, 1, "班次"));
			ws.addCell(new jxl.write.Label(4, 1, "上班时间"));
			ws.addCell(new jxl.write.Label(5, 1, "下班时间"));
			ws.addCell(new jxl.write.Label(6, 1, "中午上班时间"));
			ws.addCell(new jxl.write.Label(7, 1, "中午下班时间"));
			ws.addCell(new jxl.write.Label(8, 1, "无早打卡"));
			ws.addCell(new jxl.write.Label(9, 1, "无晚打卡"));
			ws.addCell(new jxl.write.Label(10, 1, "夜宵补助"));
			ws.addCell(new jxl.write.Label(11, 1, "可否角逐满勤"));
			ws.addCell(new jxl.write.Label(12, 1, "迟到可能"));
			ws.addCell(new jxl.write.Label(13, 1, "当日实际小时数(不算加班)"));
			ws.addCell(new jxl.write.Label(14, 1, "当日加班(时)"));
			ws.addCell(new jxl.write.Label(15, 1, "当日总小时数(时)"));
			ws.addCell(new jxl.write.Label(16, 1, "请假详细"));
			int demoSize = ve.size();
			TAttendance demo = new TAttendance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < demoSize; i++)
			{
				demo = (TAttendance) ve.get(i);
				ws.addCell(new jxl.write.Label(0, i + 2, demo.getName()));
				ws.addCell(new jxl.write.Label(1, i + 2, !demo.getaDate().equals("")?sdf.format(demo.getaDate()):""));
				ws.addCell(new jxl.write.Label(2, i + 2, demo.getWeekDay()));
				ws.addCell(new jxl.write.Label(3, i + 2, demo.getWorkTime()));
				ws.addCell(new jxl.write.Label(4, i + 2, demo.getStartWork()));
				ws.addCell(new jxl.write.Label(5, i + 2, demo.getEndWork()));
				ws.addCell(new jxl.write.Label(6, i + 2, demo.getStartWork2()));
				ws.addCell(new jxl.write.Label(7, i + 2, demo.getEndWork2()));
				ws.addCell(new jxl.write.Label(8, i + 2, demo.getNoEarlyPunch()==0?"否":"是"));
				ws.addCell(new jxl.write.Label(9, i + 2, demo.getNoLatePunch()==0?"否":"是"));
				ws.addCell(new jxl.write.Label(10, i + 2, demo.getSupper()==0?"无":"有"));
				ws.addCell(new jxl.write.Label(11, i + 2, demo.getWorkFullHours()==0?"No":"Yes"));
				ws.addCell(new jxl.write.Label(12, i + 2, demo.getIsBeLate()==0?"无":"有"));
				ws.addCell(new jxl.write.Label(13, i + 2, demo.getRealTimeAttendance()));
				ws.addCell(new jxl.write.Label(14, i + 2, demo.getPlusWork()));
				ws.addCell(new jxl.write.Label(15, i + 2, demo.getUpWork()));
				ws.addCell(new jxl.write.Label(16, i + 2, demo.getPersonalLeaveDetailed()));
			}
			ws.addCell(new jxl.write.Label(0, 0, startDate + "-" + endDate+"考勤统计"));
			long end = new Date().getTime();
			SaveLog(startDate + "-" + endDate+"某时间段数据导出成功,耗时:" + (end - start) / 1000 + "秒" );
			System.out.println(startDate + "-" + endDate+"某时间段数据导出成功,耗时:" + (end - start) / 1000 + "秒");
			wwb.write();
			// 关闭Excel工作薄对象
			wwb.close();
			os.flush();
			os.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public TAttendanceService getAttendanceService()
	{
		return attendanceService;
	}

	public void setAttendanceService(TAttendanceService attendanceService)
	{
		this.attendanceService = attendanceService;
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

	public TPersonalLeaveService getPersonalLeaveService()
	{
		return personalLeaveService;
	}

	public void setPersonalLeaveService(
			TPersonalLeaveService personalLeaveService)
	{
		this.personalLeaveService = personalLeaveService;
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

	
}
