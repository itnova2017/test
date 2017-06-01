package com.tywh.service;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tywh.dao.UserInfoDao;
import com.tywh.orm.TDepartment;
import com.tywh.orm.TUserinfo;
import com.tywh.util.Page;

/**
 *	TUserInfoService 用户相关的Service
 *  author：杜泉
 *  2014-6-27 上午10:14:03
 */
public class TUserInfoService
{
	private UserInfoDao  userInfoDao;
	
	private static Log log = LogFactory.getLog(TUserInfoService.class);
	
	/**
	 * 查询所有的用户方法
	 * @throws ParseException 
	 */
	public List<TUserinfo> queryAllUserList(String daptId,String userName,Page page) throws ParseException {
		page.setTotalRecord(userInfoDao.query(daptId,userName));
		return userInfoDao.query(daptId,userName,page.getStart(),page.getEnd());
	}

	
	/**
	 * 用户的删除方法  删除用户只是对该用户的isUse字段进行修改 变为不可用 所有数据进行保留
	 */
	public void deleteUserById(String userId)
	{
		userInfoDao.deleteUserById(userId);
		//删除部门与用户表数据
		userInfoDao.deleteDaptUserById(userId);
		//删除部门用户角色表数据
		userInfoDao.deleteDaptRoleUserById(userId);
	}
	
	/**
	 * 查询当前的登录名是否唯一
	 */
	public boolean repeatName(String name)
	{
		return userInfoDao.repeatName(name);
	}
	
	/**
	 * 添加人员
	 */
	public void saveUserInfo(TUserinfo tui)
	{
		userInfoDao.saveUserInfo(tui);
	}
	
	/**
	 * 通过用户的信息查询用户
	 * @throws ParseException 
	 */
	public TUserinfo queryUserInfo(TUserinfo tu) throws ParseException
	{
		return userInfoDao.queryUserInfo(tu);
	}
	
	/**
	 * 编辑保存人员数据
	 * @throws ParseException 
	 */
	public void editUserInfo(TUserinfo tui)
	{
		userInfoDao.editUserInfo(tui);
	}
	
	/**
	 * 通过部门ID 查询用户集合
	 * @throws ParseException 
	 */
	public List<TUserinfo> queryUserByDepart(String daptId) throws ParseException
	{
		return userInfoDao.queryUserByDepart(daptId);
	}
	
	/**
	 * 根据用户信息查询 本人的 部门  角色   名称  。。。。
	 */
	public List<TUserinfo> queryUDRs(TUserinfo tu)
	{
		return userInfoDao.queryUDRs(tu);
	}
	
	public UserInfoDao getUserInfoDao()
	{
		return userInfoDao;
	}

	public void setUserInfoDao(UserInfoDao userInfoDao)
	{
		this.userInfoDao = userInfoDao;
	}



	











	
	
	
	
}
