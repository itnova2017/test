package com.tywh.filter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.tywh.dao.LogDao;
import com.tywh.orm.TLog;
import com.tywh.orm.TUserinfo;

/**
 *	LogInterceptor
 *  author：杜泉
 *  2014-8-20 上午10:50:25
 */
public class LogInterceptor extends AbstractInterceptor{    
	private static final long	serialVersionUID	= -4843278066786257185L;
	private String logName;    
    private String logContent;    
    protected Logger log = Logger.getLogger(getClass());    
    protected HttpSession getSession()
    {
        return getRequest().getSession();
    }
    protected HttpServletRequest getRequest()
    {
        return ServletActionContext.getRequest();
    }
    public void init() {    
     }    
    public LogDao logDao;
    public LogDao getLogDao()
	{
		return logDao;
	}
	public void setLogDao(LogDao logDao)
	{
		this.logDao = logDao;
	}
	@Override   
    public String intercept(ActionInvocation ai) throws Exception   {    
        Object action= ai.getAction();    
        String method= ai.getProxy().getMethod();    
        System.out.println("当前action："+action);
        System.out.println("当前method："+method);
//       try{    
//           if(StringUtils.isBlank(method)) method = "method";    
//			HttpSession session = ServletActionContext.getRequest().getSession();//获取session中当前登陆的人
//			TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
//            String userName = "";    
//           if(curUser!=null) userName = curUser.getT_loginName();    
//            String currentTime = DateFun.getDateLongString(new Date());//转换当前时间
//            String logContentHead = "用户："+userName+",操作时间："+currentTime;    
//            ai.invoke();//执行被拦截action    
//               
//           if (action instanceof AuditFolderAction) {    
//               if(method.equals("save")){    
//                    logName = "保存部门";    
//                    logContent = logContentHead +"保存部门："+ai.getStack().findValue("sysOrg.orgName");    
//                    log.info(logContent);    
//                    addSysLog(logName,logContent);    
//                }    
//               if(method.equals("delete")){    
//                    logName = "删除部门";    
//                    logContent = logContentHead +"删除"+((String[])(ai.getStack().findValue("flag_id"))).length+"条部门信息";    
//                    log.info(logContent);    
//                    addSysLog(logName,logContent);    
//                }    
//            }    
//           if (action instanceof AuditFolderDetailAction) {
//               if(method.equals("login")){    
//                    logName = "登录系统";    
//                    logContent = logContentHead;    
//                    log.info(logContent);    
//                    addSysLog(logName,logContent);    
//                }    
//               if(method.equals("changePassword")){    
//                    logName = "修改密码";    
//                    logContent = logContentHead +"删除1条单位信息";    
//                    log.info(logContent);    
//                    addSysLog(logName,logContent);    
//                }    
//            }                   
//        }catch(Exception e){    
//            e.printStackTrace();    
//        }    
        return null;
    }          
    /** 
      * 插入系统日志表中数据
      * @param logName 
      * @param logContent 
      */   
   private void addSysLog(String logName,String logContent){    
	   HttpSession session = ServletActionContext.getRequest().getSession();//获取session中当前登陆的人
	   TUserinfo curUser = (TUserinfo) session.getAttribute("curManagUser");
	   TLog logs = new TLog();
	   logs.setT_user(curUser.getT_loginName());
	   logs.setT_content(logName+":"+logContent);
	  // logDao.saveLog(logs);
    } 
}
