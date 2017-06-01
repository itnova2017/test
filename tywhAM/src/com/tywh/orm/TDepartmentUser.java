package com.tywh.orm;

import java.io.Serializable;

/**
 *	TDepartmentUser  部门与人员关联表
 *  author：杜泉
 *  2014-6-24 上午10:54:57
 */
public class TDepartmentUser implements Serializable
{
	private static final long	serialVersionUID	= 3426005381259880471L;
	private int t_id;//编号id
	private int t_departId;//部门id
	private int t_userId;//用户id
	
	public int getT_id()
	{
		return t_id;
	}
	public void setT_id(int t_id)
	{
		this.t_id = t_id;
	}
	public int getT_departId()
	{
		return t_departId;
	}
	public void setT_departId(int t_departId)
	{
		this.t_departId = t_departId;
	}
	public int getT_userId()
	{
		return t_userId;
	}
	public void setT_userId(int t_userId)
	{
		this.t_userId = t_userId;
	}
}
