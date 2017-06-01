package com.tywh.util;


public final class Page {
	private int pageSize;

	private int curPage;

	private int totalRecord;

	private int totalPage;

	private int start;

	private int end;

	
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		init();
	}

	public Page(int pageSize, int curPage) {
		super();
		this.pageSize = pageSize;
		this.curPage = curPage;
	}

	public Page(int pageSize, int curPage, int totalRecord) {
		super();
		this.pageSize = pageSize;
		this.curPage = curPage;
		this.totalRecord = totalRecord;
		init();
	}
	
	
	private void init(){
		
		if(pageSize==0){
			
			totalPage=1;
			
		}else{
		totalPage=((totalRecord-1) / pageSize)+1;//经典算法,兼容整除和不整除
		}
		start=((curPage - 1) * pageSize);

		end=Math.min(pageSize, totalRecord);
	}

	public int getCurPage() {
		return curPage;
	}

	public int getEnd() {
		return end;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getStart() {
		return start;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}
	
}
