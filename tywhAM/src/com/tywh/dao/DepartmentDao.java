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

import com.tywh.orm.TDepartRoleUser;
import com.tywh.orm.TDepartRolerMenu;
import com.tywh.orm.TDepartment;
import com.tywh.orm.TDepartmentUser;
import com.tywh.orm.TRoleBusiness;

/**
 *	TDepartmentDao
 *  author：杜泉
 *  2014-6-27 下午3:14:17
 */
public class DepartmentDao extends HibernateDaoSupport
{
	private static Log log = LogFactory.getLog(DepartmentDao.class);
	
	public int query(String daptName)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select  count(1)  from tywh_department  td  where 1=1 ";
		
		if(daptName!=null && daptName.trim().length() > 0)
		{
			sql+=" and  trim(td.t_departName) like ? ";
			obs.add("%"+daptName+"%");
		}
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
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
	
	public List<TDepartment> query(String daptName, int start, int end) throws ParseException
	{
		log.info("查询所有的部门");
		//通过部门名称查询部门
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TDepartment> list=new ArrayList<TDepartment>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="select  td.t_id,td.t_departName,td.t_createTime,td.t_isUse  from  tywh_department  td  where 1=1 ";
	
		if(daptName!=null && daptName.trim().length() > 0)
		{
			sql+=" and  trim(td.t_departName) like ? ";
			obs.add("%"+daptName+"%");
		}
		
		sql+=" ORDER BY td.t_id ";
		
		Session session=getSession();
		Query q=session.createSQLQuery(sql);
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		q.setMaxResults(end).setFirstResult(start);
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TDepartment  td=new TDepartment();
				td.setT_id(((Number)os[0]).intValue());
				td.setT_departName((String)os[1]);
				td.setT_createTime(null==os[2]?null :(Date)sdf.parse(os[2].toString().substring(0,19)));
				td.setT_isUse(((Number)os[3]).intValue());
				list.add(td);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("DepartmentDao中的query方法查询完成");
		return list;
	}
	
	//根据当前用户ID查询当前人的所有角色
	public List<TDepartment> queryAllDepart() throws ParseException
	{
		log.info("开始调用DepartmentDao中的queryAllDepart方法查询所有部门列表");
		//通过userId查询当前用户的所有角色
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TDepartment> list=new ArrayList<TDepartment>();
		String sql = "select t_id,t_departName,t_isUse,t_createTime from tywh_department ";
		
		Session session=getSession();
		Query q=session.createSQLQuery(sql);
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TDepartment  dpt=new TDepartment();
				dpt.setT_id(((Number)os[0]).intValue());
				dpt.setT_departName((String)os[1]);
				dpt.setT_isUse(null==os[2]?0 : ((Number)os[2]).intValue());
				dpt.setT_createTime(null==os[3]?null :(Date)sdf.parse(os[3].toString().substring(0,19)));
				list.add(dpt);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("DepartmentDao中的queryAllDepart方法查询完成，部门集合为"+list);
		return list;
	}
	
	/**
	 * 保存部门与人员表 关联数据
	 */
	public void saveDaptAndUser(TDepartmentUser du)
	{
		String sql = "insert into tywh_departmentuser(t_departId,t_userId)  values("+du.getT_departId()+","+du.getT_userId()+")";
		Session session=getSession();
		session.createSQLQuery(sql).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}
	
	
	
	

	/**
	 * 通过用户Id查询关联数据
	 */
	public List<TDepartmentUser> queryDaptUserById(TDepartmentUser tdu)
	{
		log.info("开始调用DepartmentDao中的queryDaptUserById方法查询所有部门列表");
		//通过userId查询当前用户的所有角色
		List<TDepartmentUser> list=new ArrayList<TDepartmentUser>();
		StringBuffer sql =new StringBuffer();
		sql.append("select  tdu.t_id,tdu.t_departId,tdu.t_userId from tywh_departmentuser  tdu where 1=1 ") ;

		if(tdu.getT_userId()>0)
		{
			sql.append(" and  tdu.t_userId ="+tdu.getT_userId());
		}
		if(tdu.getT_departId()>0)
		{
			sql.append(" and  tdu.t_departId ="+tdu.getT_departId());
		}
		sql.append(" order by tdu.t_id");
		
		Session session=getSession();
		Query q=session.createSQLQuery(sql.toString());
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TDepartmentUser  du=new TDepartmentUser();
				du.setT_id(((Number)os[0]).intValue());
				du.setT_departId(((Number)os[1]).intValue());
				du.setT_userId(((Number)os[2]).intValue());
				list.add(du);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("DepartmentDao中的queryDaptById方法查询完成，部门集合为"+list);
		return list;
	}
	
	/**
	 * 查询部门角色用户关联表数据
	 */
	public List<TDepartRoleUser> queryDaptRoleUserById(TDepartRoleUser tdru)
	{
		log.info("开始调用DepartmentDao中的queryDaptRoleUserById方法查询所有关联数据");
		//通过userId查询当前用户的所有角色
		List<TDepartRoleUser> list=new ArrayList<TDepartRoleUser>();
		StringBuffer sql =new StringBuffer();
		sql.append("select  tdru.t_id,tdru.t_daptId,tdru.t_userId,tdru.t_roleId  from  tywh_departroleuser  tdru  where 1=1 ") ;

		if(tdru.getT_userId()>0)
		{
			sql.append(" and  tdru.t_userId ="+tdru.getT_userId());
		}
		if(tdru.getT_daptId()>0)
		{
			sql.append(" and  tdru.t_daptId ="+tdru.getT_daptId());
		}
		if(tdru.getT_roleId()>0)
		{
			sql.append(" and  tdru.t_roleId ="+tdru.getT_roleId());
		}
		Session session=getSession();
		Query q=session.createSQLQuery(sql.toString());
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TDepartRoleUser  dru=new TDepartRoleUser();
				dru.setT_id(((Number)os[0]).intValue());
				dru.setT_daptId(((Number)os[1]).intValue());
				dru.setT_userId(((Number)os[2]).intValue());
				dru.setT_roleId(((Number)os[3]).intValue());
				list.add(dru);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		
		log.info("DepartmentDao中的queryDaptById方法查询完成，部门集合为"+list);
		return list;
	}

	/**
	 * 部门的删除方法
	 */
	public void deleteDepartmentById(String daptId)
	{
		String sql = "update  tywh_department set t_isUse='0' where t_id="+daptId;
		
		Session session=getSession();
		session.createSQLQuery(sql).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	/**
	 * 删除部门与用户关系
	 */
	public void deleteDaptAndUser(TDepartmentUser du)
	{
		StringBuffer sql =new StringBuffer();
		sql.append("delete from tywh_departmentuser where 1=1 " );
		if(du.getT_userId()>0)
		{
			sql.append(" and t_userId="+du.getT_userId());
		}
		
		if(du.getT_departId()>0)
		{
			sql.append(" and  t_departId = "+du.getT_departId());
		}
		
		Session session=getSession();
		session.createSQLQuery(sql.toString()).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}
	
	/**
	 * 删除部门用户角色表 
	 */
	public void deleteDaptRoleUser(TDepartRoleUser dru)
	{
		StringBuffer sql =new StringBuffer();
		sql.append("delete from tywh_departroleuser  where 1=1 ");
		
		if(dru.getT_daptId()>0)
		{
			sql.append(" and t_daptId ="+dru.getT_daptId());
		}
		
		if(dru.getT_userId()>0)
		{
			sql.append(" and t_userId="+dru.getT_userId());
		}
		
		if(dru.getT_roleId()>0)
		{
			sql.append(" and t_roleId="+dru.getT_roleId());
		}
		Session session=getSession();
		session.createSQLQuery(sql.toString()).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	/**
	 * 删除部门角色菜单关联表
	 */
	public void deleteDaptRoleMenu(TDepartRolerMenu drm)
	{
		StringBuffer sql =new StringBuffer();
		sql.append("delete from tywh_departrolermenu  where 1=1 ");
		
		if(drm.getT_daptId()>0)
		{
			sql.append(" and t_daptId ="+drm.getT_daptId());
		}
		
		if(drm.getT_menuId()>0)
		{
			sql.append("and t_menuId="+drm.getT_menuId());
		}
		
		if(drm.getT_roleId()>0)
		{
			sql.append("and t_roleId="+drm.getT_roleId());
		}
		
		Session session=getSession();
		session.createSQLQuery(sql.toString()).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}
	
	public boolean repeatName(String name)
	{
		return ((Integer)getSession().createSQLQuery("select count(1) c  from tywh_department  td   where td.t_departName = ? ").
				addScalar("c", Hibernate.INTEGER).setParameter(0, name).uniqueResult())>0;
	}

	public void saveDepartment(TDepartment td)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "insert into tywh_department (t_departName,t_isUse,t_createTime)  values( ?,?,now())";

		if(td.getT_departName()!=null && td.getT_departName().trim().length() > 0)
		{
			obs.add(td.getT_departName());
		}

		if(td.getT_isUse()==0)
		{
			obs.add(0);
		}
		else
		{
			obs.add(1);
		}
		
		Session session=getSession();
		Query q=session.createSQLQuery(sql);
		
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		q.executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	public TDepartment queryDaptById(TDepartment td) throws ParseException
	{
		log.info("开始调用DepartmentDao中的queryAllDepart方法查询所有部门列表");
		//通过userId查询当前用户的所有角色
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TDepartment> list=new ArrayList<TDepartment>();
		List<Object> obs=new ArrayList<Object>();
		String sql = "select  td.t_id,td.t_departName,td.t_isUse,td.t_createTime  from  tywh_department  td where 1=1 ";
		if(td.getT_id()>0)
		{
			sql+=" and td.t_id =? ";
			obs.add(td.getT_id());
		}
		
		if(null!=td.getT_departName() && td.getT_departName().trim().length() > 0)
		{
			sql+=" and trim(td.getT_departName) =? ";
			obs.add(td.getT_departName());
		}
		
		Session session=getSession();
		Query q=session.createSQLQuery(sql);
		
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TDepartment  dpt=new TDepartment();
				dpt.setT_id(((Number)os[0]).intValue());
				dpt.setT_departName((String)os[1]);
				dpt.setT_isUse(null==os[2]?0 : ((Number)os[2]).intValue());
				dpt.setT_createTime(null==os[3]?null :(Date)sdf.parse(os[3].toString().substring(0,19)));
				list.add(dpt);
			}
		}
		log.info("DepartmentDao中的queryDaptById方法查询完成，部门集合为"+list);
		
		if(session!=null)
		{
			session.close();
		}
		
		if(null!=list && list.size()>0)
		{
			return list.get(0);
		}
		return null;
	}

	/**
	 * 编辑保存部门数据
	 */
	public void editDepartmentInfo(TDepartment tdt)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "update  tywh_department  set t_departName =?   where t_id=? ";
		
		if(null!=tdt.getT_departName() && tdt.getT_departName().trim().length() > 0)
		{
			obs.add(tdt.getT_departName());
		}
		
		
		if(tdt.getT_id()>0)
		{
			obs.add(tdt.getT_id());
		}
		
		Session session=getSession();
		Query q=session.createSQLQuery(sql);
		
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		q.executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}
}
