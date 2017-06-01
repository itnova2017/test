package com.tywh.service;

import java.text.ParseException;
import java.util.List;

import com.tywh.dao.PersonalLeaveDao;
import com.tywh.orm.TPersonalLeave;
import com.tywh.orm.TPersonalLeaveStatistics;
import com.tywh.util.Page;



/**
 *	TPersonalLeaveService
 *  author：杜泉
 *  2014-11-3 上午10:27:32
 */
public class TPersonalLeaveService
{
	private PersonalLeaveDao personalLeaveDao;

	public PersonalLeaveDao getPersonalLeaveDao()
	{
		return personalLeaveDao;
	}

	public void setPersonalLeaveDao(PersonalLeaveDao personalLeaveDao)
	{
		this.personalLeaveDao = personalLeaveDao;
	}
	
	//个人请假展示分页
	public List<TPersonalLeave> queryAllPersonalLeaveList(String plstate,String startDate, String endDate, String userId, String leavaUser, Page page) throws ParseException
	{
		page.setTotalRecord(personalLeaveDao.query(plstate,startDate,endDate,userId,leavaUser));
		return personalLeaveDao.query(plstate,startDate,endDate,userId,leavaUser,page.getStart(),page.getEnd());
	}

	//根据请假ID查询请假详细信息
	public TPersonalLeave queryPersonalLeave(TPersonalLeave tpl) throws ParseException
	{
		return personalLeaveDao.queryPersonalLeave(tpl);
	}
	//根据请假用户ID与请假日期查询请假详细信息
	public List<TPersonalLeave> queryPersonalLeaves(TPersonalLeave tpl) throws ParseException
	{
		return personalLeaveDao.queryPersonalLeaves(tpl);
	}

	//根据请假月份与用户Id 查询 请假统计表数据
	public TPersonalLeaveStatistics queryTPersonalLeaveStatistics(TPersonalLeaveStatistics tplss) throws ParseException
	{
		return personalLeaveDao.queryTPersonalLeaveStatistics(tplss);
	}

	//增加请假统计表数据
	public void saveTPersonalLeaveStatistics(TPersonalLeaveStatistics tpls)
	{
		personalLeaveDao.saveTPersonalLeaveStatistics(tpls);
	}
		
	//修改请假统计表数据
	public void updateTPersonalLeaveStatistics(TPersonalLeaveStatistics tplsss)
	{
		personalLeaveDao.updateTPersonalLeaveStatistics(tplsss);
	}

	//删除请假数据
	public void deletePersonalLeaveById(String plId)
	{
		personalLeaveDao.deletePersonalLeaveById(plId);
	}
	
	//添加请假
	public void savePersonalLeave(TPersonalLeave tpl)
	{
		personalLeaveDao.savePersonalLeave(tpl);
	}

	//修改请假
	public void editPersonalLeave(TPersonalLeave tplss)
	{
		personalLeaveDao.editPersonalLeave(tplss);
	}

	//查询数据所有存在的年份 
	public List<String> queryTPersonalLeaveStatisticsYear()
	{
		return personalLeaveDao.queryTPersonalLeaveStatisticsYear();
	}

	//根据部门与级别查询审核数据审核请假页面
	public List<TPersonalLeave> queryPersonalLeavesForAudit(String[] dapt, int i, String leavaUser, String daptId) throws ParseException
	{
		return personalLeaveDao.queryPersonalLeavesForAudit(dapt,i,leavaUser,daptId);
	}

	//修改一级审核状态
	public void editPersonalLeaveOfOneAudit(TPersonalLeave tplss)
	{
		personalLeaveDao.editPersonalLeaveOfOneAudit(tplss);
	}

	//修改二级审核状态
	public void editPersonalLeaveOfTwoAudit(TPersonalLeave tplss)
	{
		personalLeaveDao.editPersonalLeaveOfTwoAudit(tplss);
	}

	//统计请假合计分页
	public List<TPersonalLeaveStatistics> showPersonalLeaveAllStatistics(String leavaUser, String daptId, String year, String month,Page page)
	{
		page.setTotalRecord(personalLeaveDao.queryAll(leavaUser,daptId,year,month));
		return personalLeaveDao.queryAll(leavaUser,daptId,year,month,page.getStart(),page.getEnd());
	}
	
	//查询默认的一、二级审核状态
	public int queryApproverState(int userId, String year,String month,int level)
	{
		return personalLeaveDao.queryApproverState(userId,year,month,level);
	}
	
	 //通过id与年/月查询时间段内本人考勤记录
	public List<TPersonalLeave> showPersonalLeaveById(String plstate,String userId, String year, String month) throws ParseException
	{
		return personalLeaveDao.showPersonalLeaveById(plstate,userId,year,month);
	}

	//请假统计修改页面
	public void editPersonalLeaveStatistics(TPersonalLeaveStatistics tplss)
	{
		 personalLeaveDao.editPersonalLeaveStatistics(tplss);
	}

	//查询本人所有的驾校假小时数 部分时间段限制 
	public float queryAllDrivingLeaveStatisticsTimes(int userId, String year, String month)
	{
		return personalLeaveDao.queryAllDrivingLeaveStatisticsTimes(userId,year,month);
	}

	//查询本人所有的论文假小时数 部分时间段限制 
	public float queryAllThesisStatisticsTimes(int userId, String year, String month)
	{
		return personalLeaveDao.queryAllThesisStatisticsTimes(userId,year,month);
	}

	//查询本人本月请假中是否有周六请假 
	public List<TPersonalLeave> queryPersonalLeaves(String year, String month,String num, String name, String week, String state) throws ParseException
	{
		return personalLeaveDao.queryPersonalLeaves(year,month,num,name,week,state);
	}
	
	
	
}
