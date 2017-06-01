package com.tywh.orm;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 *	TMenu  菜单类
 *  author：杜泉
 *  2014-6-24  上午10:45:07
 */
public class TUserinfo implements Serializable 
{
	private static final long	serialVersionUID	= -6547034482708197319L;
	
	private 	int t_id;//用户id
	private 	String t_daptId;//部门id
	private 	String t_userName;//用户名
	private 	String t_loginName;//用户登录名
	private 	String t_password;//用户密码
	private 	String t_telphone	;//用户手机号
	private 	String t_email	;//用户邮箱
	private 	String  t_qq;//用户QQ
	private 	int t_isUse;//是否有效  0 无效 1有效
	private 	Date t_createTime;//创建时间
	private 	Date t_lastTime;//最后登录时间
    private Set<TDepartRoleUser> roles = new HashSet<TDepartRoleUser>() ;//获取的所有用户在部门中的角色
    private String  dapts;//获取的所有用户在部门的所有名称集合 拼接 用于展示在界面上
	private String isCheck="0";//用户判断是否选中
	private String t_num;//导入考勤的用户编号
	private String t_daptName;//部门名称
	private String t_roleName;//角色名称
	
	
	public int getT_id()
	{
		return t_id;
	}
	public void setT_id(int t_id)
	{
		this.t_id = t_id;
	}
	
	public String getT_daptId()
	{
		return t_daptId;
	}
	public void setT_daptId(String t_daptId)
	{
		this.t_daptId = t_daptId;
	}
	public String getT_userName()
	{
		return t_userName;
	}
	public void setT_userName(String t_userName)
	{
		this.t_userName = t_userName;
	}
	public String getT_loginName()
	{
		return t_loginName;
	}
	public void setT_loginName(String t_loginName)
	{
		this.t_loginName = t_loginName;
	}
	public String getT_password()
	{
		return t_password;
	}
	public void setT_password(String t_password)
	{
		this.t_password = t_password;
	}
	public String getT_email()
	{
		return t_email;
	}
	public void setT_email(String t_email)
	{
		this.t_email = t_email;
	}
	public String getT_qq()
	{
		return t_qq;
	}
	public void setT_qq(String t_qq)
	{
		this.t_qq = t_qq;
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
	public Date getT_lastTime()
	{
		return t_lastTime;
	}
	public void setT_lastTime(Date t_lastTime)
	{
		this.t_lastTime = t_lastTime;
	}
	public String getT_telphone()
	{
		return t_telphone;
	}
	public void setT_telphone(String t_telphone)
	{
		this.t_telphone = t_telphone;
	}
	public Set<TDepartRoleUser> getRoles()
	{
		return roles;
	}
	public void setRoles(Set<TDepartRoleUser> roles)
	{
		this.roles = roles;
	}
	public String getDapts()
	{
		return dapts;
	}
	public void setDapts(String dapts)
	{
		this.dapts = dapts;
	}
	public String getIsCheck()
	{
		return isCheck;
	}
	public void setIsCheck(String isCheck)
	{
		this.isCheck = isCheck;
	}
	public String getT_num()
	{
		return t_num;
	}
	public void setT_num(String t_num)
	{
		this.t_num = t_num;
	}
	public String getT_daptName()
	{
		return t_daptName;
	}
	public void setT_daptName(String t_daptName)
	{
		this.t_daptName = t_daptName;
	}
	public String getT_roleName()
	{
		return t_roleName;
	}
	public void setT_roleName(String t_roleName)
	{
		this.t_roleName = t_roleName;
	}
}
