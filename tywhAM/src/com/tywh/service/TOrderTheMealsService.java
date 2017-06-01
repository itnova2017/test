package com.tywh.service;

import java.text.ParseException;
import java.util.List;

import com.tywh.dao.OrderTheMealsDao;
import com.tywh.orm.TOrderTheMeals;
import com.tywh.util.Page;

/**
 *	TOrderTheMealsService
 *  author：杜泉
 *  2014-12-16 上午10:56:37
 */
public class TOrderTheMealsService
{
	private OrderTheMealsDao orderTheMealsDao;

	public OrderTheMealsDao getOrderTheMealsDao()
	{
		return orderTheMealsDao;
	}

	public void setOrderTheMealsDao(OrderTheMealsDao orderTheMealsDao)
	{
		this.orderTheMealsDao = orderTheMealsDao;
	}

	// 查询某人订饭数据
	public List<TOrderTheMeals> queryUserList(String startDate, String endDate,int userId, Page page) throws ParseException
	{
		page.setTotalRecord(orderTheMealsDao.query(startDate,endDate,userId));
		return orderTheMealsDao.query(startDate,endDate,userId,page.getStart(),page.getEnd());
	}

	//查询今天是否订过饭
	public boolean repeatOM(int userId)
	{
		return orderTheMealsDao.repeatOM(userId);
	}

	public void saveOrderMeals(TOrderTheMeals tom)
	{
		 orderTheMealsDao.saveOrderMeals(tom);
	}

	public void deleteOrderMeals(String omId)
	{
		orderTheMealsDao.deleteOrderMeals(omId);
	}
	

	public List<TOrderTheMeals> queryAllUserList(String startDate,String endDate, Page page)
	{
		
		return null;
	}
	
	
}
