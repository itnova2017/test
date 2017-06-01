package com.tywh.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class PropertyUtil{

    private static Properties cms;
    

    private PropertyUtil(){}

    
    private static Properties init(){
    	
    	String startPath = PropertyUtil.class.getClassLoader().getResource("mskymanager.properties").getPath();
        
    			
    	
    	
    	
    	
        Properties props = new Properties();
        try{
        	startPath = startPath.substring(1);
        	
            FileInputStream in = new FileInputStream(startPath);
            props.load(in);
            in.close();
            return props;
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static void init(InputStream in){
            try {
				cms.load(in);
				in.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
    }
    
    

    
    public static String get(String key){
    	if(cms.containsKey(key)){
    		return cms.getProperty(key);
    	}else{
    		return "no such key exist! [key="+key+"]";
    	}
    }
    
    
    public static void set(String key,String value){
    	cms.setProperty(key, value);
    }
    
    
    
	private static void store(){

        String startPath = PropertyUtil.class.getClassLoader().getResource("").getPath();
        OutputStream out;
        try{
            out = new FileOutputStream(startPath + "mskymanager.properties");
            cms.store(out,"保存修改mskymanager.properties");
            out.flush();
            out.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
            throw new RuntimeException("没找到mskymanager.properties "+e.getMessage());
        }catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException("保存到mskymanager.properties错误 "+e.getMessage());
        }
    }

	
	public static Properties getCms(){
		return cms;
	}

	
	public static void setCms(Properties cms){
		PropertyUtil.cms = cms;
	}

	

}
