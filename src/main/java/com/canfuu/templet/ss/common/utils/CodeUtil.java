package com.canfuu.templet.ss.common.utils;

import java.util.Random;
import java.util.UUID;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 编码工具类 用于定义各种静态编码方法
 */
public final class CodeUtil {
    public static final String SOURCES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-","");
	}

	public static String getRandom(int number){
	    int max = ((Double) Math.pow(10, (double) number)).intValue() -1;
	    int min = ((Double) Math.pow(10, (double) number-1)).intValue() ;
        Random r = new Random();
        int s = r.nextInt(max-min)+min;
	    return s+"";
    }

    public static String generateString(int number){
        Random r = new Random();
        char[] text = new char[number];
        for (int i = 0; i < number; i++) {
            text[i] = SOURCES.charAt(r.nextInt(SOURCES.length()));
        }
        return new String(text);
    }
}
