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
import com.tywh.orm.TAttendancePeople;
import com.tywh.orm.TUserinfo;

/**
 *	AttendanceDao 考勤管理Dao
 *  author：杜泉
 *  2014-11-3 上午10:22:34
 */
public class AttendanceDao extends HibernateDaoSupport
{

	public int query(String startDate, String endDate, String userName, String numId, String daptId)
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select  count(1)  from tywh_attendance  ad left join tywh_userinfo ui  on ad.t_numId = ui.t_num  where 1=1";

		if(startDate!=null && startDate.trim().length() > 0)
		{
			sql+=" and  ad.t_aDate >= ? ";
			obs.add(startDate);
		}
		
		if(endDate!=null && endDate.trim().length() > 0)
		{
			sql+=" and  (ad.t_aDate <= ?  or  ad.t_aDate like ?) ";
			obs.add(endDate);
			obs.add(endDate+"%");
		}
		
		if(userName !=null && userName.trim().length() > 0)
		{
			sql+=" and  ad.t_name = ? ";
			obs.add(userName);
		}
		
		if(numId !=null && numId.trim().length() > 0)
		{
			sql+=" and  ad.t_numId = ? ";
			obs.add(numId);
		}
		
		if(daptId!=null && daptId.trim().length() > 0)
		{
			String[] id=daptId.split(",");
			sql+=" and( ";
			for(String ss:id)
			{
				if(!ss.equals(""))
				{
					sql+=" ui.t_daptId like ?  or ";
					obs.add("%,"+ss+",%");
				}
			}
			sql=sql.substring(0,sql.length()-3);
			sql+=" ) ";
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

	public List<TAttendance> query(String startDate, String endDate,String userName,String numId,String daptId, int start, int end) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		List<TAttendance> list=new ArrayList<TAttendance>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select ad.t_id,ad.t_numId,ad.t_name,ad.t_aDate,ad.t_weekDay,ad.t_startWork,ad.t_endWork,ad.t_startWork2,ad.t_endWork2,ad.t_workTime,ad.t_plusWork,ad.t_holidayWork," +
				"	ad.t_absenceWork,ad.t_absenceCount,ad.t_noEarlyPunch,ad.t_noLatePunch,ad.t_createTime,ad.t_updateTime,ad.t_createPersonnel,ad.t_operatingPersonnel,ad.t_upWork,ad.t_supper,ad.t_workFullHours,ad.t_isBeLate,ad.t_realTimeAttendance  from tywh_attendance  ad left join tywh_userinfo ui  on ad.t_numId = ui.t_num   where 1=1 " ;
	
		if(startDate!=null && startDate.trim().length() > 0)
		{
			sql+=" and  ad.t_aDate >= ? ";
			obs.add(startDate);
		}
		
		if(endDate!=null && endDate.trim().length() > 0)
		{
			sql+=" and  (ad.t_aDate <= ?  or  ad.t_aDate like ?) ";
			obs.add(endDate);
			obs.add(endDate+"%");
		}
		
		if(userName !=null && userName.trim().length() > 0)
		{
			sql+=" and  ad.t_name like ? ";
			obs.add(userName+"%");
		}
		
		if(numId !=null && numId.trim().length() > 0)
		{
			sql+=" and  ad.t_numId = ? ";
			obs.add(numId);
		}

		if(daptId!=null && daptId.trim().length() > 0)
		{
			String[] id=daptId.split(",");
			sql+=" and( ";
			for(String ss:id)
			{
				if(!ss.equals(""))
				{
					sql+=" ui.t_daptId like ?  or ";
					obs.add("%,"+ss+",%");
				}
			}
			sql=sql.substring(0,sql.length()-3);
			sql+=" ) ";
		}
		
		sql+=" ORDER BY  ad.t_numId,ad.t_aDate  ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		q.setMaxResults(end).setFirstResult(start);
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TAttendance  ta=new TAttendance();
				ta.setId(((Number)os[0]).intValue());
				ta.setNumId((String)os[1]);
				ta.setName((String)os[2]);
				ta.setaDate(null==os[3]?null :(Date)sdf2.parse(os[3].toString().substring(0,10)));
				ta.setWeekDay((String)os[4]);
				ta.setStartWork((String)os[5]);
				ta.setEndWork((String)os[6]);			
				ta.setStartWork2((String)os[7]);
				ta.setEndWork2((String)os[8]);
				ta.setWorkTime((String)os[9]);
				ta.setPlusWork((String)os[10]);
				ta.setHolidayWork((String)os[11]);
				ta.setAbsenceWork((String)os[12]);
				ta.setAbsenceCount(((Number)os[13]).intValue());
				ta.setNoEarlyPunch(((Number)os[14]).intValue());
				ta.setNoLatePunch(((Number)os[15]).intValue());
				ta.setCreateTime(null==os[16]?null :(Date)sdf.parse(os[16].toString().substring(0,19)));
				ta.setUpdateTime(null==os[17]?null :(Date)sdf.parse(os[17].toString().substring(0,19)));
				ta.setCreatePersonnel((String)os[18]);
				ta.setOperatingPersonnel((String)os[19]);
				ta.setUpWork((String)os[20]);
				ta.setSupper(((Number)os[21]).intValue());
				ta.setWorkFullHours(((Number)os[22]).intValue());
				ta.setIsBeLate(((Number)os[23]).intValue());
				ta.setRealTimeAttendance(((Number)os[24]).floatValue()+"");
				list.add(ta);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	public List<TAttendancePeople> queryAllAttendancePeople() throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TAttendancePeople> list=new ArrayList<TAttendancePeople>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="select t_id,t_name,t_num,t_createTime  from tywh_attendancepeople	ORDER BY  t_id  ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
		
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TAttendancePeople  tap=new TAttendancePeople();
				tap.setId(((Number)os[0]).intValue());
				tap.setName((String)os[1]);
				tap.setNum((String)os[2]);
				tap.setCreateTime(null==os[3]?null :(Date)sdf.parse(os[3].toString().substring(0,19)));
				list.add(tap);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	public List<String> queryAllAttendancePeopleName()
	{
		List<String> list=new ArrayList<String>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="select t_name  from tywh_attendancepeople ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
		
		List<Object> li=q.list();
		if(li!=null){
			for(Object os:li){
				list.add((String)os);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	//保存所有用户的名称到 用户名称表中
	public void addAttendancePeople(TAttendancePeople tap)
	{
			String sql = null;
			List<Object> obs = new ArrayList<Object>();
			sql = "insert into  tywh_attendancepeople (t_name,t_num,t_createTime)  values( ?,?,now())";

			if(tap.getName()!=null && tap.getName().trim().length() > 0)
			{
				obs.add(tap.getName());
			}
			
			if(tap.getNum()!=null && tap.getNum().trim().length() > 0)
			{
				obs.add(tap.getNum());
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

	public void addAttendance(TAttendance ta)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		sql = "insert into  tywh_attendance (t_numId,t_name,t_aDate,t_weekDay,t_startWork,t_endWork,t_startWork2,t_endWork2,t_workTime,t_plusWork,t_holidayWork," +
				"	t_absenceWork,t_absenceCount,t_noEarlyPunch,t_noLatePunch,t_createTime,t_createPersonnel,t_upWork,t_supper,t_workFullHours,t_isBeLate,t_realTimeAttendance)  values( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?)";

		if(ta.getNumId()!=null && ta.getNumId().trim().length() > 0)
		{
			obs.add(ta.getNumId());
		}
		else{
			obs.add("");
		}
		
		if(ta.getName()!=null && ta.getName().trim().length() > 0)
		{
			obs.add(ta.getName());
		}
		else{
			obs.add("");
		}
		
		if(ta.getaDate()!=null && ta.getaDate().toString().trim().length() > 0)
		{
			obs.add(ta.getaDate());
		}
		else{
			obs.add("");
		}
		
		if(ta.getWeekDay()!=null && ta.getWeekDay().trim().length() > 0)
		{
			obs.add(ta.getWeekDay());
		}
		else{
			obs.add("");
		}
		
		if(ta.getStartWork()!=null && ta.getStartWork().trim().length() > 0)
		{
			obs.add(ta.getStartWork());
		}
		else{
			obs.add("");
		}
		
		if(ta.getEndWork()!=null && ta.getEndWork().trim().length() > 0)
		{
			obs.add(ta.getEndWork());
		}
		else{
			obs.add("");
		}
		
		if(ta.getStartWork2()!=null && ta.getStartWork2().trim().length() > 0)
		{
			obs.add(ta.getStartWork2());
		}
		else{
			obs.add("");
		}
		
		if(ta.getEndWork2()!=null && ta.getEndWork2().trim().length() > 0)
		{
			obs.add(ta.getEndWork2());
		}
		else{
			obs.add("");
		}
		
		if(ta.getWorkTime()!=null && ta.getWorkTime().trim().length() > 0)
		{
			obs.add(ta.getWorkTime());
		}
		else{
			obs.add("");
		}
		
		if(ta.getPlusWork()!=null && ta.getPlusWork().trim().length() > 0)
		{
			obs.add(ta.getPlusWork());
		}
		else{
			obs.add("");
		}
		
		if(ta.getHolidayWork()!=null && ta.getHolidayWork().trim().length() > 0)
		{
			obs.add(ta.getHolidayWork());
		}
		else{
			obs.add("");
		}

		if(ta.getAbsenceWork()!=null && ta.getAbsenceWork().trim().length() > 0)
		{
			obs.add(ta.getAbsenceWork());
		}
		else{
			obs.add("");
		}
		
		if(ta.getAbsenceCount()> 0)
		{
			obs.add(ta.getAbsenceCount());
		}
		else{
			obs.add(0);
		}
		
		if(ta.getNoEarlyPunch()> 0)
		{
			obs.add(ta.getNoEarlyPunch());
		}
		else{
			obs.add(0);
		}
		
		if(ta.getNoLatePunch()> 0)
		{
			obs.add(ta.getNoLatePunch());
		}
		else{
			obs.add(0);
		}
		
		if(ta.getCreatePersonnel()!=null && ta.getCreatePersonnel().trim().length() > 0)
		{
			obs.add(ta.getCreatePersonnel());
		}
		else{
			obs.add("");
		}
		
		if(ta.getUpWork()!=null && ta.getUpWork().trim().length() > 0)
		{
			obs.add(ta.getUpWork());
		}
		else{
			obs.add("");
		}
		
		if(ta.getSupper()> 0)
		{
			obs.add(ta.getSupper());
		}
		else{
			obs.add(0);
		}
		
		if(ta.getWorkFullHours()> 0)
		{
			obs.add(ta.getWorkFullHours());
		}
		else{
			obs.add(0);
		}
		
		if(ta.getIsBeLate()> 0)
		{
			obs.add(ta.getIsBeLate());
		}
		else{
			obs.add(0);
		}
		
		if(ta.getRealTimeAttendance()!=null && ta.getRealTimeAttendance().trim().length() > 0)
		{
			obs.add(ta.getRealTimeAttendance());
		}
		else{
			obs.add(0);
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
	//判断人员姓名是否重复
	public boolean repeatName(String name)
	{
		return ((Integer)getSession().createSQLQuery("select count(1) c  from tywh_attendancepeople  ta   where ta.t_name = ? ").
				addScalar("c", Hibernate.INTEGER).setParameter(0, name).uniqueResult())>0;
	}

	//通过日期与人员信息获取当天的考勤状况
	public TAttendance queryAttendance(String date, String num, String name) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		List<TAttendance> list=new ArrayList<TAttendance>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select t_id,t_numId,t_name,t_aDate,t_weekDay,t_startWork,t_endWork,t_startWork2,t_endWork2,t_workTime,t_plusWork,t_holidayWork," +
				"	t_absenceWork,t_absenceCount,t_noEarlyPunch,t_noLatePunch,t_createTime,t_updateTime,t_createPersonnel,t_operatingPersonnel,t_upWork,t_supper,t_workFullHours,t_isBeLate,t_realTimeAttendance  from tywh_attendance where 1=1 " ;
		
		if(date!=null && date.trim().length() > 0)
		{
			sql+=" and   t_aDate like ?  ";
			obs.add(date+"%");
		}
		
		if(name !=null && name.trim().length() > 0)
		{
			sql+=" and  t_name = ? ";
			obs.add(name);
		}
		
		if(num !=null && num.trim().length() > 0)
		{
			sql+=" and  t_numId = ? ";
			obs.add(num);
		}
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TAttendance  ta=new TAttendance();
				ta.setId(((Number)os[0]).intValue());
				ta.setNumId((String)os[1]);
				ta.setName((String)os[2]);
				ta.setaDate(null==os[3]?null :(Date)sdf2.parse(os[3].toString().substring(0,10)));
				ta.setWeekDay((String)os[4]);
				ta.setStartWork((String)os[5]);
				ta.setEndWork((String)os[6]);			
				ta.setStartWork2((String)os[7]);
				ta.setEndWork2((String)os[8]);
				ta.setWorkTime((String)os[9]);
				ta.setPlusWork((String)os[10]);
				ta.setHolidayWork((String)os[11]);
				ta.setAbsenceWork((String)os[12]);
				ta.setAbsenceCount(((Number)os[13]).intValue());
				ta.setNoEarlyPunch(((Number)os[14]).intValue());
				ta.setNoLatePunch(((Number)os[15]).intValue());
				ta.setCreateTime(null==os[16]?null :(Date)sdf.parse(os[16].toString().substring(0,19)));
				ta.setUpdateTime(null==os[17]?null :(Date)sdf.parse(os[17].toString().substring(0,19)));
				ta.setCreatePersonnel((String)os[18]);
				ta.setOperatingPersonnel((String)os[19]);
				ta.setUpWork((String)os[20]);
				ta.setSupper(((Number)os[21]).intValue());
				ta.setWorkFullHours(((Number)os[22]).intValue());
				ta.setIsBeLate(((Number)os[23]).intValue());
				ta.setRealTimeAttendance(((Number)os[24]).floatValue()+"");
				list.add(ta);
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

	public TAttendancePeople queryAttendancePeople(TAttendancePeople tp) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TAttendancePeople> list=new ArrayList<TAttendancePeople>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="select t_id,t_name,t_num,t_createTime  from tywh_attendancepeople  where 1=1 ";
		
		if(tp.getNum()!=null && tp.getNum().trim().length() > 0)
		{
			sql+=" and   t_num = ?  ";
			obs.add(tp.getNum());
		}
		
		if(tp.getName() !=null && tp.getName().trim().length() > 0)
		{
			sql+="and  t_name = ? ";
			obs.add(tp.getName());
		}
		
		sql+=" order by t_num";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
		
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TAttendancePeople  tap=new TAttendancePeople();
				tap.setId(((Number)os[0]).intValue());
				tap.setName((String)os[1]);
				tap.setNum((String)os[2]);
				tap.setCreateTime(null==os[3]?null :(Date)sdf.parse(os[3].toString().substring(0,19)));
				list.add(tap);
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

	//查询考勤人员个数
	public int queryAll()
	{
		List<Object> obs = new ArrayList<Object>();
		String sql = "";
		sql+="select  count(1)  from tywh_attendancepeople   where 1=1 ";

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

	public List<TAttendancePeople> queryAll(int start, int end) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TAttendancePeople> list=new ArrayList<TAttendancePeople>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="select t_id,t_name,t_num,t_createTime  from tywh_attendancepeople   order by t_num";

		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
		q.setMaxResults(end).setFirstResult(start);
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TAttendancePeople  tap=new TAttendancePeople();
				tap.setId(((Number)os[0]).intValue());
				tap.setName((String)os[1]);
				tap.setNum((String)os[2]);
				tap.setCreateTime(null==os[3]?null :(Date)sdf.parse(os[3].toString().substring(0,19)));
				list.add(tap);
			}
		}
		if(session!=null)
		{
			session.close();
		}
		return list;
	}
	
 	//考勤插入用户数据
	public void saveTUserinfo(TUserinfo tui)
	{
		String sql = null;
		List<Object> obs = new ArrayList<Object>();
		
		sql = "insert into tywh_userinfo (t_userName,t_loginName,t_password,t_isUse,t_num,t_createTime)  values( ?,?,?,?,?,now())";
		
		
		if(tui.getT_userName()!=null && tui.getT_userName().trim().length() > 0)
		{
			obs.add(tui.getT_userName());
		}
		
		if(tui.getT_loginName()!=null && tui.getT_loginName().trim().length() > 0)
		{
			obs.add(tui.getT_loginName());
		}
		else
		{
			obs.add("");
		}
		if(tui.getT_password()!=null &&tui.getT_password().trim().length() > 0)
		{
			obs.add(tui.getT_password());
		}
		else
		{
			obs.add("");
		}
		
		if(tui.getT_isUse()==0)
		{
			obs.add(0);
		}
		else
		{
			obs.add(1);
		}
		
		if(tui.getT_num()!=null &&tui.getT_num().trim().length() > 0)
		{
			obs.add(tui.getT_num());
		}
		else
		{
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

	//根据年月查询考勤人
	public List<TAttendancePeople> queryAttendanceUser(String year, String month)
	{
		List<TAttendancePeople> list=new ArrayList<TAttendancePeople>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql=" select distinct t_numId,t_name  from tywh_attendance where  t_aDate like  ?  order by  t_numId ";
		obs.add(year+"-"+month+"%");
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TAttendancePeople  tap=new TAttendancePeople();
				tap.setNum((String)os[0]);
				tap.setName((String)os[1]);
				list.add(tap);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	public TAttendance queryAttendanceInfo(String year, String month, String num, int isWeek)
	{
		List<TAttendance> list=new ArrayList<TAttendance>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql=" select  sum(t_plusWork),sum(t_supper),sum(t_upWork),sum(t_workFullHours),sum(t_realTimeAttendance),sum(t_isBeLate)  From  tywh_attendance   where 1=1  and  t_aDate like '"+year+"-"+month+"%'  and t_numId ='"+num+"'   ";
		
		if(isWeek==0)//不包含周六周日
		{
			sql+=" and t_weekDay!='星期六' and t_weekDay!='星期日' ";
		}
		else if(isWeek==1)//只查周六的加班
		{
			sql+=" and t_weekDay='星期六' ";
		}
		else if(isWeek==2)//只查周日的加班
		{
			sql+=" and t_weekDay='星期日' ";
		}
		else if(isWeek==3)//只查周六和周日的加班
		{
			sql+=" and (t_weekDay='星期六' || t_weekDay='星期日') ";
		}
		else if(isWeek==5)//不等于周日的加班
		{
			sql+=" and t_weekDay!='星期日'";
		}
		
		
		
		sql+=" group by t_numId ";
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TAttendance  ta=new TAttendance();
				ta.setPlusWork(((Number)os[0]).doubleValue()+"");
				ta.setSupper(((Number)os[1]).intValue());
				ta.setUpWork(((Number)os[2]).doubleValue()+"");
				ta.setWorkFullHours(((Number)os[3]).intValue());
				ta.setRealTimeAttendance(((Number)os[4]).doubleValue()+"");
				ta.setBeLateCount(((Number)os[5]).intValue()+"");
				list.add(ta);
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

	//查询周日的考勤情况
	public List<TAttendance> queryAttendances(String year, String month,String num, String name, String weeks) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		List<TAttendance> list=new ArrayList<TAttendance>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select t_id,t_numId,t_name,t_aDate,t_weekDay,t_startWork,t_endWork,t_startWork2,t_endWork2,t_workTime,t_plusWork,t_holidayWork," +
				"	t_absenceWork,t_absenceCount,t_noEarlyPunch,t_noLatePunch,t_createTime,t_updateTime,t_createPersonnel,t_operatingPersonnel,t_upWork,t_supper,t_workFullHours,t_isBeLate,t_realTimeAttendance  from tywh_attendance where 1=1 " ;
	
		if(year!=null && year.trim().length() > 0)
		{
			sql+=" and  substring(t_aDate,1,4)="+year ;
		}
		
		if(month!=null && month.trim().length() > 0)
		{
			sql+=" and  substring(t_aDate,6,2)="+month;
		}
		
		if(num !=null && num.toString().trim().length() > 0)
		{
			sql+=" and  t_numId = ? ";
			obs.add(num);
		}
		
		if(name !=null && name.toString().trim().length() > 0)
		{
			sql+=" and  t_name = ? ";
			obs.add(name);
		}
		
		if(weeks !=null && weeks.toString().trim().length() > 0)
		{
			sql+=" and  t_weekDay = ? ";
			obs.add(weeks);
		}
		
		sql+=" ORDER BY  t_aDate  ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TAttendance  ta=new TAttendance();
				ta.setId(((Number)os[0]).intValue());
				ta.setNumId((String)os[1]);
				ta.setName((String)os[2]);
				ta.setaDate(null==os[3]?null :(Date)sdf2.parse(os[3].toString().substring(0,10)));
				ta.setWeekDay((String)os[4]);
				ta.setStartWork((String)os[5]);
				ta.setEndWork((String)os[6]);			
				ta.setStartWork2((String)os[7]);
				ta.setEndWork2((String)os[8]);
				ta.setWorkTime((String)os[9]);
				ta.setPlusWork((String)os[10]);
				ta.setHolidayWork((String)os[11]);
				ta.setAbsenceWork((String)os[12]);
				ta.setAbsenceCount(((Number)os[13]).intValue());
				ta.setNoEarlyPunch(((Number)os[14]).intValue());
				ta.setNoLatePunch(((Number)os[15]).intValue());
				ta.setCreateTime(null==os[16]?null :(Date)sdf.parse(os[16].toString().substring(0,19)));
				ta.setUpdateTime(null==os[17]?null :(Date)sdf.parse(os[17].toString().substring(0,19)));
				ta.setCreatePersonnel((String)os[18]);
				ta.setOperatingPersonnel((String)os[19]);
				ta.setUpWork((String)os[20]);
				ta.setSupper(((Number)os[21]).intValue());
				ta.setWorkFullHours(((Number)os[22]).intValue());
				ta.setIsBeLate(((Number)os[23]).intValue());
				ta.setRealTimeAttendance(((Number)os[24]).floatValue()+"");
				list.add(ta);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	//查询当前部门的人员
	public List<TAttendancePeople> queryAttendanceListForDapt(String startDate,String endDate, String daptId)
	{
		List<TAttendancePeople> list=new ArrayList<TAttendancePeople>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select distinct ad.t_numId,ad.t_name  from tywh_attendance  ad left join tywh_userinfo ui  on ad.t_numId = ui.t_num   where 1=1 " ;
	
		if(startDate!=null && startDate.trim().length() > 0)
		{
			sql+=" and  ad.t_aDate >= ? ";
			obs.add(startDate);
		}
		
		if(endDate!=null && endDate.trim().length() > 0)
		{
			sql+=" and  (ad.t_aDate <= ?  or  ad.t_aDate like ?) ";
			obs.add(endDate);
			obs.add(endDate+"%");
		}

		if(daptId!=null && daptId.trim().length() > 0)
		{
			String[] id=daptId.split(",");
			sql+=" and( ";
			for(String ss:id)
			{
				if(!ss.equals(""))
				{
					sql+=" ui.t_daptId like ?  or ";
					obs.add("%,"+ss+",%");
				}
			}
			sql=sql.substring(0,sql.length()-3);
			sql+=" ) ";
		}
		
		sql+=" ORDER BY  ad.t_numId,ad.t_aDate  ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}

		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TAttendancePeople  tap=new TAttendancePeople();
				tap.setNum((String)os[0]);
				tap.setName((String)os[1]);
				list.add(tap);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}

	//查询某时间段的所有考勤
	public List<TAttendance> query(String startDate, String endDate) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		List<TAttendance> list=new ArrayList<TAttendance>();
		String sql=null;
		List<Object> obs=new ArrayList<Object>();
		
		sql="	select ad.t_id,ad.t_numId,ad.t_name,ad.t_aDate,ad.t_weekDay,ad.t_startWork,ad.t_endWork,ad.t_startWork2,ad.t_endWork2,ad.t_workTime,ad.t_plusWork,ad.t_holidayWork," +
				"	ad.t_absenceWork,ad.t_absenceCount,ad.t_noEarlyPunch,ad.t_noLatePunch,ad.t_createTime,ad.t_updateTime,ad.t_createPersonnel,ad.t_operatingPersonnel,ad.t_upWork,ad.t_supper,ad.t_workFullHours,ad.t_isBeLate,ad.t_realTimeAttendance  from tywh_attendance  ad left join tywh_userinfo ui  on ad.t_numId = ui.t_num   where 1=1 " ;
	
		if(startDate!=null && startDate.trim().length() > 0)
		{
			sql+=" and  ad.t_aDate >= ? ";
			obs.add(startDate);
		}
		
		if(endDate!=null && endDate.trim().length() > 0)
		{
			sql+=" and  (ad.t_aDate <= ?  or  ad.t_aDate like ?) ";
			obs.add(endDate);
			obs.add(endDate+"%");
		}
		
		sql+=" ORDER BY  ad.t_numId,ad.t_aDate  ";
		
		Session session=getSession();
		Query q = session.createSQLQuery(sql.toString());
		for(int i=0;i<obs.size();++i){
			q.setParameter(i, obs.get(i));
		}
	
		List<Object[]> li=q.list();
		if(li!=null){
			for(Object[] os:li){
				TAttendance  ta=new TAttendance();
				ta.setId(((Number)os[0]).intValue());
				ta.setNumId((String)os[1]);
				ta.setName((String)os[2]);
				ta.setaDate(null==os[3]?null :(Date)sdf2.parse(os[3].toString().substring(0,10)));
				ta.setWeekDay((String)os[4]);
				ta.setStartWork((String)os[5]);
				ta.setEndWork((String)os[6]);			
				ta.setStartWork2((String)os[7]);
				ta.setEndWork2((String)os[8]);
				ta.setWorkTime((String)os[9]);
				ta.setPlusWork((String)os[10]);
				ta.setHolidayWork((String)os[11]);
				ta.setAbsenceWork((String)os[12]);
				ta.setAbsenceCount(((Number)os[13]).intValue());
				ta.setNoEarlyPunch(((Number)os[14]).intValue());
				ta.setNoLatePunch(((Number)os[15]).intValue());
				ta.setCreateTime(null==os[16]?null :(Date)sdf.parse(os[16].toString().substring(0,19)));
				ta.setUpdateTime(null==os[17]?null :(Date)sdf.parse(os[17].toString().substring(0,19)));
				ta.setCreatePersonnel((String)os[18]);
				ta.setOperatingPersonnel((String)os[19]);
				ta.setUpWork((String)os[20]);
				ta.setSupper(((Number)os[21]).intValue());
				ta.setWorkFullHours(((Number)os[22]).intValue());
				ta.setIsBeLate(((Number)os[23]).intValue());
				ta.setRealTimeAttendance(((Number)os[24]).floatValue()+"");
				list.add(ta);
			}
		}
		
		if(session!=null)
		{
			session.close();
		}
		return list;
	}
}
