package com.tywh.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.log4j.Logger;

public class LogUtil {
	private static Logger logger=Logger.getLogger(com.tywh.util.LogUtil.class);
	public static void readFile(String file, Map<String, Long[]> map,Map<String, Long[]> map2) {
		BufferedReader br=null;
		InputStream in=null;
		try {
			logger.info(file);
			File fi=new File(file);
			in=new FileInputStream(fi);
			 br=new BufferedReader(new InputStreamReader(in,"utf-8"));
			
			String str=null;
			while((str=br.readLine())!=null){
				//sb.append(str);
				
				try {
					String[] spp=str.split("\\]\\[");
					String pid=spp[2];
					String time=spp[8].split("\\|")[2];
					Long t=0L;
					try {
						t = Long.parseLong(time);
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Long[] ls=map.get(pid);
					if(ls==null){
						ls=new Long[]{0L,0L,0L,0L};
						map.put(pid, ls);
					}
					if(ls[0]==0L||t<ls[0]){
						ls[0]=t;
					}
					if(ls[1]==0L||t>ls[1]){
						ls[1]=t;
					}
					ls[2]=ls[2]+t;
					ls[3]=ls[3]+1L;
					ls=map2.get(pid);
					if(ls==null){
						ls=new Long[]{0L,0L,0L,0L};
						map2.put(pid, ls);
					}
					if(ls[0]==0L||t<ls[0]){
						ls[0]=t;
					}
					if(ls[1]==0L||t>ls[1]){
						ls[1]=t;
					}
					ls[2]=ls[2]+t;
					ls[3]=ls[3]+1L;
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(br!=null)
				br.close();
				if(in!=null)
					in.close();
			} catch (Exception e) {
				
			}
		}
		
	}
}
