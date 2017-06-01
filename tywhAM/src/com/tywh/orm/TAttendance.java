package com.tywh.orm;

import java.io.Serializable;
import java.util.Date;

/**
 *	TAttendance 考勤表
 *  author：杜泉
 *  2014-11-3 上午9:05:59
 */
public class TAttendance implements Serializable
{
	private static final long	serialVersionUID	= -7841556996658041956L;
	private int id;//编号id
	private String numId;//人员编号
	private String name;//姓名
	private Date aDate;//考勤日期
	private String weekDay;//星期
	private String startWork;//上班
	private String endWork;//下班
	private String startWork2;//中午上班
	private String endWork2;//中午下班
	private String workTime;//白班
	private String upWork;//上班小时数
	private String plusWork;//平时加班
	private String holidayWork;//公休加班
	private String absenceWork;//缺勤
	private int absenceCount;//缺勤次数
	private int noEarlyPunch;//无早打卡 0为否 1为是
	private int noLatePunch;//无晚打卡 0为否 1为是
	private int supper;//是否夜宵补助 0为否 1为是
	private int workFullHours;//是否当天满勤 0为否 1为是
	private int isBeLate;//是否有迟到可能性 0为否 1为是 
	private String realTimeAttendance;//当日实际出勤时间
	private Date createTime;//创建时间
	private Date updateTime;//修改时间
	private String createPersonnel;//创建人
	private String operatingPersonnel;//操作人
	private String personalLeaveDetailed;//请假详细
	private String beLateCount;// 迟到次数 通过某天是否又迟到可能然后  查询在请假范围内 是否有请假
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getNumId()
	{
		return numId;
	}
	public void setNumId(String numId)
	{
		this.numId = numId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Date getaDate()
	{
		return aDate;
	}
	public void setaDate(Date aDate)
	{
		this.aDate = aDate;
	}
	public String getWeekDay()
	{
		return weekDay;
	}
	public void setWeekDay(String weekDay)
	{
		this.weekDay = weekDay;
	}
	public String getStartWork()
	{
		return startWork;
	}
	public void setStartWork(String startWork)
	{
		this.startWork = startWork;
	}
	public String getEndWork()
	{
		return endWork;
	}
	public void setEndWork(String endWork)
	{
		this.endWork = endWork;
	}
	public String getStartWork2()
	{
		return startWork2;
	}
	public void setStartWork2(String startWork2)
	{
		this.startWork2 = startWork2;
	}
	public String getEndWork2()
	{
		return endWork2;
	}
	public void setEndWork2(String endWork2)
	{
		this.endWork2 = endWork2;
	}
	public String getWorkTime()
	{
		return workTime;
	}
	public void setWorkTime(String workTime)
	{
		this.workTime = workTime;
	}
	public String getPlusWork()
	{
		return plusWork;
	}
	public void setPlusWork(String plusWork)
	{
		this.plusWork = plusWork;
	}
	public String getHolidayWork()
	{
		return holidayWork;
	}
	public void setHolidayWork(String holidayWork)
	{
		this.holidayWork = holidayWork;
	}
	public String getAbsenceWork()
	{
		return absenceWork;
	}
	public void setAbsenceWork(String absenceWork)
	{
		this.absenceWork = absenceWork;
	}
	public int getAbsenceCount()
	{
		return absenceCount;
	}
	public void setAbsenceCount(int absenceCount)
	{
		this.absenceCount = absenceCount;
	}
	public int getNoEarlyPunch()
	{
		return noEarlyPunch;
	}
	public void setNoEarlyPunch(int noEarlyPunch)
	{
		this.noEarlyPunch = noEarlyPunch;
	}
	public int getNoLatePunch()
	{
		return noLatePunch;
	}
	public void setNoLatePunch(int noLatePunch)
	{
		this.noLatePunch = noLatePunch;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	public Date getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}
	public String getCreatePersonnel()
	{
		return createPersonnel;
	}
	public void setCreatePersonnel(String createPersonnel)
	{
		this.createPersonnel = createPersonnel;
	}
	public String getOperatingPersonnel()
	{
		return operatingPersonnel;
	}
	public void setOperatingPersonnel(String operatingPersonnel)
	{
		this.operatingPersonnel = operatingPersonnel;
	}
	public String getPersonalLeaveDetailed()
	{
		return personalLeaveDetailed;
	}
	public void setPersonalLeaveDetailed(String personalLeaveDetailed)
	{
		this.personalLeaveDetailed = personalLeaveDetailed;
	}
	public String getUpWork()
	{
		return upWork;
	}
	public void setUpWork(String upWork)
	{
		this.upWork = upWork;
	}
	public int getSupper()
	{
		return supper;
	}
	public void setSupper(int supper)
	{
		this.supper = supper;
	}
	public int getWorkFullHours()
	{
		return workFullHours;
	}
	public void setWorkFullHours(int workFullHours)
	{
		this.workFullHours = workFullHours;
	}
	public int getIsBeLate()
	{
		return isBeLate;
	}
	public void setIsBeLate(int isBeLate)
	{
		this.isBeLate = isBeLate;
	}
	public String getRealTimeAttendance()
	{
		return realTimeAttendance;
	}
	public void setRealTimeAttendance(String realTimeAttendance)
	{
		this.realTimeAttendance = realTimeAttendance;
	}
	public String getBeLateCount()
	{
		return beLateCount;
	}
	public void setBeLateCount(String beLateCount)
	{
		this.beLateCount = beLateCount;
	}
	
}
