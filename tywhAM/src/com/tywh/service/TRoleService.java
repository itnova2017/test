package com.tywh.service;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tywh.dao.DepartmentDao;
import com.tywh.dao.RoleDao;
import com.tywh.orm.TDepartRoleUser;
import com.tywh.orm.TDepartRolerMenu;
import com.tywh.orm.TMenu;
import com.tywh.orm.TRole;
import com.tywh.orm.TRoleBusiness;
import com.tywh.util.Page;

/**
 *	TMenuService 菜单中查询的所有方法
 *  author：杜泉
 *  2014-6-24 下午2:59:42
 */
public class TRoleService
{
	private RoleDao roledao;
	private DepartmentDao   departDao; 
	
	private static Log log = LogFactory.getLog(TRoleService.class);


	public RoleDao getRoledao()
	{
		return roledao;
	}

	public void setRoledao(RoleDao roledao)
	{
		this.roledao = roledao;
	}

	public DepartmentDao getDepartDao()
	{
		return departDao;
	}

	public void setDepartDao(DepartmentDao departDao)
	{
		this.departDao = departDao;
	}
	
	/**
	 * 通过用户Id查询所有的角色
	 * @param sql 
	 */
	public List<TDepartRoleUser> queryRoleByUserId(int userId, StringBuilder sql) {
		try
		{
			log.info("开始调用TRoleService中的queryRoleByUserId方法查询该用户的所有角色");
			//通过userId查询当前用户的所有角色
			List<TDepartRoleUser> userRoles = roledao.queryRoleByUserId(userId,sql);
			log.info("TRoleService中的queryRoleByUserId方法查询完成，角色集合为"+userRoles);
			return userRoles;
		}
		catch (Exception e)
		{
			//在此捕获“异常”
			e.printStackTrace(); 
			log.error("异常![" + e.getMessage() + "]", e);
		}
		return null;
	}
	
	/**
	 * 通过用户Id查询所有的角色
	 * @throws ParseException 
	 */
	public TMenu findByRoleSet(Set<TDepartRoleUser> roles, Set<Integer> idSet) throws ParseException
	{
		Iterator it = roles.iterator();
		Set<Integer>roleIds = new HashSet<Integer>();
		Set<Integer>daptIds = new HashSet<Integer>();
		while(it.hasNext()){
			TDepartRoleUser role = (TDepartRoleUser)it.next();
			roleIds.add(role.getT_roleId());
			daptIds.add(role.getT_daptId());
		}
		//这里是查询所有这个人的菜单
		TMenu t = findByMenuRoleIds(roleIds,daptIds,idSet);
		if(t==null){
			throw new RuntimeException("找不到菜单");
		}else{
			return t;
		}
	}
	
	private TMenu findByMenuRoleIds(Set<Integer> roleIds,Set<Integer> daptIds,Set<Integer> idSet) throws ParseException{
		return roledao.findByMenuRoleIds(roleIds,daptIds,idSet);
	}

	/**
	 * 查询部门下的所有的角色
	 * @throws ParseException 
	 */
	public List<TRole> queryAllRoleList(String daptId, String roleName,Page page) throws ParseException
	{
		page.setTotalRecord(roledao.query(daptId,roleName));
		return roledao.query(daptId,roleName,page.getStart(),page.getEnd());
	}

	/**
	 * 角色的删除方法
	 */
	public void deleteRoleById(String roleId)
	{	
		//删除角色表数据.
		TRole tr=new TRole();
		tr.setT_id(Integer.parseInt(roleId));
		roledao.deleteRole(tr);
		
		//删除部门用户角色表 
		TDepartRoleUser dru=new TDepartRoleUser();
		dru.setT_daptId(0);
		dru.setT_roleId(Integer.parseInt(roleId));
		dru.setT_userId(0);
		departDao.deleteDaptRoleUser(dru);
		
		//删除部门角色菜单关联表
		TDepartRolerMenu drm=new TDepartRolerMenu();
		drm.setT_daptId(0);
		drm.setT_menuId(0);
		drm.setT_roleId(Integer.parseInt(roleId));
		departDao.deleteDaptRoleMenu(drm);
		
		//删除部门业务表数据
		TRoleBusiness tb=new TRoleBusiness();
		tb.setT_roleId(Integer.parseInt(roleId));
		tb.setT_busiId(0);
		roledao.deleteRoleBusiness(tb);
	}

	/**
	 * 保存角色
	 */
	public void saveRole(TRole tr)
	{
		roledao.saveRole(tr);
	}

	/**
	 * 查询角色
	 * @throws ParseException 
	 */
	public TRole queryRole(TRole tr) throws ParseException
	{
		return roledao.queryRole(tr);
	}

	/**
	 * 添加角色与部门用户关联表数据
	 */
	public void saveDaptRoleUser(TDepartRoleUser tdru)
	{
		roledao.saveDaptRoleUser(tdru);
	}

	/**
	 * 查询当前的角色名是否唯一
	 */
	public boolean repeatName(String name)
	{
		return roledao.repeatName(name);
	}

	/**
	 * 通过角色ID查询
	 * @throws ParseException 
	 */
	public TRole queryRoleById(TRole tr) throws ParseException
	{
		return roledao.queryRole(tr);
	}

	/**
	 * 修改角色信息
	 * @throws ParseException 
	 */
	public void editRole(TRole trt)
	{
		roledao.editRole(trt);
	}

	/**
	 * 删除级联数据
	 */
	public void deleteDaptRoleUser(TDepartRoleUser tdru)
	{
		roledao.deleteDaptRoleUser(tdru);
	}

	/**
	 * 增加级联数据
	 */
	public void insertDaptRoleUser(TDepartRoleUser tdru)
	{
		roledao.saveDaptRoleUser(tdru);
	}
	
	/**
	 * 查询权限数据的分页
	 */
	public List<TRole> queryAllDaptToRoleList(String daptId, String roleName,Page page)
	{
		page.setTotalRecord(roledao.queryAllDaptToRoleList(daptId,roleName));
		return roledao.queryAllDaptToRoleList(daptId,roleName,page.getStart(),page.getEnd());
	}

}