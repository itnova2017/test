package com.tywh.orm;

import java.io.Serializable;
import java.util.Date;

/**
 *	TPersonalLeave 请假列表
 *  author：杜泉
 *  2014-11-3 上午9:24:05
 */
public class TPersonalLeave implements Serializable
{
	private static final long	serialVersionUID	= 852494043456514877L;
	private int id;//编号id
	private String daptId;//部门id
	private int userId;//请假人id
	private int state;//请假类型 1.事假 2.病假 3.公出 4.串休 5.丧假 6.驾校假 7.产假 8.哺乳假 9.婚假 10.论文假
	private String leaveTimes;//请假时间段
	private float leaveDuration;//请假时长
	private Date leaveDay;//请假日期
	private String leaveWeekDay;//请假星期
	private String leaveType;//请假原因
	private String oneApprover;//一级审批人
	private String twoApprover;//二级审批人
	private int oneApproverState;//一级审核状态
	private int twoApproverState;//二级审核状态
	private int isTwoApprover;//是否需要二级审核
	private Date createTime;//创建时间
	private Date updateTime;//修改时间
	private String createPersonnel;//创建人
	private String operatingPersonnel;//操作人
	private Boolean isEdit;//是否可修改
	private String userName;//用户名
	private int isNoState;//不是公休和串休
	
	public Boolean getIsEdit()
	{
		return isEdit;
	}
	public void setIsEdit(Boolean isEdit)
	{
		this.isEdit = isEdit;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getDaptId()
	{
		return daptId;
	}
	public void setDaptId(String daptId)
	{
		this.daptId = daptId;
	}
	public int getUserId()
	{
		return userId;
	}
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state = state;
	}
	public String getLeaveTimes()
	{
		return leaveTimes;
	}
	public void setLeaveTimes(String leaveTimes)
	{
		this.leaveTimes = leaveTimes;
	}
	public float getLeaveDuration()
	{
		return leaveDuration;
	}
	public void setLeaveDuration(float leaveDuration)
	{
		this.leaveDuration = leaveDuration;
	}
	public Date getLeaveDay()
	{
		return leaveDay;
	}
	public void setLeaveDay(Date leaveDay)
	{
		this.leaveDay = leaveDay;
	}
	public String getLeaveType()
	{
		return leaveType;
	}
	public void setLeaveType(String leaveType)
	{
		this.leaveType = leaveType;
	}
	public String getOneApprover()
	{
		return oneApprover;
	}
	public void setOneApprover(String oneApprover)
	{
		this.oneApprover = oneApprover;
	}
	public String getTwoApprover()
	{
		return twoApprover;
	}
	public void setTwoApprover(String twoApprover)
	{
		this.twoApprover = twoApprover;
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
	public int getIsTwoApprover()
	{
		return isTwoApprover;
	}
	public void setIsTwoApprover(int isTwoApprover)
	{
		this.isTwoApprover = isTwoApprover;
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
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getLeaveWeekDay()
	{
		return leaveWeekDay;
	}
	public void setLeaveWeekDay(String leaveWeekDay)
	{
		this.leaveWeekDay = leaveWeekDay;
	}
	public int getIsNoState()
	{
		return isNoState;
	}
	public void setIsNoState(int isNoState)
	{
		this.isNoState = isNoState;
	}
}
