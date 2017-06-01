package com.tywh.util;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日期常用转化类
 */
public class DateFun
{

	public static String		FORMAT				= "yyyy-MM-dd";
	/**
	 * 日期格式化模板 yyyy-MM-dd HH:mm:ss
	 */
	public static final String	DATE_FORMAT_LONG	= "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期格式化模板 yyyy-MM-dd
	 */
	public static final String	DATE_FORMAT_SHORT	= "yyyy-MM-dd";

	/**
	 * 获取new Date() 无格式
	 */
	public static Date getDate()
	{
		return new Date();
	}

	/**
	 * 根据传入的日期与格式 导出相应Date类型
	 */
	public static Date toDate(String date, String format)
	{
		if (date == null || "".equals(date))
			return null;
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try
		{
			d = sdf.parse(date);
		}
		catch (ParseException pe)
		{
			throw new RuntimeException(pe.getMessage());
		}
		return d;
	}

	/**
	 * 传入String类型日期 导出Date类型日期 按照yyyy-MM-dd格式转换
	 */
	public static Date toDate(String date)
	{
		if ("".equals(date) || date == null)
			return null;
		return toDate(date, FORMAT);
	}

	/**
	 * 传入String类型日期 导出Date类型日期 按照yyyy-MM-dd HH:mm:ss格式转换
	 */
	public static Date toDate2(String date)
	{
		if ("".equals(date) || date == null)
			return null;
		return toDate(date, DATE_FORMAT_LONG);
	}

	/**
	 * 根据传入的日期与格式 导出相应String日期
	 */
	public static String toString(Date date, String format)
	{
		if (date == null)
			return null;
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 传入Date类型日期 导出String类型日期 按照yyyy-MM-dd格式转换
	 */
	public static String toString(Date date)
	{
		if (date == null)
			return null;
		return toString(date, FORMAT);
	}

	/**
	 * 传入Date类型日期 导出String类型日期 按照yyyy-MM-dd HH:mm:ss格式转换
	 */
	public static String toString2(Date date)
	{
		if (date == null)
			return null;
		return toString(date, DATE_FORMAT_LONG);
	}

	/**
	 * 传入Date类型日期与增加的分钟 导出Date类型增加后的日期时间
	 */
	public static Date addMinute(Date date, int n)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, n);
		return c.getTime();
	}

	/**
	 * 传入Date类型日期与增加的天数 导出Date类型增加后的日期
	 */
	public static Date addDay(Date date, int n)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, n);
		return c.getTime();
	}

	/**
	 * 传入Date类型日期 获取本月的天数
	 */
	public static int maxDayOfMonth(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 传入Date类型日期 获取今年的天数
	 */
	public static int maxDayOfYear(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 传入Date类型日期 获取今天的最晚时间
	 */
	public static Date maxDateOfDay(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/**
	 * 传入Date类型日期 获取今天的最早时间
	 */
	public static Date minDateOfDay(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 传入Date类型日期 获取本月的最晚时间
	 */
	public static Date maxDateOfMonth(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/**
	 * 传入Date类型日期 获取本月的最早时间
	 */
	public static Date minDateOfMonth(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 传入Date类型日期 获取今年的最后时间
	 */
	public static Date maxDateOfYear(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), 11, 31, 23, 59, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/**
	 * 传入Date类型日期 获取今年的最早时间
	 */
	public static Date minDateOfYear(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), 0, 1, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 传入Date类型日期 获取明天的当前时间(就是日期加1)
	 */
	public static Date getNextDayDate(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	 * 传入String类型日期 获取传入时间的Date类型（Sat Jan 03 00:00:00 CST 2015）时间
	 */
	public static Date getDate(String yyyy_mm_dd)
	{
		return getCalendar(yyyy_mm_dd).getTime();
	}

	public static Calendar getCalendar(String yyyy_mm_dd)
	{
		Calendar c = Calendar.getInstance();
		c.clear();
		try
		{
			c.set(Integer.parseInt(yyyy_mm_dd.substring(0, 4)),
					Integer.parseInt(yyyy_mm_dd.substring(5, 7)) - 1,
					Integer.parseInt(yyyy_mm_dd.substring(8, 10)));
			return c;
		}
		catch (Exception e)
		{
			return null;
		}

	}

	/**
	 * 传入Date类型日期 获取long值
	 */
	public long getDays(Date date) throws ParseException
	{
		java.util.Date d = DateFun.minDateOfDay(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		long timethis = calendar.getTimeInMillis();
		calendar.setTime(d);
		long timeend = calendar.getTimeInMillis();
		long theday = (timethis - timeend) / (1000 * 60 * 60 * 24);
		return theday;
	}
	
	/**
	 * 日期转换为字符串 要转换的日期
	 * 
	 * @return 默认转为yyyy-mm-dd的字符串结果
	 */
	public static String getDateString(Date date)
	{

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SHORT);

		return sdf.format(date);

	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 *            要转换的日期
	 * @return 默认转为yyyy-mm-dd hh:mm:ss的字符串结果
	 */
	public static String getDateLongString(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_LONG);
		return sdf.format(date);

	}

	/**
	 * 字符串转换为日期
	 * 
	 * @param str
	 *            要转换的字符串
	 * @param format
	 *            转换格式，yyyy-mm-dd
	 * @return Date
	 * @throws Exception
	 */
	public static Date formatStrToDate(String str, String format)
			throws Exception
	{

		SimpleDateFormat fmt = new SimpleDateFormat(format);

		return fmt.parse(str);

	}

	/**
	 * 把日期字符串"2012-01-01"转换成字符串"01JAN"
	 */
	public static String changeStringDateToHostFormat(String strDate)
	{
		if (strDate == null)
			return null;
		String strHostFormat = "";
		strDate = strDate.trim();
		if (strDate.length() == 0 || strDate.indexOf("-") == -1)
			return strDate;
		String month = "";
		String day = strDate.substring(8, 10);
		int mon = Integer.parseInt(strDate.substring(5, 7));
		switch (mon)
		{
		case 1: // '\001'
			month = "JAN";
			break;

		case 2: // '\002'
			month = "FEB";
			break;

		case 3: // '\003'
			month = "MAR";
			break;

		case 4: // '\004'
			month = "APR";
			break;

		case 5: // '\005'
			month = "MAY";
			break;

		case 6: // '\006'
			month = "JUN";
			break;

		case 7: // '\007'
			month = "JUL";
			break;

		case 8: // '\b'
			month = "AUG";
			break;

		case 9: // '\t'
			month = "SEP";
			break;

		case 10: // '\n'
			month = "OCT";
			break;

		case 11: // '\013'
			month = "NOV";
			break;

		case 12: // '\f'
			month = "DEC";
			break;
		}
		strHostFormat = day.trim() + month.trim();
		return strHostFormat;
	}

	/**
	 * yyyy-MM-dd
	 */
	public static final String CurrentTime()
	{
		String curTime = "";
		// 格式化时间开始
		SimpleDateFormat formatter;
		java.util.Date currentDate = new java.util.Date();
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = Calendar.getInstance().getTime();
		// 格式化时间结束
		curTime = formatter.format(currentDate);
		return curTime;
	}

	/**
	 * yyyy年MM月dd日
	 */
	public static final String CurrentYMDTime()
	{
		String curTime = "";
		// 格式化时间开始
		SimpleDateFormat formatter;
		java.util.Date currentDate = new java.util.Date();
		formatter = new SimpleDateFormat("yyyy年MM月dd日");
		currentDate = Calendar.getInstance().getTime();
		// 格式化时间结束
		curTime = formatter.format(currentDate);
		return curTime;
	}

	/**
	 * 传入Date类型日期 获取 yyyy年MM月dd日
	 */
	public static final String CurrentYMDTime(String date)
	{
		String curTime = "";
		// 格式化时间开始
		SimpleDateFormat formatter;
		java.util.Date currentDate = new java.util.Date();
		formatter = new SimpleDateFormat("yyyy年MM月dd日");
		try
		{
			currentDate = formatter.parse(date);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		// 格式化时间结束
		curTime = formatter.format(currentDate);
		return curTime;
	}

	/**
	 * yyyy年MM月dd日 HH:mm:ss
	 */
	public static final String CurrentYMDHSMTime()
	{
		String curTime = "";
		// 格式化时间开始
		SimpleDateFormat formatter;
		java.util.Date currentDate = new java.util.Date();
		formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		currentDate = Calendar.getInstance().getTime();
		// 格式化时间结束
		curTime = formatter.format(currentDate);
		return curTime;
	}

	public static final String N()
	{
		String curTime = "";
		// 格式化时间开始
		SimpleDateFormat formatter;
		java.util.Date currentDate = new java.util.Date();
		formatter = new SimpleDateFormat("yyyy");
		currentDate = Calendar.getInstance().getTime();
		// 格式化时间结束
		curTime = formatter.format(currentDate);
		return curTime;
	}

	/**
	 * 获得当前日期相对i天的日期
	 * 
	 * @param offset
	 *            天数(可正可负)
	 * @return Date 改变后的日期
	 */
	public static Date getDate(int offset)
	{
		return getDate(getDate(), offset);
	}

	/**
	 * 获得相对某日i天的日期
	 * 
	 * @param date
	 *            参照日期
	 * @param offset
	 *            天数(可正可负)
	 * @return Date 改变后的日期
	 */
	public static Date getDate(Date date, int offset)
	{
		Date rDate = new Date();
		for (int j = 0; j < offset; j++)
		{
			rDate.setTime(date.getTime() + 86400000);
			date = rDate;
		}
		return rDate;
	}

	/**
	 * 从字符串中按照指定的格式生成日期对象.
	 * 
	 * @param value
	 *            日期
	 * @param type
	 *            格式
	 * @return Date 格式后的日期
	 */
	public static Date getDate(String value, String type)
	{
		Date rtndate = null;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(type);
			rtndate = sdf.parse(value);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return rtndate;
	}

	/**
	 * 根据整型的年度、月份和日生成日期
	 * 
	 * @param year
	 *            年度
	 * @param month
	 *            月份
	 * @param day
	 *            日
	 * @return Date
	 */
	public static Date getDate(int year, int month, int day)
	{
		Calendar xmas = new GregorianCalendar(year, month - 1, day);
		return xmas.getTime();
	}

	/**
	 * 根据字符型的年度、月份和日生成日期
	 * 
	 * @param year
	 *            年度
	 * @param month
	 *            月份
	 * @param day
	 *            日
	 * @return Date
	 */
	public static Date getDate(String year, String month, String day)
	{
		return getDate(new Integer(year).intValue(),
				new Integer(month).intValue() - 1, new Integer(day).intValue());
	}

	/**************************** 格式化日期时间的一系列方法 *************************************/
	/**
	 * 格式化日期时间
	 * 
	 * @param date
	 *            Date 日期
	 * @param datestyle
	 *            int 日期格式化类型
	 * @param timestyle
	 *            int 时间格式化类型
	 * @return String
	 */
	public static String dateFormat(Date date, int datestyle, int timestyle)
	{
		DateFormat df = DateFormat.getDateTimeInstance(datestyle, timestyle);
		String rtndate = df.format(date);
		return rtndate;
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param strFormat
	 *            String 日期格式化类型
	 * @return String
	 */
	public static String dateFormat(Date date, String strFormat)
	{
		DateFormat df = new SimpleDateFormat(strFormat);
		return df.format(date);
	}

	/**************************** 获得年度、月份、日期、小时、分和秒一系列方法 *************************************/
	/**
	 * 获得指定日期的年度字符串
	 * 
	 * @param date
	 *            Date 日期
	 * @return String
	 */
	public static String getYear(Date date)
	{
		return String.valueOf(_getYear(date));
	}

	/**
	 * 获得指定日期的月份字符串，不足2位，前面补0
	 * 
	 * @param date
	 *            Date 日期
	 * @return String
	 */
	public static String getMonth(Date date)
	{
		int month = _getMonth(date);
		if (month < 10)
		{
			return "0" + month;
		}
		else
		{
			return String.valueOf(month);
		}
	}

	/**
	 * 获得指定日期的日字符串，不足2位，前面补0
	 * 
	 * @param date
	 *            Date 日期
	 * @return String
	 */
	public static String getDay(Date date)
	{
		int day = _getDay(date);
		if (day < 10)
		{
			return "0" + day;
		}
		else
		{
			return String.valueOf(day);
		}
	}

	public static String getYear()
	{
		return getYear(getDate());
	}

	/**
	 * 获得指定日期的月份字符串，不足2位，前面补0
	 * 
	 * @param date
	 *            Date 日期
	 * @return String
	 */
	public static String getMonth()
	{
		return getMonth(getDate());
	}

	/**
	 * 获得指定日期的日字符串，不足2位，前面补0
	 * 
	 * @param date
	 *            Date 日期
	 * @return String
	 */
	public static String getDay()
	{
		return getDay(getDate());
	}

	/**
	 * 获得指定日期的小时字符串，不足2位，前面补0
	 * 
	 * @param date
	 *            Date 日期
	 * @return String
	 */
	public static String getHours(Date date)
	{
		int hours = _getHours(date);
		if (hours < 10)
		{
			return "0" + hours;
		}
		else
		{
			return String.valueOf(hours);
		}
	}

	/**
	 * 获得指定日期的分钟字符串，不足2位，前面补0
	 * 
	 * @param date
	 *            Date 日期
	 * @return String
	 */
	public static String getMinutes(Date date)
	{
		int minutes = _getMinutes(date);
		if (minutes < 10)
		{
			return "0" + minutes;
		}
		else
		{
			return String.valueOf(minutes);
		}
	}

	/**
	 * 获得指定日期的秒字符串，不足2位，前面补0
	 * 
	 * @param date
	 *            Date 日期
	 * @return String
	 */
	public static String getSeconds(Date date)
	{
		int seconds = _getSeconds(date);
		if (seconds < 10)
		{
			return "0" + seconds;
		}
		else
		{
			return String.valueOf(seconds);
		}
	}

	/**
	 * 获得指定日期的年度
	 * 
	 * @param date
	 *            Date 日期
	 * @return int
	 */
	public static int _getYear(Date date)
	{
		return getCalendar(date).get(Calendar.YEAR);
	}

	/**
	 * 获得指定日期的月份
	 * 
	 * @param date
	 *            Date 日期
	 * @return int
	 */
	public static int _getMonth(Date date)
	{
		return getCalendar(date).get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得指定日期的日
	 * 
	 * @param date
	 *            Date 日期
	 * @return int
	 */
	public static int _getDay(Date date)
	{
		return getCalendar(date).get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得指定日期的小时
	 * 
	 * @param date
	 *            Date 日期
	 * @return int
	 */
	public static int _getHours(Date date)
	{
		return getCalendar(date).get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获得指定日期的分钟
	 * 
	 * @param date
	 *            Date 日期
	 * @return int
	 */
	public static int _getMinutes(Date date)
	{
		return getCalendar(date).get(Calendar.MINUTE);
	}

	/**
	 * 获得指定日期的秒
	 * 
	 * @param date
	 *            Date 日期
	 * @return int
	 */
	public static int _getSeconds(Date date)
	{
		return getCalendar(date).get(Calendar.SECOND);
	}

	/**
	 * 根据指定日期获得日历对象
	 * 
	 * @param date
	 *            Date 日期
	 * @return Calendar
	 */
	public static Calendar getCalendar(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return c;
	}

	/********************* 与日期差值有关的方法 *******************************/
	/**
	 * 获得两个日期之间的差值(可跨时区)
	 * 
	 * @param date1
	 * @param date2
	 * @param tz
	 * @return long 天数
	 */
	public static long getDateDiff(Date date1, Date date2, TimeZone tz)
	{
		Calendar cal1 = null;
		Calendar cal2 = null;
		if (tz == null)
		{
			cal1 = Calendar.getInstance();
			cal2 = Calendar.getInstance();
		}
		else
		{
			cal1 = Calendar.getInstance(tz);
			cal2 = Calendar.getInstance(tz);
		}
		cal1.setTime(date1);
		long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET)
				+ cal1.get(Calendar.DST_OFFSET);
		cal2.setTime(date2);
		long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET)
				+ cal2.get(Calendar.DST_OFFSET);
		long hr1 = (long) (ldate1 / 3600000); // 60*60*1000
		long hr2 = (long) (ldate2 / 3600000);
		long days1 = (long) hr1 / 24;
		long days2 = (long) hr2 / 24;
		return days2 - days1;
		/*
		 * int weekOffset =(cal2.get(Calendar.DAY_OF_WEEK) -
		 * cal1.get(Calendar.DAY_OF_WEEK)) < 0 ? 1 : 0; int weekDiff = dateDiff
		 * / 7 + weekOffset; int yearDiff = cal2.get(Calendar.YEAR) -
		 * cal1.get(Calendar.YEAR); int monthDiff = yearDiff * 12 +
		 * cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
		 */
	}

	/**
	 * 获得同一时区两个日期之间的差值
	 * 
	 * @param date1
	 * @param date2
	 * @return long 天数
	 */
	public static long getDateDiff(Date date1, Date date2)
	{
		if (date1 == null)
			date1 = getDate();
		if (date2 == null)
			date2 = getDate();
		long ldate1 = date1.getTime();
		long ldate2 = date2.getTime();
		long iDatenum = 0;
		iDatenum = (long) ((ldate2 - ldate1) / 86400000);
		return iDatenum;
	}

	/******************************** 与周和星期有关的方法 ****************************/
	/**
	 * 获得指定字符串类型日期的星期
	 * 
	 * @param sdate
	 *            String 日期
	 * @param fmt
	 *            String 格式化类型
	 * @return String
	 */
	public static String getWeek(String sdate, String fmt)
	{
		SimpleDateFormat df = new SimpleDateFormat(fmt);
		try
		{
			return getWeek(df.parse(sdate));
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得指定日期的星期
	 * 
	 * @param date
	 *            Date 日期
	 * @return String
	 */
	public static String getWeek(Date date)
	{
		Calendar cal1 = Calendar.getInstance();
		String chiweek = null;
		cal1.setTime(date);
		int bh = cal1.get(Calendar.DAY_OF_WEEK);
		switch (bh)
		{
		case 1:
			chiweek = "星期日";
			break;
		case 2:
			chiweek = "星期一";
			break;
		case 3:
			chiweek = "星期二";
			break;
		case 4:
			chiweek = "星期三";
			break;
		case 5:
			chiweek = "星期四";
			break;
		case 6:
			chiweek = "星期五";
			break;
		case 7:
			chiweek = "星期六";
			break;
		}
		return chiweek;
	}

	/**
	 * 获得当前日期是今年的第几周
	 * 
	 * @return int 周数
	 */
	public static int getWeekOfYear()
	{
		return getWeekOfYear(getDate());
	}

	/**
	 * 获得指定日期是今年的第几周
	 * 
	 * @param date
	 *            Date 日期
	 * @return int 周数
	 */
	public static int getWeekOfYear(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获得当前日期offset天是今年的第几周
	 * 
	 * @param offset
	 *            int 日期
	 * @return int 周数
	 */
	public static int getWeekOfYear(int offset)
	{
		return getWeekOfYear(getDate(), offset);
	}

	/**
	 * 获得某日期offset天是今年的第几周
	 * 
	 * @param date
	 *            Date 参照日期
	 * @param offset
	 *            int 日期
	 * @return int 周数
	 */
	public static int getWeekOfYear(Date date, int offset)
	{
		return getWeekOfYear(getDate(date, offset));
	}

	/**
	 * 获得指定年有多少周
	 * 
	 * @param int year 年度
	 * @return int 周数
	 */
	public static int getWeekNumbersOfYear(int year)
	{
		GregorianCalendar cal = new GregorianCalendar(year, 12, 31);
		return cal.getMaximum(GregorianCalendar.WEEK_OF_YEAR);
	}

	/**
	 * 获得指定年有多少周
	 * 
	 * @param String
	 *            year 年度
	 * @return int 周数
	 */
	public static int getWeekNumbersOfYear(String year)
	{
		return getWeekNumbersOfYear(new Integer(year).intValue());
	}

	/**
	 * 得到指定年月的最后一天
	 * 
	 * @param strDate
	 *            String 字符串格式的日期
	 * @return String 月份最后一天的字符
	 */
	public static String getLastDayOfMonth(String strDate)
	{
		int year = new Integer(strDate.substring(0, 4)).intValue();
		int month = new Integer(strDate.substring(5, 7)).intValue();
		return dateFormat(getLastDate(year, month), "dd");
	}

	/**
	 * 得到指定年月的最后一天的日期
	 * 
	 * @param date
	 *            int 年
	 * @param month
	 *            int 月
	 * @return Date
	 */
	public static Date getLastDate(int year, int month)
	{
		int day = 0;
		String strmonth = null;
		boolean blrn = false; // 是否为闰年
		if ((month < 1) || (month > 12))
		{
			return null;
		}
		if (((year % 4 == 0) || (year % 100 == 0)) && (year % 400 == 0))
		{
			blrn = true;
		}
		else
		{
			blrn = false;
		}
		if ((month == 1) || (month == 3) || (month == 5) || (month == 7)
				|| (month == 8) || (month == 10) || (month == 12))
		{
			day = 31;
		}
		if ((month == 4) || (month == 6) || (month == 9) || (month == 11))
		{
			day = 30;
		}
		if (month == 2)
		{
			if (blrn == true)
			{
				day = 29;
			}
			else
			{
				day = 28;
			}
		}
		if (month < 10)
		{
			strmonth = "0" + Integer.toString(month);
		}
		else
		{
			strmonth = Integer.toString(month);
		}
		return getDate(
				Integer.toString(year) + "-" + strmonth + "-"
						+ Integer.toString(day), "yyyy-MM-dd");
	}

	/**
	 * 获取某一时间段内的所有日期
	 */
	public static ArrayList<String> TimeQuantum(String startDay, String endDay)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			ArrayList<String> list = new ArrayList<String>();
			Date begin = sdf.parse(startDay);
			Date end = sdf.parse(endDay);
			double between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			double day = between / (24 * 3600);

			for (int i = 0; i <= day; i++)
			{
				Calendar cd = Calendar.getInstance();
				cd.setTime(sdf.parse(startDay));
				cd.add(Calendar.DATE, i);// 增加一天
				list.add(sdf.format(cd.getTime()));
			}
			return list;
		}
		catch (ParseException e)
		{
			System.out.println("日期转换异常");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 某一时间 是否大于当前时间点 基准小时 referenceTime 待比较时间 compareTime
	 */
	public static boolean getHourCompare(int referenceTime, String compareTime)
	{
		try
		{
			// 格式化时间
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			if(compareTime.equals("")) return false;
			Date oldDate = format.parse(compareTime);
			if (oldDate.getHours() >= referenceTime)
			{
				return true;
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 某一时间 是否大于当前时间点 基准时间 referenceTime 待比较时间 compareTime
	 */
	public static boolean getTimeCompare(String referenceTime, String compareTime)
	{
		try
		{
			// 格式化时间
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			if(referenceTime.equals("") || compareTime.equals("")) return false;
			Date oldDate = format.parse(referenceTime);//基准时间
			Date newDate= format.parse(compareTime);//待比较时间
			if (oldDate.getTime() >= newDate.getTime())
			return true;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 某一时间与当前时间的四舍五入的小时数 基准时间 referenceTime 待比较时间 compareTime
	 */
	public static Double getTimeDifferenceCompare(String referenceTime, String compareTime)
	{
		try
		{
			// 格式化时间
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			if(referenceTime.equals("") || compareTime.equals("") || compareTime.equals("17:45")) return 0d; //等于17：45 也不考虑在内
			Date oldDate = format.parse(referenceTime.trim());//基准时间
			Date newDate= format.parse(compareTime.trim());//待比较时间 等于17：45 也不考虑在内
			int oldD=(oldDate.getHours()*60)+oldDate.getMinutes();
			int newD=(newDate.getHours()*60)+newDate.getMinutes();

			Double s=(double) (newD-oldD);
			double zhi=(double) (s/60);
	        NumberFormat nf = NumberFormat.getInstance();
	        nf.setRoundingMode(RoundingMode.HALF_UP);//设置四舍五入
	        nf.setMaximumFractionDigits(1);//设置最大保留几位小数
	        zhi=Double.parseDouble(nf.format(zhi));
	        if (zhi<=0) return 0d;
			return zhi;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return 0d;
	}
	
	
	/**
	 * 某一时间与当前时间的小时数 基准时间 referenceTime 待比较时间 compareTime
	 */
	public static Double getTimeDifferenceCompare2(String referenceTime, String compareTime)
	{
		try
		{
			// 格式化时间
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			if(referenceTime.equals("") || compareTime.equals("") || compareTime.equals("17:45")) return 0d; //等于17：45 也不考虑在内
			Date oldDate = format.parse(referenceTime.trim());//基准时间
			Date newDate= format.parse(compareTime.trim());//待比较时间 等于17：45 也不考虑在内
			int oldD=(oldDate.getHours()*60)+oldDate.getMinutes();
			int newD=(newDate.getHours()*60)+newDate.getMinutes();

			Double s=(double) (newD-oldD);
			double zhi=(double) (s/60);
	        NumberFormat nf = NumberFormat.getInstance();
	        nf.setMaximumFractionDigits(2);//设置最大保留几位小数
	        zhi=Double.parseDouble(nf.format(zhi));
	        if (zhi<=0) return 0d;
			return zhi;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return 0d;
	}
	
	/**
	 * 计算除法 获取几位小数保留  带有四舍五入功能
	 */
	public static String rounding(Double shu)
	{
        NumberFormat nf = NumberFormat.getInstance();
        nf.setRoundingMode(RoundingMode.HALF_UP);//设置四舍五入
        //nf.setMinimumFractionDigits(2);//设置最小保留几位小数
        nf.setMaximumFractionDigits(1);//设置最大保留几位小数
        return nf.format(shu);
	}

	/***
	 * 通过年月  获取本月有几个星期
	 */
	public static int getWeekQuantiy(int year, int month) {
		  Calendar cal = Calendar.getInstance();

		  cal.set(Calendar.YEAR, year);
		  cal.set(Calendar.MONTH, month - 1);

		  return cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
		 }
	
	public static void main(String[] args) throws ParseException
	{
		System.out.println(DateFun.rounding(Double.parseDouble("184.299998760223")));
	}
}
