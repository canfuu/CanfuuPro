package com.canfuu.templet.ss.common.resources.cache.threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 锁迟（低封装）
 */
public class LockCache {
	public Lock getLock(){
		return new ReentrantLock();
	}
}
