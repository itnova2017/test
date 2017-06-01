package com.tywh.orm;

import java.io.Serializable;
import java.util.Date;

/**
 *	TRoleBusiness
 *  author：杜泉
 *  2014-7-5 上午10:53:22
 */
public class TRoleBusiness implements Serializable
{
	private static final long	serialVersionUID	= -2869282943801263632L;
	private int t_id;//编号id
	private int t_busiId;//业务id
	private int t_roleId;//角色id
	private int t_user;//最后操作人
	private Date t_updateTime;//最后操作时间
	
	public int getT_id()
	{
		return t_id;
	}
	public void setT_id(int t_id)
	{
		this.t_id = t_id;
	}
	public int getT_busiId()
	{
		return t_busiId;
	}
	public void setT_busiId(int t_busiId)
	{
		this.t_busiId = t_busiId;
	}
	public int getT_roleId()
	{
		return t_roleId;
	}
	public void setT_roleId(int t_roleId)
	{
		this.t_roleId = t_roleId;
	}
	public int getT_user()
	{
		return t_user;
	}
	public void setT_user(int t_user)
	{
		this.t_user = t_user;
	}
	public Date getT_updateTime()
	{
		return t_updateTime;
	}
	public void setT_updateTime(Date t_updateTime)
	{
		this.t_updateTime = t_updateTime;
	}
}
