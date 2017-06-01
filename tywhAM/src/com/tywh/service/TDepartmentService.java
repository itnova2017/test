package com.tywh.service;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tywh.dao.DepartmentDao;
import com.tywh.orm.TDepartRoleUser;
import com.tywh.orm.TDepartRolerMenu;
import com.tywh.orm.TDepartment;
import com.tywh.orm.TDepartmentUser;
import com.tywh.util.Page;

/**
 *	TDepartmentService  部门的Service
 *  author：杜泉
 *  2014-6-27 下午3:02:18
 */
public class TDepartmentService
{	
	 private DepartmentDao   departDao; 
	 
		private static Log log = LogFactory.getLog(TRoleService.class);
		
		
		public List<TDepartment> queryAllDaptList(String daptName, Page page) throws ParseException
		{
			page.setTotalRecord(departDao.query(daptName));
			return departDao.query(daptName,page.getStart(),page.getEnd());
		}
		
		/**
		 * 查询所有的部门
		 * @throws ParseException 
		 */
		public List<TDepartment> queryAllDepart() throws ParseException {
				return departDao.queryAllDepart();
		}
		
		/**
		 * 添加部门与用户关联表
		 */
		public void saveDaptAndUser(TDepartmentUser du)
		{
			departDao.saveDaptAndUser(du);
		}

		/**
		 * 删除部门与用户关系
		 */
		public void deleteDaptAndUser(TDepartmentUser du)
		{
			departDao.deleteDaptAndUser(du);
		}
		
		/**
		 * 通过关联表查询
		 */
		public List<TDepartmentUser> queryDaptUserById(TDepartmentUser tdu)
		{
			return departDao.queryDaptUserById(tdu);
		}
		
		/**
		 * 通过关联表查询
		 */
		public List<TDepartRoleUser> queryDaptRoleUserById(TDepartRoleUser tdru)
		{
			return departDao.queryDaptRoleUserById(tdru);
		}
		
		/**
		 * 通过部门ID
		 * @throws ParseException 
		 */
		public TDepartment queryDaptById(TDepartment td) throws ParseException
		{
			return departDao.queryDaptById(td);
		}
		/**
		 * 部门的删除方法
		 */
		public void deleteDepartmentById(String daptId)
		{
			//删除部门表
			departDao.deleteDepartmentById(daptId);
			//删除用户与部门关联的表   其实这里可以创建一个对象不过有别的方法调用所以没有改
			TDepartmentUser du=new TDepartmentUser();
			du.setT_userId(0);
			du.setT_departId(Integer.parseInt(daptId));
			departDao.deleteDaptAndUser(du);
			//删除部门用户角色表 
			TDepartRoleUser dru=new TDepartRoleUser();
			dru.setT_daptId(Integer.parseInt(daptId));
			dru.setT_roleId(0);
			dru.setT_userId(0);
			departDao.deleteDaptRoleUser(dru);
			//删除部门角色菜单关联表
			TDepartRolerMenu drm=new TDepartRolerMenu();
			drm.setT_daptId(Integer.parseInt(daptId));
			drm.setT_menuId(0);
			drm.setT_roleId(0);
			departDao.deleteDaptRoleMenu(drm);
		}
		
		/**
		 * 编辑保存部门数据
		 */
		public void editDepartmentInfo(TDepartment tdt)
		{
			departDao.editDepartmentInfo(tdt);
		}
		
		public void saveDepartment(TDepartment td)
		{
			departDao.saveDepartment(td);
		}
		
		public boolean repeatName(String name)
		{
			return departDao.repeatName(name);
		}
		
		public DepartmentDao getDepartDao()
		{
			return departDao;
		}

		public void setDepartDao(DepartmentDao departDao)
		{
			this.departDao = departDao;
		}













}
