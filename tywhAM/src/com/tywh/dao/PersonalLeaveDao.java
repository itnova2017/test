package com.tywh.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.tywh.orm.TPersonalLeave;
import com.tywh.orm.TPersonalLeaveStatistics;

/**
 *	PersonalLeaveDao 请假管理Dao
 *  author：杜泉
 *  2014-11-3 上午10:26:01
 */
public class PersonalLeaveDao  extends HibernateDaoSupport
{


	 //查看个人请假个数
	public int query(String plstate, String startDate, String endDate,String userId, String leavaUser)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select  count(1)  from tywh_personalleave pl left join tywh_userinfo ui  on pl.t_userId = ui.t_id    where 1=1 ";
		
		if(plstate!=null && plstate.trim().length() > 0)
		{
			if("1".equals(plstate))
			{
				sql+=" and  pl.t_state = 1 ";
			}
			if("2".equals(plstate))
			{
				sql+=" and  pl.t_state = 2 ";
			}
			if("3".equals(plstate))
			{
				sql+=" and  pl.t_state > 2 ";
			}
		}
		
		if(startDate!=null && startDate.trim().length() > 0)
		{
			sql+=" and  pl.t_leaveDay >= ? ";
			obs.add(startDate);
		}
		
		if(endDate!=null && endDate.trim().length() > 0)
		{
			sql+=" and  (pl.t_leaveDay <= ?  or  pl.t_leaveDay like ?) ";
			obs.add(endDate);
			obs.add(endDate+"%");
		}
		
		if(userId!=null && userId.trim().length() > 0 && !userId.equals("0"))
		{
			sql+=" and  ui.t_num = ? ";
			obs.add(userId);
		}
		
		if(leavaUser!=null && leavaUser.trim().length() > 0)
		{
			sql+=" and  ui.t_userName like ? ";
			obs.add("%"+leavaUser+"%");
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
	
	 //查看个人请假个数 分页数据
	public List<TPersonalLeave> query(String plstate, String startDate,String endDate,String userId, String leavaUser, int start, int end) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		List<TPersonalLeave> list=new ArrayList<TPersonalLeave>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select pl.t_id,pl.t_daptId,pl.t_userId,pl.t_state,pl.t_leaveTimes,pl.t_leaveDuration,pl.t_leaveDay,pl.t_leaveType,pl.t_oneApprover,pl.t_twoApprover," +
				"	pl.t_isTwoApprover,pl.t_createTime,pl.t_updateTime,pl.t_createPersonnel,pl.t_operatingPersonnel,pl.t_oneApproverState,pl.t_twoApproverState,pl.t_leaveWeekDay,ui.t_userName  from  tywh_personalleave " +
				"	pl left join tywh_userinfo ui  on pl.t_userId = ui.t_id  where 1=1 ";
	
		if(plstate!=null && plstate.trim().length() > 0)
		{
			if("1".equals(plstate))
			{
				sql+=" and  pl.t_state = 1 ";
			}
			if("2".equals(plstate))
			{
				sql+=" and  pl.t_state = 2 ";
			}
			if("3".equals(plstate))
			{
				sql+=" and  pl.t_state > 2 ";
			}
		}
		
		if(startDate!=null && startDate.trim().length() > 0)
		{
			sql+=" and  pl.t_leaveDay >= ? ";
			obs.add(startDate);
		}
		
		if(endDate!=null && endDate.trim().length() > 0)
		{
			sql+=" and  (pl.t_leaveDay <= ?  or  pl.t_leaveDay like ?) ";
			obs.add(endDate);
			obs.add(endDate+"%");
		}
		
		if(userId!=null && userId.trim().length() > 0 && !userId.equals("0"))
		{
			sql+=" and  ui.t_num = ? ";
			obs.add(userId);
		}
		
		if(leavaUser!=null && leavaUser.trim().length() > 0)
		{
			sql+=" and  ui.t_userName like ? ";
			obs.add("%"+leavaUser+"%");
		}
		
		sql+=" ORDER BY pl.t_leaveDay  ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		q.setMaxResults(end).setFirstResult(start);
		List<Object[]> li=q.list();
		Date date =new  Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,-2);//把日期往后增加一天.整数往后推,负数往前移动
		if(li!=null){
			for(Object[] os:li){
				TPersonalLeave  tp=new TPersonalLeave();
				tp.setId(((Number)os[0]).intValue());
				tp.setDaptId((String)os[1]);
				tp.setUserId(((Number)os[2]).intValue());
				tp.setState(((Number)os[3]).intValue());
				tp.setLeaveTimes((String)os[4]);
				tp.setLeaveDuration(null==os[5]?null :((Number)os[5]).floatValue());
				tp.setLeaveDay(null==os[6]?null :(Date)sdf2.parse(os[6].toString().substring(0,10)));
				//判断一下请假日期是否小于昨天以前
				Date d =sdf2.parse(os[6].toString().substring(0,10));
				tp.setIsEdit(d.before(calendar.getTime()));
				tp.setLeaveType((String)os[7]);
				tp.setOneApprover((String)os[8]);
				tp.setTwoApprover((String)os[9]);
				tp.setIsTwoApprover(null==os[10]?0 :((Number)os[10]).intValue());
				tp.setCreateTime(null==os[11]?null :(Date)sdf.parse(os[11].toString().substring(0,19)));
				tp.setUpdateTime(null==os[12]?null :(Date)sdf.parse(os[12].toString().substring(0,19)));
				tp.setCreatePersonnel((String)os[13]);
				tp.setOperatingPersonnel((String)os[14]);
				tp.setOneApproverState(null==os[15]?0 :((Number)os[15]).intValue());
				tp.setTwoApproverState(null==os[16]?0 :((Number)os[16]).intValue());
				tp.setLeaveWeekDay((String)os[17]);
				tp.setUserName((String)os[18]);
				list.add(tp);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	//查询请假
	public TPersonalLeave queryPersonalLeave(TPersonalLeave tpl) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		List<TPersonalLeave> list=new ArrayList<TPersonalLeave>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select t_id,t_daptId,t_userId,t_state,t_leaveTimes,t_leaveDuration,t_leaveDay,t_leaveType,t_oneApprover,t_twoApprover," +
				"	t_isTwoApprover,t_createTime,t_updateTime,t_createPersonnel,t_operatingPersonnel,t_oneApproverState,t_twoApproverState,t_leaveWeekDay   from  tywh_personalleave  where 1=1 ";

		if(tpl.getId() > 0)
		{
			sql+=" and  t_id = ? ";
			obs.add(tpl.getId());
		}
		
		if(tpl.getUserId() > 0)
		{
			sql+=" and  t_userId = ? ";
			obs.add(tpl.getUserId());
		}
		
		if(tpl.getLeaveDay() !=null && tpl.getLeaveDay().toString().trim().length() > 0)
		{
			sql+=" and  t_leaveDay = ? ";
			obs.add(tpl.getLeaveDay());
		}
		
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		List<Object[]> li=q.list();
		Date date =new  Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
		if(li!=null){
			for(Object[] os:li){
				TPersonalLeave  tp=new TPersonalLeave();
				tp.setId(((Number)os[0]).intValue());
				tp.setDaptId((String)os[1]);
				tp.setUserId(((Number)os[2]).intValue());
				tp.setState(((Number)os[3]).intValue());
				tp.setLeaveTimes((String)os[4]);
				tp.setLeaveDuration(null==os[5]?0 :((Number)os[5]).floatValue());
				tp.setLeaveDay(null==os[6]?null :(Date)sdf2.parse(os[6].toString().substring(0,10)));
				//判断一下请假日期是否小于昨天以前
				Date d =sdf2.parse(os[6].toString().substring(0,10));
				tp.setIsEdit(d.before(calendar.getTime()));
				tp.setLeaveType((String)os[7]);
				tp.setOneApprover((String)os[8]);
				tp.setTwoApprover((String)os[9]);
				tp.setIsTwoApprover(null==os[10]?0 :((Number)os[10]).intValue());
				tp.setCreateTime(null==os[11]?null :(Date)sdf.parse(os[11].toString().substring(0,19)));
				tp.setUpdateTime(null==os[12]?null :(Date)sdf.parse(os[12].toString().substring(0,19)));
				tp.setCreatePersonnel((String)os[13]);
				tp.setOperatingPersonnel((String)os[14]);
				tp.setOneApproverState(null==os[15]?0 :((Number)os[15]).intValue());
				tp.setTwoApproverState(null==os[16]?0 :((Number)os[16]).intValue());
				tp.setLeaveWeekDay((String)os[17]);
				list.add(tp);
			}
		}
		
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
	
	
	//查询请假
		public List<TPersonalLeave> queryPersonalLeaves(TPersonalLeave tpl) throws ParseException
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
			List<TPersonalLeave> list=new ArrayList<TPersonalLeave>();
			String sql=null;
			List<Object> obs=new ArrayList<Object>();
			
			sql="	select tpl.t_id,tpl.t_daptId,tpl.t_userId,tpl.t_state,tpl.t_leaveTimes,tpl.t_leaveDuration,tpl.t_leaveDay,tpl.t_leaveType,tpl.t_oneApprover,tpl.t_twoApprover," +
					"	tpl.t_isTwoApprover,tpl.t_createTime,tpl.t_updateTime,tpl.t_createPersonnel,tpl.t_operatingPersonnel,tpl.t_oneApproverState,tpl.t_twoApproverState,t_leaveWeekDay,tu.t_userName  " +
					"	from  tywh_personalleave tpl left join tywh_userinfo tu on tpl.t_userId=tu.t_id   where 1=1 ";

			if(tpl.getUserId() > 0)
			{
				sql+=" and  tpl.t_userId = ? ";
				obs.add(tpl.getUserId());
			}
			
			if(tpl.getLeaveDay() !=null && tpl.getLeaveDay().toString().trim().length() > 0)
			{
				sql+=" and  tpl.t_leaveDay = ? ";
				obs.add(tpl.getLeaveDay());
			}
			
			if(tpl.getUserName() !=null && tpl.getUserName().trim().length() > 0)
			{
				sql+=" and  tu.t_userName = ? ";
				obs.add(tpl.getUserName());
			}
			
			if(tpl.getIsNoState() >0)
			{
				sql+=" and  tpl.t_state != 3 and  tpl.t_state != 4 ";
			}
			
			Session session=getSession();
			Query q = session.createSQLQuery(sql.toString());
			for(int i=0;i<obs.size();++i){
				q.setParameter(i, obs.get(i));
			}
		
			List<Object[]> li=q.list();
			Date date =new  Date();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
			if(li!=null){
				for(Object[] os:li){
					TPersonalLeave  tp=new TPersonalLeave();
					tp.setId(((Number)os[0]).intValue());
					tp.setDaptId((String)os[1]);
					tp.setUserId(((Number)os[2]).intValue());
					tp.setState(((Number)os[3]).intValue());
					tp.setLeaveTimes((String)os[4]);
					tp.setLeaveDuration(null==os[5]?0 :((Number)os[5]).floatValue());
					tp.setLeaveDay(null==os[6]?null :(Date)sdf2.parse(os[6].toString().substring(0,10)));
					//判断一下请假日期是否小于昨天以前
					Date d =sdf2.parse(os[6].toString().substring(0,10));
					tp.setIsEdit(d.before(calendar.getTime()));
					tp.setLeaveType((String)os[7]);
					tp.setOneApprover((String)os[8]);
					tp.setTwoApprover((String)os[9]);
					tp.setIsTwoApprover(null==os[10]?0 :((Number)os[10]).intValue());
					tp.setCreateTime(null==os[11]?null :(Date)sdf.parse(os[11].toString().substring(0,19)));
					tp.setUpdateTime(null==os[12]?null :(Date)sdf.parse(os[12].toString().substring(0,19)));
					tp.setCreatePersonnel((String)os[13]);
					tp.setOperatingPersonnel((String)os[14]);
					tp.setOneApproverState(null==os[15]?0 :((Number)os[15]).intValue());
					tp.setTwoApproverState(null==os[16]?0 :((Number)os[16]).intValue());
					tp.setLeaveWeekDay((String)os[17]);
					tp.setUserName((String)os[18]);
					list.add(tp);
				}
			}
			
			if(session!=null)
			{
				session.close();
			}
			return list;
		}

	//查询请假统计
	public TPersonalLeaveStatistics queryTPersonalLeaveStatistics(TPersonalLeaveStatistics tplsss) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TPersonalLeaveStatistics> list=new ArrayList<TPersonalLeaveStatistics>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select tp.t_id,tp.t_daptId,tp.t_userId,tp.t_sickLeaveStatistics,tp.t_leaveStatistics,tp.t_maritalLeaveStatistics,tp.t_maternityLeaveStatistics,tp.t_funeralLeaveStatistics,tp.t_drivingLeaveStatistics,tp.t_businessLeaveStatistics, " +
			   "	tp.t_stringBreakLeaveStatistics,tp.t_leaveCountStatistics,tp.t_leaveHourStatisics,tp.t_leaveYear,tp.t_leaveMonth,tp.t_monthViolationFineAggregate,tp.t_remarks,tp.t_createTime,tp.t_updateTime,tp.t_createPersonnel,tp.t_operatingPersonnel,tp.t_lactationLeaveStatistics,tp.t_thesisStatistics,tu.t_userName " +
			   "	from tywh_personalleavestatistics  tp left join tywh_userinfo tu on tp.t_userId=tu.t_id  where 1=1 ";

		if(tplsss.getT_userId() > 0)
		{
			sql+=" and  tp.t_userId = ? ";
			obs.add(tplsss.getT_userId());
		}
		
		if(tplsss.getT_leaveYear()!=null && tplsss.getT_leaveYear().trim().length() > 0)
		{
			sql+=" and  tp.t_leaveYear = ? ";
			obs.add(tplsss.getT_leaveYear());
		}
		
		if(tplsss.getT_leaveMonth()!=null && tplsss.getT_leaveMonth().trim().length() > 0)
		{
			sql+=" and  tp.t_leaveMonth = ? ";
			obs.add(tplsss.getT_leaveMonth());
		}
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TPersonalLeaveStatistics  tp=new TPersonalLeaveStatistics();
				tp.setT_id(((Number)os[0]).intValue());
				tp.setT_daptId((String)os[1]);
				tp.setT_userId(((Number)os[2]).intValue());
				tp.setT_sickLeaveStatistics(null==os[3]?0 :((Number)os[3]).floatValue());
				tp.setT_leaveStatistics(null==os[4]?0 :((Number)os[4]).floatValue());
				tp.setT_maritalLeaveStatistics(null==os[5]?0 :((Number)os[5]).floatValue());
				tp.setT_maternityLeaveStatistics(null==os[6]?0 :((Number)os[6]).floatValue());
				tp.setT_funeralLeaveStatistics(null==os[7]?0 :((Number)os[7]).floatValue());
				tp.setT_drivingLeaveStatistics(null==os[8]?0 :((Number)os[8]).floatValue());
				tp.setT_businessLeaveStatistics(null==os[9]?0 :((Number)os[9]).floatValue());
				tp.setT_stringBreakLeaveStatistics(null==os[10]?0 :((Number)os[10]).floatValue());
				tp.setT_leaveCountStatistics(null==os[11]?0 :((Number)os[11]).intValue());
				tp.setT_leaveHourStatisics(null==os[12]?0 :((Number)os[12]).floatValue());
				tp.setT_leaveYear(os[13].toString());
				tp.setT_leaveMonth(os[14].toString());
				tp.setT_monthViolationFineAggregate(null==os[15]?0 :((Number)os[15]).floatValue());
				tp.setT_remarks((String)os[16]);
				tp.setT_createTime(null==os[17]?null :(Date)sdf.parse(os[17].toString().substring(0,19)));
				tp.setT_updateTime(null==os[18]?null :(Date)sdf.parse(os[18].toString().substring(0,19)));
				tp.setT_createPersonnel((String)os[19]);
				tp.setT_operatingPersonnel((String)os[20]);
				tp.setT_lactationLeaveStatistics(null==os[21]?0 :((Number)os[21]).floatValue());
				tp.setT_thesisStatistics(null==os[22]?0 :((Number)os[22]).floatValue());
				tp.setUserName((String)os[23]);
				list.add(tp);
			}
		}
		
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

	//查询请假统计修改请假统计数据
	public void updateTPersonalLeaveStatistics(TPersonalLeaveStatistics tplsss)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "update  tywh_personalleavestatistics  set  t_sickLeaveStatistics=?,t_leaveStatistics=?,t_maritalLeaveStatistics=?,t_maternityLeaveStatistics=?,t_funeralLeaveStatistics=?,t_drivingLeaveStatistics=?,t_businessLeaveStatistics=?, " +
			   "	t_stringBreakLeaveStatistics=?,t_leaveCountStatistics=?,t_leaveHourStatisics=?,t_leaveYear=?,t_leaveMonth=?,t_monthViolationFineAggregate=?,t_remarks=?,t_updateTime=now(),t_operatingPersonnel=?,t_lactationLeaveStatistics=?,t_thesisStatistics=?    where t_id=? ";
		
		if(tplsss.getT_sickLeaveStatistics()> 0)
		{
			obs.add(tplsss.getT_sickLeaveStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_leaveStatistics()> 0)
		{
			obs.add(tplsss.getT_leaveStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_maritalLeaveStatistics()> 0)
		{
			obs.add(tplsss.getT_maritalLeaveStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_maternityLeaveStatistics()> 0)
		{
			obs.add(tplsss.getT_maternityLeaveStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_funeralLeaveStatistics()> 0)
		{
			obs.add(tplsss.getT_funeralLeaveStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_drivingLeaveStatistics()> 0)
		{
			obs.add(tplsss.getT_drivingLeaveStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_businessLeaveStatistics()> 0)
		{
			obs.add(tplsss.getT_businessLeaveStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_stringBreakLeaveStatistics()> 0)
		{
			obs.add(tplsss.getT_stringBreakLeaveStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_leaveCountStatistics()> 0)
		{
			obs.add(tplsss.getT_leaveCountStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_leaveHourStatisics()> 0)
		{
			obs.add(tplsss.getT_leaveHourStatisics());
		}
		else{
			obs.add(0);
		}

		if(null!=tplsss.getT_leaveYear() && tplsss.getT_leaveYear().trim().length() > 0)
		{
			obs.add(tplsss.getT_leaveYear());
		}
		else{
			obs.add("");
		}
		
		if(null!=tplsss.getT_leaveMonth() && tplsss.getT_leaveMonth().trim().length() > 0)
		{
			obs.add(tplsss.getT_leaveMonth());
		}
		else{
			obs.add("");
		}
		
		if(tplsss.getT_monthViolationFineAggregate()> 0)
		{
			obs.add(tplsss.getT_monthViolationFineAggregate());
		}
		else{
			obs.add(0);
		}

		if(null!=tplsss.getT_remarks() && tplsss.getT_remarks().trim().length() > 0)
		{
			obs.add(tplsss.getT_remarks());
		}
		else{
			obs.add("");
		}
		
		if(null!=tplsss.getT_operatingPersonnel() && tplsss.getT_operatingPersonnel().trim().length() > 0)
		{
			obs.add(tplsss.getT_operatingPersonnel());
		}
		else{
			obs.add("");
		}
		
		if(tplsss.getT_lactationLeaveStatistics() > 0)
		{
			obs.add(tplsss.getT_lactationLeaveStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_thesisStatistics() > 0)
		{
			obs.add(tplsss.getT_thesisStatistics());
		}
		else{
			obs.add(0);
		}
		
		if(tplsss.getT_id()>0)
		{
			obs.add(tplsss.getT_id());
		}
		

		
		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		q.executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	//删除请假数据
	public void deletePersonalLeaveById(String plId)
	{
		String sql = "delete  from  tywh_personalleave  where  t_id="+plId;
		Session session=getSession();
		session.createSQLQuery(sql).executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	public void savePersonalLeave(TPersonalLeave tpl)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		sql = "insert into  tywh_personalleave (t_daptId,t_userId,t_state,t_leaveTimes,t_leaveDuration,t_leaveDay,t_leaveType," +
				"	t_isTwoApprover,t_createTime,t_createPersonnel,t_oneApproverState,t_twoApproverState,t_leaveWeekDay)  values( ?,?,?,?,?,?,?,?,now(),?,0,0,?)";

		if(tpl.getDaptId()!=null && tpl.getDaptId().trim().length() > 0)
		{
			obs.add(tpl.getDaptId());
		}
		
		if(tpl.getUserId()> 0)
		{
			obs.add(tpl.getUserId());
		}
		
		if(tpl.getState()> 0)
		{
			obs.add(tpl.getState());
		}
		
		if(tpl.getLeaveTimes()!=null && tpl.getLeaveTimes().trim().length() > 0)
		{
			obs.add(tpl.getLeaveTimes());
		}
		
		if(tpl.getLeaveDuration()> 0)
		{
			obs.add(tpl.getLeaveDuration());
		}

		if(tpl.getLeaveDay()!=null && tpl.getLeaveDay().toString().trim().length() > 0)
		{
			obs.add(tpl.getLeaveDay());
		}
		
		if(tpl.getLeaveType()!=null && tpl.getLeaveType().toString().trim().length() > 0)
		{
			obs.add(tpl.getLeaveType());
		}
		else
		{
			obs.add("");
		}
		
		if(tpl.getIsTwoApprover()>=0)
		{
			obs.add(tpl.getIsTwoApprover());
		}
		
		
		if(tpl.getCreatePersonnel()!=null && tpl.getCreatePersonnel().toString().trim().length() > 0)
		{
			obs.add(tpl.getCreatePersonnel());
		}else
		{
			obs.add("--");
		}
		
		if(tpl.getLeaveWeekDay()!=null && tpl.getLeaveWeekDay().toString().trim().length() > 0)
		{
			obs.add(tpl.getLeaveWeekDay());
		}
		else
		{
			obs.add("--");
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

	public void saveTPersonalLeaveStatistics(TPersonalLeaveStatistics tpl)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		sql = "	insert into  tywh_personalleavestatistics (t_daptId,t_userId,t_sickLeaveStatistics,t_leaveStatistics,t_maritalLeaveStatistics," +
				"	t_maternityLeaveStatistics,t_funeralLeaveStatistics,t_drivingLeaveStatistics,t_businessLeaveStatistics, " +
				"	t_stringBreakLeaveStatistics,t_leaveCountStatistics,t_leaveYear,t_leaveMonth," +
				"   t_createTime,t_createPersonnel,t_lactationLeaveStatistics,t_thesisStatistics)  " +
				"	values( ?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?)";

		if(tpl.getT_daptId()!=null && tpl.getT_daptId().trim().length() > 0)
		{
			obs.add(tpl.getT_daptId());
		}
		
		if(tpl.getT_userId()> 0)
		{
			obs.add(tpl.getT_userId());
		}
		
		if(tpl.getT_sickLeaveStatistics()>=0)
		{
			obs.add(tpl.getT_sickLeaveStatistics());
		}
		
		if(tpl.getT_leaveStatistics()>=0)
		{
			obs.add(tpl.getT_leaveStatistics());
		}
		
		if(tpl.getT_maritalLeaveStatistics()>=0)
		{
			obs.add(tpl.getT_maritalLeaveStatistics());
		}

		if(tpl.getT_maternityLeaveStatistics() >=0)
		{
			obs.add(tpl.getT_maternityLeaveStatistics());
		}
		
		if(tpl.getT_funeralLeaveStatistics()>=0)
		{
			obs.add(tpl.getT_funeralLeaveStatistics());
		}
		
		if(tpl.getT_drivingLeaveStatistics()>=0)
		{
			obs.add(tpl.getT_drivingLeaveStatistics());
		}
		
		if(tpl.getT_businessLeaveStatistics()>=0)
		{
			obs.add(tpl.getT_businessLeaveStatistics());
		}
		
		if(tpl.getT_stringBreakLeaveStatistics()>=0)
		{
			obs.add(tpl.getT_stringBreakLeaveStatistics());
		}
		
		if(tpl.getT_leaveCountStatistics()>=0)
		{
			obs.add(tpl.getT_leaveCountStatistics());
		}
		
		if(tpl.getT_leaveYear()!=null && tpl.getT_leaveYear().trim().length() > 0)
		{
			obs.add(tpl.getT_leaveYear());
		}
		
		if(tpl.getT_leaveMonth()!=null && tpl.getT_leaveMonth().trim().length() > 0)
		{
			obs.add(tpl.getT_leaveMonth());
		}
		
		if(tpl.getT_createPersonnel()!=null && tpl.getT_createPersonnel().toString().trim().length() > 0)
		{
			obs.add(tpl.getT_createPersonnel());
		}
		
		if(tpl.getT_lactationLeaveStatistics()>= 0)
		{
			obs.add(tpl.getT_lactationLeaveStatistics());
		}
		
		if(tpl.getT_thesisStatistics()>= 0)
		{
			obs.add(tpl.getT_thesisStatistics());
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

	public void editPersonalLeave(TPersonalLeave tplss)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "update tywh_personalleave set t_state=?,t_leaveTimes=?,t_leaveDuration=?,t_leaveDay=?,t_leaveType=?," +
				"	t_isTwoApprover=?,t_updateTime=now(),t_operatingPersonnel=?,t_leaveWeekDay=?    where t_id=? ";
		
		if(tplss.getState() > 0)
		{
			obs.add(tplss.getState() );
		}
		
		if(tplss.getLeaveTimes()!=null && tplss.getLeaveTimes().trim().length() > 0)
		{
			obs.add(tplss.getLeaveTimes());
		}
		
		if(tplss.getLeaveDuration()> 0)
		{
			obs.add(tplss.getLeaveDuration());
		}
		
		if(tplss.getLeaveDay()!=null && tplss.getLeaveDay().toString().trim().length() > 0)
		{
			obs.add(tplss.getLeaveDay());
		}
		
		if(tplss.getLeaveType()!=null && tplss.getLeaveType().trim().length() > 0)
		{
			obs.add(tplss.getLeaveType());
		}
		else
		{
			obs.add("");
		}

		if(tplss.getIsTwoApprover()>=0)
		{
			obs.add(tplss.getIsTwoApprover());
		}
		
		if(tplss.getOperatingPersonnel()!=null && tplss.getOperatingPersonnel().toString().trim().length() > 0)
		{
			obs.add(tplss.getOperatingPersonnel());
		}
		else
		{
			obs.add("--");
		}
		
		if(tplss.getLeaveWeekDay()!=null && tplss.getLeaveWeekDay().toString().trim().length() > 0)
		{
			obs.add(tplss.getLeaveWeekDay());
		}
		else
		{
			obs.add("--");
		}
		
		if(tplss.getId()>0)
		{
			obs.add(tplss.getId());
		}
		

		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		q.executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}
	
	//查询数据所有存在的年份 
	public List<String> queryTPersonalLeaveStatisticsYear()
	{
		List<String> list=new ArrayList<String>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="select DISTINCT t_leaveYear from  tywh_personalleavestatistics  ORDER BY t_leaveYear ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
		List<Object> li=q.list();
		if(li!=null){
			for(Object os:li){
				list.add(os.toString());
			}
		}
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	//根据部门与级别查询审核数据审核请假页面
	public List<TPersonalLeave> queryPersonalLeavesForAudit(String[] dapt, int level, String leavaUser, String daptId) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		List<TPersonalLeave> list=new ArrayList<TPersonalLeave>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select tp.t_id,tp.t_daptId,tp.t_userId,tp.t_state,tp.t_leaveTimes,tp.t_leaveDuration,tp.t_leaveDay,tp.t_leaveType,tp.t_oneApprover,tp.t_twoApprover," +
			   "	tp.t_isTwoApprover,tp.t_createTime,tp.t_updateTime,tp.t_createPersonnel,tp.t_operatingPersonnel,tp.t_oneApproverState,tp.t_twoApproverState,t_leaveWeekDay,tu.t_userName   from  tywh_personalleave tp left join tywh_userinfo  tu on tp.t_userId= tu.t_id  where 1=1 ";
		//根据级别不同 筛选的条件了也是不同的
		if(level==1)
		{
			if(!dapt.equals(""))
			{
				sql+="and  (";
				for(String s:dapt)
				{
					if(!"".equals(s)){
						sql+=" tp.t_daptId like ?  or ";
						obs.add("%,"+s+",%");
					}
				}
				sql=sql.substring(0,sql.length()-3);
				sql+=")";
			}
			
			sql+=" and  (tp.t_oneApprover is null  or  tp.t_oneApprover = ''  or  tp.t_oneApproverState=0  )";
		}
		else if(level==2)
		{
			if(daptId!=null && daptId.trim().length() > 0)
			{
				sql+=" and  tp.t_daptId like  ?  ";
				obs.add("%,"+daptId+",%");
			}
			sql+=" and  (tp.t_twoApprover is null  or  tp.t_twoApprover = ''  or  tp.t_twoApproverState=0  )  and  tp.t_isTwoApprover=1";
		}
		
		if(leavaUser!=null && leavaUser.trim().length() > 0)
		{
			sql+=" and  tu.t_userName  like  ?  ";
			obs.add("%"+leavaUser+"%");
		}
		sql+=" order by   tp.t_leaveDay  desc";
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TPersonalLeave  tp=new TPersonalLeave();
				tp.setId(((Number)os[0]).intValue());
				tp.setDaptId((String)os[1]);
				tp.setUserId(((Number)os[2]).intValue());
				tp.setState(((Number)os[3]).intValue());
				tp.setLeaveTimes((String)os[4]);
				tp.setLeaveDuration(null==os[5]?null :((Number)os[5]).floatValue());
				tp.setLeaveDay(null==os[6]?null :(Date)sdf2.parse(os[6].toString().substring(0,10)));
				tp.setLeaveType((String)os[7]);
				tp.setOneApprover((String)os[8]);
				tp.setTwoApprover((String)os[9]);
				tp.setIsTwoApprover(null==os[10]?0 :((Number)os[10]).intValue());
				tp.setCreateTime(null==os[11]?null :(Date)sdf.parse(os[11].toString().substring(0,19)));
				tp.setUpdateTime(null==os[12]?null :(Date)sdf.parse(os[12].toString().substring(0,19)));
				tp.setCreatePersonnel((String)os[13]);
				tp.setOperatingPersonnel((String)os[14]);
				tp.setOneApproverState(null==os[15]?0 :((Number)os[15]).intValue());
				tp.setTwoApproverState(null==os[16]?0 :((Number)os[16]).intValue());
				tp.setLeaveWeekDay((String)os[17]);
				tp.setUserName((String)os[18]);
				list.add(tp);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	public void editPersonalLeaveOfOneAudit(TPersonalLeave tplss)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "update tywh_personalleave set t_oneApprover=?,t_updateTime=now(),t_oneApproverState=1,t_operatingPersonnel=?    where t_id=? ";
		

		if(tplss.getOneApprover()!=null && tplss.getOneApprover().toString().trim().length() > 0)
		{
			obs.add(tplss.getOneApprover());
		}
		
		if(tplss.getOperatingPersonnel()!=null && tplss.getOperatingPersonnel().toString().trim().length() > 0)
		{
			obs.add(tplss.getOperatingPersonnel());
		}
		
		if(tplss.getId()>0)
		{
			obs.add(tplss.getId());
		}
		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		q.executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	public void editPersonalLeaveOfTwoAudit(TPersonalLeave tplss)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "update tywh_personalleave set t_twoApprover=?,t_updateTime=now(),t_twoApproverState=1,t_operatingPersonnel=?    where t_id=? ";
		

		if(tplss.getTwoApprover()!=null && tplss.getTwoApprover().toString().trim().length() > 0)
		{
			obs.add(tplss.getTwoApprover());
		}
		
		if(tplss.getOperatingPersonnel()!=null && tplss.getOperatingPersonnel().toString().trim().length() > 0)
		{
			obs.add(tplss.getOperatingPersonnel());
		}
		
		if(tplss.getId()>0)
		{
			obs.add(tplss.getId());
		}
		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		q.executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}

	public int queryAll(String leavaUser, String daptId, String year,String month)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select  count(1)  from tywh_personalleavestatistics tp left join tywh_userinfo tu on tp.t_userId=tu.t_id   where 1=1 ";
		
		if(leavaUser!=null && leavaUser.trim().length() > 0)
		{
				sql+=" and  tu.t_userName like ?";
				obs.add("%"+leavaUser+"%");
		}
		
		if(daptId!=null && daptId.trim().length() > 0)
		{
			sql+=" and  tp.t_daptId like  ?  ";
			obs.add("%,"+daptId+",%");
		}
		
		if(year!=null && year.trim().length() > 0)
		{
			sql+=" and  tp.t_leaveYear = ? ";
			obs.add(year);
		}
		
		if(month!=null && month.trim().length() > 0)
		{
			sql+=" and  tp.t_leaveMonth = ? ";
			obs.add(month);
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

	public List<TPersonalLeaveStatistics> queryAll(String leavaUser,String daptId, String year, String month, int start, int end)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TPersonalLeaveStatistics> list=new ArrayList<TPersonalLeaveStatistics>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select tp.t_id as 'id',tp.t_daptId as '部门Id',tp.t_userId as '用户Id',tu.t_userName as '用户名',tp.t_leaveStatistics as '事假统计',tp.t_leaveCountStatistics as '事假假次数统计',tp.t_monthViolationFineAggregate  as '月违规罚款合计',tp.t_sickLeaveStatistics as '病假统计',tp.t_maritalLeaveStatistics  as '婚假统计'," +
				"	tp.t_maternityLeaveStatistics  as '产假统计',tp.t_funeralLeaveStatistics  as '丧假统计',tp.t_drivingLeaveStatistics as '驾校假统计',tp.t_businessLeaveStatistics as '公出统计',tp.t_stringBreakLeaveStatistics as '串休统计'," +
				"	tp.t_lactationLeaveStatistics as '哺乳假统计',tp.t_thesisStatistics as '论文假统计',tp.t_leaveYear as '请假年份',tp.t_leaveMonth as '请假月份'" +
				"	from tywh_personalleavestatistics tp left join tywh_userinfo tu on tp.t_userId=tu.t_id   where 1=1 ";
	
		if(leavaUser!=null && leavaUser.trim().length() > 0)
		{
				sql+=" and  tu.t_userName like ?";
				obs.add("%"+leavaUser+"%");
		}
		
		if(daptId!=null && daptId.trim().length() > 0)
		{
			sql+=" and  tp.t_daptId like  ?  ";
			obs.add("%,"+daptId+",%");
		}
		
		if(year!=null && year.trim().length() > 0)
		{
			sql+=" and  tp.t_leaveYear = ? ";
			obs.add(year);
		}
		
		if(month!=null && month.trim().length() > 0)
		{
			sql+=" and  tp.t_leaveMonth = ? ";
			obs.add(month);
		}
		
		sql+=" order by tp.t_leaveYear desc,tp.t_leaveMonth desc  ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		q.setMaxResults(end).setFirstResult(start);
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TPersonalLeaveStatistics  tp=new TPersonalLeaveStatistics();
				tp.setT_id(((Number)os[0]).intValue());
				tp.setT_daptId((String)os[1]);
				tp.setT_userId(((Number)os[2]).intValue());
				tp.setUserName((String)os[3]);
				tp.setT_leaveStatistics(null==os[4]?0 :((Number)os[4]).floatValue());
				tp.setT_leaveCountStatistics(null==os[5]?0 :((Number)os[5]).intValue());
				tp.setT_monthViolationFineAggregate(null==os[6]?0 :((Number)os[6]).floatValue());
				tp.setT_sickLeaveStatistics(null==os[7]?0 :((Number)os[7]).floatValue());
				tp.setT_maritalLeaveStatistics(null==os[8]?0 :((Number)os[8]).floatValue());
				tp.setT_maternityLeaveStatistics(null==os[9]?0 :((Number)os[9]).floatValue());
				tp.setT_funeralLeaveStatistics(null==os[10]?0 :((Number)os[10]).floatValue());
				tp.setT_drivingLeaveStatistics(null==os[11]?0 :((Number)os[11]).floatValue());
				tp.setT_businessLeaveStatistics(null==os[12]?0 :((Number)os[12]).floatValue());
				tp.setT_stringBreakLeaveStatistics(null==os[13]?0 :((Number)os[13]).floatValue());
				tp.setT_lactationLeaveStatistics(null==os[14]?0 :((Number)os[14]).floatValue());
				tp.setT_thesisStatistics(null==os[15]?0 :((Number)os[15]).floatValue());
				tp.setT_leaveYear(os[16].toString());
				tp.setT_leaveMonth(os[17].toString());
				list.add(tp);
			}
		}
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	public int queryApproverState(int userId, String year,String month,int level)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select COUNT(*) from tywh_personalleave  where 1=1   ";
		

		if(userId> 0)
		{
				sql+=" and   t_userId = ?";
				obs.add(userId);
		}
		
		if(year!=null && year.trim().length() > 0)
		{
			sql+=" and  SUBSTRING(t_leaveDay,1,4) ="+year;
		}
		
		if(month!=null && month.trim().length() > 0)
		{
			sql+=" and  SUBSTRING(t_leaveDay,6,2)  ="+month;
		}
		
		if(level==1)
		{
			sql+=" and (t_oneApprover is null or t_oneApprover ='')  and  t_oneApproverState =0 ";
		}
		else if(level==2)
		{
			sql+=" and (t_twoApprover is null or t_twoApprover ='')  and t_twoApproverState =0  and t_isTwoApprover =1";
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

	 //通过id与年/月查询时间段内本人考勤记录
	public List<TPersonalLeave> showPersonalLeaveById(String plstate,String userId, String year, String month) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		List<TPersonalLeave> list=new ArrayList<TPersonalLeave>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select tp.t_id,tp.t_daptId,tp.t_userId,tp.t_state,tp.t_leaveTimes,tp.t_leaveDuration,tp.t_leaveDay,tp.t_leaveType,tp.t_oneApprover,tp.t_twoApprover," +
				"	tp.t_isTwoApprover,tp.t_createTime,tp.t_updateTime,tp.t_createPersonnel,tp.t_operatingPersonnel,tp.t_oneApproverState,tp.t_twoApproverState,tp.t_leaveWeekDay,tu.t_userName  " +
				"	from  tywh_personalleave tp  left join tywh_userinfo tu on tp.t_userId=tu.t_id   where 1=1  ";
	
		if(plstate!=null && plstate.trim().length() > 0)
		{
			if("1".equals(plstate))
			{
				sql+=" and  tp.t_state = 1 ";
			}
			if("2".equals(plstate))
			{
				sql+=" and  tp.t_state = 2 ";
			}
			if("3".equals(plstate))
			{
				sql+=" and  tp.t_state > 2 ";
			}
		}

		if(userId!=null && userId.trim().length() > 0)
		{
				sql+=" and   tp.t_userId = ? ";
				obs.add(userId);
		}
		
		if(year!=null && year.trim().length() > 0)
		{
			sql+=" and  substring(tp.t_leaveDay,1,4)="+year ;
		}
		
		if(month!=null && month.trim().length() > 0)
		{
			sql+=" and  substring(tp.t_leaveDay,6,2)="+month;
		}

		sql+=" ORDER BY tp.t_leaveDay  ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TPersonalLeave  tp=new TPersonalLeave();
				tp.setId(((Number)os[0]).intValue());
				tp.setDaptId((String)os[1]);
				tp.setUserId(((Number)os[2]).intValue());
				tp.setState(((Number)os[3]).intValue());
				tp.setLeaveTimes((String)os[4]);
				tp.setLeaveDuration(null==os[5]?null :((Number)os[5]).floatValue());
				tp.setLeaveDay(null==os[6]?null :(Date)sdf2.parse(os[6].toString().substring(0,10)));				
				tp.setLeaveType((String)os[7]);
				tp.setOneApprover((String)os[8]);
				tp.setTwoApprover((String)os[9]);
				tp.setIsTwoApprover(null==os[10]?0 :((Number)os[10]).intValue());
				tp.setCreateTime(null==os[11]?null :(Date)sdf.parse(os[11].toString().substring(0,19)));
				tp.setUpdateTime(null==os[12]?null :(Date)sdf.parse(os[12].toString().substring(0,19)));
				tp.setCreatePersonnel((String)os[13]);
				tp.setOperatingPersonnel((String)os[14]);
				tp.setOneApproverState(null==os[15]?0 :((Number)os[15]).intValue());
				tp.setTwoApproverState(null==os[16]?0 :((Number)os[16]).intValue());
				tp.setLeaveWeekDay((String)os[17]);
				tp.setUserName((String)os[18]);
				list.add(tp);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	//跳转到请假统计修改页面
	public void queryPersonalLeaveStatisticsById(TPersonalLeaveStatistics tpls)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "update  tywh_personalleavestatistics  set  t_monthViolationFineAggregate=?,t_remarks=?,t_updateTime=now(),t_operatingPersonnel=?   where t_id=? ";
		
		if(tpls.getT_monthViolationFineAggregate()> 0)
		{
			obs.add(tpls.getT_monthViolationFineAggregate());
		}
		else{
			obs.add(0);
		}

		if(null!=tpls.getT_remarks() && tpls.getT_remarks().trim().length() > 0)
		{
			obs.add(tpls.getT_remarks());
		}
		else{
			obs.add("");
		}
		
		if(null!=tpls.getT_operatingPersonnel() && tpls.getT_operatingPersonnel().trim().length() > 0)
		{
			obs.add(tpls.getT_operatingPersonnel());
		}
		else{
			obs.add("");
		}
		
		if(tpls.getT_id()>0)
		{
			obs.add(tpls.getT_id());
		}
		
		if(null!=tpls.getT_leaveYear() && tpls.getT_leaveYear().trim().length() > 0)
		{
			sql+=" and t_leaveYear = ?";
			obs.add(tpls.getT_leaveYear());
		}
		
		if(null!=tpls.getT_leaveMonth() && tpls.getT_leaveMonth().trim().length() > 0)
		{
			sql+=" and t_leaveMonth = ?";
			obs.add(tpls.getT_leaveMonth());
		}
		

		Session session = getSession();
		Query q = session.createSQLQuery(sql);
		for (int i = 0; i < obs.size(); ++i) {
			q.setParameter(i, obs.get(i));
		}
		q.executeUpdate();
		
		if(session!=null)
		{
			session.close();
		}
	}
	
	
	//查询请假统计修改请假统计数据
		public void editPersonalLeaveStatistics(TPersonalLeaveStatistics tplsss)
		{
			String sql = null;
			List<Object> obs = new ArrayList<Object>();
			
			sql = "update  tywh_personalleavestatistics  set t_monthViolationFineAggregate=?,t_remarks=?,t_updateTime=now(),t_operatingPersonnel=?  where  t_id=?  and  t_leaveYear=?  and  t_leaveMonth=?  ";

			
			if(tplsss.getT_monthViolationFineAggregate()> 0)
			{
				obs.add(tplsss.getT_monthViolationFineAggregate());
			}
			else{
				obs.add(0);
			}

			if(null!=tplsss.getT_remarks() && tplsss.getT_remarks().trim().length() > 0)
			{
				obs.add(tplsss.getT_remarks());
			}
			else{
				obs.add("");
			}
			
			if(null!=tplsss.getT_operatingPersonnel() && tplsss.getT_operatingPersonnel().trim().length() > 0)
			{
				obs.add(tplsss.getT_operatingPersonnel());
			}
			else{
				obs.add("");
			}
			
			if(tplsss.getT_id()>0)
			{
				obs.add(tplsss.getT_id());
			}
			
			if(null!=tplsss.getT_leaveYear() && tplsss.getT_leaveYear().trim().length() > 0)
			{
				obs.add(tplsss.getT_leaveYear());
			}
			else{
				obs.add("");
			}
			
			
			if(null!=tplsss.getT_leaveMonth() && tplsss.getT_leaveMonth().trim().length() > 0)
			{
				obs.add(tplsss.getT_leaveMonth());
			}
			else{
				obs.add("");
			}
			
			Session session = getSession();
			Query q = session.createSQLQuery(sql);
			for (int i = 0; i < obs.size(); ++i) {
				q.setParameter(i, obs.get(i));
			}
			q.executeUpdate();
			
			if(session!=null)
			{
				session.close();
			}
		}

		//查询本人所有的驾校假小时数 部分时间段限制 
		public float queryAllDrivingLeaveStatisticsTimes(int userId, String year, String month)
		{
			List<Object> obs = new ArrayList<Object>();
			String sql = "";
			sql+=" select sum(t_drivingLeaveStatistics)  from tywh_personalleavestatistics   where 1=1   ";
			
			if(userId> 0)
			{
					sql+=" and   t_userId = ?";
					obs.add(userId);
			}
			
			//年小于当年的加年等于当年的month小于当前月
			sql+=" and  (t_leaveYear < ?   or ( t_leaveYear = ?  and   t_leaveMonth < ?) )";
			obs.add(year);
			obs.add(year);
			obs.add(month);
			
		
			Session session=getSession();
			Query q = session.createSQLQuery(sql.toString());
			for (int i = 0; i < obs.size(); ++i) {
				q.setParameter(i, obs.get(i));
			}
			
			Number n =0;
			if(null!=q.uniqueResult())
			{
				n = (Number) q.uniqueResult();
			} 
			
			if(session!=null)
			{
				session.close();
			}
			
			return n.floatValue();
		}

		//查询本人所有的论文假小时数 部分时间段限制 
		public float queryAllThesisStatisticsTimes(int userId, String year, String month)
		{
			List<Object> obs = new ArrayList<Object>();
			String sql = "";
			sql+=" select sum(t_thesisStatistics)  from tywh_personalleavestatistics   where 1=1   ";
			
			if(userId> 0)
			{
					sql+=" and   t_userId = ?";
					obs.add(userId);
			}
		
			//年小于当年的加年等于当年的month小于当前月
			sql+=" and  (t_leaveYear < ?   or ( t_leaveYear = ?  and   t_leaveMonth < ?) )";
			obs.add(year);
			obs.add(year);
			obs.add(month);
			
			Session session=getSession();
			Query q = session.createSQLQuery(sql.toString());
			for (int i = 0; i < obs.size(); ++i) {
				q.setParameter(i, obs.get(i));
			}
			
			Number n =0;
			if(null!=q.uniqueResult())
			{
				n = (Number) q.uniqueResult();
			} 
			
			if(session!=null)
			{
				session.close();
			}
			
			return n.floatValue();
		}

		//查询本人本月请假中是否有周六请假 
		public List<TPersonalLeave> queryPersonalLeaves(String year,String month, String num, String name, String week, String state) throws ParseException
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
			List<TPersonalLeave> list=new ArrayList<TPersonalLeave>();
			String sql=null;
			List<Object> obs=new ArrayList<Object>();
			
			sql="  select tp.t_id,tp.t_daptId,tp.t_userId,tp.t_state,tp.t_leaveTimes,tp.t_leaveDuration,tp.t_leaveDay,tp.t_leaveType,tp.t_oneApprover,tp.t_twoApprover," +
				   "  tp.t_isTwoApprover,tp.t_createTime,tp.t_updateTime,tp.t_createPersonnel,tp.t_operatingPersonnel,tp.t_oneApproverState,tp.t_twoApproverState,tp.t_leaveWeekDay,tu.t_userName  " +
				   "  from  tywh_personalleave tp  left join tywh_userinfo tu on tp.t_userId=tu.t_id   where 1=1  ";

			if(year!=null && year.trim().length() > 0)
			{
				sql+=" and  substring(tp.t_leaveDay,1,4)="+year ;
			}
			
			if(month!=null && month.trim().length() > 0)
			{
				sql+=" and  substring(tp.t_leaveDay,6,2)="+month;
			}
			
			if(num !=null && num.toString().trim().length() > 0)
			{
				sql+=" and  tu.t_num = ? ";
				obs.add(num);
			}
			
			if(name !=null && name.toString().trim().length() > 0)
			{
				sql+=" and  tu.t_userName = ? ";
				obs.add(name);
			}
			
			if(week !=null && week.toString().trim().length() > 0)
			{
				sql+=" and  tp.t_leaveWeekDay = ? ";
				obs.add(week);
			}
			
			if(state !=null && state.toString().trim().length() > 0)
			{
				sql+=" and  tp.t_state = ? ";
				obs.add(state);
			}
			
			Session session=getSession();
			Query q = session.createSQLQuery(sql.toString());
			for(int i=0;i<obs.size();++i){
				q.setParameter(i, obs.get(i));
			}
		
			List<Object[]> li=q.list();
			Date date =new  Date();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
			if(li!=null){
				for(Object[] os:li){
					TPersonalLeave  tp=new TPersonalLeave();
					tp.setId(((Number)os[0]).intValue());
					tp.setDaptId((String)os[1]);
					tp.setUserId(((Number)os[2]).intValue());
					tp.setState(((Number)os[3]).intValue());
					tp.setLeaveTimes((String)os[4]);
					tp.setLeaveDuration(null==os[5]?0 :((Number)os[5]).floatValue());
					tp.setLeaveDay(null==os[6]?null :(Date)sdf2.parse(os[6].toString().substring(0,10)));
					//判断一下请假日期是否小于昨天以前
					Date d =sdf2.parse(os[6].toString().substring(0,10));
					tp.setIsEdit(d.before(calendar.getTime()));
					tp.setLeaveType((String)os[7]);
					tp.setOneApprover((String)os[8]);
					tp.setTwoApprover((String)os[9]);
					tp.setIsTwoApprover(null==os[10]?0 :((Number)os[10]).intValue());
					tp.setCreateTime(null==os[11]?null :(Date)sdf.parse(os[11].toString().substring(0,19)));
					tp.setUpdateTime(null==os[12]?null :(Date)sdf.parse(os[12].toString().substring(0,19)));
					tp.setCreatePersonnel((String)os[13]);
					tp.setOperatingPersonnel((String)os[14]);
					tp.setOneApproverState(null==os[15]?0 :((Number)os[15]).intValue());
					tp.setTwoApproverState(null==os[16]?0 :((Number)os[16]).intValue());
					tp.setLeaveWeekDay((String)os[17]);
					list.add(tp);
				}
			}
			
			if(session!=null)
			{
				session.close();
			}
			
			if(null!=list && list.size()>0)
			{
				return list;
			}
			return null;
		}
}
