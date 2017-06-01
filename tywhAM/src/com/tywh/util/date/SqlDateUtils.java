package com.tywh.util.date;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class SqlDateUtils {
	/**
	 * 转换为中文时间格式（2000年12月12日12时12分）
	 * 
	 * @param t
	 * @return
	 */
	public static String getTimestamp_zh_CN(Timestamp t) {
		if (t == null)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(1900+t.getYear()).append("年");
		sb.append(t.getMonth()+1).append("月");
		sb.append(t.getDate()).append("日");
		sb.append(t.getHours()).append("时");
		sb.append(t.getMinutes()).append("分");
		return sb.toString();
	}	
	public String getTimestamp_zh_CN_(Timestamp t) {
		if (t == null)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(1900+t.getYear()).append("年");
		sb.append(t.getMonth()+1).append("月");
		sb.append(t.getDate()).append("日");
		sb.append(t.getHours()).append("时");
		sb.append(t.getMinutes()).append("分");
		return sb.toString();
	}
	/**
	 * 转换为中文时间格式（2000年12月12日12时12分）
	 * 
	 * @param t
	 * @return
	 */
	public static String getYMDHM_zh_CN(Timestamp t) {
		if (t == null)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(1900+t.getYear()).append("年");
		sb.append(t.getMonth()+1).append("月");
		sb.append(t.getDate()).append("日");
		sb.append(t.getHours()).append("时");
		sb.append(t.getMinutes()).append("分");
		return sb.toString();
	}	
	/**
	 * 转换为中文时间格式（2000年12月12日）
	 * 
	 * @param t
	 * @return
	 */
	public static String getYMD_zh_CN(Timestamp t) {
		if (t == null)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(1900+t.getYear()).append("年");
		sb.append(t.getMonth()+1).append("月");
		sb.append(t.getDate()).append("日");		
		return sb.toString();
	}	
	/**
	 * 转换为标准时间格式（2000-12-12 12:12:12）
	 * 
	 * @param t
	 * @return
	 */
	public static String getTimestamp_YMDHMS(Timestamp t) {
		if (t == null)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(1900+t.getYear()).append("-");
		sb.append(t.getMonth()+1).append("-");
		sb.append(t.getDate()).append(" ");
		sb.append(t.getHours()).append(":");
		sb.append(t.getMinutes()).append(":");
		sb.append(t.getSeconds());
		return sb.toString();
	}

	/**
	 * 转换为标准日期格式（2000-12-12）
	 * 
	 * @param t
	 * @return
	 */
	public static String getTimestamp_YMD(Timestamp t) {
		if (t == null)
			return "";
		StringBuffer sb = new StringBuffer();
		String  year = Integer.toString(1900+t.getYear());
		String month = "";
		String date = "";
		int m = t.getMonth()+1;
		if(m < 10){
			
			month = "0"+ m;
		}else{
			month = Integer.toString(m);
		}
		int d = t.getDate();
		if(d<10){			
			date = "0"+ d;
		}else{
			date = Integer.toString(d);
		}
		 
		sb.append(year).append("-");
		sb.append(month).append("-");
		sb.append(date);		
		return sb.toString();
	}
	/**
	 * 转换为中文时间格式（2000年12月12日）
	 * 
	 * @param t
	 * @return
	 */
	public static String getDate_zh_CN(Timestamp t) {
		if (t == null)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(1900+t.getYear()).append("年");
		sb.append(t.getMonth()+1).append("月");
		sb.append(t.getDate()).append("日");		
		return sb.toString();
	}

	
	 public static void main(String[] args) throws Exception{
		 Timestamp t = new Timestamp(2000,0,12,12,12,12,12);
		 System.out.println(getTimestamp_zh_CN(t));
	 // this.
	 }
	/**
	 * method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp）
	 * 
	 * @param dateString
	 *            需要转换为timestamp的字符串
	 * @return dataTime timestamp
	 */
	public final static java.sql.Timestamp string2Time(String dateString)
			throws java.text.ParseException {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS",
				Locale.ENGLISH);// 设定格式
		// dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",
		// Locale.ENGLISH);
		dateFormat.setLenient(false);
		java.util.Date timeDate = dateFormat.parse(dateString);// util类型
		java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());// Timestamp类型,timeDate.getTime()返回一个long型
		return dateTime;
	}
	/**
	 * 转换为中文时间格式（2000年12月12日）
	 * 
	 * @param t
	 * @return
	 */
	public static String getYMD_zh_CN_Date(Date t) {
		if (t == null)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append(1900+t.getYear()).append("年");
		sb.append(t.getMonth()+1).append("月");
		sb.append(t.getDate()).append("日");		
		return sb.toString();
	}	
	/**
	 * method 将字符串类型的日期转换为一个Date（java.sql.Date）
	 * 
	 * @param dateString
	 *            需要转换为Date的字符串
	 * @return dataTime Date
	 */
	public final static java.sql.Date string2Date(String dateString)
			throws java.lang.Exception {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		dateFormat.setLenient(false);
		java.util.Date timeDate = dateFormat.parse(dateString);// util类型
		java.sql.Date dateTime = new java.sql.Date(timeDate.getTime());// sql类型
		return dateTime;
	}
	public final static boolean equals(Date date1,Date date2)
	throws java.lang.Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
//		dateFormat.setLenient(false);
		String dateString1 = dateFormat.format(date1);
		String dateString2 = dateFormat.format(date2);
		if(dateString1.equals(dateString2)) return true;
		return false;
	}
	public final static Date add(Date date,int i){
//		Date Adate = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.add(5,i);
//		System.out.println("###############################################==========time___"+i+"________======="+cal.getTime().toString());

		return  new Date(cal.getTimeInMillis());
	}
	public final static String DateToString(Date date){
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("MM月dd日", Locale.CHINA);
		
	return dateFormat.format(date);
}
	
}
