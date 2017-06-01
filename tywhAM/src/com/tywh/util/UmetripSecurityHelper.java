package com.tywh.util;

import com.travelsky.msky.util.PropertyProfileUtil;
import com.travelsky.umetrip.security.cryptor.IDentityCryptor;
import com.travelsky.umetrip.security.cryptor.MobileCryptor;
import com.travelsky.umetrip.security.cryptor.PropertyCryptor;

/**
 *   Nov 13, 2013  10:49:19 AM
 *    duquan
 **/

public class UmetripSecurityHelper {
	static{
		PropertyProfileUtil.addResourceProperty("security");
	}

	// 解密用户的手机号码
	public static String decrypt(String title) {
		String  deTitle =MobileCryptor.decrypt(title);// 默认值
		return deTitle;
	}
	// 加密密用户的手机号码
	public static String encrypt(String title) {
		String  enTitle =MobileCryptor.encrypt(title);// 默认值
		return enTitle;
	}
	
	// 解密用户的证件号
	public static String decryptCID(String cid) {
		String  deCid =IDentityCryptor.decrypt(cid);// 默认值
		return deCid;
	}
	
	// 加密用户的证件号
	public static String encryptCID(String cid) {
		String  enCid =IDentityCryptor.encrypt(cid);// 默认值
		return enCid;
	}
	
	// 解密用户的字符串
	public static String decryptPassword(String cid) {
		String  deCid =PropertyCryptor.decrypt(cid);// 默认值
		return deCid;
	}
	// 加密用户的字符串
	public static String encryptPassword(String cid) {
		String  enCid =PropertyCryptor.encrypt(cid);// 默认值
		return enCid;
	}
	
	public static void main(String[] args) {
		String ss="123456";
		System.out.println(encryptPassword(ss));
	}
}


