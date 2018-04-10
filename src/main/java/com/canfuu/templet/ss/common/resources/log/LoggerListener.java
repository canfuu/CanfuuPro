package com.canfuu.templet.ss.common.resources.log;



import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;

/**
 * In CanfuuPro->com.cts.system.resources.listeners
 * <p>
 * Create in 22:18 2017/11/15
 *
 * @author canfuu
 * @version v1.0:日志监听器
 */

public final class LoggerListener implements ServletContextListener{
	private int initCount = 0;
	private int classLimit = 10;
	private int classLimitMax = 20;
	private boolean run = true;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LoggerFactory.init();
		Executors.newCachedThreadPool().submit(
				()-> {
					while (run) {
						if (System.currentTimeMillis() % (3600000) == 0) {
							check();
						}
					}
				}
		);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		run = false;
		LoggerFactory.init();
	}

	private void check(){
		if(LoggerFactory.getLoggerMap().size()>=classLimit){
			LoggerFactory.init();
			initCount++;
			if(initCount>classLimit){
				classLimit++;
				initCount=0;
				if(classLimit>=classLimitMax){
					classLimitMax++;
					classLimit=classLimitMax/2;
				}
			}
		}

	}
}
