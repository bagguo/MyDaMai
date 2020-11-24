package com.bagguo.mydamai.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * 使用MD5算法对传入的key进行加密并返回。
	 */
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			//信息摘要，得到md5实例
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			//更新摘要，key得到字节
			mDigest.update(key.getBytes());
			//摘要
			byte[] bytes = mDigest.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				//将与0xFF与运算的结果，转为16进制字符串
				// 0xFF进行与运算都是取16进制的后两位
//				-127(负数127)的16进制为：   FFFFFF81
//				0xFF的16进制为：           000000FF
//				则&（与）运算是结果为：     00000081
//				所以：Integer.toHexStriing(-127 & 0xFF) 的结果为：81。
				String hex = Integer.toHexString(0xFF & bytes[i]);
				//
				if (hex.length() == 1) {
					sb.append('0');
				}
				sb.append(hex);
			}
			cacheKey = sb.toString();//转成string
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());//将key的hashcode变为字符串
		}
		return cacheKey;
	}
}
