package com.tywh.util;

import net.sf.json.JSONObject;

/**
 *   Sep 16, 2013  3:10:30 PM
 *    duquan
 **/
public class JsonUtil {

		public String fromObject(Object obj){
			JSONObject json=JSONObject.fromObject(obj);
			return json.toString();
		}

}


