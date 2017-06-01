package com.tywh.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *	UtilConfig  获取全局定义的参数
 *  author：杜泉
 *  2014-12-22 上午9:23:47
 */
public class UtilConfig
{
	private static Properties props = new Properties(); 
	static{
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getValue(String key){
		return props.getProperty(key);
	}

    public static void updateProperties(String key,String value) {    
            props.setProperty(key, value); 
    } 
}
