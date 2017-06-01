package com.tywh.util;

/**
 *	TestMain
 *  author：杜泉
 *  2014-6-27 下午3:49:26
 */
public class TestMain
{
	public static void main(String[] args)
	{
     //截取字符串
		String ss=",1,2,3,4,";
		String[] sss=ss.split(",2,");
			System.out.println(sss[0] +","+sss[1]);
		

//		for(int i=0;i<10;i++)
//		{
//			 if(i%5==0 && i!=0)
//			 {
//				 System.out.println(i);
//			 }
//		}
		
//		String ss=",1,2,3,";
//		String s=",1,";
//		System.out.println(ss.indexOf(s));
	}
}
