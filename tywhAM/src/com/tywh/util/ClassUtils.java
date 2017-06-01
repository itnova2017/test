package com.tywh.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClassUtils {
	
	/**
	 * 将源对象的属性拷贝到目标对象中；
	 * @param source[源对象]
	 * @param Target[目标对象]
	 * @throws Exception
	 */
	public static void coptyProperties(Object source,Object target) throws Exception{
		//原对象内省；
	   BeanInfo bSourceObject = Introspector.getBeanInfo(source.getClass());
//	 源对象Get、Set函数列表；
	   MethodDescriptor[] mSourceMethods= bSourceObject.getMethodDescriptors();

	   Map mapMethods = getObjectMethods(target);
	   //目标对象Get、Set函数列表；
	   for(int i=0;i<mSourceMethods.length;i++){
		   String methodName = mSourceMethods[i].getName();
		   if(methodName.startsWith("get")){			   
			   String setMethodName = methodName.replaceFirst("get", "set");
			   if(mapMethods.containsKey(setMethodName)){
				   Object objValue = mSourceMethods[i].getMethod().invoke(source,null);				   
				   ((Method)mapMethods.get(setMethodName)).invoke(target,new Object[]{objValue});
			   }
		   }
	   }

	}
	
	public static void coptyExceptProperties(Object source,Object target,String[] except) throws Exception{
		//没有指定对象属性；直接返回；
		List listExcept = new ArrayList();
		if(except!=null && except.length>0){
			listExcept = java.util.Arrays.asList(except);	
		}
		
		
		//原对象内省；
		BeanInfo bSourceObject = Introspector.getBeanInfo(source.getClass());
		//源对象Get、Set函数列表；
		MethodDescriptor[] mSourceMethods= bSourceObject.getMethodDescriptors();		   //目标对象Get、Set函数列表；
		Map mapMethods = getObjectMethods(target);
	    for(int i=0;i<mSourceMethods.length;i++){
		   String methodName = mSourceMethods[i].getName();
		   if(!listExcept.contains(StringUtils.discardGetProperty(methodName)) && methodName.startsWith("get")){			   
			   String setMethodName = methodName.replaceFirst("get", "set");
			   if(mapMethods.containsKey(setMethodName)){
				   System.out.println("methodName = " + setMethodName);
				   Object objValue = mSourceMethods[i].getMethod().invoke(source,null);
				   System.out.println("value=" + objValue);
				   ((Method)mapMethods.get(setMethodName)).invoke(target,new Object[]{objValue});
			   }
		   }
	    }
	}
	
	
	public static void coptySpecialProperties(Object source,Object target,String[] special) throws Exception{
		//没有指定对象属性；直接返回；
		if(special==null || special.length==0)	return;
		List listSpecial = java.util.Arrays.asList(special);
		//原对象内省；
		BeanInfo bSourceObject = Introspector.getBeanInfo(source.getClass());
		//源对象Get、Set函数列表；
		MethodDescriptor[] mSourceMethods= bSourceObject.getMethodDescriptors();		   //目标对象Get、Set函数列表；
		Map mapMethods = getObjectMethods(target);
	    for(int i=0;i<mSourceMethods.length;i++){
		   String methodName = mSourceMethods[i].getName();
		   if(listSpecial.contains(StringUtils.discardGetProperty(methodName)) && methodName.startsWith("get")){			   
			   String setMethodName = methodName.replaceFirst("get", "set");
			   if(mapMethods.containsKey(setMethodName)){
//				   System.out.println("methodName = " + methodName);
				   Object objValue = mSourceMethods[i].getMethod().invoke(source,null);				   
				   ((Method)mapMethods.get(setMethodName)).invoke(target,new Object[]{objValue});
			   }
		   }
	    }
	}
	/**
	 *  获取对象的函数列表(Map("函数名称",函数对象));
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map getObjectMethods(Object obj) throws Exception{
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		Map mapMethods = new HashMap();
		MethodDescriptor[] methods = beanInfo.getMethodDescriptors();
		for(int i=0;i<methods.length;i++){
			mapMethods.put(methods[i].getName(), methods[i].getMethod());
		}
		return mapMethods;
	}
	public static boolean hasMethod(Object obj,String methodName) throws Exception{
		Map map = getObjectMethods(obj);
		Iterator it = map.keySet().iterator();
		for(;it.hasNext();){
			String name = (String) it.next();
			if(name.equalsIgnoreCase("get" + methodName)) return true;
		}
		return false;
	}
	/**
	 * 打印对象内部数据；
	 * @param obj
	 * @throws Exception
	 */
	public static void printObjectValues(Object obj) throws Exception{
		Map mapMethods = getObjectMethods(obj);
		for(Iterator it=mapMethods.keySet().iterator();it.hasNext();){
			String methodName = (String) it.next();
			if(methodName.startsWith("get")){
				Method d = ((Method)mapMethods.get(methodName));
				if(d.getParameterTypes()!=null && d.getParameterTypes().length>0) continue;
				System.out.println("######" + methodName + "=" + ((Method)mapMethods.get(methodName)).invoke(obj,null));
			}
		}
	}
	
	public static Object getPropertyValue(Object obj,String property) throws Exception{
		System.out.println("########property =" + property.length());
		String[] ps = property.split("\\.");
		
		System.out.println("length=" + ps.length);
		Map mapMethods = getObjectMethods(obj);
		for(Iterator it=mapMethods.keySet().iterator();it.hasNext();){
			String methodName = (String) it.next();
			if(methodName.startsWith("get")){
				Method d = ((Method)mapMethods.get(methodName));
				if(ps.length == 1){
					String mName = "get" + ps[0];
					if(mName.equalsIgnoreCase(d.getName())){
						System.out.println("#########methodName=" + methodName);
						return ((Method)mapMethods.get(methodName)).invoke(obj,null);
					}
				}
				if(ps.length == 2){
					String mName = "get" + ps[1];
					if(mName.equalsIgnoreCase(d.getName())){
						return ((Method)mapMethods.get(methodName)).invoke(obj,null);
					}
				}
			}
		}
		throw new Exception("获取属性信息失败!");
	}

//	public static void wapperClassData(Object obj,Class cls,int count) throws Exception{
//		if(obj==null) return ;
//		System.out.println("==" + cls.getSimpleName());
//		//创建对象；
//
//		Object list_ = ClassUtils.getPropertyValue(obj, cls.getSimpleName());
//		if(list_!=null && (list_ instanceof Set)){
//			Set set = (Set) list_;
//			//for(int i=)
//			int size = set.size();
//			for(int i=0;i<count-size;i++){
//				Class c = Class.forName(cls.getName());
//				Object b = c.newInstance();
//				set.add(b);
//			}
//			System.out.println("set.size=" + set.size());
//		}		
//	}
	/**
	 * 调用对象特定方法；
	 * @param obj
	 * @param methodName
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Object invoke(Object obj,String methodName,Object[] args) throws Exception{
		try {
			Map mapMethod = ClassUtils.getObjectMethods(obj);
			Method method =(Method) mapMethod.get(methodName);
			return method.invoke(obj,args);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}
	public String test() throws Exception{
//		System.out.println("test()=== a=" + a);
//		System.out.println("test()=== B=" + b);
		System.out.println("test()=== B=");
		return "ok";
	}
	
	public static void main(String[] args) throws Exception{
//		DMB dmb = new DMB();
//		dmb.setBZ_MC("BZ_MC");
//		dmb.setBZ("bz");
//		
//		DMB dmb_ = new DMB();
//		ClassUtils.printObjectValues(dmb);
//		ClassUtils.coptyExceptProperties(dmb,dmb_,new String[]{"BZ"});
//		ClassUtils.printObjectValues(dmb_);
		ClassUtils s = new ClassUtils();
		ClassUtils.invoke(s,"test", null);
	}
}
