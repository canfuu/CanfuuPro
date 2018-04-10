package com.canfuu.templet.ss.common.resources.cache;


import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisKeyIsNullException;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 所有缓存对外类的父类
 */
public interface Cache {

	/**
	 *
	 */
	void cacheInitialized();

	/**
	 * 返回是否存在资源
	 * @param key
	 * @return true 存在资源 ／ false 不存在资源
	 */
	boolean hasResources(Object key) throws RedisKeyIsNullException;

	/**
	 * 返回是否存在资源
	 * @param key
	 * @return true 存在资源 ／ 不存在资源
	 */
	boolean hasResources(String key) throws RedisKeyIsNullException;


}
