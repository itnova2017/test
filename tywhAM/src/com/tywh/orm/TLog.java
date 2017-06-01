package com.tywh.orm;

import java.io.Serializable;
import java.util.Date;

/**
 *	TLog
 *  author：杜泉
 *  2014-7-14 下午3:39:33
 */
public class TLog implements Serializable
{
	private static final long	serialVersionUID	= -1609898776305093750L;
	private int t_id;
	private String t_content;
	private String t_user;
	private Date t_operationTime;
	
	public int getT_id()
	{
		return t_id;
	}
	public void setT_id(int t_id)
	{
		this.t_id = t_id;
	}
	public String getT_content()
	{
		return t_content;
	}
	public void setT_content(String t_content)
	{
		this.t_content = t_content;
	}
	public String getT_user()
	{
		return t_user;
	}
	public void setT_user(String t_user)
	{
		this.t_user = t_user;
	}
	public Date getT_operationTime()
	{
		return t_operationTime;
	}
	public void setT_operationTime(Date t_operationTime)
	{
		this.t_operationTime = t_operationTime;
	}
}
