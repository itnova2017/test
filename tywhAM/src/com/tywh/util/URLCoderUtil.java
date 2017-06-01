package com.tywh.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *   Jun 20, 2013  2:44:41 PM
 *    duquan
 **/

public class URLCoderUtil {
	public static String encode(String s){
		try {
			return URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String decode(String s){
		try {
			return URLDecoder.decode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}


