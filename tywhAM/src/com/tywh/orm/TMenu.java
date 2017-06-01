package com.tywh.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *	TMenu  菜单类
 *  author：杜泉
 *  2014-6-24  上午10:45:07
 */
public class TMenu implements Serializable
{	
	private static final long	serialVersionUID	= -8060162232333693310L;
	
	private 	int t_id;//菜单id
	private 	String t_name	;//菜单名称
	private 	String t_content;//菜单描述
	private 	int t_parentId;//菜单父节点id
	private 	int t_state;//菜单状态 0:无效 1:有效
	private 	String t_num;//菜单编号(主要用于资源)
	private 	int t_order;//菜单位置排序
	private 	String t_url;//菜单url
	private 	String t_parameter;//菜单参数
	private 	String t_remark;//菜单备注
	private 	Date t_creartTime;//创建时间
	private 	int t_user;//最后操作人
	private 	Date t_updateTime;//最后操作时间
    private     Set roles = new HashSet();//所有角色集合
    private     List children = new ArrayList();//所有子节点的集合
	private 	String t_parentName	;//父菜单名称
	
	
	
	public int getT_id()
	{
		return t_id;
	}
	public void setT_id(int t_id)
	{
		this.t_id = t_id;
	}
	public String getT_name()
	{
		return t_name;
	}
	public void setT_name(String t_name)
	{
		this.t_name = t_name;
	}
	public String getT_content()
	{
		return t_content;
	}
	public void setT_content(String t_content)
	{
		this.t_content = t_content;
	}
	public int getT_parentId()
	{
		return t_parentId;
	}
	public void setT_parentId(int t_parentId)
	{
		this.t_parentId = t_parentId;
	}
	public int getT_state()
	{
		return t_state;
	}
	public void setT_state(int t_state)
	{
		this.t_state = t_state;
	}
	public String getT_num()
	{
		return t_num;
	}
	public void setT_num(String t_num)
	{
		this.t_num = t_num;
	}
	public int getT_order()
	{
		return t_order;
	}
	public void setT_order(int t_order)
	{
		this.t_order = t_order;
	}
	public String getT_url()
	{
		return t_url;
	}
	public void setT_url(String t_url)
	{
		this.t_url = t_url;
	}
	public String getT_parameter()
	{
		return t_parameter;
	}
	public void setT_parameter(String t_parameter)
	{
		this.t_parameter = t_parameter;
	}
	public String getT_remark()
	{
		return t_remark;
	}
	public void setT_remark(String t_remark)
	{
		this.t_remark = t_remark;
	}
	public Date getT_creartTime()
	{
		return t_creartTime;
	}
	public void setT_creartTime(Date t_creartTime)
	{
		this.t_creartTime = t_creartTime;
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
	public Set getRoles()
	{
		return roles;
	}
	public void setRoles(Set roles)
	{
		this.roles = roles;
	}
	public List getChildren()
	{
		return children;
	}
	public void setChildren(List children)
	{
		this.children = children;
	}
	public String getT_parentName()
	{
		return t_parentName;
	}
	public void setT_parentName(String t_parentName)
	{
		this.t_parentName = t_parentName;
	}
	
	
}
