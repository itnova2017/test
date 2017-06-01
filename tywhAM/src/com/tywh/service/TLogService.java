package com.tywh.service;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tywh.dao.LogDao;
import com.tywh.orm.TLog;
import com.tywh.util.Page;

/**
 *	TLogService
 *  author：杜泉
 *  2014-7-14 下午3:38:34
 */
public class TLogService
{
		private LogDao   logDao; 
	 	
		private static Log log = LogFactory.getLog(TLogService.class);
		
		
		public List<TLog> queryAllLogList(String keyword,String startDate,String endDate,Page page) throws ParseException
		{
			page.setTotalRecord(logDao.query(keyword,startDate,endDate));
			return logDao.query(keyword,startDate,endDate,page.getStart(),page.getEnd());
		}


		public LogDao getLogDao()
		{
			return logDao;
		}


		public void setLogDao(LogDao logDao)
		{
			this.logDao = logDao;
		}

		/**
		 * 查看
		 * @throws ParseException 
		 */
		public TLog queryLogById(String logId) throws ParseException
		{
			return logDao.queryLogById(logId);
		}
		
		/**
		 * 记录日志
		 * @throws ParseException 
		 */
		public void saveLog(String content,String user) throws ParseException
		{
			logDao.saveLog(content,user);
		}
		
}
