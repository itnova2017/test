package com.tywh.orm;

import java.io.Serializable;

/**
 * TSuperModel 导出考勤的NB超级综合Model 哈哈 author：杜泉 2015-1-8 下午4:20:33
 */
public class TSuperModel implements Serializable
{
	private static final long	serialVersionUID	= 1L;
	private String				daptName;					// 大部门
	private String				roleName;					// 角色
	private String				userName;					// 姓名
	private String				plusWorks;					// 加班小时数
	private String				weekPlusWorks;				// 周六（或周日）加班
	private String				plusWorkSum;				// 加班合计
	private String				leaveStatistics;			// 事假合计
	private String				sickLeaveStatistics;		// 病假合计
	private String				otherLeaveStatistics;		// 其他请假合计(不算病假、事假)
	private String				leaveHourStatisicsTime;	// 事假超8.5小时*2倍扣罚
	private String				leaveCountStatisticsTime;	// 事假超3次*5倍扣罚
	private String				latePenaltyMoney;			// 迟到罚钱合计
	private String				leaveHoursStatisics;		// 合计扣发工时
	private String				leaveCountStatistics;		// 请假次数 (本月的请假表  Count)
	private String				beLateCount;				// 迟到次数 通过某天是否又迟到可能然后  查询在请假范围内 是否有请假
	private String				supperSum;					// 夜宵补助 统计集合
	private String				realTime;					// 完成工时 （可以算加班也可以单独算出勤时间 或是单独的把周六的出勤算在里面）
	private String				isWorkFullHours;			// 满勤奖
	private String				remark;					// 备注

	public String getDaptName()
	{
		return daptName;
	}

	public void setDaptName(String daptName)
	{
		this.daptName = daptName;
	}

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPlusWorks()
	{
		return plusWorks;
	}

	public void setPlusWorks(String plusWorks)
	{
		this.plusWorks = plusWorks;
	}

	public String getWeekPlusWorks()
	{
		return weekPlusWorks;
	}

	public void setWeekPlusWorks(String weekPlusWorks)
	{
		this.weekPlusWorks = weekPlusWorks;
	}

	public String getPlusWorkSum()
	{
		return plusWorkSum;
	}

	public void setPlusWorkSum(String plusWorkSum)
	{
		this.plusWorkSum = plusWorkSum;
	}

	public String getLeaveStatistics()
	{
		return leaveStatistics;
	}

	public void setLeaveStatistics(String leaveStatistics)
	{
		this.leaveStatistics = leaveStatistics;
	}

	public String getSickLeaveStatistics()
	{
		return sickLeaveStatistics;
	}

	public void setSickLeaveStatistics(String sickLeaveStatistics)
	{
		this.sickLeaveStatistics = sickLeaveStatistics;
	}

	public String getOtherLeaveStatistics()
	{
		return otherLeaveStatistics;
	}

	public void setOtherLeaveStatistics(String otherLeaveStatistics)
	{
		this.otherLeaveStatistics = otherLeaveStatistics;
	}

	public String getLeaveHourStatisicsTime()
	{
		return leaveHourStatisicsTime;
	}

	public void setLeaveHourStatisicsTime(String leaveHourStatisicsTime)
	{
		this.leaveHourStatisicsTime = leaveHourStatisicsTime;
	}

	public String getLeaveCountStatisticsTime()
	{
		return leaveCountStatisticsTime;
	}

	public void setLeaveCountStatisticsTime(String leaveCountStatisticsTime)
	{
		this.leaveCountStatisticsTime = leaveCountStatisticsTime;
	}

	public String getLatePenaltyMoney()
	{
		return latePenaltyMoney;
	}

	public void setLatePenaltyMoney(String latePenaltyMoney)
	{
		this.latePenaltyMoney = latePenaltyMoney;
	}

	public String getLeaveHoursStatisics()
	{
		return leaveHoursStatisics;
	}

	public void setLeaveHoursStatisics(String leaveHoursStatisics)
	{
		this.leaveHoursStatisics = leaveHoursStatisics;
	}

	public String getLeaveCountStatistics()
	{
		return leaveCountStatistics;
	}

	public void setLeaveCountStatistics(String leaveCountStatistics)
	{
		this.leaveCountStatistics = leaveCountStatistics;
	}

	public String getBeLateCount()
	{
		return beLateCount;
	}

	public void setBeLateCount(String beLateCount)
	{
		this.beLateCount = beLateCount;
	}

	public String getSupperSum()
	{
		return supperSum;
	}

	public void setSupperSum(String supperSum)
	{
		this.supperSum = supperSum;
	}

	public String getRealTime()
	{
		return realTime;
	}

	public void setRealTime(String realTime)
	{
		this.realTime = realTime;
	}

	public String getIsWorkFullHours()
	{
		return isWorkFullHours;
	}

	public void setIsWorkFullHours(String isWorkFullHours)
	{
		this.isWorkFullHours = isWorkFullHours;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

}
