package com.tywh.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.tywh.orm.TLog;

/**
 *	LogDao
 *  author：杜泉
 *  2014-7-14 下午3:45:40
 */
public class LogDao extends HibernateDaoSupport
{
	private static Log log = LogFactory.getLog(LogDao.class);
	public int query(String keyword, String startDate, String endDate)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select  count(1)  from tywh_log  tl  where 1=1 ";
		
		if(keyword!=null && keyword.trim().length() > 0)
		{
			sql+=" and  trim(tl.t_content) like ? ";
			obs.add("%"+keyword+"%");
		}
		
		if(startDate!=null && startDate.trim().length() > 0)
		{
			sql+=" and  tl.t_operationTime > ? ";
			obs.add(startDate);
		}
		
		if(endDate!=null && endDate.trim().length() > 0)
		{
			sql+=" and  tl.t_operationTime < ? ";
			obs.add(endDate);
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

	public List<TLog> query(String keyword, String startDate, String endDate,int start, int end) throws ParseException
	{
		log.info("查询所有的日志");
		//通过关键词查询日志
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TLog> list=new ArrayList<TLog>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="select  t_id,t_content,t_user,t_operationTime   from  tywh_log  tl  where 1=1 ";
	
		if(keyword!=null && keyword.trim().length() > 0)
		{
			sql+=" and  trim(tl.t_content) like ? ";
			obs.add("%"+keyword+"%");
		}
		
		if(startDate!=null && startDate.trim().length() > 0)
		{
			sql+=" and  tl.t_operationTime > ? ";
			obs.add(startDate);
		}
		
		if(endDate!=null && endDate.trim().length() > 0)
		{
			sql+=" and  tl.t_operationTime < ? ";
			obs.add(endDate);
		}
		
		sql+=" ORDER BY tl.t_operationTime  desc";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		q.setMaxResults(end).setFirstResult(start);
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TLog  tl=new TLog();
				tl.setT_id(((Number)os[0]).intValue());
				tl.setT_content((String)os[1]);
				tl.setT_user((String)os[2]);
				tl.setT_operationTime(null==os[3]?null :(Date)sdf.parse(os[3].toString().substring(0,19)));
				list.add(tl);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		log.info("LogDao中的query方法查询完成");
		return list;
	}

	public TLog queryLogById(String logId) throws ParseException
	{
		log.info("通过ID查询日志详细");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TLog> list=new ArrayList<TLog>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="select  tl.t_id,tl.t_content,tl.t_user,tl.t_operationTime   from  tywh_log  tl  where 1=1 ";
	
		if(logId!=null && logId.trim().length() > 0)
		{
			sql+=" and  tl.t_id = ? ";
			obs.add(logId);
		}

		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
		
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TLog  tl=new TLog();
				tl.setT_id(((Number)os[0]).intValue());
				tl.setT_content((String)os[1]);
				tl.setT_user((String)os[2]);
				tl.setT_operationTime(null==os[3]?null :(Date)sdf.parse(os[3].toString().substring(0,19)));
				list.add(tl);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		
		log.info("LogDao中的queryLogById方法查询完成");
		if(null!=li && li.size()>0)
		{
			return list.get(0);
		}
		return null;
	}

	public void saveLog(String content,String user)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		sql = "insert into  tywh_log (t_content,t_user,t_operationTime)" +
				"	values(?,?,now())";

		obs.add(content);
		obs.add(user);
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
}
