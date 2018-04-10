package com.canfuu.templet.ss.common.utils;


import com.canfuu.templet.ss.common.resources.cache.redis.RedisOperate;
import com.canfuu.templet.ss.common.resources.log.CanfuuLogger;
import com.canfuu.templet.ss.common.resources.log.LogLevel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Map工具类，用于处理相关java.util.Map的操作方法
 */
public class MapUtil {
	public static Map<String,String> objToMap(Object o){
		Field[] fs = o.getClass().getDeclaredFields();
		Map<String,String> map = new HashMap<>(fs.length);
		for (Field f:fs) {
			try {
				String name =f.getName();
				String temp = StringUtil.captureName(name);
				Method method = o.getClass().getMethod("get"+temp);
				String value = method.invoke(o).toString();
				map.put(name,value);
			} catch (NoSuchMethodException |IllegalAccessException | InvocationTargetException e) {

				CanfuuLogger.log(e,RedisOperate.class, LogLevel.ERROR);

				return null;
			}
		}
		return map;
	}
	public static Object mapToObj(Map<String,String> map,Class c){
		try {
			int j= 0;
			Object o = c.newInstance();
			Iterator<Map.Entry<String,String>> i = map.entrySet().iterator();
			while (i.hasNext()){
				Map.Entry<String,String> entry = i.next();
				Field field;
				try {
					field = c.getField(entry.getKey());
					String methodName ="set"+ StringUtil.captureName(field.getName());
					Method method = c.getMethod(methodName);
					method.invoke(o,entry.getValue());
				} catch (NoSuchFieldException | NoSuchMethodException e) {
					CanfuuLogger.log(e,RedisOperate.class, LogLevel.ERROR);
					continue;
				}

				j++;
			}
			if(j==0) {
				return null;
			}
			return o;
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
			CanfuuLogger.log(e,RedisOperate.class, LogLevel.ERROR);
			return null;
		}
	}
}
