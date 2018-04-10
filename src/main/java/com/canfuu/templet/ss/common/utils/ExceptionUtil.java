package com.canfuu.templet.ss.common.utils;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Exception工具类 用于处理简单的Exception问题
 */
public class ExceptionUtil {
	public static String explainError(String explain,String fixSupport){
		return "\n发生原因："+explain+"\n"+"建议解决方法："+fixSupport + "\nDetails:";
	}
}
