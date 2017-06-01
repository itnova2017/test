package com.tywh.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tywh.dao.BaseDao;

/**
 *	TUserManagerService
 *  author：杜泉
 *  下午4:37:43
 */
public class TUserManagerService<T>
{
	private BaseDao dao;
	
	private static Log log = LogFactory.getLog(TUserManagerService.class);
	
	public T doLogin(String userName,String password) {
		try
		{
			if(userName == null || password == null) return null;
			//加密的密码  通过类解析
			String queryString = "SELECT u FROM TUserinfo u WHERE u.t_loginName = '"+userName +"' AND u.t_password = '"+password+"'  and u.t_isUse =1";
			List<T> users = dao.getObjects(queryString);
			if(users!=null  &&  users.size()>0)
			{
				return users.get(0);
			}
			return null;
		}
		catch (Exception e)
		{
			//在此捕获“异常”
			e.printStackTrace(); 
			log.error("异常![" + e.getMessage() + "]", e);
		}
		return null;
	}
	
	public void addUser(T user) throws Exception{
		dao.addObject(user);
	}
	
	public void modifyUser(T user) throws Exception{
		dao.updateObject(user);
	}
	
	public void deleteUser(int id,Class<T> clazz) throws Exception{
		T u = dao.getObject(clazz, id);
		dao.deleteObject(u);
	}
	
	public T getUser(Class<T> clazz, int id){
		return dao.getObject(clazz, id);
	}
	
	public BaseDao getDao() {
		return dao;
	}

	public void setDao(BaseDao dao) {
		this.dao = dao;
	}
}
