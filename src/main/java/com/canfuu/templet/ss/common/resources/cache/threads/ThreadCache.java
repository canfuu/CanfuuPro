package com.canfuu.templet.ss.common.resources.cache.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 线程池（低封装）
 */
public class ThreadCache {
	private static ExecutorService executorService;
	public ExecutorService getExecutorService() {
		if(executorService==null){
			executorService = Executors.newCachedThreadPool();
		}
		return executorService;
	}
}
