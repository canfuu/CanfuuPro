package com.canfuu.templet.ss.common.utils;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 字符串处理工具类
 */
public class StringUtil {
	public static String captureName(String name) {
		char[] cs=name.toCharArray();
		if(Character.isLowerCase(cs[0])) {
			cs[0] -= 32;
		}
		return String.valueOf(cs);

	}
	public static String upCaptureName(String name) {

		char[] cs=name.toCharArray();
		if(Character.isUpperCase(cs[0])) {
			cs[0] += 32;
		}
		return String.valueOf(cs);
	}


	/**
	 * 字符串数组转换字符串
	 * @param arr
	 * @param PinJieFu
	 * @return
	 */
	public static String arryToString(String[] arr,String PinJieFu) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i < arr.length - 1) {
				sb.append(arr[i] + PinJieFu);
			} else {
				sb.append(arr[i]);
			}
		}
		String name;
		name = sb.toString();
		return name;
	}

	/**
	 * 字符串以某个String分割成字符串数组
	 * @param name
	 * @return
	 */
	public static String[] stringToArr(String name,String fenGeFu){
		String[] split = name.split(fenGeFu);
		return split;
	}
}
