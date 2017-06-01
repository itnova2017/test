package com.tywh.orm;

import java.io.Serializable;
import java.util.Date;

/**
 *	TDepartment   部门表
 *  author：杜泉
 *  2014-6-24 上午10:52:08
 */
public class TDepartment implements Serializable
{	
	private static final long	serialVersionUID	= 1109778423327572060L;
	private int t_id;//部门id
	private String t_departName;//部门名称
	private Date t_createTime;//部门创建时间
	private int t_isUse;//是否有效   
	private String isCheck="0";//用户判断是否选中
	
	
	public int getT_id()
	{
		return t_id;
	}
	public void setT_id(int t_id)
	{
		this.t_id = t_id;
	}
	public String getT_departName()
	{
		return t_departName;
	}
	public void setT_departName(String t_departName)
	{
		this.t_departName = t_departName;
	}
	public Date getT_createTime()
	{
		return t_createTime;
	}
	public void setT_createTime(Date t_createTime)
	{
		this.t_createTime = t_createTime;
	}
	public int getT_isUse()
	{
		return t_isUse;
	}
	public void setT_isUse(int t_isUse)
	{
		this.t_isUse = t_isUse;
	}
	public String getIsCheck()
	{
		return isCheck;
	}
	public void setIsCheck(String isCheck)
	{
		this.isCheck = isCheck;
	}
}
