package com.canfuu.templet.ss.common.resources.log;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In CanfuuPro->com.cts.system.utils.factory
 * <p>
 * Create in 21:58 2017/11/15
 *
 * @author canfuu
 * @version v1.0:日志工厂
 */
public final class LoggerFactory {
	private static final HashMap<String,Logger> LOGGER_MAP =new HashMap<>(5);
	private static final Lock L = new ReentrantLock();

	public static Logger getLogger(String className) {

		if (!LOGGER_MAP.containsKey(className)) {
			savePut(className, Logger.getLogger(className));
		}
		return LOGGER_MAP.get(className);
	}

	public static Logger getLogger(Class c){
		String name;
		if(c==null){
			CanfuuLogger.GLOBAL_LOGGER.error("日志源为空，无法进行日志记录。");
			return null;
		}else {
			name = c.getName();
			if (!LOGGER_MAP.containsKey(name)) {
				savePut(name, Logger.getLogger(c));
			}
			return LOGGER_MAP.get(name);
		}
	}
	private static void savePut(String key,Logger value){
		try{
			L.lock();

			if (!LOGGER_MAP.containsKey(key)) {
				LOGGER_MAP.put(key, value);
			}
		}finally {
			L.unlock();
		}
	}

	public static HashMap<String, Logger> getLoggerMap() {
		return LOGGER_MAP;
	}

	public static void init(){
		LoggerFactory.LOGGER_MAP.clear();
	}

}
