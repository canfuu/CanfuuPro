package com.canfuu.templet.ss.common.resources.cache;

import com.canfuu.templet.ss.common.resources.cache.redis.RedisOperate;
import com.canfuu.templet.ss.common.resources.cache.threads.LockCache;
import com.canfuu.templet.ss.common.resources.cache.threads.ThreadCache;

import javax.annotation.Resource;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 缓存池
 */
public class CachePool {

	@Resource
	private RedisOperate redisCache;

	@Resource
	private ThreadCache threadCache;

	@Resource
	private LockCache lockCache;

	public LockCache getLockCache() {
		return lockCache;
	}

	public ThreadCache getThreadCache() {
		return threadCache;
	}

	public RedisOperate getRedisCache() {
		return redisCache;
	}
}
