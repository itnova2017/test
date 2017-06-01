package com.tywh.util;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class StringUtils {
	/**sybase数据库的字符集*/
	private static final String SYBEncode = "cp850";
	/**中文字符集*/
	private static final String GBKEncode = "GBK";
	/**中文字符集*/
	private static final String GBEncode = "GB2312";
	/**页面的字符集*/
	private static final String IEEncode = "ISO8859-1";
	/**
   * 根据用户提供的原始字符集和目标字符集，将字符串转换成目标字符集的字符串，需要提供待转换的字符串、原始字符集、目标字符集
   * @param sourceString String 待转换的字符串
   * @param sourceEncode String 原始字符集
   * @param targetEncode String 目标字符集
   * @return String
   */
	public static String convertEncode(String sourceString,String sourceEncode,String targetEncode){
		String rtnString = null;
		if (sourceString == null) System.out.println("源字符串为 null,不能完成转换!");
		if (sourceEncode == null || sourceEncode.equals("")) System.out.println("请提供有效的原始字符集!");
		if (targetEncode == null || targetEncode.equals("")) System.out.println("请提供有效的目标字符集!");
		try {
			rtnString =  new String(sourceString.getBytes(sourceEncode),targetEncode);
		} catch (Exception e) {
			System.out.println("数据转换失败,错误信息如下:" + e.getMessage());
			rtnString = null;
		}
		return rtnString;
	}
	/**
   * 将需要转换的字符串从"cp850"转换到"gb2312"
   * @param s String 待转换的字符串
   * @return String
   */
	public static String sybToGb(String s){
		return convertEncode(s, SYBEncode, GBEncode);
	}
	/**
   * 将需要转换的字符串从"cp850"转换到"gbk"
   * @param s String 待转换的字符串
   * @return String
   */
	public static String sybToGbk(String s){
		return convertEncode(s, SYBEncode, GBKEncode);
	}
	/**
   * 将需要转换的字符串从"gb2312"转换到 "cp850"
   * @param s String 待转换的字符串
   * @return String
   */
	public static String gbToSyb(String s) {
		return convertEncode(s, GBEncode, SYBEncode);
	}
	/**
   * 将需要转换的字符串从"gbk"转换到 "cp850"
   * @param s String 待转换的字符串
   * @return String
   */
	public static String gbkToSyb(String s) {
		return convertEncode(s, GBKEncode, SYBEncode);
	}
	/**
   * 将需要转换的字符串从"ISO8859-1"转换到 "gb2312"
   * @param s String 待转换的字符串
   * @return String
   */
	public static String iEToGb(String s){
		return convertEncode(s, IEEncode, GBEncode);		
	}
	/**
   * 将需要转换的字符串从"ISO8859-1"转换到 "gbk"
   * @param s String 待转换的字符串
   * @return String
   */
	public static String iEToGbk(String s){
		return convertEncode(s, IEEncode, GBKEncode);		
	}
  	/**
   * 转码方法
   * @param plainText String 需要转码的字符串
   * @return String
   */
  	private static String encode(String plainText){
	  	if (plainText==null) return "";
	  	String s = "";	  	
    	StringBuffer encodedText = new StringBuffer();
        for(int i = 0; i < plainText.length(); i++){
            int iChar = plainText.charAt(i);
            if(iChar > 255){
                s = Integer.toString(iChar, 16);
                for(int j = s.length(); j < 4; j++)
                    s = "0" + s;
                encodedText.append("#" + s);
            } else if(iChar < 48 || iChar > 57 && iChar < 65 || iChar > 90 && iChar < 97 || iChar > 122){
                s = Integer.toString(iChar, 16);
                for(int j = s.length(); j < 2; j++)
                    s = "0" + s;
                encodedText.append("~" + s);
            } else {
                encodedText.append(plainText.charAt(i));
            }
        }
        return encodedText.toString(); 
  	}

  	/**
     * 扩展转码方法
     * @param plainText String 需要转码的字符串
     * @return String
     */
	public static String extraencode(String strIn) {
		int	intLen = strIn.length();
		String	strOut = "";
		String	strFlag = "";
		String	strAscii = "";
		int intTemp=0;
		if (strIn==null) System.out.println("源字符串为 null,不能完成转换!");
		for(int	i=0;i<intLen;i++){								
			intTemp=strIn.charAt(i);			
			strAscii=Integer.toString(intTemp,16);				
			if (intTemp>255){
				if (strFlag.equals("")){
					strFlag="#";
					strOut=strOut+"[#"+strAscii;
				}else if (strFlag.equals("~")){
					strFlag="#";
					strOut=strOut+"#"+strAscii;
				}else if (strFlag.equals("#")){
					strOut=strOut+strAscii;
				}
			}else{
				if (intTemp < 48 || (intTemp > 57 && intTemp < 65) || (intTemp > 90 && intTemp < 97) || intTemp > 122){
					String tmp=strAscii;
					if (tmp.length()==1) tmp = "0"+tmp;
					if (strFlag.equals("")){
						strFlag="~";
						strOut=strOut+"[~"+tmp;
					}else if (strFlag.equals("#")){
						strFlag="~";
						strOut=strOut+"~"+tmp;
					}else if (strFlag.equals("~")){
						strOut=strOut+tmp;
					}
				}else{
					if (strFlag.equals("#") || strFlag.equals("~")){
						strFlag="";
						strOut=strOut+"]"+strIn.charAt(i);
					}else{
						strOut=strOut+strIn.charAt(i);
					}
				}
			}		
		}
		return (strOut);
	} 
  	/**
   * 解码方法
   * @param encodedText String 需要解码的字符串
   * @return String
   */
	private static String decode(String encodedText){
        if(encodedText == null) return "";
        String s = "";
        StringBuffer plainText = new StringBuffer();
        for(int i = 0; i < encodedText.length(); i++){
            char c = encodedText.charAt(i);
            switch(c) {
	            case 126: // '~'
	                s = encodedText.substring(i + 1, i + 3);
	                plainText.append((char)Integer.parseInt(s, 16));
	                i += 2;
	                break;	
	            case 35: // '#'
	                s = encodedText.substring(i + 1, i + 5);
	                plainText.append((char)Integer.parseInt(s, 16));
	                i += 4;
	                break;	
	            default:
	                plainText.append(c);
	                break;
            }
        }
        return plainText.toString();
    } 
	/**
   * 获得转码后的字符串
   * @param s String 需要转码的字符串
   * @return String
   */ 
	public static String getEncodeStr(String s){
		return encode(s);
	} 
	/**
   * 获得解码后的字符串
   * @param s String 需要解码的字符串
   * @return String
   */  
	 public static String getDecodeStr(String s){
		return decode(s);
	}	
	/**
   * 根据指定的分隔符将给定的字符串分解开，并且存储到Vector中
   * @param source String 给定的字符串
   * @param separator String 分隔符
   * @return Vector
   */
	public static Vector analyzeString(String source,String separator){
		Vector rtn = new Vector();
		int wz=source.indexOf(separator);
		int len_separator = separator.length();
		if (source.length()>0&&wz==0){
			return null;
		}else{				
			while (wz>0){
				String temps = source.substring(0,wz);
				rtn.add(temps);	
				source = source.substring(wz+len_separator,source.length());
				wz = source.indexOf(separator);									
			}
		}
		if(!source.equals("")) rtn.add(source);	
		return rtn;	
	}

	/**
   * 根据用户指定的格式化字符串格式化双精度数值
   * @param v 需要格式化的数字
   * @param style String 格式化类型
   * @return String 格式化后的结果
   */
	public static String formatDouble(double v, String style) {
		DecimalFormat df = new DecimalFormat(style);
		return df.format(v);
	}
	/**
   * 将字符串中中文部分转换成utf8字符集
   * 解决动态生成doc时，文件名称的中文乱码问题
   * @param s String 需要转换的字符串
   * @return String 转换后的字符
   */ 
	 public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}
	public static boolean isEmpty(String str){
		if(str==null || str.length()==0) return true;		
		return false;
	}
//	public static String trim(String obj){
//		return null;
//	}
	public static String startWithGet(String method){
		if(isEmpty(method)) return null;		
		return "get" + upperStartChar(method);
	}
	public static String startWithSet(String method){
		if(isEmpty(method)) return null;		
		return "set" + upperStartChar(method);
	}
	public static String[] toArrays(List list){
		if(list==null || list.size()==0) return null;
		String[] str = new String[list.size()];
		for(int i=0;i<list.size();i++){
			str[i] = list.get(i).toString();
		}
		return str;
	}
	/**
	  * 此方法将给出的字符串source使用delim划分为单词数组。
	  * @param source 需要进行划分的原字符串
	  * @param delim 单词的分隔字符串
	  * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组，
	  *         如果delim为null则使用逗号作为分隔字符串。
	  * @since  0.1
	  */
	public static String[] split(String source, String delim) {
		String[] wordLists;
		if (source == null) {
			wordLists = new String[1];
			wordLists[0] = source;
			return wordLists;
		}
		if (delim == null) {
			delim = ",";
		}
		StringTokenizer st = new StringTokenizer(source, delim);
		int total = st.countTokens();
		wordLists = new String[total];
		for (int i = 0; i < total; i++) {
			wordLists[i] = st.nextToken();
		}
		return wordLists;
	}
	public static String convertToPath(String package_) throws Exception{
		if(isEmpty(package_)) return null;
		String[] str = split(package_,".");
		StringBuffer sb = new StringBuffer();
		sb.append(File.separator);
		for(int i=0;i<str.length;i++){
//			File f;
//			if(i==str.length-1){
//				sb.append(str[i]);
//				break;
//			}
			sb.append(str[i]).append(File.separator);	
		}		
		return sb.toString();
	}
	/**
	 * 首字母大写;
	 * @param str
	 * @return
	 */
	public static String upperStartChar(String str){
		if(isEmpty(str)) return str;
		char[] ch = str.toCharArray();
		if(!Character.isLowerCase(ch[0])) return str;
		ch[0] = Character.toUpperCase(ch[0]);		
		return new String(ch);
	}
	/**
	 * 首字母小写;
	 * @param str
	 * @return
	 */
	public static String lowerStartChar(String str){
		if(isEmpty(str)) return str;
		char[] ch = str.toCharArray();
		if(Character.isLowerCase(ch[0])) return str;
		ch[0] = Character.toLowerCase(ch[0]);		
		return new String(ch);
	}
	
	/**
	 * 去掉方法名称前的get
	 * @param str
	 * @return
	 */
	public static String discardGetProperty(String str){
		if(!str.startsWith("get")) return str;
		String noGet = str.substring(3,str.length());
		return noGet;
	}
	 
	public static String wapperStr2SpecLen2Left(String str,int length) throws Exception{
		if(str==null) str="";
		if(str.length()>=length) return str;
		int len = length - str.length();
		for(int i=0;i<len;i++){
			str = " " + str; 
		}
		return str;
	}
	public static String wapperStr2SpecLen2Right(String str,int length) throws Exception{
		if(str==null) str="";
		if(str.length()>=length) return str;
		int len = length - str.length();
		for(int i=0;i<len;i++){
			str = str + " "; 
		}
		return str;

	}
	public static String wapperStr2SpecLen(String str,int length) throws Exception{
		if(str==null) str="";
		if(str.length()>=length) return str;
		int len = (length - str.length())/2;
		for(int i=0;i<len;i++){
			str = " " + str + " "; 
		}
		return str;
	}

	public static void main(String[] args) throws Exception{
//		System.out.println(lowerStartChar("Nasf"));
		System.out.println("name=" + discardGetProperty("getCom"));
	}
}
