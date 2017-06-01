package com.tywh.dao;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.tywh.orm.TAttendance;
import com.tywh.orm.TOrderTheMeals;

/**
 *	OrderTheMealsDao
 *  author：杜泉
 *  2014-12-16 上午10:57:33
 */
public class OrderTheMealsDao extends HibernateDaoSupport
{

	// 查询某人订饭数据个数
	public int query(String startDate, String endDate, int userId)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select  count(1)  from tywh_orderthemeals   where 1=1 ";

		if(startDate!=null && startDate.trim().length() > 0)
		{
			sql+=" and  t_createTime >= ? ";
			obs.add(startDate);
		}
		
		if(endDate!=null && endDate.trim().length() > 0)
		{
			sql+=" and  (t_createTime <= ?  or  t_createTime like ?) ";
			obs.add(endDate);
			obs.add(endDate+"%");
		}
		
		if(userId > 0)
		{
			sql+=" and  t_userId = ? ";
			obs.add(userId);
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

	// 查询某人订饭数据
	public List<TOrderTheMeals> query(String startDate, String endDate,int userId, int start, int end) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TOrderTheMeals> list=new ArrayList<TOrderTheMeals>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="select tom.t_id,tom.t_userId,tom.t_week,tom.t_createTime,tu.t_userName,tu.t_num  from tywh_orderthemeals tom  left join  tywh_userinfo tu on tom.t_userId=tu.t_id   where 1=1 " ;
	
		if(startDate!=null && startDate.trim().length() > 0)
		{
			sql+=" and  tom.t_createTime >= ? ";
			obs.add(startDate);
		}
		
		if(endDate!=null && endDate.trim().length() > 0)
		{
			sql+=" and  (tom.t_createTime <= ?  or  tom.t_createTime like ?) ";
			obs.add(endDate);
			obs.add(endDate+"%");
		}
		
		if(userId > 0)
		{
			sql+=" and  tom.t_userId = ? ";
			obs.add(userId);
		}
		
		sql+=" ORDER BY  tom.t_createTime  ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		q.setMaxResults(end).setFirstResult(start);
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TOrderTheMeals  tm=new TOrderTheMeals();
				tm.setT_id(((Number)os[0]).intValue());
				tm.setT_userId(((Number)os[1]).intValue());
				tm.setT_week(os[2].toString());
				tm.setT_createTime(null==os[3]?null :(Date)sdf.parse(os[3].toString().substring(0,10)));
				tm.setUserName(os[4].toString());
				tm.setUserNum(os[5].toString());
				tm.setTime(null==os[3]?null :os[3].toString().substring(11,16));
				//判断如果有当天的数据 小于下午几点之前就不能进行删除了
				if(null!=os[3] && !os[3].equals(""))
				{
					//如果今天存在数据 且当前时间小于今天的15：00  显示当前的可删除状态  可删除  在删除功能中 在进行判断 如果时间大于了15：00 就提示不允许删除了
					String  onDay=sdf.format(new Date()).substring(5,10);//判断数据的创建时间 是否是今天
					String  createDay=os[3].toString().substring(5,10);//判断数据的创建时间 是否是今天
					String  dates=os[3].toString().substring(0,10)+" 15:00:00";//判断每天三点时间
					long    now = sdf2.parse(sdf2.format(new Date())).getTime();//获取当前时间的long值
					long    now2=sdf2.parse(dates).getTime();//获取每天三点的long时间
					if(onDay.equals(createDay) &&  now<now2)  //判断是当前且当前时间小于三点之前
					{
						 tm.setStateIsDel(1);
					}
					else{
						tm.setStateIsDel(0);
					}
				}
				list.add(tm);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	//查询今天是否订过饭
	public boolean repeatOM(int userId)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String  onDay=sdf.format(new Date()).substring(0,10)+"%";
		return ((Integer)getSession().createSQLQuery("select count(1) c  from tywh_orderthemeals  tom   where tom.t_createTime like ? and tom.t_userId=?").
				addScalar("c", Hibernate.INTEGER).setParameter(0, onDay).setParameter(1, userId).uniqueResult())>0;
	}

	//插入抢饭数据
	public void saveOrderMeals(TOrderTheMeals tom)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "insert into tywh_orderthemeals (t_userId,t_week,t_createTime)  values( ?,?,now())";

		if(tom.getT_userId() > 0)
		{
			obs.add(tom.getT_userId());
		}

		if(tom.getT_week()!=null && tom.getT_week().trim().length() > 0)
		{
			obs.add(tom.getT_week());
		}
		else
		{
			obs.add("");
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

	public void deleteOrderMeals(String omId)
	{
		StringBuffer sql =new StringBuffer();
		sql.append("delete from tywh_orderthemeals  where 1=1 ");
		
		if(omId!=null && omId.trim().length() > 0)
		{
			sql.append(" and t_id ="+omId);
		}
	
		Session session=getSession();
		session.createSQLQuery(sql.toString()).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}
	
	
	
}
