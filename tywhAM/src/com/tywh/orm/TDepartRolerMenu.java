package com.tywh.orm;

import java.io.Serializable;

/**
 *	TDepartRolerMenu  部门 角色 菜单 关联表
 *  author：杜泉
 *  2014-6-24 上午11:20:10
 */
public class TDepartRolerMenu implements Serializable
{ 
	private static final long	serialVersionUID	= 3077370700527928100L;
	private int t_id;//编号id
	private int t_daptId;//部门id
	private int t_roleId;//角色id
	private int t_menuId;//菜单id
	
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
	public int getT_roleId()
	{
		return t_roleId;
	}
	public void setT_roleId(int t_roleId)
	{
		this.t_roleId = t_roleId;
	}
	public int getT_menuId()
	{
		return t_menuId;
	}
	public void setT_menuId(int t_menuId)
	{
		this.t_menuId = t_menuId;
	}
}
