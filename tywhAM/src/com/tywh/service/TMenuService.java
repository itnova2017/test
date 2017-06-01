package com.tywh.service;

import java.text.ParseException;
import java.util.List;

import com.tywh.dao.DepartmentDao;
import com.tywh.dao.MenuDao;
import com.tywh.orm.TDepartRolerMenu;
import com.tywh.orm.TMenu;
import com.tywh.util.Page;

/**
 *	TMenu
 *  author：杜泉
 *  2014-7-11 上午11:29:23
 */
public class TMenuService
{
    private  MenuDao  menuDao;
	private DepartmentDao   departDao; 
	public MenuDao getMenuDao()
	{
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao)
	{
		this.menuDao = menuDao;
	}

	/**
	 * 通过当前的信息查询关联表中菜单表的数据信息
	 */
	public List<TDepartRolerMenu> queryMenuIdsByDaptAndRole(TDepartRolerMenu tdru)
	{
		return menuDao.queryMenuIdsByDaptAndRole(tdru);
	}

	/**
	 * 查询菜单表数据 
	 * @param b 
	 * @param menuName 
	 * @throws ParseException 
	 */
	public List<TMenu> queryMenus(TDepartRolerMenu tdru, boolean b, String menuName) throws ParseException
	{
		return menuDao.queryMenus(tdru,b,menuName);
	}

	/**
	 * 添加数据到菜单管理表中
	 */
	public void saveMenuIdsByDaptAndRole(TDepartRolerMenu tdru)
	{
		menuDao.saveMenuIdsByDaptAndRole(tdru);
	}

	/**
	 * 删除数据到菜单管理表中
	 */
	public void deleteMenuIdsByDaptAndRole(TDepartRolerMenu tdru)
	{
		menuDao.deleteMenuIdsByDaptAndRole(tdru);
	}

	/**
	 * 查询菜单的分页
	 * @throws ParseException 
	 */
	public List<TMenu> queryAllMenuList(String menuName, Page page) throws ParseException
	{
		page.setTotalRecord(menuDao.query(menuName));
		return menuDao.query(menuName,page.getStart(),page.getEnd());
	}

	/**
	 * 菜单的删除方法
	 */
	public void deleteMenuById(String menuId)
	{
		//删除菜单表数据
		menuDao.deleteMenuById(menuId);
		//删除菜单关联用户的数据
		TDepartRolerMenu drm=new TDepartRolerMenu();
		drm.setT_daptId(0);
		drm.setT_menuId(Integer.parseInt(menuId));
		drm.setT_roleId(0);
		departDao.deleteDaptRoleMenu(drm);
		
	}

	/**
	 * 查询所有菜单
	 */
	public List<TMenu> queryMenu() throws ParseException
	{
		return menuDao.queryMenu();
	}

	/**
	 * 添加菜单
	 */
	public void saveMenu(TMenu tm)
	{
		menuDao.saveMenu(tm);
	}

	/**
	 * 查询当前的菜单名是否唯一
	 */
	public boolean repeatName(String name)
	{
		return menuDao.repeatName(name);
	}

	/**
	 * 查询菜单信息
	 * @throws ParseException 
	 */
	public TMenu queryMenuById(TMenu tm) throws ParseException
	{
		return menuDao.queryMenuById(tm);
	}
	
	/**
	 * 编辑保存菜单数据
	 */
	public void editMenu(TMenu tms)
	{
		menuDao.editMenu(tms);
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
	 * 批量添加菜单到权限
	 */
	public void addMenus()
	{
		menuDao.addMenus();
	}


}
