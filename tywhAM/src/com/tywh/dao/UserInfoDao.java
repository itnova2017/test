package com.tywh.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.tywh.orm.TUserinfo;

/**
 *	UserInfoDao 用户的Dao
 *  author：杜泉
 *  2014-6-27 上午10:51:04
 */
public class UserInfoDao  extends HibernateDaoSupport
{
	private static Log log = LogFactory.getLog(UserInfoDao.class);
	/**
	 * 查询用户的总数
	 */
	public int query(String daptId, String userName)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select  COUNT(1)  from tywh_userinfo  tui  where 1=1 ";
		
		if(daptId!=null && daptId.trim().length() > 0)
		{
			sql+=" and trim(tui.t_daptId) like  ? ";
			obs.add("%,"+daptId+",%");
		}
		
		if(userName!=null && userName.trim().length() > 0)
		{
			sql+=" and  trim(tui.t_userName) like ? ";
			obs.add("%"+userName+"%");
		}
		
		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		Number n = (Number) q.uniqueResult();
		
		
		if(session!=null)
		{
			session.close();
		}
		return n.intValue();
	}

	public List<TUserinfo> query(String daptId, String userName, int start,int end) throws ParseException
	{
			log.info("查询所有的用户");
			//通过userId查询当前用户的所有角色
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			List<TUserinfo> list=new ArrayList<TUserinfo>();
			String sql=null;
			List<Object> obs=new ArrayList<Object>();
			
			sql="select  tui.t_id,tui.t_daptId,tui.t_userName,tui.t_loginName,tui.t_password,tui.t_telphone,tui.t_email,tui.t_qq,tui.t_isUse,tui.t_createTime,tui.t_lastTime  from  tywh_userinfo  tui  where 1=1 ";
	
			if(daptId!=null && daptId.trim().length() > 0)
			{
				sql+=" and trim(tui.t_daptId) like  ? ";
				obs.add("%,"+daptId+",%");
			}
			
			if(userName!=null && userName.trim().length() > 0)
			{
				sql+=" and  trim(tui.t_userName) like ? ";
				obs.add("%"+userName+"%");
			}
			
			sql+=" ORDER BY tui.t_id ";
			
			Session session = getSession();
			Query q = session.createSQLQuery(sql);
			for(int i=0;i<obs.size();++i){
				q.setParameter(i, obs.get(i));
			}

			q.setMaxResults(end).setFirstResult(start);
			List<Object[]> li=q.list();
			if(li!=null){
				for(Object[] os:li){
					TUserinfo  tui=new TUserinfo();
					tui.setT_id(((Number)os[0]).intValue());
					tui.setT_daptId((String)os[1]);
					tui.setT_userName((String)os[2]);
					tui.setT_loginName((String)os[3]);
					tui.setT_password((String)os[4]);
					tui.setT_telphone((String)os[5]);
					tui.setT_email((String)os[6]);
					tui.setT_qq((String)os[7]);
					tui.setT_isUse(null==os[8]?null : ((Number)os[8]).intValue());
					tui.setT_createTime(null==os[9]?null :(Date)sdf.parse(os[9].toString().substring(0,19)));
					tui.setT_lastTime(null==os[10]?null :(Date)sdf.parse(os[10].toString().substring(0,19)));
					list.add(tui);
				}
			}
			
			if(session!=null)
			{
				session.close();
			}
			log.info("UserInfoDao中的queryAllUserList方法查询完成");
			return list;
	}

	/**
	 * 用户的删除方法  变更IsUser字段为不可用
	 */
	public void deleteUserById(String userId)
	{
		String sql = "update  tywh_userinfo set t_isUse='0' where t_id="+userId;
		Session session = getSession();
		session.createSQLQuery(sql).executeUpdate();
		if(session!=null)
		{
			session.close();
		}
	}


	public boolean repeatName(String name)
	{
		return ((Integer)getSession().createSQLQuery("select count(1) c  from tywh_userinfo tui  where tui.t_loginName = ? ").
				addScalar("c", Hibernate.INTEGER).setParameter(0, name).uniqueResult())>0;
	}

	
	/**
	 * 添加人员
	 */
	public void saveUserInfo(TUserinfo tui)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "insert into tywh_userinfo (t_daptId,t_userName,t_loginName,t_password,t_telphone,t_email,t_qq,t_isUse,t_createTime,t_lastTime)  values( ?,?,?,?,?,?,?,?,now(),now())";
		
		if(tui.getT_daptId()!=null && tui.getT_daptId().trim().length() > 0)
		{
			obs.add(tui.getT_daptId());
		}
		
		if(tui.getT_userName()!=null && tui.getT_userName().trim().length() > 0)
		{
			obs.add(tui.getT_userName());
		}
		
		if(tui.getT_loginName()!=null && tui.getT_loginName().trim().length() > 0)
		{
			obs.add(tui.getT_loginName());
		}
		else
		{
			obs.add("");
		}
		if(tui.getT_password()!=null &&tui.getT_password().trim().length() > 0)
		{
			obs.add(tui.getT_password());
		}
		else
		{
			obs.add("");
		}
		
		if(tui.getT_telphone()!=null && tui.getT_telphone().trim().length() > 0)
		{
			obs.add(tui.getT_telphone());
		}
		else
		{
			obs.add("");
		}
		
		if(tui.getT_email()!=null && tui.getT_email().trim().length() > 0)
		{
			obs.add(tui.getT_email());
		}
		else
		{
			obs.add("");
		}
		
		if(tui.getT_qq()!=null && tui.getT_qq().trim().length() > 0)
		{
			obs.add(tui.getT_qq());
		}
		else
		{
			obs.add("");
		}
		
		if(tui.getT_isUse()==0)
		{
			obs.add(0);
		}
		else
		{
			obs.add(1);
		}
		
		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		q.executeUpdate();
		
		
		if(session!=null)
		{
			session.close();
		}
	}


	/**
	 * 通过用户的信息查询用户
	 * @throws ParseException 
	 */
	public TUserinfo queryUserInfo(TUserinfo tu) throws ParseException
	{
		log.info("通过用户的信息查询用户的数据");
		//通过userId查询当前用户的所有角色
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TUserinfo> list=new ArrayList<TUserinfo>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		sql="select  tui.t_id,tui.t_daptId,tui.t_userName,tui.t_loginName,tui.t_password,tui.t_telphone,tui.t_email,tui.t_qq,tui.t_isUse,tui.t_createTime,tui.t_lastTime,t_num  from  tywh_userinfo  tui  where 1=1 ";

		if(tu.getT_id()>0)
		{
			sql+=" and tui.t_id =? ";
			obs.add(tu.getT_id());
		}
		
		if(null!=tu.getT_daptId() && tu.getT_daptId().trim().length() > 0)
		{
			sql+=" and trim(tui.t_daptId) =? ";
			obs.add(tu.getT_daptId());
		}
		
		if(null!=tu.getT_userName() && tu.getT_userName().trim().length() > 0)
		{
			sql+=" and  trim(tui.t_userName) = ? ";
			obs.add(tu.getT_userName());
		}
		
		if(null!=tu.getT_loginName() && tu.getT_loginName().trim().length() > 0)
		{
			sql+=" and  trim(tui.t_loginName) = ? ";
			obs.add(tu.getT_loginName());
		}
		
		if(null!=tu.getT_password() && tu.getT_password().trim().length() > 0)
		{
			sql+=" and  trim(tui.t_password) = ? ";
			obs.add(tu.getT_password());
		}
		
		if(null!=tu.getT_telphone() && tu.getT_telphone().trim().length() > 0)
		{
			sql+=" and  trim(tui.t_telphone) = ? ";
			obs.add(tu.getT_telphone());
		}
		
		if(null!=tu.getT_email() && tu.getT_email().trim().length() > 0)
		{
			sql+=" and  trim(tui.t_email) = ? ";
			obs.add(tu.getT_email());
		}
		
		if(null!=tu.getT_qq() && tu.getT_qq().trim().length() > 0)
		{
			sql+=" and  trim(tui.t_qq) = ? ";
			obs.add(tu.getT_qq());
		}
		
		if(null!=tu.getT_createTime())
		{
			sql+=" and  tui.t_createTime like ? ";
			obs.add("%"+tu.getT_createTime()+"%");
		}
		
		if(null!=tu.getT_lastTime())
		{
			sql+=" and  tui.t_lastTime like ? ";
			obs.add("%"+tu.getT_lastTime()+"%");
		}
		
		if(null!=tu.getT_num() && tu.getT_num().trim().length() > 0)
		{
			sql+=" and trim(tui.t_num) =? ";
			obs.add(tu.getT_num());
		}
		
		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}

		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TUserinfo  tui=new TUserinfo();
				tui.setT_id(((Number)os[0]).intValue());
				tui.setT_daptId((String)os[1]);
				tui.setT_userName((String)os[2]);
				tui.setT_loginName((String)os[3]);
				tui.setT_password((String)os[4]);
				tui.setT_telphone((String)os[5]);
				tui.setT_email((String)os[6]);
				tui.setT_qq((String)os[7]);
				tui.setT_isUse(null==os[8]?null : ((Number)os[8]).intValue());
				tui.setT_createTime(null==os[9]?null :(Date)sdf.parse(os[9].toString().substring(0,19)));
				tui.setT_lastTime(null==os[10]?null :(Date)sdf.parse(os[10].toString().substring(0,19)));
				tui.setT_num((String)os[11]);
				list.add(tui);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("UserInfoDao中的通过用户的信息查询用户queryUserInfo方法查询完成");
		if(null!=list && list.size()>0)
		{
			return list.get(0);
		}
		return null;
	}

	/**
	 * 通过部门ID 查询用户集合
	 * @throws ParseException 
	 */
	public List<TUserinfo> queryUserByDepart(String daptId) throws ParseException
	{
		log.info("通过部门ID 查询用户集合");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TUserinfo> list=new ArrayList<TUserinfo>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		sql="select  tui.t_id,tui.t_daptId,tui.t_userName,tui.t_loginName,tui.t_password,tui.t_telphone,tui.t_email,tui.t_qq,tui.t_isUse,tui.t_createTime,tui.t_lastTime from tywh_userinfo  tui  where 1=1 ";

		if(daptId!=null && daptId.trim().length() > 0)
		{
			sql+=" and trim(tui.t_daptId) like  ? ";
			obs.add("%,"+daptId+",%");
		}
		
		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}

		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TUserinfo  tui=new TUserinfo();
				tui.setT_id(((Number)os[0]).intValue());
				tui.setT_daptId((String)os[1]);
				tui.setT_userName((String)os[2]);
				tui.setT_loginName((String)os[3]);
				tui.setT_password((String)os[4]);
				tui.setT_telphone((String)os[5]);
				tui.setT_email((String)os[6]);
				tui.setT_qq((String)os[7]);
				tui.setT_isUse(null==os[8]?null : ((Number)os[8]).intValue());
				tui.setT_createTime(null==os[9]?null :(Date)sdf.parse(os[9].toString().substring(0,19)));
				tui.setT_lastTime(null==os[10]?null :(Date)sdf.parse(os[10].toString().substring(0,19)));
				list.add(tui);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("UserInfoDao中的queryUserByDepart方法查询完成");
		return list;
	}
	
	/**
	 * 编辑保存人员数据
	 */
	public void editUserInfo(TUserinfo tu)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "update  tywh_userinfo set t_daptId =?,t_userName=?,t_loginName=?,t_password=?,t_telphone=?,t_email=?,t_qq=?,t_lastTime=now()   where t_id=? ";
		
		if(null!=tu.getT_daptId() && tu.getT_daptId().trim().length() > 0)
		{
			obs.add(tu.getT_daptId());
		}
		else{
			obs.add("");
		}
		
		if(null!=tu.getT_userName() && tu.getT_userName().trim().length() > 0)
		{
			obs.add(tu.getT_userName());
		}
		else{
			obs.add("");
		}
		
		if(null!=tu.getT_loginName() && tu.getT_loginName().trim().length() > 0)
		{
			obs.add(tu.getT_loginName());
		}
		else{
			obs.add("");
		}
		
		if(null!=tu.getT_password() && tu.getT_password().trim().length() > 0)
		{
			obs.add(tu.getT_password());
		}
		else{
			obs.add("123456");
		}
		
		if(null!=tu.getT_telphone() && tu.getT_telphone().trim().length() > 0)
		{
			obs.add(tu.getT_telphone());
		}
		else
		{
			obs.add("");
		}
		
		if(null!=tu.getT_email() && tu.getT_email().trim().length() > 0)
		{
			obs.add(tu.getT_email());
		}
		else
		{
			obs.add("");
		}
		
		if(null!=tu.getT_qq() && tu.getT_qq().trim().length() > 0)
		{
			obs.add(tu.getT_qq());
		}
		else
		{
			obs.add("");
		}
		
		if(tu.getT_id()>0)
		{
			obs.add(tu.getT_id());
		}
		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		q.executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	/**
	 * 删除部门与用户表数据
	 */
	public void deleteDaptUserById(String userId)
	{
		String sql = "delete  from  tywh_departmentuser  where t_userId="+userId;
		Session session = getSession();
		session.createSQLQuery(sql).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	/**
	 * 删除部门用户角色表数据
	 */
	public void deleteDaptRoleUserById(String userId)
	{
		String sql = "delete  from  tywh_departroleuser where t_userId="+userId;
		Session session = getSession();
		session.createSQLQuery(sql).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	/**
	 * 根据用户信息查询 本人的 部门  角色   名称  。。。。
	 */
	public List<TUserinfo> queryUDRs(TUserinfo tu)
	{
		log.info("通过部门ID 查询用户集合");
		List<TUserinfo> list=new ArrayList<TUserinfo>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		sql="select td.t_departName,tr.t_roleName,tu.t_num,tu.t_userName,tu.t_id from tywh_userinfo tu  left join  tywh_departroleuser  tdru  on tdru.t_userId = tu.t_id  left join tywh_department td on tdru.t_daptId=td.t_id  left join tywh_role tr on tdru.t_roleId=tr.t_id   where 1=1 ";

		if(null!=tu.getT_userName() && tu.getT_userName().trim().length() > 0)
		{
			sql+=" and  trim(tu.t_userName) = ? ";
			obs.add(tu.getT_userName());
		}
		 
		if(null!=tu.getT_userName() && tu.getT_userName().trim().length() > 0)
		{
			sql+=" and  trim(tu.t_num) = ? ";
			obs.add(tu.getT_num());
		}
		
		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}

		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TUserinfo  tui=new TUserinfo();
				tui.setT_daptName((String)os[0]);
				tui.setT_roleName((String)os[1]);
				tui.setT_num((String)os[2]);
				tui.setT_userName((String)os[3]);
				tui.setT_id(((Number)os[4]).intValue());
				list.add(tui);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("UserInfoDao中的queryUserByDepart方法查询完成");
		return list;
	}
}
