package com.evstudio.thefirstlottery.mobile.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * 缓存数据集合工具类
 */
public class CustomSharedPref {
	/**
	 * 存储数据
	 * 
	 * @param mContext
	 *            上下文
	 * @param tempName
	 *            存储名称
	 * @param tempList
	 *            数据集合
	 */
	public static void setData(Context mContext, String tempName, List<?> tempList) {
//		SharedPreferences sps = mContext.getSharedPreferences("base64", Context.MODE_PRIVATE);
//		// 创建字节输出流
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		try {
//			// 创建对象输出流，并封装字节流
//			ObjectOutputStream oos = new ObjectOutputStream(baos);
//			// 将对象写入字节流
//			oos.writeObject(tempList);
//			// 将字节流编码成base64的字符串
//			String tempBase64 = new String(Base64.encodeBase64(baos.toByteArray()));
//			Editor editor = sps.edit();
//			editor.putString(tempName, tempBase64);
//			editor.commit();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Log.i("zcx", tempName + "  Save successed!");
	}

	/**
	 * 读取数据
	 * 
	 * @param mContext
	 *            上下文
	 * @param tempName
	 *            读取名称
	 * @param tempList
	 *            数据集合
	 * @return
	 */
	public static List<?> getData(Context mContext, String tempName, List<?> tempList) {
//		SharedPreferences sps = mContext.getSharedPreferences("base64", Context.MODE_PRIVATE);
//		String tempBase64 = sps.getString(tempName, "");// 初值空
//		if (tempBase64 == null || "".equals(tempBase64)) {
//			return tempList;
//		}
//		// 读取字节
//		byte[] base64 = Base64.decodeBase64(tempBase64.getBytes());
//		// 封装到字节流
//		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
//		try {
//			// 再次封装
//			ObjectInputStream ois = new ObjectInputStream(bais);
//			// 读取对象
//			tempList = (List<?>) ois.readObject();
//		} catch (StreamCorruptedException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		Log.i("zcx", tempName + "  Read Successed!");
		return tempList;

	}

}
