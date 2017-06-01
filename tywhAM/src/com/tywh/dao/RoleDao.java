package com.tywh.dao;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.tywh.orm.TDepartRoleUser;
import com.tywh.orm.TDepartment;
import com.tywh.orm.TMenu;
import com.tywh.orm.TRole;
import com.tywh.orm.TRoleBusiness;

/**
 *	RoleDao  所有角色Dao方法
 *  author：杜泉
 *  2014-6-24 下午3:08:28
 */
public class RoleDao extends HibernateDaoSupport
{
	private static Log log = LogFactory.getLog(RoleDao.class);
	//根据当前用户ID查询当前人的所有角色
	public List<TDepartRoleUser> queryRoleByUserId(int  userId, StringBuilder dapts)
	{
		log.info("开始调用RoleDao中的queryRoleByUserId方法查询改用户的所有角色");
		List<TDepartRoleUser> list=new ArrayList<TDepartRoleUser>();
		StringBuffer sql = new StringBuffer();
		sql.append("select  t_id,t_daptId,t_userId,t_roleId  from tywh_departroleuser  dru  where  dru.t_userId = "+userId +""+dapts.toString());
		Session session = getSession();
		Query q = session.createSQLQuery(sql.toString());
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TDepartRoleUser  deu=new TDepartRoleUser();
				deu.setT_id(((Number)os[0]).intValue());
				deu.setT_daptId(((Number)os[1]).intValue());
				deu.setT_userId(((Number)os[2]).intValue());
				deu.setT_roleId(((Number)os[3]).intValue());
				list.add(deu);
			}
		}
		if(session!=null)
		{
			session.close();
		}
		log.info("RoleDao中的queryRoleByUserId方法查询完成，角色集合为"+list);
		return list;
	}
	
	
	/**
	 * 获取角色的菜单
	 * @throws ParseException 
	 */
	public TMenu findByMenuRoleIds(Set<Integer> roleIds, Set<Integer> daptIds,Set<Integer> idSet) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select  t_id,t_name,t_content,t_parentId,t_state,t_num,t_order,t_url,t_parameter,t_remark,t_creartTime,t_user,t_updateTime " +
				" from tywh_menu   m  where m.t_parentId =0 ";
		Session session = getSession();
		Query query = session.createSQLQuery(sql);
		List<TMenu> list=new ArrayList<TMenu>();
		List<Object[]> li=query.list();
		if(li!=null){
			for(Object[] os:li){
				TMenu  menu=new TMenu();
				menu.setT_id(((Number)os[0]).intValue());
				menu.setT_name((String)os[1]);
				menu.setT_content((String)os[2]);
				menu.setT_parentId(null==os[3]?0 : ((Number)os[3]).intValue());
				menu.setT_state(null==os[4]?0 : ((Number)os[4]).intValue());
				menu.setT_num((String)os[5]);
				menu.setT_order(null==os[6]?0 : ((Number)os[6]).intValue());
				menu.setT_url((String)os[7]);
				menu.setT_parameter((String)os[8]);
				menu.setT_remark((String)os[9]);
				menu.setT_creartTime(null==os[10]?null :(Date)sdf.parse(os[10].toString().substring(0,19)));
				menu.setT_user(null==os[11]?0 : ((Number)os[11]).intValue());
				menu.setT_updateTime(null==os[12]?null :(Date)sdf.parse(os[12].toString().substring(0,19)));
				list.add(menu);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		
		if(null!= list &&list.size()>0)
		{
			TMenu root = list.get(0);
			idSet.add(root.getT_id());//把查询到的父菜单id放入set中
			String sqlAboutRoleIds = makeSqlAboutRoleIds(roleIds);//把所有的角色拼成sql用于查询
			String sqlAboutDaptIds = makeSqlAboutDaptIds(daptIds);//把所有的部门拼成sql用于查询
			List children = findByParentIdAndManagRoleIds(root, sqlAboutRoleIds,sqlAboutDaptIds,idSet);
			root.setChildren(children);
			return root;
		}
		return null;
	}
	
	/**
	 * 把所有该用户的角色拼成sql用于查询
	 */
	private String makeSqlAboutRoleIds(Set<Integer> roleIds){
		StringBuilder sql = new StringBuilder();
		if(roleIds.size()==1){
			int managRoleId = roleIds.iterator().next();
			sql.append(" and tdrm.t_roleId=" + managRoleId);
		}else if(roleIds.size()>1){
			Iterator it = roleIds.iterator();
			sql.append(" and tdrm.t_roleId in(");
			while(it.hasNext()){
				int managRoleId = (Integer)it.next();
				sql.append(managRoleId+",");
			}
			sql.delete(sql.length()-1, sql.length());
			sql.append(")");
		}else{
			throw new RuntimeException("尝试获取菜单时出错!当前用户没有角色!");
		}
		return sql.toString();
	}
	
	/**
	 * 把所有该用户的角色拼成sql用于查询
	 */
	private String makeSqlAboutDaptIds(Set<Integer> daptIds){
		StringBuilder sql = new StringBuilder();
		if(daptIds.size()==1){
			int managDaptId = daptIds.iterator().next();
			sql.append(" and tdrm.t_daptId=" + managDaptId);
		}else if(daptIds.size()>1){
			Iterator it = daptIds.iterator();
			sql.append(" and tdrm.t_daptId in(");
			while(it.hasNext()){
				int managDaptId = (Integer)it.next();
				sql.append(managDaptId+",");
			}
			sql.delete(sql.length()-1, sql.length());
			sql.append(")");
		}else{
			throw new RuntimeException("尝试获取菜单时出错!当前用户没有角色!");
		}
		return sql.toString();
	}
	
	/**
	 * 根据角色与菜单关联表查询该用户的通过角色Id关联的菜单
	 */
	private List findByParentIdAndManagRoleIds(TMenu root,String sqlAboutRoleIds,String sqlAboutDaptIds,Set<Integer> idSet){
			//log.info("角色下的菜单"+root.getT_id());
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct tm.t_id,tm.t_name,tm.t_content,tm.t_parentId,tm.t_state,tm.t_num,tm.t_order,tm.t_url,tm.t_parameter,tm.t_remark,tm.t_creartTime,tm.t_user,tm.t_updateTime " +
									"	from tywh_menu tm,tywh_departrolermenu tdrm where tm.t_id=tdrm.t_menuId and tm.t_parentId= "+root.getT_id()+ " and tm.t_state=1 ");
			sql.append(sqlAboutRoleIds);
			sql.append(sqlAboutDaptIds);
			sql.append(" order by  tm.t_order,tdrm.t_menuId ");
			
			Session session = getSession();
			SQLQuery query = session.createSQLQuery(sql.toString());
			
			List list = (List) query.list();
			List<TMenu> children = new ArrayList<TMenu>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				TMenu parent;
				try {
				parent = getTMenu(obj);
				idSet.add(parent.getT_id());
				} catch (ParseException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}		
				children.add(parent);
				List result = findByParentIdAndManagRoleIds(parent, sqlAboutRoleIds,sqlAboutDaptIds,idSet);
				parent.setChildren(result);
			}
			
			if(session!=null)
			{
				session.close();
			}
			return children;

	}
	
	
	
	private TMenu getTMenu(Object[] obj) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TMenu root = new TMenu();
		root.setT_id(((Number)obj[0]).intValue());
		root.setT_name((String)obj[1]);
		root.setT_content((String)obj[2]);
		root.setT_parentId(null==obj[3]?null : ((Number)obj[3]).intValue());
		root.setT_state(null==obj[4]?null : ((Number)obj[4]).intValue());
		root.setT_num((String)obj[5]);
		root.setT_order(null==obj[6]?null : ((Number)obj[6]).intValue());
		root.setT_url((String)obj[7]);
		root.setT_parameter((String)obj[8]);
		root.setT_remark((String)obj[9]);
		root.setT_creartTime(null==obj[10]?null :(Date)sdf.parse(obj[10].toString().substring(0,19)));
		root.setT_user(null==obj[11]?null : ((Number)obj[11]).intValue());
		root.setT_updateTime(null==obj[12]?null :(Date)sdf.parse(obj[12].toString().substring(0,19)));
		return root;
	}


	public int query(String daptId, String roleName)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select  count(1)  from tywh_role  tr  where 1=1 ";
		
		if(daptId!=null && daptId.trim().length() > 0)
		{
			sql+=" and  tr.t_departId = ? ";
			obs.add(daptId);
		}
		
		if(roleName!=null && roleName.trim().length() > 0)
		{
			sql+=" and  trim(tr.t_roleName) like ? ";
			obs.add("%"+roleName+"%");
		}
		
		Session session=getSession();
		Query q=session.createSQLQuery(sql);
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


	public List<TRole> query(String daptId, String roleName, int start, int end) throws ParseException
	{
		//通过部门名称查询部门
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				List<TRole> list=new ArrayList<TRole>();
				String sql=null;
				List<Object> obs=new ArrayList<Object>();

				sql="select  tr.t_id,tr.t_roleName,tr.t_departId,tr.t_createTime,tr.t_isUse  from  tywh_role  tr  where 1=1 ";
			
				if(daptId!=null && daptId.trim().length() > 0)
				{
					sql+=" and  tr.t_departId = ? ";
					obs.add(daptId);
				}
				
				if(roleName!=null && roleName.trim().length() > 0)
				{
					sql+=" and  trim(tr.t_roleName) like ? ";
					obs.add("%"+roleName+"%");
				}
				
				sql+=" ORDER BY tr.t_id ";
				
				Session session=getSession();
				Query q=session.createSQLQuery(sql);
				for(int i=0;i<obs.size();++i){
					q.setParameter(i, obs.get(i));
				}
			
				q.setMaxResults(end).setFirstResult(start);
				List<Object[]> li=q.list();
				if(li!=null){
					for(Object[] os:li){
						TRole  tr=new TRole();
						tr.setT_id(((Number)os[0]).intValue());
						tr.setT_roleName((String)os[1]);
						tr.setT_departId(((Number)os[2]).intValue());
						tr.setT_createTime(null==os[3]?null :(Date)sdf.parse(os[3].toString().substring(0,19)));
						tr.setT_isUse(((Number)os[4]).intValue());
						list.add(tr);
					}
				}
				
				if(session!=null)
				{
					session.close();
				}
				log.info("RoleDao中的query方法查询完成");
				return list;
	}
	
	/**
	 * 删除角色业务表数据
	 */
	public void deleteRole(TRole tr)
	{
		StringBuffer sql =new StringBuffer();
		sql.append("delete from tywh_role  where 1=1 ");
		
		if(tr.getT_id()>0)
		{
			sql.append("and t_id="+tr.getT_id());
		}
		
		
		Session session=getSession();
		session.createSQLQuery(sql.toString()).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
		
	}
	
	/**
	 * 删除角色业务表数据
	 */
	public void deleteRoleBusiness(TRoleBusiness tb)
	{
		StringBuffer sql =new StringBuffer();
		sql.append("delete from tywh_rolebusiness  where 1=1 ");
		
		if(tb.getT_roleId()>0)
		{
			sql.append("and t_roleId="+tb.getT_roleId());
		}
		
		if(tb.getT_busiId()>0)
		{
			sql.append("and t_busiId="+tb.getT_busiId());
		}
		
		Session session=getSession();
		session.createSQLQuery(sql.toString()).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
		
	}


	public void saveRole(TRole tr)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "insert into tywh_role(t_roleName,t_departId,t_isUse,t_createTime)  values( ?,?,?,now())";

		if(tr.getT_roleName()!=null && tr.getT_roleName().trim().length() > 0)
		{
			obs.add(tr.getT_roleName());
		}

		if(tr.getT_departId()> 0)
		{
			obs.add(tr.getT_departId());
		}
		
		if(tr.getT_isUse()==0)
		{
			obs.add(0);
		}
		else
		{
			obs.add(1);
		}
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
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
	 * 查询角色
	 * @throws ParseException 
	 */
	public TRole queryRole(TRole tr) throws ParseException
	{
		//通过部门名称查询部门
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TRole> list=new ArrayList<TRole>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();

		sql="select  tr.t_id,tr.t_roleName,tr.t_departId,tr.t_createTime,tr.t_isUse  from  tywh_role  tr  where 1=1 ";
	
		if(tr.getT_id() > 0)
		{
			sql+=" and  tr.t_id = ? ";
			obs.add(tr.getT_id());
		}
		
		if(tr.getT_departId() > 0)
		{
			sql+=" and  tr.t_departId = ? ";
			obs.add(tr.getT_departId());
		}
		
		if(tr.getT_roleName()!=null && tr.getT_roleName().trim().length() > 0)
		{
			sql+=" and  trim(tr.t_roleName) = ? ";
			obs.add(tr.getT_roleName());
		}
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TRole  trs=new TRole();
				trs.setT_id(((Number)os[0]).intValue());
				trs.setT_roleName((String)os[1]);
				trs.setT_departId(((Number)os[2]).intValue());
				trs.setT_createTime(null==os[3]?null :(Date)sdf.parse(os[3].toString().substring(0,19)));
				trs.setT_isUse(((Number)os[4]).intValue());
				list.add(trs);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("RoleDao中的queryRole方法查询完成");
		if(null!=list && list.size()>0)
		{
			return list.get(0);
		}
		return null;
	}


	/**
	 * 添加角色与部门用户关联表数据
	 */
	public void saveDaptRoleUser(TDepartRoleUser tdru)
	{
		String sql = null;
		sql = "insert into tywh_departroleuser(t_daptId,t_userId,t_roleId)  values("+tdru.getT_daptId()+","+tdru.getT_userId()+","+tdru.getT_roleId()+")";
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());

		q.executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}
	
	public boolean repeatName(String name)
	{
		return ((Integer)getSession().createSQLQuery("select count(1) c  from  tywh_role  tr   where tr.t_roleName = ? ").
				addScalar("c", Hibernate.INTEGER).setParameter(0, name).uniqueResult())>0;
	}

	/**
	 * 通过角色ID查询
	 */
	public TRole queryRoleById(TRole tr)
	{
		
		return null;
	}

	/**
	 * 编辑角色
	 */
	public void editRole(TRole trt)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "update  tywh_role  set t_roleName =?  where t_id=? ";
		
		if(null!=trt.getT_roleName() && trt.getT_roleName().trim().length() > 0)
		{
			obs.add(trt.getT_roleName());
		}
		
		
		if(trt.getT_id()>0)
		{
			obs.add(trt.getT_id());
		}
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
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
	 * 删除关联数据
	 */
	public void deleteDaptRoleUser(TDepartRoleUser tdru)
	{
		StringBuffer sql =new StringBuffer();
		sql.append("delete from tywh_departroleuser   where 1=1 ");
		
		if(tdru.getT_roleId()>0)
		{
			sql.append(" and t_roleId="+tdru.getT_roleId());
		}
		
		if(tdru.getT_daptId()>0)
		{
			sql.append(" and t_daptId="+tdru.getT_daptId());
		}
		
		if(tdru.getT_userId()>0)
		{
			sql.append(" and t_userId="+tdru.getT_userId());
		}		
		
		Session session=getSession();
		session.createSQLQuery(sql.toString()).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	/**
	 * 查询权限数据的分页
	 */
	public int queryAllDaptToRoleList(String daptId, String roleName)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select count(1) from tywh_role tr left join tywh_department td on tr.t_departId =td.t_id  where  1=1  and  tr.t_isUse=1 ";
		
		if(daptId!=null && daptId.trim().length() > 0)
		{
			sql+=" and  td.t_id = ? ";
			obs.add(daptId);
		}
		
		
		if(roleName!=null && roleName.trim().length() > 0)
		{
			sql+=" and  trim(tr.t_roleName) like ? ";
			obs.add("%"+roleName+"%");
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
		
		log.info("RoleDao中的queryAllDaptToRoleList个数方法查询完成");
		return n.intValue();
	}

	/**
	 * 查询权限数据的分页
	 */
	public List<TRole> queryAllDaptToRoleList(String daptId, String roleName,int start, int end)
	{
 		List<TRole> list=new ArrayList<TRole>();
		String sql="";
		List<Object> obs=new ArrayList<Object>();

		sql+="select tr.t_departId,tr.t_id,td.t_departName,tr.t_roleName  from tywh_role tr left join tywh_department td on tr.t_departId =td.t_id  where  1=1  and  tr.t_isUse=1 ";
		
		if(daptId!=null && daptId.trim().length() > 0)
		{
			sql+=" and  td.t_id  = ? ";
			obs.add(daptId);
		}
		if(roleName!=null && roleName.trim().length() > 0)
		{
			sql+=" and  trim(tr.t_roleName) like ? ";
			obs.add("%"+roleName+"%");
		}
		
		sql+=" order by td.t_id,tr.t_id  ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		q.setMaxResults(end).setFirstResult(start);
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TRole  tr=new TRole();
				tr.setT_departId(((Number)os[0]).intValue());
				tr.setT_id(((Number)os[1]).intValue());
				tr.setT_departName((String)os[2]);
				tr.setT_roleName((String)os[3]);
				list.add(tr);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("RoleDao中的queryAllDaptToRoleList方法查询完成");
		return list;
	}
}
