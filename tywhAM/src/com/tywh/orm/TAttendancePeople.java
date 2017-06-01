package com.tywh.orm;

import java.io.Serializable;
import java.util.Date;

/**
 *	TAttendancePeople 请假人员列表
 *  author：杜泉
 *  2014-11-3 上午9:18:09
 */
public class TAttendancePeople implements Serializable
{
	private static final long	serialVersionUID	= 93771616047300201L;
	private int id;//编号id
	private String name;//人员姓名
	private String num;//人员编号
	private Date createTime;//创建时间
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getNum()
	{
		return num;
	}
	public void setNum(String num)
	{
		this.num = num;
	}
	public Date getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
}
