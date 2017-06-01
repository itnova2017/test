package com.tywh.util;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Result {

	private boolean flag;
	private String msg;
	private Exception e;
	private String stackTrace;

	public String getStackTrace(){
		return stackTrace;
	}
	
	private void setStackTrace(String stackTrace){
		this.stackTrace = stackTrace;
	}
	
	public void setStackTrace(Exception e){
		if(e!=null){
			this.setStackTrace(getStackTrace(e));
		}
	}

	private String getStackTrace(Throwable t){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}

	public Exception getE(){
		return e;
	}

	public void setE(Exception e){
		this.e = e;
	}

	public Result(){

	}

	public Result(boolean flag,String msg){
		this.flag = flag;
		this.msg = msg;
	}

	public Result(boolean flag,Exception e){
		this.flag = flag;
		setMsg(e);
		this.e = e;
		setStackTrace(e);
	}

	public Result(boolean flag,String msg,Exception e){
		this.flag = flag;
		this.msg = msg;
		this.e = e;
		setStackTrace(e);
	}

	public boolean isFlag(){
		return flag;
	}

	public void setFlag(boolean flag){
		this.flag = flag;
	}

	public String getMsg(){
		return msg;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public void setMsg(Exception e){
		this.msg = "异常:" + e.getMessage();
	}

}
