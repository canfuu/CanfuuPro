package com.canfuu.templet.ss.common.resources.log;

import org.apache.log4j.Logger;

/**
 * In CanfuuPro->com.cts.system.resources.log
 * <p>
 * Create in :2017/11/15
 *
 * @author canfuu
 * @version v1.0: 日志静态类
 */
public final class CanfuuLogger {
	static final Logger GLOBAL_LOGGER = Logger.getLogger(CanfuuLogger.class);

	public static void log(String message,Class c, LogLevel level){
		Logger logger = LoggerFactory.getLogger(c);
		loggerMessage(message,logger,level);
	}
	public static void log(Exception e, Class c, LogLevel level){
		Logger logger = LoggerFactory.getLogger(c);
		loggerException(e,logger,level);
	}
	public static void log(String message,Object o, LogLevel level){
		Logger logger = LoggerFactory.getLogger(o.getClass());
		loggerMessage(message,logger,level);
	}

	public static void log(Exception e, Object o, LogLevel level){
		Logger logger = LoggerFactory.getLogger(o.getClass());
		loggerException(e,logger,level);
	}

	private static void loggerMessage(String message ,Logger logger , LogLevel level){
		switch (level) {
			case DEBUG:
				logger.debug(message);
				break;
			case FATAL:
				logger.fatal(message);
				break;
			case ERROR:
				logger.error(message);
				break;
			case INFO:
				logger.info(message);
				break;
			case TRACE:
				logger.trace(message);
				break;
			case WARN:
				logger.warn(message);
				break;
			default:
				GLOBAL_LOGGER.error("无法识别的Logger类型！");
				break;
		}
	}
	private static void loggerException(Exception e ,Logger logger , LogLevel level){
		switch (level) {
			case DEBUG:
				logger.debug(e,e.fillInStackTrace());
				break;
			case FATAL:
				logger.fatal(e,e.fillInStackTrace());
				break;
			case ERROR:
				logger.error(e,e.fillInStackTrace());
				break;
			case INFO:
				logger.info(e,e.fillInStackTrace());
				break;
			case TRACE:
				logger.trace(e,e.fillInStackTrace());
				break;
			case WARN:
				logger.warn(e,e.fillInStackTrace());
				break;
			default:
				GLOBAL_LOGGER.error("无法识别的Logger类型！");
				break;
		}
	}
}
