package com.tywh.orm;

import java.io.Serializable;
import java.util.Date;

/**
 *	TRole   角色表
 *  author：杜泉
 *  2014-6-24 上午11:01:02
 */
public class TRole implements Serializable
{
	private static final long	serialVersionUID	= 7677816144168914587L;
	private  int t_id;//角色id
	private  String  t_roleName;//角色名称
	private  int t_departId;//部门id
	private  int t_isUse;//角色是否有效 0:无效 1:有效
	private  Date  t_createTime;//角色创建时间
	private  String  t_departName;//部门名称

	
	public int getT_id()
	{
		return t_id;
	}
	public void setT_id(int t_id)
	{
		this.t_id = t_id;
	}
	public String getT_roleName()
	{
		return t_roleName;
	}
	public void setT_roleName(String t_roleName)
	{
		this.t_roleName = t_roleName;
	}
	public int getT_departId()
	{
		return t_departId;
	}
	public void setT_departId(int t_departId)
	{
		this.t_departId = t_departId;
	}
	public int getT_isUse()
	{
		return t_isUse;
	}
	public void setT_isUse(int t_isUse)
	{
		this.t_isUse = t_isUse;
	}
	public Date getT_createTime()
	{
		return t_createTime;
	}
	public void setT_createTime(Date t_createTime)
	{
		this.t_createTime = t_createTime;
	}
	public String getT_departName()
	{
		return t_departName;
	}
	public void setT_departName(String t_departName)
	{
		this.t_departName = t_departName;
	}
}
