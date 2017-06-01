package com.tywh.service;

import java.text.ParseException;
import java.util.List;

import com.tywh.dao.AttendanceDao;
import com.tywh.orm.TAttendance;
import com.tywh.orm.TAttendancePeople;
import com.tywh.orm.TUserinfo;
import com.tywh.util.Page;

/**
 *	TAttendanceService
 *  author：杜泉
 *  2014-11-3 上午10:21:40
 */
public class TAttendanceService
{
	 private AttendanceDao  attendanceDao;

	public AttendanceDao getAttendanceDao()
	{
		return attendanceDao;
	}

	public void setAttendanceDao(AttendanceDao attendanceDao)
	{
		this.attendanceDao = attendanceDao;
	}

	public List<TAttendance> queryAttendanceList(String startDate,String endDate, String userName,String numId, String daptId, Page page) throws ParseException
	{
		page.setTotalRecord(attendanceDao.query(startDate,endDate,userName,numId,daptId));
		return attendanceDao.query(startDate,endDate,userName,numId,daptId,page.getStart(),page.getEnd());
	}

	public List<TAttendancePeople> queryAllAttendancePeople() throws ParseException
	{
		return attendanceDao.queryAllAttendancePeople();
	}

	//查询所有的考勤人员名称
	public List<String> queryAllAttendancePeopleName()
	{
		return attendanceDao.queryAllAttendancePeopleName();
	}

	//保存所有用户的名称到 用户名称表中
	public void addAttendancePeople(TAttendancePeople tap)
	{
		attendanceDao.addAttendancePeople(tap);
	}

    //然后循环list中的数据 保存到数据库中
	public void addAttendance(TAttendance ta)
	{
		attendanceDao.addAttendance(ta);
	}

	 //判断是否重名
	public boolean repeatName(String name)
	{
		return attendanceDao.repeatName(name);
	}

	//通过日期与人员信息获取当天的考勤状况
	public TAttendance queryAttendance(String date, String num, String name) throws ParseException
	{
		return  attendanceDao.queryAttendance(date,num,name);
	}
	
	//查询考勤用户的名单
	public TAttendancePeople queryAttendancePeople(TAttendancePeople tp) throws ParseException
	{	
		return attendanceDao.queryAttendancePeople(tp);
	}

	//查询考勤用户的名单分页
	public List<TAttendancePeople> queryAllAttendancePeople(Page page) throws ParseException
	{
		page.setTotalRecord(attendanceDao.queryAll());
		return attendanceDao.queryAll(page.getStart(),page.getEnd());
	}
	
 	//考勤插入用户数据
	public void saveTUserinfo(TUserinfo tui)
	{
		attendanceDao.saveTUserinfo(tui);
	}
	
	//根据年月查询考勤人
	public List<TAttendancePeople> queryAttendanceUser(String year, String month)
	{
		return attendanceDao.queryAttendanceUser(year,month);
	}

	//获取部分考勤集合
	public TAttendance queryAttendanceInfo(String year, String month, String num, int isWeek)
	{
		return attendanceDao.queryAttendanceInfo(year,month,num,isWeek);
	}

	//查询周日的考勤情况
	public List<TAttendance> queryAttendances(String year, String month,String num, String name, String weeks) throws ParseException
	{
		return attendanceDao.queryAttendances(year,month,num,name,weeks);
	}

	//查询当前部门的人员
	public List<TAttendancePeople> queryAttendanceListForDapt(String startDate,String endDate, String daptId)
	{
		return attendanceDao.queryAttendanceListForDapt(startDate,endDate,daptId);
	}

	//查询某时间段的所有考勤
	public List<TAttendance> queryAttendanceList(String startDate,String endDate) throws ParseException
	{
		return attendanceDao.query(startDate,endDate);
	}

}
