package com.tywh.orm;

import java.io.Serializable;
import java.util.Date;

/**
 *	TPersonalLeaveStatistics 请假统计
 *  author：杜泉
 *  2014-11-3 上午9:38:08
 */
public class TPersonalLeaveStatistics  implements Serializable
{
	private static final long	serialVersionUID	= 8995187890721353209L;
	private int t_id;//编号id
	private String t_daptId;//部门id
	private int t_userId;//请假人id
	private float t_sickLeaveStatistics;//病假统计
	private float t_leaveStatistics;//事假统计
	private float t_maritalLeaveStatistics;//婚假统计
	private float t_maternityLeaveStatistics;//产假统计
	private float t_funeralLeaveStatistics;//丧假统计
	private float t_drivingLeaveStatistics;//驾校假统计
	private float t_businessLeaveStatistics;//公出统计
	private float t_stringBreakLeaveStatistics;//串休统计
	private float t_lactationLeaveStatistics;//哺乳假统计
	private float t_thesisStatistics;//论文假统计
	private int t_leaveCountStatistics;//事假次数统计
	private float t_leaveHourStatisics;//请假小时数统计
	private String t_leaveYear;//请假年份
	private String t_leaveMonth;//请假月份
	private float t_monthViolationFineAggregate;//月违规罚款合计
	private String t_remarks;//月违规罚款备注
	private Date t_createTime;//创建时间
	private Date t_updateTime;//修改时间
	private String t_createPersonnel;//创建人
	private String t_operatingPersonnel;//操作人
	private float t_leaveHoursStatisics;//所有请假小时数
	private int oneApproverState;//一级审核状态 大于0 有未通过请假  0通过请假
	private int twoApproverState;//二级审核状态 大于0 有未通过请假  0通过请假
	private String userName;//用户名
	private float leaveCountStatisticsTime;//请假次数查过三次 乘以5
	private float leaveHourStatisicsTime;//请假小时数超过8.5部分乘以2
	private float maxStatisics;//事假最大罚时
	
	public int getT_id()
	{
		return t_id;
	}
	public void setT_id(int t_id)
	{
		this.t_id = t_id;
	}
	public String getT_daptId()
	{
		return t_daptId;
	}
	public void setT_daptId(String t_daptId)
	{
		this.t_daptId = t_daptId;
	}
	public int getT_userId()
	{
		return t_userId;
	}
	public void setT_userId(int t_userId)
	{
		this.t_userId = t_userId;
	}
	public float getT_sickLeaveStatistics()
	{
		return t_sickLeaveStatistics;
	}
	public void setT_sickLeaveStatistics(float t_sickLeaveStatistics)
	{
		this.t_sickLeaveStatistics = t_sickLeaveStatistics;
	}
	public float getT_leaveStatistics()
	{
		return t_leaveStatistics;
	}
	public void setT_leaveStatistics(float t_leaveStatistics)
	{
		this.t_leaveStatistics = t_leaveStatistics;
	}
	public float getT_maritalLeaveStatistics()
	{
		return t_maritalLeaveStatistics;
	}
	public void setT_maritalLeaveStatistics(float t_maritalLeaveStatistics)
	{
		this.t_maritalLeaveStatistics = t_maritalLeaveStatistics;
	}
	public float getT_maternityLeaveStatistics()
	{
		return t_maternityLeaveStatistics;
	}
	public void setT_maternityLeaveStatistics(float t_maternityLeaveStatistics)
	{
		this.t_maternityLeaveStatistics = t_maternityLeaveStatistics;
	}
	public float getT_funeralLeaveStatistics()
	{
		return t_funeralLeaveStatistics;
	}
	public void setT_funeralLeaveStatistics(float t_funeralLeaveStatistics)
	{
		this.t_funeralLeaveStatistics = t_funeralLeaveStatistics;
	}
	public float getT_drivingLeaveStatistics()
	{
		return t_drivingLeaveStatistics;
	}
	public void setT_drivingLeaveStatistics(float t_drivingLeaveStatistics)
	{
		this.t_drivingLeaveStatistics = t_drivingLeaveStatistics;
	}
	public float getT_businessLeaveStatistics()
	{
		return t_businessLeaveStatistics;
	}
	public void setT_businessLeaveStatistics(float t_businessLeaveStatistics)
	{
		this.t_businessLeaveStatistics = t_businessLeaveStatistics;
	}
	public float getT_stringBreakLeaveStatistics()
	{
		return t_stringBreakLeaveStatistics;
	}
	public void setT_stringBreakLeaveStatistics(float t_stringBreakLeaveStatistics)
	{
		this.t_stringBreakLeaveStatistics = t_stringBreakLeaveStatistics;
	}
	public int getT_leaveCountStatistics()
	{
		return t_leaveCountStatistics;
	}
	public void setT_leaveCountStatistics(int t_leaveCountStatistics)
	{
		this.t_leaveCountStatistics = t_leaveCountStatistics;
	}
	public float getT_leaveHourStatisics()
	{
		return t_leaveHourStatisics;
	}
	public void setT_leaveHourStatisics(float t_leaveHourStatisics)
	{
		this.t_leaveHourStatisics = t_leaveHourStatisics;
	}
	public String getT_leaveMonth()
	{
		return t_leaveMonth;
	}
	public void setT_leaveMonth(String t_leaveMonth)
	{
		this.t_leaveMonth = t_leaveMonth;
	}
	public float getT_monthViolationFineAggregate()
	{
		return t_monthViolationFineAggregate;
	}
	public void setT_monthViolationFineAggregate(float t_monthViolationFineAggregate)
	{
		this.t_monthViolationFineAggregate = t_monthViolationFineAggregate;
	}
	public String getT_remarks()
	{
		return t_remarks;
	}
	public void setT_remarks(String t_remarks)
	{
		this.t_remarks = t_remarks;
	}
	public Date getT_createTime()
	{
		return t_createTime;
	}
	public void setT_createTime(Date t_createTime)
	{
		this.t_createTime = t_createTime;
	}
	public Date getT_updateTime()
	{
		return t_updateTime;
	}
	public void setT_updateTime(Date t_updateTime)
	{
		this.t_updateTime = t_updateTime;
	}
	public String getT_createPersonnel()
	{
		return t_createPersonnel;
	}
	public void setT_createPersonnel(String t_createPersonnel)
	{
		this.t_createPersonnel = t_createPersonnel;
	}
	public String getT_operatingPersonnel()
	{
		return t_operatingPersonnel;
	}
	public void setT_operatingPersonnel(String t_operatingPersonnel)
	{
		this.t_operatingPersonnel = t_operatingPersonnel;
	}
	public float getT_lactationLeaveStatistics()
	{
		return t_lactationLeaveStatistics;
	}
	public void setT_lactationLeaveStatistics(float t_lactationLeaveStatistics)
	{
		this.t_lactationLeaveStatistics = t_lactationLeaveStatistics;
	}
	public float getT_leaveHoursStatisics()
	{
		return t_leaveHoursStatisics;
	}
	public void setT_leaveHoursStatisics(float t_leaveHoursStatisics)
	{
		this.t_leaveHoursStatisics = t_leaveHoursStatisics;
	}
	public String getT_leaveYear()
	{
		return t_leaveYear;
	}
	public void setT_leaveYear(String t_leaveYear)
	{
		this.t_leaveYear = t_leaveYear;
	}
	public int getOneApproverState()
	{
		return oneApproverState;
	}
	public void setOneApproverState(int oneApproverState)
	{
		this.oneApproverState = oneApproverState;
	}
	public int getTwoApproverState()
	{
		return twoApproverState;
	}
	public void setTwoApproverState(int twoApproverState)
	{
		this.twoApproverState = twoApproverState;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public float getLeaveCountStatisticsTime()
	{
		return leaveCountStatisticsTime;
	}
	public void setLeaveCountStatisticsTime(float leaveCountStatisticsTime)
	{
		this.leaveCountStatisticsTime = leaveCountStatisticsTime;
	}
	public float getLeaveHourStatisicsTime()
	{
		return leaveHourStatisicsTime;
	}
	public void setLeaveHourStatisicsTime(float leaveHourStatisicsTime)
	{
		this.leaveHourStatisicsTime = leaveHourStatisicsTime;
	}
	public float getMaxStatisics()
	{
		return maxStatisics;
	}
	public void setMaxStatisics(float maxStatisics)
	{
		this.maxStatisics = maxStatisics;
	}
	public float getT_thesisStatistics()
	{
		return t_thesisStatistics;
	}
	public void setT_thesisStatistics(float t_thesisStatistics)
	{
		this.t_thesisStatistics = t_thesisStatistics;
	}
}
