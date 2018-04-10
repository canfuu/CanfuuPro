package com.canfuu.templet.ss.common.utils;


import org.springframework.util.SerializationUtils;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 序列化工具类
 */
public final class SerializeUtil {
	//序列化
	public static byte [] serialize(Object obj){
		return SerializationUtils.serialize(obj);
	}

	//反序列化
	public static Object unserizlize(byte[] byt){
		return SerializationUtils.deserialize(byt);
	}
}
