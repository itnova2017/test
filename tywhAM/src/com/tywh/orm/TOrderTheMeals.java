package com.tywh.orm;

import java.util.Date;

/**
 *	TOrderTheMeals 订餐表
 *  author：杜泉
 *  2014-12-16 上午10:30:31
 */
public class TOrderTheMeals
{
	private int t_id;//订餐Id
	private int t_userId;//订餐人ID
	private Date t_createTime;//订饭日期
	private String t_week;//星期
	private String userName;//用户名
	private String userNum;//用户编号
	private String time;//订餐时间
	private int stateIsDel;//订餐删除状态之前   下午3点之后不可删除了
	
	
	
	public int getT_id()
	{
		return t_id;
	}
	public void setT_id(int t_id)
	{
		this.t_id = t_id;
	}
	public int getT_userId()
	{
		return t_userId;
	}
	public void setT_userId(int t_userId)
	{
		this.t_userId = t_userId;
	}
	public Date getT_createTime()
	{
		return t_createTime;
	}
	public void setT_createTime(Date t_createTime)
	{
		this.t_createTime = t_createTime;
	}
	public String getT_week()
	{
		return t_week;
	}
	public void setT_week(String t_week)
	{
		this.t_week = t_week;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getUserNum()
	{
		return userNum;
	}
	public void setUserNum(String userNum)
	{
		this.userNum = userNum;
	}
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public int getStateIsDel()
	{
		return stateIsDel;
	}
	public void setStateIsDel(int stateIsDel)
	{
		this.stateIsDel = stateIsDel;
	}
}
