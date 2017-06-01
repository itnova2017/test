package com.tywh.filter;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.tywh.orm.TUserinfo;

/**
 *	SessionIterceptor
 *  author：杜泉
 *  2014-8-20 上午9:59:20
 */
public class SessionIterceptor extends AbstractInterceptor
{
	private static final long	serialVersionUID	= 7464058290477491771L;

	public String intercept(ActionInvocation invocation) throws Exception {
			System.out.println("进入拦截器SessionIterceptor");
		    ActionContext ctx=invocation.getInvocationContext();
			TUserinfo curUser=(TUserinfo) ctx.getSession().get("curManagUser");
			if(curUser!=null){
		       return invocation.invoke();
		    }
		//如果超时，返回提示页面
		         return "loginPage";
		}
}
