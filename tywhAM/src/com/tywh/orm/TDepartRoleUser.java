package com.tywh.orm;

import java.io.Serializable;

/**
 *	TDepartRoleUser  部门角色人员表
 *  author：杜泉
 *  2014-6-24 上午10:58:04
 */
public class TDepartRoleUser implements Serializable
{
	private static final long	serialVersionUID	= 1766345764999340381L;
	private int t_id;//编号id
	private int t_daptId;//部门id
	private int t_userId;//用户id
	private int t_roleId;//角色id
	
	public int getT_id()
	{
		return t_id;
	}
	public void setT_id(int t_id)
	{
		this.t_id = t_id;
	}
	public int getT_daptId()
	{
		return t_daptId;
	}
	public void setT_daptId(int t_daptId)
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
	public int getT_roleId()
	{
		return t_roleId;
	}
	public void setT_roleId(int t_roleId)
	{
		this.t_roleId = t_roleId;
	}
}
