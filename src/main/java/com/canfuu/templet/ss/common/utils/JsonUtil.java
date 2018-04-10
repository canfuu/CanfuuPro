package com.canfuu.templet.ss.common.utils;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: json工具类 用于处理Json工具
 */
public class JsonUtil {
	public static String toJSON(Serializable obj){
		return new Gson().toJson(obj);
	}

}
