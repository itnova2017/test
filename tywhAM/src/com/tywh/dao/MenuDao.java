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
import com.tywh.orm.TMenu;
import com.tywh.orm.TRole;

/**
 *	TMenuDao
 *  author：杜泉
 *  2014-7-11 上午11:30:13
 */
public class MenuDao  extends HibernateDaoSupport
{
	private static Log log = LogFactory.getLog(MenuDao.class);
	/**
	 * 通过当前的信息查询关联表中菜单表的数据信息
	 */
	public List<TDepartRolerMenu> queryMenuIdsByDaptAndRole(TDepartRolerMenu tdru)
	{
		log.info("开始调用TMenuDao中的queryMenuIdsByDaptAndRole方法查询所有以存在的菜单ID");
		List<TDepartRolerMenu> list=new ArrayList<TDepartRolerMenu>();
		StringBuffer sql = new StringBuffer();
		List<Object> obs = new ArrayList<Object>();
		sql.append("select t_id,t_daptId,t_roleId,t_menuId from tywh_departrolermenu  where 1=1 ");
		
		if(tdru.getT_daptId()> 0)
		{
			sql.append(" and  t_daptId = ? ");
			obs.add(tdru.getT_daptId());
		}
		
		if(tdru.getT_roleId()> 0)
		{
			sql.append(" and  t_roleId = ? ");
			obs.add(tdru.getT_roleId());
		}
		
		if(tdru.getT_menuId()> 0)
		{
			sql.append(" and  t_menuId = ? ");
			obs.add(tdru.getT_menuId());
		}
		
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TDepartRolerMenu  tdrus=new TDepartRolerMenu();
				tdrus.setT_id(((Number)os[0]).intValue());
				tdrus.setT_daptId(((Number)os[1]).intValue());
				tdrus.setT_roleId(((Number)os[2]).intValue());
				tdrus.setT_menuId(((Number)os[3]).intValue());
				list.add(tdrus);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("MenuDao中的queryMenuIdsByDaptAndRole方法查询完成，菜单关联集合为"+list);
		return list;
	}
	
	/**
	 * 查询菜单表数据 
	 * @param b 
	 * @param menuName 
	 * @param i 
	 * @throws ParseException 
	 */
	public List<TMenu> queryMenus(TDepartRolerMenu tdru, boolean b, String menuName) throws ParseException
	{
		log.info("开始调用TMenuDao中的queryMenus方法查询未添加的菜单");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TMenu> list=new ArrayList<TMenu>();
		StringBuffer sql = new StringBuffer();
		List<Object> obs = new ArrayList<Object>();
		sql.append("select tm.t_id,tm.t_name,tm.t_content,tm.t_parentId,tm.t_state,tm.t_num,tm.t_order,tm.t_url,tm.t_parameter,tm.t_remark,tm.t_creartTime,tm.t_user,tm.t_updateTime,tms.t_name  as Pname from tywh_menu tm left join  tywh_menu tms  on tm.t_parentId=tms.t_id  where 1=1 ");
		
		if(!b)
		{
			sql.append(" and  tm.t_id not in (  ");	
		}
		else
		{
			sql.append(" and  tm.t_id  in (  ");	
		}
		
		sql.append("select  t_menuId from tywh_departrolermenu  where 1=1 ");
		if(tdru.getT_daptId()> 0)
		{
			sql.append(" and  t_daptId = ? ");
			obs.add(tdru.getT_daptId());
		}
		
		if(tdru.getT_roleId()> 0)
		{
			sql.append(" and  t_roleId = ? ");
			obs.add(tdru.getT_roleId());
		}
		
		if(tdru.getT_menuId()> 0)
		{
			sql.append(" and  t_menuId = ? ");
			obs.add(tdru.getT_menuId());
		}
		sql.append(" ) ");
		
		if(null!=menuName && !menuName.equals(""))
		{
			sql.append(" and  tm.t_name  like ? ");
			obs.add("%"+menuName+"%");
		}

		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		List<Object[]> li=q.list();
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
				menu.setT_parentName((String)os[13]);
				list.add(menu);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("MenuDao中的queryMenus方法查询完成，为被选中菜单集合为"+list);
		return list;
	}

	/**
	 * 添加数据到菜单管理表中
	 */
	public void saveMenuIdsByDaptAndRole(TDepartRolerMenu tdru)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		sql = "insert into tywh_departrolermenu(t_daptId,t_roleId,t_menuId)  values(?,?,?)";

		if(tdru.getT_daptId()> 0)
		{
			obs.add(tdru.getT_daptId());
		}
		
		if(tdru.getT_roleId()> 0)
		{
			obs.add(tdru.getT_roleId());
		}
		
		if(tdru.getT_menuId()> 0)
		{
			obs.add(tdru.getT_menuId());
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
	 * 删除数据到菜单管理表中
	 */
	public void deleteMenuIdsByDaptAndRole(TDepartRolerMenu tdrm)
	{
		StringBuffer sql =new StringBuffer();
		sql.append("delete from tywh_departrolermenu  where 1=1 ");
		
		if(tdrm.getT_roleId()>0)
		{
			sql.append(" and t_roleId="+tdrm.getT_roleId());
		}
		
		if(tdrm.getT_daptId()>0)
		{
			sql.append(" and t_daptId="+tdrm.getT_daptId());
		}
		
		if(tdrm.getT_menuId()>0)
		{
			sql.append(" and t_menuId="+tdrm.getT_menuId());
		}		
		
		Session session=getSession();
		session.createSQLQuery(sql.toString()).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	/**
	 * 查询菜单的分页
	 */
	public int query(String menuName)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select  count(1)  from tywh_menu  tm  where 1=1 ";
		
		if(menuName!=null && menuName.trim().length() > 0)
		{
			sql+=" and  trim(tm.t_name) like ? ";
			obs.add("%"+menuName+"%");
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

	/**
	 * 查询菜单的分页
	 * @throws ParseException 
	 */
	public List<TMenu> query(String menuName, int start, int end) throws ParseException
	{
		//通过部门名称查询部门
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TMenu> list=new ArrayList<TMenu>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();

		sql="select  tm.t_id,tm.t_name,tm.t_content,tm.t_parentId,tm.t_state,tm.t_num,tm.t_order,tm.t_url,tm.t_parameter,tm.t_remark,tm.t_creartTime,tm.t_user,tm.t_updateTime,tms.t_name  as Pname   from  tywh_menu  tm  left join tywh_menu  tms  on tm.t_parentId =tms.t_id  where 1=1 ";
	
		if(menuName!=null && menuName.trim().length() > 0)
		{
			sql+=" and  trim(tm.t_name) like ? ";
			obs.add("%"+menuName+"%");
		}
		
		sql+=" order by tm.t_id ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		q.setMaxResults(end).setFirstResult(start);
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TMenu  tm=new TMenu();
				tm.setT_id(((Number)os[0]).intValue());
				tm.setT_name((String)os[1]);
				tm.setT_content((String)os[2]);
				tm.setT_parentId(null==os[3]?null :((Number)os[3]).intValue());
				tm.setT_state(((Number)os[4]).intValue());
				tm.setT_num((String)os[5]);
				tm.setT_order(((Number)os[6]).intValue());
				tm.setT_url((String)os[7]);
				tm.setT_parameter((String)os[8]);
				tm.setT_remark((String)os[9]);
				tm.setT_creartTime(null==os[10]?null :(Date)sdf.parse(os[10].toString().substring(0,19)));
				tm.setT_user(((Number)os[11]).intValue());
				tm.setT_updateTime(null==os[12]?null :(Date)sdf.parse(os[12].toString().substring(0,19)));
				tm.setT_parentName((String)os[13]);
				list.add(tm);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("MenuDao中的query方法查询完成");
		return list;
	}
	
	/**
	 * 菜单的删除方法
	 */
	public void deleteMenuById(String menuId)
	{
		StringBuffer sql =new StringBuffer();
		sql.append("delete from tywh_menu  where 1=1 ");
		
		if(null!=menuId &&Integer.parseInt(menuId)>0)
		{
			sql.append("and t_id="+menuId);
		}
		Session session=getSession();
		session.createSQLQuery(sql.toString()).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	/**
	 * 查询全部菜单
	 */
	public List<TMenu> queryMenu() throws ParseException
	{
		//通过部门名称查询部门
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				List<TMenu> list=new ArrayList<TMenu>();
				String sql=null;
				List<Object> obs=new ArrayList<Object>();

				sql="select  tm.t_id,tm.t_name,tm.t_content,tm.t_parentId,tm.t_state,tm.t_num,tm.t_order,tm.t_url,tm.t_parameter,tm.t_remark,tm.t_creartTime,tm.t_user,tm.t_updateTime   from  tywh_menu  tm  order by tm.t_id";
				
				Session session=getSession();
				Query q = session.createSQLQuery(sql.toString());
				for(int i=0;i<obs.size();++i){
					q.setParameter(i, obs.get(i));
				}
			
				List<Object[]> li=q.list();
				if(li!=null){
					for(Object[] os:li){
						TMenu  tm=new TMenu();
						tm.setT_id(((Number)os[0]).intValue());
						tm.setT_name((String)os[1]);
						tm.setT_content((String)os[2]);
						tm.setT_parentId(((Number)os[3]).intValue());
						tm.setT_state(((Number)os[4]).intValue());
						tm.setT_num((String)os[5]);
						tm.setT_order(((Number)os[6]).intValue());
						tm.setT_url((String)os[7]);
						tm.setT_parameter((String)os[8]);
						tm.setT_remark((String)os[9]);
						tm.setT_creartTime(null==os[10]?null :(Date)sdf.parse(os[10].toString().substring(0,19)));
						tm.setT_user(((Number)os[11]).intValue());
						tm.setT_updateTime(null==os[12]?null :(Date)sdf.parse(os[12].toString().substring(0,19)));
						list.add(tm);
					}
				}
				
				if(session!=null)
				{
					session.close();
				}
				log.info("MenuDao中的queryMenu方法查询完成");
				return list;
	}

	/**
	 * 添加方法
	 */
	public void saveMenu(TMenu tm)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "insert into  tywh_menu  (t_name,t_content,t_parentId,t_state,t_num,t_order,t_url,t_parameter,t_remark,t_creartTime,t_user,t_updateTime)  values(?,?,?,?,?,?,?,?,?,now(),?,now()); ";

		if(tm.getT_name()!=null && tm.getT_name().trim().length() > 0)
		{
			obs.add(tm.getT_name());
		}
		
		if(tm.getT_content()!=null && tm.getT_content().trim().length() > 0)
		{
			obs.add(tm.getT_content());
		}
		else{
			obs.add("");
		}

		if(tm.getT_parentId()> 0)
		{
			obs.add(tm.getT_parentId());
		}
		
		if(tm.getT_state()==0)
		{
			obs.add(0);
		}
		else
		{
			obs.add(1);
		}
		
		if(tm.getT_num()!=null && tm.getT_num().trim().length() > 0)
		{
			obs.add(tm.getT_num());
		}
		else
		{
			obs.add("");
		}
		
		if(tm.getT_order()>0)
		{
			obs.add(tm.getT_order());
		}
		else
		{
			obs.add(0);
		}
		
		if(tm.getT_url()!=null && tm.getT_url().trim().length() > 0)
		{
			obs.add(tm.getT_url());
		}
		else
		{
			obs.add("");
		}
		
		if(tm.getT_parameter()!=null && tm.getT_parameter().trim().length() > 0)
		{
			obs.add(tm.getT_parameter());
		}
		else
		{
			obs.add("");
		}	
		
		if(tm.getT_remark()!=null && tm.getT_remark().trim().length() > 0)
		{
			obs.add(tm.getT_remark());
		}
		else
		{
			obs.add("");
		}		
		
		if(tm.getT_user()>0)
		{
			obs.add(tm.getT_user());
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
	 * 查询当前的菜单名是否唯一
	 */
	public boolean repeatName(String name)
	{
		return ((Integer)getSession().createSQLQuery("select count(1) c  from  tywh_menu  tm   where tm.t_name = ? ").
				addScalar("c", Hibernate.INTEGER).setParameter(0, name).uniqueResult())>0;
	}

	
	public TMenu queryMenuById(TMenu tmt) throws ParseException
	{
		log.info("开始调用MenuDao中的queryMenuById方法查询菜单属性");
		//通过menuId查询菜单数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TMenu> list=new ArrayList<TMenu>();
		List<Object> obs=new ArrayList<Object>();
		String sql = "select tm.t_id,tm.t_name,tm.t_content,tm.t_parentId,tm.t_state,tm.t_num,tm.t_order,tm.t_url,tm.t_parameter,tm.t_remark,tm.t_creartTime,tm.t_user,tm.t_updateTime   from  tywh_menu tm  where 1=1 ";
		if(tmt.getT_id()>0)
		{
			sql+=" and tm.t_id =? ";
			obs.add(tmt.getT_id());
		}
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TMenu  tm=new TMenu();
				tm.setT_id(((Number)os[0]).intValue());
				tm.setT_name((String)os[1]);
				tm.setT_content((String)os[2]);
				tm.setT_parentId(((Number)os[3]).intValue());
				tm.setT_state(((Number)os[4]).intValue());
				tm.setT_num((String)os[5]);
				tm.setT_order(((Number)os[6]).intValue());
				tm.setT_url((String)os[7]);
				tm.setT_parameter((String)os[8]);
				tm.setT_remark((String)os[9]);
				tm.setT_creartTime(null==os[10]?null :(Date)sdf.parse(os[10].toString().substring(0,19)));
				tm.setT_user(((Number)os[11]).intValue());
				tm.setT_updateTime(null==os[12]?null :(Date)sdf.parse(os[12].toString().substring(0,19)));
				list.add(tm);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("MenuDao中的queryMenuById方法查询完成，菜单为"+list);
		if(null!=list && list.size()>0)
		{
			return list.get(0);
		}
		return null;
	}

	/**
	 * 编辑保存菜单数据
	 */
	public void editMenu(TMenu tms)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		sql = "update  tywh_menu tm  set tm.t_name =?,tm.t_content=?,tm.t_parentId=?,tm.t_num=?,tm.t_order=?,tm.t_url=?,tm.t_parameter=?,tm.t_remark=?,tm.t_user=?,tm.t_updateTime=now()   where tm.t_id=? ";
		
		if(tms.getT_name()!=null && tms.getT_name().trim().length() > 0)
		{
			obs.add(tms.getT_name());
		}
		
		if(tms.getT_content()!=null && tms.getT_content().trim().length() > 0)
		{
			obs.add(tms.getT_content());
		}
		else{
			obs.add("");
		}

		if(tms.getT_parentId()> 0)
		{
			obs.add(tms.getT_parentId());
		}
		
		if(tms.getT_num()!=null && tms.getT_num().trim().length() > 0)
		{
			obs.add(tms.getT_num());
		}
		else
		{
			obs.add("");
		}
		
		if(tms.getT_order()>0)
		{
			obs.add(tms.getT_order());
		}
		else
		{
			obs.add(0);
		}
		
		if(tms.getT_url()!=null && tms.getT_url().trim().length() > 0)
		{
			obs.add(tms.getT_url());
		}
		else
		{
			obs.add("");
		}
		
		if(tms.getT_parameter()!=null && tms.getT_parameter().trim().length() > 0)
		{
			obs.add(tms.getT_parameter());
		}
		else
		{
			obs.add("");
		}
		
		if(tms.getT_remark()!=null && tms.getT_remark().trim().length() > 0)
		{
			obs.add(tms.getT_remark());
		}
		else
		{
			obs.add("");
		}	
		
		if(tms.getT_user()>0)
		{
			obs.add(tms.getT_user());
		}
		
		
		if(tms.getT_id()>0)
		{
			obs.add(tms.getT_id());
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
	 * 批量添加菜单到权限
	 */
	public void addMenus()
	{
		for(int i=0;i<74;i++)
		{
			String sql =new String();
			  sql = " insert into  tywh_departrolermenu(t_daptId,t_roleId,t_menuId) values ('5','4',"+i+")";
			  Session session = getSession();
			  Query query = session.createSQLQuery(sql.toString());
			  query.executeUpdate();
		}
		System.out.println("插入完成---------");
	}


}
