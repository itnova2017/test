package com.tywh.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.tywh.orm.TMenu;

/**
 *	DieDai
 *  author：杜泉
 *  2014-6-26 上午11:03:08
 */
public class DieDai
{
	int i=0;
	private int findByParentIdAndManagRoleIds(int i){
		
		i=i+1;
		int result=0;
		if(i<10)
		{
			result = findByParentIdAndManagRoleIds(i);
		}
		System.out.println(i);
		return result;
	}
	
	public static void main(String[] args)
	{
		DieDai dd=new DieDai();
		dd.findByParentIdAndManagRoleIds(0);
	}
}
